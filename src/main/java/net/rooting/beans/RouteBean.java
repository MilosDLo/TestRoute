package net.rooting.beans;


import java.sql.ResultSet;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import net.rooting.Util.JsonRouteUtil;
import net.rooting.db.MySQLManager;

@Dependent
public class RouteBean {

	@Inject
	MySQLManager mysql;
	
	@Inject
	JsonRouteUtil jsonRouteUtil;
	
	
	
	public String getRouteJson(String routesID){
		String json = "";
		
		String[] routes = getRoutesArray(routesID);
		ResultSet result = mysql.getRoute(routes);
		json = jsonRouteUtil.getJsonFromResultSet(result);
				
		
		
		return json;
	}

	private String[] getRoutesArray(String routesID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	
}
