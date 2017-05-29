package net.rooting.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	
	
	
	public Map<String, Object> getOptimizedOrderOfPoints(String[] points) throws ApiException, InterruptedException, IOException{

		DistanceMatrix distanceMatrixResult = distanceMatrixManager.getDistancesBeetwenPoints(points);
		long[][] distanceMatrixArray = getDistanceMatrixArray(distanceMatrixResult);

		Map<String, Object> orderOfOrgs = ortools.getOrderOfOrgs(distanceMatrixArray);
		@SuppressWarnings("unchecked")
		ArrayList<Integer> orderList = (ArrayList<Integer>) orderOfOrgs.get("order");
		int[] order = getArrayFromArrayList(orderList);
		orderOfOrgs.replace("order", order);
		
		return orderOfOrgs;
		
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
	
	private int[] getArrayFromArrayList(ArrayList<Integer> list) {
		int[] arr = new int[list.size()];
		for(int i = 0; i < list.size(); i++) {
		    if (list.get(i) != null) {
		        arr[i] = list.get(i);
		    }
		}
		return arr;
	}
	
	
	
	
	
	
	
	
}
