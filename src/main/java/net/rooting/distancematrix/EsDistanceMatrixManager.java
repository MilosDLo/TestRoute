package net.rooting.distancematrix;

import java.io.IOException;

import javax.enterprise.context.Dependent;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;

@Dependent
public class EsDistanceMatrixManager {

	private final  String DIRECTIONS_API_KEY = "AIzaSyAx7SO89sRhwv9yAoIyY3NgDVcWM6mh838"; //da li ovaj key u neki properties fajl?
	
	
	public  DistanceMatrix getDistancesBeetwenPoints(String[] points) throws ApiException, InterruptedException, IOException{
		//String[] pointsArray = getPointsArray(points);
		
		GeoApiContext geoApiContext = new GeoApiContext();
		geoApiContext.setApiKey(DIRECTIONS_API_KEY);
		//geoApiContext.setEnterpriseCredentials(arg0, arg1)   //ako budemo uzeli premium nalog
		
		DistanceMatrixApiRequest distanceMatrixReq = DistanceMatrixApi.newRequest(geoApiContext);
		DistanceMatrix matrixResult = distanceMatrixReq
				.origins(points)
				.destinations(points)
				.mode(TravelMode.DRIVING)
				//.avoid(RouteRestriction.HIGHWAYS)   //napisi bolje,kao opcioni parametar metode
				.await();
		
		return matrixResult;	
		
	}	
	
	
	private static String[] getPointsArray(String points) {
		
		String[] pointsArray = points.split(",");
		return pointsArray;
	}	
	
	
	
}
