package net.rooting.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.Dependent;

import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.LocalSearchMetaheuristic;
import com.google.ortools.constraintsolver.NodeEvaluator2;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;


@Dependent
public class EsORTools {

	static {
		JniorToolsLoader.loadNativeLibrary();
	}
	
	
	//Node Distance Evaluation
	  public static class NodeDistance extends NodeEvaluator2 {
	    private long[][] costMatrix;

	    public NodeDistance(long[][] costMatrix) {
	      this.costMatrix = costMatrix;
	    }

	    @Override
	    public long run(int firstIndex, int secondIndex) {
	      return costMatrix[firstIndex][secondIndex];
	    }
	  }
	
	  public Map<String,Object> solve(long[][] costMatrix) {                      //TODO !M promeni posle kad pokazes goranu razliku izmedju 2 pristupa
		  ArrayList<Integer> globalRes = new ArrayList<>();
		    RoutingModel routing = new RoutingModel(costMatrix.length, 1, 0);
		    RoutingSearchParameters parameters =
		        RoutingSearchParameters.newBuilder()
		            .mergeFrom(RoutingModel.defaultSearchParameters())
		            //.setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
		            .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)  //unapredjen algoritam
		            .setTimeLimitMs(2000)    //2sec timeout,jer ovaj alg moze da potraje trazeci optimalnije resenje
		            .build();
		    NodeDistance distances = new NodeDistance(costMatrix);
		    routing.setArcCostEvaluatorOfAllVehicles(distances);

		    Assignment solution = routing.solveWithParameters(parameters); //md
		    if (solution != null) {
		      int route_number = 0;
		      for (long node = routing.start(route_number);!routing.isEnd(node);node = solution.value(routing.nextVar(node))) {		    	
		        globalRes.add((int) node);  //total distance        
		      }
		    }
		    
		    long globalResCost = solution.objectiveValue();
		    
		    Map<String,Object> resp = new HashMap<String,Object>();
		    resp.put("order", globalRes);
		    resp.put("totalDistance", globalResCost);
		    return resp;		    
		  }		  	
	
	
	public Map<String,Object> getOrderOfOrgs(long[][] distanceMatrix){
		Map<String,Object> order = solve(distanceMatrix);
		return order;
	}
	
	
	
	
}
