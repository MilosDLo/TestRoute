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
	
}
