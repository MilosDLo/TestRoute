package net.rooting.Util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import net.esteh.mobile.domain.OrderOrgTemplate;
import net.rooting.beans.RouteBean;
import net.rooting.domain.Org;

@Dependent
public class JsonDirectionUtil {

	
	private static final String OK_STATUS = "OK";
	private static final String ERROR_STATUS = "ERROR";

	public String getJsonFromResultSet(Map<String,Object> orderedOrgs ) {
		
		String json = "";
		
		long totalDistance = getTotalDistance(orderedOrgs);
		//long totalDuration = getTotalDuration(orderedOrgs);     ako bude trebalo
		
		@SuppressWarnings("unchecked")
		List<Org> orgs = (List<Org>) orderedOrgs.get("orgs");
		
		JsonArrayBuilder orgsArrayBuilder = Json.createArrayBuilder();
		for (Org org : orgs) {
			orgsArrayBuilder.add(getOrgJsonObject(org));
		}
				
		
		
		JsonObjectBuilder dataBuilder = Json.createObjectBuilder();	
		dataBuilder.add("orgs", orgsArrayBuilder);
		dataBuilder.add("totalDistance", totalDistance);
		//dataBuilder.add("totalDuration", totalDuration);
		
		
		JsonObjectBuilder jsonRootBuilder = Json.createObjectBuilder();	
		jsonRootBuilder.add("data", dataBuilder);
		jsonRootBuilder.add("status", OK_STATUS);
		
		JsonObject jsonObj = jsonRootBuilder.build();
		json = jsonObj.toString();
						
		return json;
	}

	
	
	private JsonObject getOrgJsonObject(Org org) {
		JsonObjectBuilder orgBuilder = Json.createObjectBuilder();
		orgBuilder.add("name", org.getName());
		orgBuilder.add("ext_code", org.getExtCode());
		orgBuilder.add("id", org.getId());
		orgBuilder.add("latitude", ((double)org.getLatitude())/1000000000);
		orgBuilder.add("longitude", ((double)org.getLongitude())/1000000000);
		return orgBuilder.build();
		
	}

	private long getTotalDistance(Map<String, Object> orderedOrgs) {
		if (!orderedOrgs.containsKey("totalDistance")) {
			return -1;
		}
		else{
			long totalDistance = (long)orderedOrgs.get("totalDistance");
			return totalDistance;			
		}
		
	}
	
	
	public String getErrorJsonString(String message) {
		
		JsonObjectBuilder errorBuilder = Json.createObjectBuilder();
		errorBuilder.addNull("data");
		errorBuilder.add("status", ERROR_STATUS);
		errorBuilder.add("errorMsg", "Server error:" + message);
		
		return errorBuilder.build().toString();
		
	}



	public String getJsonFromOrgsNamesAndCount(List<Object[]> orgs) {
		String json = "";
		
		JsonArrayBuilder routeArrayBuilder = Json.createArrayBuilder();
		for (Object[] org : orgs) {
			routeArrayBuilder.add(getRouteNameCountJsonObject(org));
		}
				
		JsonObjectBuilder dataBuilder = Json.createObjectBuilder();	
		dataBuilder.add("routeName&Count", routeArrayBuilder);		
		
		JsonObjectBuilder jsonRootBuilder = Json.createObjectBuilder();	
		jsonRootBuilder.add("data", dataBuilder);
		jsonRootBuilder.add("status", OK_STATUS);
		
		JsonObject jsonObj = jsonRootBuilder.build();
		json = jsonObj.toString();
		
		return json;			
	}



	private JsonObject getRouteNameCountJsonObject(Object[] org) {
		JsonObjectBuilder routeBuilder = Json.createObjectBuilder();
		routeBuilder.add("name", org[1].toString());
		routeBuilder.add("count", org[0].toString());
		return routeBuilder.build();
	}



	public String getJsonFromAll(List<Map<String,Object>> orgs) {		
		String json = "";
		
		JsonArrayBuilder routeArrayBuilder = Json.createArrayBuilder();
		for (Map<String, Object> map : orgs) {
			routeArrayBuilder.add(getRouteInfoJsonObject(map));
		}			
		
		
		JsonObjectBuilder dataBuilder = Json.createObjectBuilder();	
		dataBuilder.add("routes", routeArrayBuilder);		
		
		JsonObjectBuilder jsonRootBuilder = Json.createObjectBuilder();	
		jsonRootBuilder.add("data", dataBuilder);
		jsonRootBuilder.add("status", OK_STATUS);
		
		JsonObject jsonObj = jsonRootBuilder.build();
		json = jsonObj.toString();
		
		return json;
	}



	private JsonValue getRouteInfoJsonObject(Map<String, Object> map) {
		JsonObjectBuilder routeBuilder = Json.createObjectBuilder();
		routeBuilder.add("name", map.get("name").toString());
		routeBuilder.add("shipment_route", map.get("shipment_route").toString());
		routeBuilder.add("longitude", map.get("longitude").toString());
		routeBuilder.add("latitude", map.get("latitude").toString());
		routeBuilder.add("weightNet", map.get("weightNet").toString());		
		return routeBuilder.build();
	}



	



	public JsonObject getRouteArrayJsonObject(Map<String, Object> orgsMap, Long totalWeightOnRoute, double totalDistance,
			String route) {
		
		JsonObjectBuilder routeBuilder = Json.createObjectBuilder();
		
		routeBuilder.add("shipment_route", route);
		routeBuilder.add("totalWeight", totalWeightOnRoute);
		routeBuilder.add("totalDistance", totalDistance);
		routeBuilder.add("price", "-");		   //promeniti
		
		routeBuilder.add("orgs", getOrgForRouteJsonArray(orgsMap));
		
		
		return routeBuilder.build();
		
		
	}



	private javax.json.JsonArray getOrgForRouteJsonArray(Map<String, Object> orgsMap) {
		@SuppressWarnings("unchecked")
		List<Org> orgs = (List<Org>) orgsMap.get("orgs");
		
		JsonArrayBuilder orgsArrayBuilder = Json.createArrayBuilder();
		for (Org org : orgs) {
			JsonObject orgJsonObj = getOrgAllJsonObject(org);
			if (orgJsonObj!= null) {
				orgsArrayBuilder.add(orgJsonObj);				
			}
		}
		return orgsArrayBuilder.build();				
	}
	
	
	private JsonObject getOrgAllJsonObject(Org org) {
		JsonObjectBuilder orgBuilder = Json.createObjectBuilder();
		//orgBuilder.add("name", org.getName());
		if (org.getLatitude() == null || org.getLongitude() == null) {
			return null;
		}else{
			orgBuilder.add("lat", ((double)org.getLatitude())/1000000000);
			orgBuilder.add("lng", ((double)org.getLongitude())/1000000000);
			return orgBuilder.build();			
		}
		
	}



	public String getTotalJsonFinalOMG(JsonArrayBuilder routeArrayBuilder) {
		String json = "";
		JsonObjectBuilder dataBuilder = Json.createObjectBuilder();	
		dataBuilder.add("routes", routeArrayBuilder);		
		
		JsonObjectBuilder jsonRootBuilder = Json.createObjectBuilder();	
		jsonRootBuilder.add("data", dataBuilder);
		jsonRootBuilder.add("status", OK_STATUS);
		
		JsonObject jsonObj = jsonRootBuilder.build();
		json = jsonObj.toString();
		
		return json;
	}
		
	
}
