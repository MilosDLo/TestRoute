package net.rooting.beans;

import java.io.IOException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;

import net.rooting.Util.EsORTools;
import net.rooting.distancematrix.EsDistanceMatrixManager;

@Dependent
public class EsPointsOptimizer {
	//MAKE CLASS NAME GREAT AGAIN
	
	@Inject
	EsDistanceMatrixManager distanceMatrixManager;
	
	@Inject
	EsORTools ortools;
	
	
	
	public String getOptimizedOrderOfPoints(String[] points){
		
		try {
			DistanceMatrix distanceMatrixResult = distanceMatrixManager.getDistancesBeetwenPoints(points);
			long[][] distanceMatrixArray = getDistanceMatrixArray(distanceMatrixResult);
//			long[] orderOfOrgs = ortools.getOrderOfOrgs(distanceMatrixArray);
			
			String test = "";
						
			
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}




	private long[][] getDistanceMatrixArray(DistanceMatrix distanceMatrixResult) {
		
		int lenght = distanceMatrixResult.rows.length;
		long[][] distanceMatrixArray = new long[lenght][lenght];		
		
		DistanceMatrixRow[] distanceMatrixRow = distanceMatrixResult.rows;
		
		for (int i = 0; i < distanceMatrixRow.length; i++) {
			DistanceMatrixElement[] elements = distanceMatrixRow[i].elements;
			for (int j = 0; j < elements.length; j++) {
				long distance = elements[j].distance.inMeters;
				distanceMatrixArray[i][j] = distance;
			}
		}		
		return distanceMatrixArray;

	}
	
	
	
	
	
	
	
	
}
