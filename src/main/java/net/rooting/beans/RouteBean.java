package net.rooting.beans;


import java.sql.ResultSet;
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
	
	
	public String getRouteJson(String companyID,String shipment_route,String date){
		String json = "";
		
		List<Org> orgs = man.getOrgsInRoute(companyID,shipment_route,date);
		json = jsonDirectionUtil.getJsonFromResultSet(orgs);		
		return json;
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
