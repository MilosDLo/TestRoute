package net.rooting.beans;


import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.apache.commons.lang3.ArrayUtils;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;

import net.esteh.em.util.JsonUtil;
import net.rooting.Util.JsonDirectionUtil;
import net.rooting.distancematrix.DirectionApiManager;
//import net.esteh.mobile.domain.Org;
import net.rooting.domain.Order;
import net.rooting.domain.Org;
import net.rooting.manager.OrgManager;

@Dependent
public class RouteBean {
	public static final String ORDER = "order";
	public static final String TOTAL_DISTANCE = "totalDistance";
	public static final String ORGS = "orgs";

	
	@Inject
	JsonDirectionUtil jsonDirectionUtil;
	
	@Inject
	OrgManager man;
	
	@Inject
	EsPointsOptimizer opt;
	
	@Inject
	DirectionApiManager directionApiManager;
	
	public String getRouteJson(String companyID,String shipment_route,String date){
		String json = "";
		
		try {
			List<Org> orgs = man.getOrgsInRoute(companyID,shipment_route,date);
			
			if (orgs.size() > 2) {                                         //TODO !M promeni u >25
				Map<String,Object> orderedOrgs = getOrderedOrgsList(orgs);
				json = jsonDirectionUtil.getJsonFromResultSet(orderedOrgs);
				
			} else {
				Map<String,Object> orgsMap = new HashMap<>();
				orgsMap.put(ORGS, orgs);
				json = jsonDirectionUtil.getJsonFromResultSet(orgsMap);
			}
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			json = jsonDirectionUtil.getErrorJsonString(e.getMessage());
		}
				
		return json;
	}

	private Map<String,Object> getOrderedOrgsList(List<Org> orgs) throws ApiException, InterruptedException, IOException {
		String[] points = getPoints(orgs); 
		Map<String,Object> orderAndDistance = opt.getOptimizedOrderOfPoints(points);
		Map<String,Object> orderedOrgsAndTotalDistance = getOrderedOrgsAndTotalDistance(orgs,orderAndDistance);
		return orderedOrgsAndTotalDistance;
	}	
	
	
	private Map<String,Object> getOrderedOrgsAndTotalDistance(List<Org> orgs,Map<String,Object> orderAndDistance) {
		
		//for petlja sutra
		List<Org> finalOrderedOrgs = new ArrayList<>();
		int[] order = (int[])orderAndDistance.get(ORDER);
		
		for (int i = 0; i < order.length; i++) {
			finalOrderedOrgs.add(i, orgs.get(order[i])); 
		}
		
		
		Map<String,Object> orderedOrgsAndTotalDistance = new HashMap<>();
		orderedOrgsAndTotalDistance.put(ORGS, finalOrderedOrgs);
		orderedOrgsAndTotalDistance.put(TOTAL_DISTANCE, orderAndDistance.get(TOTAL_DISTANCE));
		return orderedOrgsAndTotalDistance;
		
	}

	private String[] getPoints(List<Org> orgs) {
		
		String[] pointsArray = new String[orgs.size()];
		
		for (int i = 0; i < orgs.size(); i++) {
			String p = ""+ ((double)orgs.get(i).getLatitude())/1000000000 +","+ ((double)orgs.get(i).getLongitude())/1000000000;
			pointsArray[i] = p;
		}
		return pointsArray;		
	}



	public String getAll(String companyA, String dateA) {		
		String json = "";
		
		try {
//			List<Map<String,Object>> orgsInAllRoutes = man.getAll(companyA,dateA);
			JsonArrayBuilder routeArrayBuilder = Json.createArrayBuilder();
					
			
			List<String> routesName = man.getRoutesName(companyA, dateA);
			
			//samo test,brisi posle //TODO !M skraceno za test,promeni posle 
//			if (routesName.size()>100) {
//				routesName.subList(100, routesName.size()).clear();
//			}
			////TODO !M test brisi posleee!
			//routesName.subList(0, 180).clear();

			
			
			for (String route : routesName) {
				//List<Map<String, Object>> routeInfo = man.getInfoForEachRoute(companyA, route, dateA);
				List<Org> orgs = man.getOrgsInRoute(companyA, route, dateA);
				Org storage = man.getStorageOrgInRoute(companyA,route);
				if (storage !=null) {
					orgs.add(0, storage);
				}
				
				
				System.out.println(orgs.size());
				System.out.println(route);
				
				
				//skraceno za test
				if (orgs.size()>24)
					orgs.subList(24, orgs.size()).clear();                       //TODO !M skraceno za test,promeni posle dole na orgs
					
				 
				if (orgs.size() > 25) {                                         //TODO !M promeni u >25
					Map<String,Object> orderedOrgs = getOrderedOrgsList(orgs);
					Long totalWeightOnRoute = man.getTotalWeightInKg(route, companyA, dateA);//kg		
					double totalDistance = getTotalDistance(orderedOrgs,false);
					
					JsonObject oneRoute = jsonDirectionUtil.getRouteArrayJsonObject(orderedOrgs,totalWeightOnRoute,totalDistance,route);				
					routeArrayBuilder.add(oneRoute);
					
				} else {
					Map<String,Object> orgsMap = new HashMap<>();
					orgsMap.put(ORGS, orgs);
					
					Long totalWeightOnRoute = man.getTotalWeightInKg(route, companyA, dateA);//kg					
					double totalDistance = getTotalDistance(orgsMap,true); 
					
					JsonObject oneRoute = jsonDirectionUtil.getRouteArrayJsonObject(orgsMap,totalWeightOnRoute,totalDistance,route);
					routeArrayBuilder.add(oneRoute);
				}			
			}
			
			json = jsonDirectionUtil.getTotalJsonFinalOMG(routeArrayBuilder);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			json = jsonDirectionUtil.getErrorJsonString(e.getMessage());
		}		
		return json;	
	}


	private double getTotalDistance(Map<String,Object> orderedOrgs,boolean optimize) throws ApiException, InterruptedException, IOException {
		double totalDistance = 0;
		
		if (  ((List<Org>)orderedOrgs.get(ORGS)).size() > 1) {
			String origin = getOrigin(orderedOrgs);
			String destination = getDestination(orderedOrgs);
			String[] waypoints = getWaypoints(orderedOrgs);
			
			if (origin == null || destination == null) {
				return totalDistance; //da vrati 0?
			}
			DirectionsResult result = directionApiManager.getDirectionResultFromDirectionApi(origin, destination,
					waypoints, optimize);
			
			if (result.routes.length == 0) {
				return (double) -1;  //da vrati 0?
			}
			
			DirectionsLeg[] legs = result.routes[0].legs;
			for (DirectionsLeg directionsLeg : legs) {
				totalDistance += directionsLeg.distance.inMeters;
			} 
		}
		return totalDistance / 1000; //km		
	}

	private String[] getWaypoints(Map<String, Object> orderedOrgs) {
		@SuppressWarnings("unchecked")
		List<Org> orgs = (List<Org>) orderedOrgs.get(ORGS);
		
		if (orgs.size() == 1) {   //nema waypoints-a
			return null;
		}
		
		List<Integer> indexesOfNulls = new ArrayList<>();
		
		//
		String[] waypoints = new String[orgs.size() - 1]; //bez prvog origin/dest
		for (int i = 1, j=0; i < orgs.size(); i++,j++) {         //preskacemo 0 jer je to origin/destination
			if (orgs.get(i).getLatitude() == null || orgs.get(i).getLongitude() == null) {
				waypoints[j] = "null";
				indexesOfNulls.add(j);				
			}else{
				waypoints[j] = ""+ ((double)orgs.get(i).getLatitude())/1000000000 +","+ ((double)orgs.get(i).getLongitude())/1000000000;				
			}
		}
		
		if (orgs.size() == indexesOfNulls.size()) {
			return null;     // kad su svi null/null onda i nema waypointsa
		}
		
		if (indexesOfNulls.size() > 0) {
			for (int index = 0, temp = 0 ; index < indexesOfNulls.size(); index++, temp++) {
				waypoints = ArrayUtils.remove(waypoints, indexesOfNulls.get(index) - temp);
			}			
		}
		
		
//		for (Integer index : indexesOfNulls) {
//			waypoints = ArrayUtils.remove(waypoints, index);
//		}
		
		return waypoints;
	}

	private String getOrigin(Map<String, Object> orderedOrgs) {
		@SuppressWarnings("unchecked")
		List<Org> orgs = (List<Org>) orderedOrgs.get(ORGS);
		if (orgs.get(0).getLatitude() == null || orgs.get(0).getLongitude() == null) {
			return null;
		}
		String origin = ""+ ((double)orgs.get(0).getLatitude())/1000000000 +","+ ((double)orgs.get(0).getLongitude())/1000000000;
		return origin;
	}
	
	private String getDestination(Map<String, Object> orderedOrgs) {
		//for now same as origin
		@SuppressWarnings("unchecked")
		List<Org> orgs = (List<Org>) orderedOrgs.get(ORGS);
		if (orgs.get(0).getLatitude() == null || orgs.get(0).getLongitude() == null) {
			return null;
		}
		String origin = ""+ ((double)orgs.get(0).getLatitude())/1000000000 +","+ ((double)orgs.get(0).getLongitude())/1000000000;
		return origin;
	}
	
	
//	public String getRouteNameAndCount(String companyID, String date) {
//	String json = "";
//	
//	try {
//		List<Object[]> orgs = man.getRouteNameAndCount(companyID,date);
//		json = jsonDirectionUtil.getJsonFromOrgsNamesAndCount(orgs);
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		//e.printStackTrace();
//		json = jsonDirectionUtil.getErrorJsonString(e.getMessage());
//	}		
//	return json;		
//}

	
	
	//brisati posle testa
//	public String getTestWithoutOptim(String companyID,String shipment_route,String date) {
//		String json = "";
//		
//		try {
//			List<Org> orgs = man.getOrgsInRoute(companyID,shipment_route,date);
//			
//			Map<String,Object> orgsMap = new HashMap<>();
//			orgsMap.put(ORGS, orgs);
//			json = jsonDirectionUtil.getJsonFromResultSet(orgsMap);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			//e.printStackTrace();
//			json = jsonDirectionUtil.getErrorJsonString(e.getMessage());
//		}
//		
//		return json;
//	}
	
}
