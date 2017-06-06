package net.rooting.distancematrix;

import java.io.IOException;

import javax.enterprise.context.Dependent;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

@Dependent
public class DirectionApiManager {
	
	private final String DIRECTIONS_API_KEY = "AIzaSyAx7SO89sRhwv9yAoIyY3NgDVcWM6mh838"; //da li ovaj key u neki properties fajl?
	
	
	
	public DirectionsResult getDirectionResultFromDirectionApi(String origin, String destination, String[] wayArray,boolean optimize)
			throws ApiException, InterruptedException, IOException {
		
		
		GeoApiContext geoApiContext = new GeoApiContext();
		geoApiContext.setApiKey(DIRECTIONS_API_KEY);
		//geoApiContext.setEnterpriseCredentials(arg0, arg1)   //ako budemo uzeli premium nalog
		
		
		DirectionsApiRequest directionReq = DirectionsApi.newRequest(geoApiContext)
				.origin(origin)
				.destination(destination)
				.mode(TravelMode.DRIVING);
		
		if(wayArray != null){
			directionReq.waypoints(wayArray)
			.optimizeWaypoints(optimize);
			
		}
		
		DirectionsResult direction = directionReq.await();
		return direction;
	}
	
	
	
	
	
}
