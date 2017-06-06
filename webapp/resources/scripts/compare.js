
	function initMap(){
		var directionsService1 = new google.maps.DirectionsService;  
	    var map1 = new google.maps.Map(document.getElementById('leftMap'), {
	      zoom: 8,
	      center: {lat: 44.7866, lng: 20.4489}
	    });
	    var rendererOptions1 = {
	    	    map: map1,
	    	    polylineOptions: {
	    	      strokeColor: "red"
	    	    }
	    	  };
	    var directionsDisplay1 = new google.maps.DirectionsRenderer(rendererOptions1);
	    directionsDisplay1.setMap(map1);
	    
	    
	    
	    //-----------------map2--------------
	    
	    var directionsService2 = new google.maps.DirectionsService;
	    var map2 = new google.maps.Map(document.getElementById('rightMap'), {
		      zoom: 8,
		      center: {lat: 44.7866, lng: 20.4489}
		    });
	    var rendererOptions2 = {
	    	    map: map2,
	    	    polylineOptions: {
	    	      strokeColor: "red"
	    	    }
	    	  };
	    var directionsDisplay2 = new google.maps.DirectionsRenderer(rendererOptions2);
	    directionsDisplay2.setMap(map2);
	    
	
	    
	    
	    $(".directionApi-btn").click(function () { 

	    	//kasnije ce kao parametar biti za koju firmu/koja ruta etc.. za sada hardkodovano

			var companyID = "194";
			var shipment_route = "L07";
			var date = "20160901";
			var action = "getWthOptim"

		    setRouteMap(action,companyID,shipment_route,date,directionsService1,directionsDisplay1,true,1);	        	   	                 
	    });   

		$(".directionORToolsTable-btn").click(function () { 
			
			//kasnije ce kao parametar biti za koju firmu/koja ruta etc.. za sada hardkodovano

			var companyID = "194";
			var shipment_route = "L07";
			var date = "20160901";
			var action = "getRoutes"

		    setRouteMap(action,companyID,shipment_route,date,directionsService2,directionsDisplay2,false,2);
	    });

	}
	




	function setRouteMap(action,companyID,shipment_route,date,directionsService,directionsDisplay,optimizeRoute,who){

		var data = {};
	        data["action"] = action;
	        data["companyID"] = companyID;
	        data["shipment_route"] = shipment_route;
	        data["date"] = date;
	        

	        $.ajax({
	            type: "POST",
	            url: "http://127.0.0.1:8080/RootingProject/routingServlet",
	            data: data,
	            dataType: "json",
	            success: function (response) {
					if (response.status === "ERROR") {
						alert(response.errorMsg);
					} else {
						var origin = getOrigin(response);
						var destination = getDestination(response);
						var waypoints = getWaypoints(response);

						getDirectionRouteFromApi(directionsService,directionsDisplay,origin,destination,waypoints,optimizeRoute,who);
					}

	            }
	        });
	}






	
	function getDirectionRouteFromApi(directionsService,directionsDisplay,origin,destination,waypoints,optimizeRoute,who){
		directionsService.route({
			origin:origin,
			destination:destination,
			waypoints:waypoints,
			optimizeWaypoints:optimizeRoute,
			travelMode: 'DRIVING'
		},function(response,status) {
			if (status === 'OK') {
				directionsDisplay.setDirections(response);

				var totalDistance = 0;
				var totalDuration = 0;
				var legs = response.routes[0].legs;
				for(var i=0; i<legs.length; ++i) {
    				totalDistance += legs[i].distance.value;
    				totalDuration += legs[i].duration.value;
				}
				if(who === 1){
					$('.apiInput:text').val(totalDistance);
					$('.apiInputTime:text').val(totalDuration);

				}else{
					$('.ortoolsInput').val(totalDistance);
					$('.ortoolsInputTime').val(totalDuration);   
				}
				

			}
		});
		
	}

	function getOrigin(response){
		var latitude = response.data.orgs[0].latitude;
		var longitude = response.data.orgs[0].longitude;
		var origin = latitude+","+longitude;
		return origin;

	}
	function getDestination(response){
		var latitude = response.data.orgs[0].latitude;
		var longitude = response.data.orgs[0].longitude;
		var destination = latitude+","+longitude;
		return destination;
	}
	function getWaypoints(response){
		var orgs = response.data.orgs;
		var waypoints = [];
		
		for (var i = 0; i < orgs.length; i++) {
			var lat = orgs[i].latitude;
			var long = orgs[i].longitude;
			var point = lat+ "," +long;
			waypoints.push({
            	location: point,
            	stopover: true
            	});       			
		}
		var waypointsWithoutOrigin = waypoints.slice(1);
		return waypointsWithoutOrigin;
	}
	
	
	
	
	///----------------------------getDirection from javaServlet ---------------------------
	
	function initMapJ() {
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

	        var directionPointsArray = [];

	        var pointsInput = $("#params").val();
	        var points = pointsInput.split('\n');
	        for (var index = 0; index < points.length; index++) {
	            var point = points[index].split("'");
	            var long = $.trim(point[5])/1000000000;
	            var lat = $.trim(point[7])/1000000000;
	            var p = lat + "," + long;
	            directionPointsArray.push(p);       
	        }
	        var origin = directionPointsArray[0];
	        var destination = directionPointsArray[directionPointsArray.length - 1];
	        var waypoints = (directionPointsArray.slice(1,-1)).join(";");

	        
	        
	        //for javaServlet
	        var data = {};
	        data["action"] = "getDirection";
	        data["origin"] = origin;
	        data["destination"] = destination;
	        data["waypoints"] = waypoints;
	        

	        $.ajax({
	            type: "POST",
	            url: "http://127.0.0.1:8080/ZZ/mapServlet",
	            data: data,
	            dataType: "json",
	            success: function (responseJ) {

                    //pokusaj hendlovanja response-a i wrapovanja u obj
                    //google.maps.DirectionsResult directionResult = getDirectionResultFromJsonObject(responseJ);

	                directionsDisplay.setDirections(responseJ);
	            }
	        });

	     });  
	  }
	   
     function  createTable(response){
    	 
    	 resetTable();
    	 
    	 var html;
    	 
    	 var data = response.data;
    	 var direction = data.direction;
    	 for (var i = 0; i < direction.length; i++) {
    		  html += '<tr>';
    		  html += '<td>' + direction[i].orderNo + '</td>';
    		  
    		  var origOrder = (direction[i].originalOrderNo === undefined)? "" : direction[i].originalOrderNo;
    		  html += '<td>' + origOrder + '</td>';
    		  html += '<td>' + direction[i].name + '</td>';
    		  html += '<td>' + direction[i].distance + '</td>';
    		  html += '<td>' + direction[i].duration + '</td>';
    		  html += '</tr>';
    		  
		}
//    	 html += '</tbody>';
//    	 
//    	 
		 
		 $("#directionTableBody").append(html);
    	 
		 var foot;

    	 //distance
		 foot += '<tr>';
		 foot += '<th colspan ="3"> Total</th>';
		 foot += '<td>' + data.totalDistance + '</td>';
		 foot += '<td>' + data.totalDuration + '</td>';
		 foot += '</tr>';
		  		  
		 
		 $("#directionTableFoot").append(foot);
    	 
     }
     
     function resetTable(){
    	 $("#directionTableBody").html("");
    	 $("#directionTableFoot").html("");
     }
     


