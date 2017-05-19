//testing

function initMap(){
		var directionsService = new google.maps.DirectionsService;  
	    var map = new google.maps.Map(document.getElementById('map'), {
	      zoom: 8,
	      center: {lat: 44.7866, lng: 20.4489}
	    });
	    var rendererOptions = {
	    	    map: map,
	    	    polylineOptions: {
	    	      strokeColor: "red"
	    	    }
	    	  };
	    var directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
	    directionsDisplay.setMap(map);
	    
	       
	    $(".send-points-btn").click(function () { 

	    	var points = getPoints();
	     	        
	        var origin = getOrigin(points);
	        var destination = getDestination(points);	        
	        var waypoints = getWaypoints(points);	        	   	        
	        
	        getDirectionRouteFromApi(directionsService,directionsDisplay,origin,destination,waypoints);

            //testJava();  
	    });        
	}