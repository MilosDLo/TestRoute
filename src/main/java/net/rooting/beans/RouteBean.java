package net.rooting.beans;


import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import net.rooting.Util.JsonDirectionUtil;
//import net.esteh.mobile.domain.Org;
import net.rooting.Util.JsonDirectionUtil;
import net.rooting.domain.Order;
import net.rooting.domain.Org;
import net.rooting.manager.OrgManager;

@Dependent
public class RouteBean {

	
	@Inject
	JsonDirectionUtil jsonDirectionUtil;
	
	@Inject
	OrgManager man;
	
	@Inject
	EsPointsOptimizer opt;
	
	public String getRouteJson(String companyID,String shipment_route,String date){
		String json = "";
		
		List<Org> orgs = man.getOrgsInRoute(companyID,shipment_route,date);
		
		if (orgs.size() > 2) {                                         //TODO !M promeni u >25
			String[] points = getPoints(orgs); 
			String result = opt.getOptimizedOrderOfPoints(points);
			
		} else {
			//TODO !M sta ako je ispod 25?
		}
		
		
		
		
		json = jsonDirectionUtil.getJsonFromResultSet(orgs);		
		return json;
	}

	private String[] getPoints(List<Org> orgs) {
		
		String[] pointsArray = new String[orgs.size()];
		
		for (int i = 0; i < orgs.size(); i++) {
			String p = ""+ ((double)orgs.get(i).getLatitude())/1000000000 +","+ ((double)orgs.get(i).getLongitude())/1000000000;
			pointsArray[i] = p;
		}
		return pointsArray;		
	}

	private String[] getRoutesArray(String routesID) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	public String getTest() {
		
//		int companyID = 194;  //Imlek		
//		String shipment_route = "F58";
//		String date = "20160901";
		return "";					
	}	
	
}
