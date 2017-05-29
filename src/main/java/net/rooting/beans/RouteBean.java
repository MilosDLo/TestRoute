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

import com.google.maps.errors.ApiException;

import net.esteh.em.util.JsonUtil;
import net.rooting.Util.JsonDirectionUtil;
//import net.esteh.mobile.domain.Org;
import net.rooting.Util.JsonDirectionUtil;
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

	
	
	
	
	//brisati posle testa
	public String getTestWithoutOptim(String companyID,String shipment_route,String date) {
		String json = "";
		
		try {
			List<Org> orgs = man.getOrgsInRoute(companyID,shipment_route,date);
			
			Map<String,Object> orgsMap = new HashMap<>();
			orgsMap.put(ORGS, orgs);
			json = jsonDirectionUtil.getJsonFromResultSet(orgsMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			json = jsonDirectionUtil.getErrorJsonString(e.getMessage());
		}
		
		return json;
	}	
	
}
