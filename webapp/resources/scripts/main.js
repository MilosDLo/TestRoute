//testing
var routesData = {};




$(document).ready(function () {


	// $(".clickable-route").click(function () {
	// 	var route = $(this).data("route");
	// 	var orgs = routesData[route].orgs;


	// 	// for (var i = 0; i < orgs.length; i++) {
	// 	// 	var org = orgs[i];
	// 	// }


	// 	// var points = getPoints();

	// 	// var origin = getOrigin(points);
	// 	var origin = orgs[0];
	// 	var destination = orgs[0];
	// 	// var waypoints = getWaypoints(points);
	// 	var waypoints = orgs.slice(1, -1);	  


	// 	getDirectionRouteFromApi(directionsService, directionsDisplay, origin, destination, waypoints);
	// });
	



});


function getOrigin(points) {
	var originLong = $.trim((points[0].split("'"))[5]) / 1000000000;
	var originLat = $.trim((points[0].split("'"))[7]) / 1000000000;
	var origin = originLat + "," + originLong;
	return origin;
}

function getDestination(points) {
	var desLong = $.trim((points[points.length - 1].split("'"))[5]) / 1000000000;
	var desLat = $.trim((points[points.length - 1].split("'"))[7]) / 1000000000;
	var destination = desLat + "," + desLong;
	return destination;
}

function getWaypoints(orgs) {
	var directionPointsArray = [];
	var waypoints = orgs.slice(1, orgs.length);    //skratim za prvi(origin = destination)
	for (var index = 0; index < waypoints.length; index++) {
		var point = waypoints[index];
		directionPointsArray.push({
			location: point,
			stopover: true
		});
	}
	//var waypoints = (directionPointsArray.slice(1, -1)); da li treba ovde ili kao gore?
	return directionPointsArray;
}









function getDirectionRouteFromApi(directionsService, directionsDisplay, origin, destination, waypoints,optimizeWaypoints) {

	if (jQuery.isEmptyObject(waypoints)) {
		directionsService.route({
			origin: origin,
			destination: destination,
			travelMode: 'DRIVING'
		}, function (response, status) {
			if (status === 'OK') {
				directionsDisplay.setDirections(response);

			}
		});
	}else{
		directionsService.route({
		origin: origin,
		destination: destination,
		waypoints: waypoints,
		optimizeWaypoints: optimizeWaypoints,
		travelMode: 'DRIVING'
	}, function (response, status) {
		if (status === 'OK') {
			directionsDisplay.setDirections(response);
		}
	});
	}



}




function initMap() {
	var directionsService = new google.maps.DirectionsService;
	var map = new google.maps.Map(document.getElementById('map'), {
		zoom: 8,
		center: {
			lat: 44.7866,
			lng: 20.4489
		}
	});
	var rendererOptions = {
		map: map,
		polylineOptions: {
			strokeColor: "red"
		}
	};
	var directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
	directionsDisplay.setMap(map);





	$("#refreshBtn").click(function () {
		var companyID = "194";
		var date = "20160901";

		var data = {};
		data["action"] = "getAll";
		data["companyID"] = companyID;
		data["date"] = date;
		
		generateSpinner();

		$.ajax({
			type: "POST",
			url: "http://127.0.0.1:8080/RootingProject/routingServlet",
			data: data,
			dataType: "json",
			success: function (response) {
				if (response.status === "ERROR") {
					resetTable();
					alert(response.errorMsg)
				} else {

					var routes = response.data.routes;
					
//					//brisi posle testa
//					var response = {"data":{"routes":[{"shipment_route":"R73","totalWeight":3900,"totalDistance":0,"price":"-","orgs":[{"lat":45.267801776,"lng":19.836829307}]},{"shipment_route":"E69","totalWeight":2013,"totalDistance":12,"price":"-","orgs":[{"lat":44.9480395,"lng":20.20937561},{"lat":44.94179552,"lng":20.2234518},{"lat":44.93788914,"lng":20.16757243},{"lat":44.9539474,"lng":20.21215463},{"lat":44.9490392,"lng":20.2075903},{"lat":44.9444452,"lng":20.2190594},{"lat":44.94765532,"lng":20.2140925},{"lat":44.9444714,"lng":20.219091},{"lat":44.9444859,"lng":20.2192002}]},{"shipment_route":"E27","totalWeight":1783,"totalDistance":31,"price":"-","orgs":[{"lat":44.69488498,"lng":20.38124266},{"lat":44.71320115,"lng":20.36821212},{"lat":44.66797853,"lng":20.39824416},{"lat":44.71617981,"lng":20.36836681},{"lat":44.6812988,"lng":20.38594967},{"lat":44.71448422,"lng":20.36817257},{"lat":44.67497617,"lng":20.40005628},{"lat":44.73016043,"lng":20.38119303},{"lat":44.72502822,"lng":20.38666611}]},{"shipment_route":"E70","totalWeight":2656,"totalDistance":111,"price":"-","orgs":[{"lat":44.96027253,"lng":20.27635225},{"lat":44.95966748,"lng":20.27841568},{"lat":44.95668127,"lng":20.28070269},{"lat":44.94991726,"lng":20.2827985},{"lat":44.95720439,"lng":20.28091265},{"lat":45.01387302,"lng":20.32484816},{"lat":45.01827315,"lng":20.33157594},{"lat":44.98472258,"lng":20.28551266},{"lat":44.9856349,"lng":20.1692888}]},{"shipment_route":"E68","totalWeight":2118,"totalDistance":45,"price":"-","orgs":[{"lat":44.8996895,"lng":20.2561025},{"lat":44.8958956,"lng":20.2934042},{"lat":44.88896379,"lng":20.3091837},{"lat":44.91730931,"lng":20.27496914},{"lat":44.90693073,"lng":20.2897396},{"lat":44.9024821,"lng":20.2346945},{"lat":44.90339537,"lng":20.25641358},{"lat":44.8973448,"lng":20.2836897},{"lat":44.89986037,"lng":20.25637654}]},{"shipment_route":"F76","totalWeight":2868,"totalDistance":4,"price":"-","orgs":[{"lat":44.75436593,"lng":20.41253541},{"lat":44.758470112,"lng":20.425082815},{"lat":44.757328148,"lng":20.421736303},{"lat":44.753345611,"lng":20.424961194},{"lat":44.764197037,"lng":20.414469821},{"lat":44.74732792,"lng":20.42162522},{"lat":44.75943921,"lng":20.41218119},{"lat":44.750045368,"lng":20.427951063},{"lat":44.7619738,"lng":20.40816128}]},{"shipment_route":"E90","totalWeight":70,"totalDistance":2,"price":"-","orgs":[{"lat":44.46080521,"lng":20.07713204},{"lat":44.45698444,"lng":20.07411267}]},{"shipment_route":"F52","totalWeight":1248,"totalDistance":17,"price":"-","orgs":[{"lat":44.85181614,"lng":20.46937541},{"lat":44.84952924,"lng":20.46762416},{"lat":44.87531671,"lng":20.46275773},{"lat":44.85152527,"lng":20.47274002},{"lat":44.88631375,"lng":20.45615627},{"lat":44.88014158,"lng":20.45216171},{"lat":44.84685367,"lng":20.48774361},{"lat":44.84510344,"lng":20.49182875},{"lat":44.85478568,"lng":20.4722614}]},{"shipment_route":"098","totalWeight":1154,"totalDistance":31,"price":"-","orgs":[{"lat":44.94510132,"lng":20.42484881},{"lat":44.87668604,"lng":20.45342128},{"lat":44.91006311,"lng":20.43279442},{"lat":44.88340804,"lng":20.45142636},{"lat":44.87492819,"lng":20.4522824},{"lat":44.88090825,"lng":20.4586379},{"lat":44.88963274,"lng":20.45238196},{"lat":44.87450115,"lng":20.48743622},{"lat":44.88243934,"lng":20.45376004}]},{"shipment_route":"F66","totalWeight":1192,"totalDistance":29,"price":"-","orgs":[{"lat":44.77227288,"lng":20.53415499},{"lat":44.78353312,"lng":20.51454323},{"lat":44.78417382,"lng":20.5194605},{"lat":44.78893131,"lng":20.5160046},{"lat":44.78123721,"lng":20.52558415},{"lat":44.78503934,"lng":20.51283218},{"lat":44.78006236,"lng":20.50897619},{"lat":44.78188805,"lng":20.51068755},{"lat":44.75902332,"lng":20.52569249}]},{"shipment_route":"002","totalWeight":3443,"totalDistance":0,"price":"-","orgs":[{"lat":44.88802779,"lng":20.46117559}]},{"shipment_route":"003","totalWeight":2059,"totalDistance":0,"price":"-","orgs":[{"lat":44.89196383,"lng":20.66737318}]},{"shipment_route":"389","totalWeight":27578,"totalDistance":0,"price":"-","orgs":[]},{"shipment_route":"105","totalWeight":1748,"totalDistance":37,"price":"-","orgs":[{"lat":44.40679491,"lng":21.04678114},{"lat":44.32777522,"lng":21.07019423},{"lat":44.44090067,"lng":21.11078851},{"lat":44.32915285,"lng":21.06817928},{"lat":44.3289277,"lng":21.07279717},{"lat":44.33219466,"lng":21.03194015},{"lat":44.33359602,"lng":21.07682419},{"lat":44.33342336,"lng":21.07734389},{"lat":44.33506716,"lng":21.06725314}]},{"shipment_route":"387","totalWeight":5236,"totalDistance":0,"price":"-","orgs":[]},{"shipment_route":"385","totalWeight":14834,"totalDistance":0,"price":"-","orgs":[]},{"shipment_route":"110","totalWeight":598,"totalDistance":0,"price":"-","orgs":[{"lat":44.60823927,"lng":21.16917257}]},{"shipment_route":"103","totalWeight":1492,"totalDistance":23,"price":"-","orgs":[{"lat":44.66515078,"lng":20.92915524},{"lat":44.66410503,"lng":20.92744792},{"lat":44.64193087,"lng":20.91403174},{"lat":44.5986719,"lng":20.98886693},{"lat":44.65282069,"lng":20.93045644},{"lat":44.6442083,"lng":20.92305724},{"lat":44.64471052,"lng":20.94332402},{"lat":44.65049623,"lng":20.92253329},{"lat":44.64469171,"lng":20.94309265}]},{"shipment_route":"108","totalWeight":826,"totalDistance":21,"price":"-","orgs":[{"lat":44.23289708,"lng":21.19605754},{"lat":44.44733554,"lng":21.21391575},{"lat":44.31853279,"lng":21.22263546},{"lat":44.24495404,"lng":21.20098869},{"lat":44.23083455,"lng":21.19869154},{"lat":44.28049876,"lng":21.20880796}]},{"shipment_route":"386","totalWeight":11872,"totalDistance":0,"price":"-","orgs":[]},{"shipment_route":"107","totalWeight":1368,"totalDistance":40,"price":"-","orgs":[{"lat":44.71705099,"lng":21.2194182},{"lat":44.62156337,"lng":21.18878219},{"lat":44.61938693,"lng":21.18390327},{"lat":44.71704323,"lng":21.22048745},{"lat":44.71008925,"lng":21.17132708},{"lat":44.71677802,"lng":21.17335394},{"lat":44.60270846,"lng":21.18029099},{"lat":44.64589529,"lng":21.2270782},{"lat":44.61962224,"lng":21.18968112}]},{"shipment_route":"101","totalWeight":1642,"totalDistance":20,"price":"-","orgs":[{"lat":44.65625832,"lng":20.93746519},{"lat":44.63027158,"lng":20.96178155},{"lat":44.66511734,"lng":20.92301495},{"lat":44.66509331,"lng":20.91923147},{"lat":44.66493554,"lng":20.92192763},{"lat":44.63025524,"lng":20.96177973},{"lat":44.66738751,"lng":20.92594874}]},{"shipment_route":"106","totalWeight":1330,"totalDistance":2,"price":"-","orgs":[{"lat":44.61731149,"lng":21.1881993},{"lat":44.62224312,"lng":21.18312008},{"lat":44.61372917,"lng":21.17939045},{"lat":44.61945535,"lng":21.1806378}]},{"shipment_route":"109","totalWeight":1300,"totalDistance":33,"price":"-","orgs":[{"lat":44.37705049,"lng":21.41887196},{"lat":44.53081868,"lng":21.31629021},{"lat":44.57900157,"lng":21.2841279},{"lat":44.38194487,"lng":21.41539918}]},{"shipment_route":"112","totalWeight":1548,"totalDistance":0,"price":"-","orgs":[{"lat":44.60818662,"lng":21.17509058}]},{"shipment_route":"111","totalWeight":4211,"totalDistance":0,"price":"-","orgs":[{"lat":44.42601292,"lng":21.0779788}]},{"shipment_route":"388","totalWeight":16624,"totalDistance":0,"price":"-","orgs":[]},{"shipment_route":"390","totalWeight":12570,"totalDistance":0,"price":"-","orgs":[]},{"shipment_route":"102","totalWeight":2102,"totalDistance":0,"price":"-","orgs":[{"lat":44.66865005,"lng":20.9367859}]},{"shipment_route":"104","totalWeight":4077,"totalDistance":0,"price":"-","orgs":[{"lat":44.60902661,"lng":21.17067933}]},{"shipment_route":"E52","totalWeight":11463,"totalDistance":0,"price":"-","orgs":[]},{"shipment_route":"E53","totalWeight":9872,"totalDistance":0,"price":"-","orgs":[]},{"shipment_route":"E54","totalWeight":16715,"totalDistance":0,"price":"-","orgs":[]},{"shipment_route":"G40","totalWeight":14159,"totalDistance":0,"price":"-","orgs":[]},{"shipment_route":"G42","totalWeight":3663,"totalDistance":0,"price":"-","orgs":[]},{"shipment_route":"G41","totalWeight":11093,"totalDistance":0,"price":"-","orgs":[]},{"shipment_route":"C14","totalWeight":2792,"totalDistance":38,"price":"-","orgs":[{"lat":43.71551215,"lng":19.69897882},{"lat":43.75138949,"lng":19.71627665},{"lat":43.63216256,"lng":19.72283057},{"lat":43.72612557,"lng":19.70420352},{"lat":43.75068017,"lng":19.71879106},{"lat":43.72540524,"lng":19.69703567},{"lat":43.73388024,"lng":19.70969152},{"lat":43.81239745,"lng":19.80043834},{"lat":43.72515522,"lng":19.69697633}]},{"shipment_route":"C06","totalWeight":4412,"totalDistance":66,"price":"-","orgs":[{"lat":43.60331048,"lng":20.20463494},{"lat":43.60073037,"lng":20.2099451},{"lat":43.75421759,"lng":20.09568769},{"lat":43.6081298,"lng":20.19201314},{"lat":43.5711894,"lng":20.23871091},{"lat":43.6069073,"lng":20.19407343},{"lat":43.74672667,"lng":20.08684046},{"lat":43.7544075,"lng":20.09563908},{"lat":43.72298182,"lng":20.12740564}]},{"shipment_route":"C05","totalWeight":1483,"totalDistance":0,"price":"-","orgs":[{"lat":43.848375883,"lng":19.85242198}]},{"shipment_route":"K03","totalWeight":2407,"totalDistance":11,"price":"-","orgs":[{"lat":43.14177039,"lng":20.521501575},{"lat":43.15502777,"lng":20.5305318},{"lat":43.130142743,"lng":20.50690777},{"lat":43.141458286,"lng":20.523055585},{"lat":43.138002184,"lng":20.518520775},{"lat":43.13337769,"lng":20.51634687},{"lat":43.13294755,"lng":20.51839734},{"lat":43.14100504,"lng":20.47883782}]},{"shipment_route":"C12","totalWeight":2987,"totalDistance":98,"price":"-","orgs":[{"lat":43.4429938,"lng":19.8204079},{"lat":43.45997428,"lng":19.81356948},{"lat":43.4595779,"lng":19.80680798},{"lat":43.46010772,"lng":19.81436218},{"lat":43.51437505,"lng":19.80074029},{"lat":43.55841313,"lng":19.53994231},{"lat":43.46015359,"lng":19.81320539},{"lat":43.58091975,"lng":19.52747723},{"lat":43.45981622,"lng":19.81034583}]},{"shipment_route":"C08","totalWeight":1120,"totalDistance":49,"price":"-","orgs":[{"lat":43.85598873,"lng":19.83769543},{"lat":43.859622953,"lng":19.83490358},{"lat":43.86328154,"lng":19.82448208},{"lat":43.857039352,"lng":19.841638258},{"lat":43.81590996,"lng":19.935250709},{"lat":43.8631734,"lng":19.82442899},{"lat":43.8562251,"lng":19.8399687},{"lat":43.855755992,"lng":19.838788481},{"lat":43.858141751,"lng":19.837310168}]},{"shipment_route":"K28","totalWeight":3112,"totalDistance":7,"price":"-","orgs":[{"lat":43.98538245,"lng":21.24293816},{"lat":43.97724222,"lng":21.25697871},{"lat":43.97990677,"lng":21.25602417},{"lat":43.96878001,"lng":21.27455116},{"lat":43.97546345,"lng":21.26072296},{"lat":43.98840431,"lng":21.26335932},{"lat":43.97762702,"lng":21.26173283},{"lat":43.9793766,"lng":21.2595274},{"lat":43.98016045,"lng":21.25747298}]},{"shipment_route":"K02","totalWeight":1819,"totalDistance":97,"price":"-","orgs":[{"lat":43.4619855,"lng":20.6416967},{"lat":43.38992482,"lng":20.6322003},{"lat":43.469609975,"lng":20.617078223},{"lat":43.285871051,"lng":20.614095811},{"lat":43.280803268,"lng":20.623351523},{"lat":43.295423839,"lng":20.618417841},{"lat":43.4681824,"lng":20.6161564},{"lat":43.6487251,"lng":20.5532731},{"lat":43.4688154,"lng":20.6176815}]},{"shipment_route":"K09","totalWeight":1371,"totalDistance":4,"price":"-","orgs":[{"lat":43.727796,"lng":20.6817914},{"lat":43.7260735,"lng":20.6900267},{"lat":43.7275505,"lng":20.6911091},{"lat":43.723192475,"lng":20.691070884},{"lat":43.730729341,"lng":20.687253192},{"lat":43.72635979,"lng":20.69268586},{"lat":43.7233531,"lng":20.68801519},{"lat":43.72605237,"lng":20.69473039},{"lat":43.72726043,"lng":20.69072069}]},{"shipment_route":"K10","totalWeight":3303,"totalDistance":14,"price":"-","orgs":[{"lat":43.7169382,"lng":20.6885106},{"lat":43.722678148,"lng":20.685241649},{"lat":43.72037321,"lng":20.69321553},{"lat":43.725535572,"lng":20.678586534},{"lat":43.7220744,"lng":20.705921},{"lat":43.71901381,"lng":20.69572698},{"lat":43.730513216,"lng":20.6592997},{"lat":43.730220634,"lng":20.664503345},{"lat":43.7172189,"lng":20.6908323}]},{"shipment_route":"K27","totalWeight":3429,"totalDistance":114,"price":"-","orgs":[{"lat":43.86711827,"lng":20.52951559},{"lat":44.035955303,"lng":20.808826651},{"lat":43.998577784,"lng":20.884608069},{"lat":44.02103961,"lng":20.90624025},{"lat":44.01575121,"lng":20.90593228},{"lat":44.02135608,"lng":20.90405029},{"lat":43.987012021,"lng":20.870305191},{"lat":43.8669664,"lng":20.52933754},{"lat":44.006254298,"lng":20.88807636}]},{"shipment_route":"K14","totalWeight":1291,"totalDistance":38,"price":"-","orgs":[{"lat":43.589394,"lng":21.36192765},{"lat":43.44251453,"lng":21.22632933},{"lat":43.55719273,"lng":21.34078333},{"lat":43.4503011,"lng":21.2357518},{"lat":43.58145711,"lng":21.32860193},{"lat":43.5547562,"lng":21.34000039},{"lat":43.58367867,"lng":21.32405076},{"lat":43.58726848,"lng":21.30961405},{"lat":43.56887307,"lng":21.32319017}]},{"shipment_route":"K15","totalWeight":2416,"totalDistance":41,"price":"-","orgs":[{"lat":43.59386691,"lng":21.27267197},{"lat":43.578293,"lng":21.33185056},{"lat":43.60077236,"lng":21.31053321},{"lat":43.57059087,"lng":21.32950268},{"lat":43.58096601,"lng":21.32505162},{"lat":43.57877098,"lng":21.32683946},{"lat":43.5885597,"lng":21.26944269},{"lat":43.57762894,"lng":21.15332297},{"lat":43.57771786,"lng":21.1526289}]},{"shipment_route":"K29","totalWeight":3479,"totalDistance":75,"price":"-","orgs":[{"lat":43.7171947,"lng":21.44105179},{"lat":43.93399304,"lng":21.37200836},{"lat":43.7194396,"lng":21.4410502},{"lat":43.86234745,"lng":21.41058448},{"lat":43.72623041,"lng":21.36815523},{"lat":43.92013265,"lng":21.37425343},{"lat":43.94081962,"lng":21.37371985},{"lat":43.862537,"lng":21.39037553},{"lat":43.86000182,"lng":21.41886405}]},{"shipment_route":"K35","totalWeight":612,"totalDistance":0,"price":"-","orgs":[{"lat":43.57029048,"lng":21.34336127}]},{"shipment_route":"C02","totalWeight":2274,"totalDistance":11,"price":"-","orgs":[{"lat":43.89678117,"lng":20.35107466},{"lat":43.90537932,"lng":20.34246724},{"lat":43.91176323,"lng":20.33962487},{"lat":43.90723128,"lng":20.34213854},{"lat":43.911187,"lng":20.3397782},{"lat":43.91203182,"lng":20.33933675},{"lat":43.92505632,"lng":20.32543106},{"lat":43.907202,"lng":20.3617837},{"lat":43.90537174,"lng":20.33431158}]},{"shipment_route":"C26","totalWeight":1810,"totalDistance":96,"price":"-","orgs":[{"lat":43.88095017,"lng":20.481374},{"lat":43.89145454,"lng":20.44811388},{"lat":43.91517678,"lng":20.40662967},{"lat":44.14121456,"lng":20.4983749},{"lat":44.0208236,"lng":20.46137952},{"lat":44.13974211,"lng":20.49431232},{"lat":44.22582218,"lng":20.23950058},{"lat":43.9158087,"lng":20.47359673},{"lat":44.14001357,"lng":20.49769757}]},{"shipment_route":"K26","totalWeight":3558,"totalDistance":34,"price":"-","orgs":[{"lat":44.01273241,"lng":20.92102239},{"lat":43.896358946,"lng":20.771338972},{"lat":44.01034555,"lng":20.91897278},{"lat":44.01235678,"lng":20.91641424},{"lat":44.011412894,"lng":20.925342968},{"lat":44.01411489,"lng":20.90901556},{"lat":44.008420021,"lng":20.923465022},{"lat":44.003233557,"lng":20.900574498},{"lat":44.01017341,"lng":20.91612914}]},{"shipment_route":"C03","totalWeight":1874,"totalDistance":13,"price":"-","orgs":[{"lat":43.88216997,"lng":20.31868976},{"lat":43.89126987,"lng":20.34299801},{"lat":43.8795179,"lng":20.37034621},{"lat":43.88166687,"lng":20.31810613},{"lat":43.8948556,"lng":20.3462533},{"lat":43.89362424,"lng":20.33657989},{"lat":43.8896035,"lng":20.34950905},{"lat":43.8897268,"lng":20.3525254},{"lat":43.89046975,"lng":20.34260743}]},{"shipment_route":"K06","totalWeight":1397,"totalDistance":76,"price":"-","orgs":[{"lat":43.61864458,"lng":21.00473338},{"lat":43.70305903,"lng":20.72632719},{"lat":43.55475978,"lng":21.03870635},{"lat":43.61862424,"lng":21.00456629},{"lat":43.60806787,"lng":21.01107612},{"lat":43.61737778,"lng":20.99704353},{"lat":43.61669929,"lng":21.00962163},{"lat":43.70281917,"lng":20.74866674},{"lat":43.61932614,"lng":20.99783334}]},{"shipment_route":"C11","totalWeight":1767,"totalDistance":7,"price":"-","orgs":[{"lat":43.852318453,"lng":19.855867867},{"lat":43.86340659,"lng":19.84928303},{"lat":43.848535697,"lng":19.864337869},{"lat":43.858385833,"lng":19.84131682},{"lat":43.861044915,"lng":19.855211922},{"lat":43.868098957,"lng":19.8500092},{"lat":43.863846,"lng":19.8493368},{"lat":43.85875644,"lng":19.84208021},{"lat":43.8556652,"lng":19.8451436}]},{"shipment_route":"C17","totalWeight":848,"totalDistance":76,"price":"-","orgs":[{"lat":43.34889388,"lng":19.51449064},{"lat":43.38364652,"lng":19.64060539},{"lat":43.37036014,"lng":19.62743944},{"lat":43.23184288,"lng":19.71451758},{"lat":43.22925996,"lng":19.7197893},{"lat":43.35282353,"lng":19.6484984},{"lat":43.35308072,"lng":19.65372382},{"lat":43.33943766,"lng":19.64288767},{"lat":43.39127828,"lng":19.65392555}]},{"shipment_route":"K13","totalWeight":1428,"totalDistance":27,"price":"-","orgs":[{"lat":43.731882081,"lng":20.646505101},{"lat":43.699985738,"lng":20.593728376},{"lat":43.730586112,"lng":20.659283786},{"lat":43.731835757,"lng":20.646853169},{"lat":43.72397804,"lng":20.6372956},{"lat":43.72086852,"lng":20.63409988},{"lat":43.732903995,"lng":20.632716156},{"lat":43.705502537,"lng":20.614274282},{"lat":43.7017986,"lng":20.6930774}]},{"shipment_route":"C01","totalWeight":1985,"totalDistance":9,"price":"-","orgs":[{"lat":43.86687139,"lng":20.3653005},{"lat":43.88910818,"lng":20.34661297},{"lat":43.87126221,"lng":20.36492396},{"lat":43.87002577,"lng":20.36582309},{"lat":43.8864226,"lng":20.3546056},{"lat":43.88527047,"lng":20.34996759},{"lat":43.8916588,"lng":20.3510778},{"lat":43.8850259,"lng":20.3510313},{"lat":43.871187,"lng":20.36494427}]},{"shipment_route":"C20","totalWeight":1728,"totalDistance":20,"price":"-","orgs":[{"lat":44.02399244,"lng":20.46230491},{"lat":44.02918914,"lng":20.46568195},{"lat":43.81137301,"lng":20.4771049},{"lat":44.03208161,"lng":20.46949564},{"lat":44.03573014,"lng":20.47439165},{"lat":43.91508533,"lng":20.40774502},{"lat":44.03712483,"lng":20.47739503},{"lat":44.02544112,"lng":20.4613081},{"lat":43.8402469,"lng":20.44166832}]},{"shipment_route":"C07","totalWeight":1875,"totalDistance":128,"price":"-","orgs":[{"lat":43.99491713,"lng":19.90698532},{"lat":43.84543492,"lng":20.03266065},{"lat":43.84435501,"lng":20.03408688},{"lat":43.96129848,"lng":20.07871122},{"lat":43.84578365,"lng":20.03795742},{"lat":43.86009025,"lng":20.14181764},{"lat":43.84577903,"lng":20.03426655},{"lat":43.83184967,"lng":20.04129268},{"lat":43.85058291,"lng":20.04542012}]},{"shipment_route":"K07","totalWeight":1676,"totalDistance":68,"price":"-","orgs":[{"lat":43.62209603,"lng":20.89150737},{"lat":43.65124063,"lng":20.88086635},{"lat":43.65079698,"lng":20.88160118},{"lat":43.61190221,"lng":20.89280166},{"lat":43.7066492,"lng":20.7013238},{"lat":43.67350971,"lng":20.82885548},{"lat":43.62592988,"lng":20.89576074},{"lat":43.62453199,"lng":20.89372578},{"lat":43.61973853,"lng":20.88842416}]},{"shipment_route":"K12","totalWeight":1565,"totalDistance":18,"price":"-","orgs":[{"lat":43.72539184,"lng":20.69875037},{"lat":43.733781592,"lng":20.6767138},{"lat":43.725478,"lng":20.7079172},{"lat":43.7254817,"lng":20.7107297},{"lat":43.72541035,"lng":20.69873271},{"lat":43.7250538,"lng":20.6955209},{"lat":43.767042245,"lng":20.632798291},{"lat":43.735439929,"lng":20.675756086},{"lat":43.7240053,"lng":20.695266}]},{"shipment_route":"K08","totalWeight":258,"totalDistance":26,"price":"-","orgs":[{"lat":44.11774767,"lng":21.35691555},{"lat":44.11710616,"lng":21.35717694},{"lat":44.11760198,"lng":21.35709457},{"lat":44.09507631,"lng":21.44128542},{"lat":44.15025792,"lng":21.34423129}]},{"shipment_route":"K20","totalWeight":854,"totalDistance":0,"price":"-","orgs":[{"lat":43.14733509,"lng":20.5216911}]},{"shipment_route":"K37","totalWeight":284,"totalDistance":12,"price":"-","orgs":[{"lat":43.38858779,"lng":20.75134119},{"lat":43.274314424,"lng":20.784202633},{"lat":43.285557891,"lng":20.810301127},{"lat":43.286234061,"lng":20.808359883},{"lat":43.274267005,"lng":20.767645706},{"lat":43.389126128,"lng":20.750161461}]},{"shipment_route":"K34","totalWeight":2746,"totalDistance":0,"price":"-","orgs":[{"lat":44.01995876,"lng":20.927978}]},{"shipment_route":"K21","totalWeight":2472,"totalDistance":35,"price":"-","orgs":[{"lat":43.156960215,"lng":20.532622542},{"lat":43.1408963,"lng":20.4681836},{"lat":43.144742212,"lng":20.519473576},{"lat":43.144660498,"lng":20.514017234},{"lat":43.1409288,"lng":20.4561518},{"lat":43.164554953,"lng":20.530482176},{"lat":43.14077736,"lng":20.51661025},{"lat":43.13424163,"lng":20.50671189},{"lat":43.1406208,"lng":20.48028836}]},{"shipment_route":"K38","totalWeight":513,"totalDistance":0,"price":"-","orgs":[{"lat":43.72633807,"lng":20.71796058}]},{"shipment_route":"K23","totalWeight":387,"totalDistance":0,"price":"-","orgs":[{"lat":43.938364557,"lng":20.783623187}]},{"shipment_route":"K18","totalWeight":277,"totalDistance":0,"price":"-","orgs":[{"lat":43.99351107,"lng":21.26612417}]},{"shipment_route":"K22","totalWeight":2464,"totalDistance":0,"price":"-","orgs":[{"lat":43.5872944,"lng":21.31478068}]},{"shipment_route":"K30","totalWeight":3016,"totalDistance":0,"price":"-","orgs":[{"lat":43.56872473,"lng":21.33535798}]},{"shipment_route":"C41","totalWeight":3600,"totalDistance":0,"price":"-","orgs":[{"lat":43.858385833,"lng":19.84131682}]},{"shipment_route":"C42","totalWeight":3600,"totalDistance":0,"price":"-","orgs":[{"lat":43.85013372,"lng":19.860630779}]},{"shipment_route":"C19","totalWeight":3541,"totalDistance":0,"price":"-","orgs":[{"lat":43.88084157,"lng":20.36903152}]},{"shipment_route":"K24","totalWeight":1303,"totalDistance":0,"price":"-","orgs":[{"lat":44.01463722,"lng":20.92383363}]},{"shipment_route":"K25","totalWeight":553,"totalDistance":0,"price":"-","orgs":[{"lat":44.008541057,"lng":20.895272554}]},{"shipment_route":"C23","totalWeight":391,"totalDistance":0,"price":"-","orgs":[{"lat":43.8785647,"lng":20.3569465}]},{"shipment_route":"K19","totalWeight":540,"totalDistance":0,"price":"-","orgs":[{"lat":44.00585296,"lng":20.89109124}]},{"shipment_route":"K17","totalWeight":395,"totalDistance":0,"price":"-","orgs":[{"lat":43.61545381,"lng":21.01383761}]},{"shipment_route":"N05","totalWeight":1823,"totalDistance":14,"price":"-","orgs":[{"lat":43.33448517,"lng":21.94121894},{"lat":43.32245397,"lng":21.90655927},{"lat":43.31958264,"lng":21.90610232},{"lat":43.32399073,"lng":21.89027603},{"lat":43.32449124,"lng":21.89136094},{"lat":43.31799384,"lng":21.91372786},{"lat":43.32500532,"lng":21.89014841},{"lat":43.32047939,"lng":21.89626167},{"lat":43.32138514,"lng":21.90787734}]},{"shipment_route":"N03","totalWeight":2545,"totalDistance":16,"price":"-","orgs":[{"lat":43.31722252,"lng":21.90300671},{"lat":43.32266431,"lng":21.93218354},{"lat":43.3484781,"lng":21.88546217},{"lat":43.34377288,"lng":21.87425422},{"lat":43.32058208,"lng":21.90000943},{"lat":43.33578906,"lng":21.9107561},{"lat":43.31812483,"lng":21.90499304},{"lat":43.33219356,"lng":21.93688555},{"lat":43.32247359,"lng":21.91287072}]},{"shipment_route":"N01","totalWeight":2420,"totalDistance":43,"price":"-","orgs":[{"lat":43.31560521,"lng":21.87616804},{"lat":43.31605665,"lng":21.89465548},{"lat":43.3179523,"lng":21.88975906},{"lat":43.31519833,"lng":21.92159985},{"lat":43.31334205,"lng":21.89828509},{"lat":43.31798282,"lng":21.89217244},{"lat":43.30971759,"lng":21.87589248},{"lat":43.22261432,"lng":22.03262411},{"lat":43.31548475,"lng":21.91668234}]},{"shipment_route":"N07","totalWeight":1907,"totalDistance":44,"price":"-","orgs":[{"lat":43.3338549,"lng":21.93681066},{"lat":43.30906255,"lng":21.97175353},{"lat":43.22257857,"lng":22.0327924},{"lat":43.32935553,"lng":21.92289901},{"lat":43.33131506,"lng":21.91530202},{"lat":43.32077657,"lng":21.91623684},{"lat":43.3009321,"lng":21.91799143},{"lat":43.32914794,"lng":21.91560426},{"lat":43.31410054,"lng":21.92311388}]},{"shipment_route":"N22","totalWeight":1737,"totalDistance":19,"price":"-","orgs":[{"lat":42.986075338,"lng":21.951667689},{"lat":42.98447966,"lng":21.959039835},{"lat":42.99581888,"lng":21.94799313},{"lat":42.994149696,"lng":21.938867895},{"lat":42.971252141,"lng":21.955959032},{"lat":42.98260262,"lng":21.95020794},{"lat":42.99393274,"lng":21.93899522},{"lat":43.003591135,"lng":21.945161217},{"lat":42.994142799,"lng":21.929384625}]},{"shipment_route":"N26","totalWeight":1511,"totalDistance":75,"price":"-","orgs":[{"lat":43.04515031,"lng":21.40117835},{"lat":43.1278704,"lng":21.29014677},{"lat":43.30707124,"lng":21.34088934},{"lat":43.04634882,"lng":21.39949482},{"lat":43.15045027,"lng":21.25371496},{"lat":43.2947191,"lng":21.29196732},{"lat":43.2975313,"lng":21.2752801},{"lat":43.13954456,"lng":21.27113656},{"lat":43.1375223,"lng":21.2710711}]},{"shipment_route":"N14","totalWeight":1655,"totalDistance":13,"price":"-","orgs":[{"lat":43.24699209,"lng":21.59926213},{"lat":43.2465305,"lng":21.6328774},{"lat":43.24209024,"lng":21.59230176},{"lat":43.24201763,"lng":21.58923565},{"lat":43.24198318,"lng":21.60295098},{"lat":43.24399565,"lng":21.61827886},{"lat":43.24386172,"lng":21.59808381},{"lat":43.22757386,"lng":21.59137987},{"lat":43.23541248,"lng":21.57853873}]},{"shipment_route":"N02","totalWeight":2213,"totalDistance":32,"price":"-","orgs":[{"lat":43.31159778,"lng":21.85903319},{"lat":43.31347189,"lng":21.89776044},{"lat":43.31638215,"lng":21.89867217},{"lat":43.31342112,"lng":21.88551259},{"lat":43.29844795,"lng":21.8665828},{"lat":43.34275507,"lng":21.87110878},{"lat":43.3730342,"lng":21.90039206},{"lat":43.34495302,"lng":21.8868964},{"lat":43.31582719,"lng":21.88570922}]},{"shipment_route":"N19","totalWeight":1211,"totalDistance":33,"price":"-","orgs":[{"lat":42.54659799,"lng":22.00256815},{"lat":42.55588898,"lng":21.9023624},{"lat":42.55475713,"lng":21.98936372},{"lat":42.55238358,"lng":21.9051606},{"lat":42.56803866,"lng":21.98278741},{"lat":42.54995512,"lng":21.8823724},{"lat":42.54541486,"lng":21.87626648},{"lat":42.56271549,"lng":21.90229774},{"lat":42.54587679,"lng":21.89981595}]},{"shipment_route":"N11","totalWeight":1485,"totalDistance":31,"price":"-","orgs":[{"lat":43.31360068,"lng":21.78371238}]},{"shipment_route":"N16","totalWeight":1482,"totalDistance":45,"price":"-","orgs":[{"lat":42.53633825,"lng":21.90448221},{"lat":42.53654692,"lng":21.90443223},{"lat":42.54657961,"lng":22.00256055},{"lat":42.55721115,"lng":21.99005972},{"lat":42.56299217,"lng":21.98520392},{"lat":42.54269607,"lng":21.88886578},{"lat":42.5640113,"lng":21.91117091},{"lat":42.54996866,"lng":21.88297456},{"lat":42.55833519,"lng":21.88885312}]},{"shipment_route":"N25","totalWeight":589,"totalDistance":8,"price":"-","orgs":[{"lat":42.30032178,"lng":21.68200769},{"lat":42.30177779,"lng":21.65128693},{"lat":42.30300881,"lng":21.66898231},{"lat":42.30085792,"lng":21.65185362},{"lat":42.30148609,"lng":21.67195628},{"lat":42.30950263,"lng":21.65002431},{"lat":42.30012887,"lng":21.68265797}]},{"shipment_route":"N17","totalWeight":479,"totalDistance":0,"price":"-","orgs":[{"lat":42.5443084,"lng":21.90102624}]},{"shipment_route":"N04","totalWeight":1437,"totalDistance":0,"price":"-","orgs":[{"lat":43.31858114,"lng":21.8416212}]},{"shipment_route":"N36","totalWeight":1310,"totalDistance":25,"price":"-","orgs":[{"lat":42.70635345,"lng":22.06536782},{"lat":42.70712509,"lng":22.05866324},{"lat":42.70346989,"lng":22.1517275},{"lat":42.70521983,"lng":22.05174925},{"lat":42.70749569,"lng":22.05970187},{"lat":42.77464344,"lng":22.09169583},{"lat":42.70251643,"lng":22.15221734},{"lat":42.70776443,"lng":22.06634366},{"lat":42.71066962,"lng":22.11254539}]},{"shipment_route":"N15","totalWeight":407,"totalDistance":0,"price":"-","orgs":[{"lat":43.32063844,"lng":21.89558678}]},{"shipment_route":"N35","totalWeight":16278,"totalDistance":0,"price":"-","orgs":[{"lat":42.994042645,"lng":21.957067241}]},{"shipment_route":"N34","totalWeight":944,"totalDistance":0,"price":"-","orgs":[{"lat":42.70593496,"lng":22.06216761}]}]},"status":"OK"};
//					var routes = response.data.routes;
//					//
					populateRoutesDataMap(routes);

					generateTable();
				}
			}
		});



	});


	$('#table').find('tbody').on('click', 'tr', function() {
		var route = $(this).data("route");
		var orgs = routesData[route].orgs;

		var origin = orgs[0];
		var destination = orgs[0];	 
		var waypoints = getWaypoints(orgs);

		getDirectionRouteFromApi(directionsService, directionsDisplay, origin, destination, waypoints,true);
	});

}



function generateSpinner(){
	resetTable();
	
	var $spinner = $(document.createElement('i')).addClass("fa fa-refresh fa-spin").css({"font-size": "24px",'position': 'relative', 'top': '50%','left': '50%'});
	
	var $container = $(document.createElement('div')).css({"height": "300px","width": "770px","position": "relative"})	
	var $tbody = $('#routesTable').find('#routesTable_body');
	
	$container.html($spinner);
	$tbody.html($container);
}





function populateRoutesDataMap(routes) {
	routesData = {};

	routes.forEach(function (route) {
		var routeInfo = {};
		routeInfo["totalWeight"] = route.totalWeight;
		routeInfo["totalDistance"] = route.totalDistance;
		routeInfo["price"] = route.price;
		routeInfo["orgs"] = route.orgs;
		routeInfo["shipment_route"] = route.shipment_route;

		routesData[route.shipment_route] = routeInfo;
	});



}





function generateTable() {
	resetTable();

	var $table = $('#routesTable');
	var $tbody = $table.find('#routesTable_body');
	var $fragment = $(document.createDocumentFragment());

	for (var key in routesData) {
		if (routesData.hasOwnProperty(key)) {
			var routeInfo = routesData[key];
			var $row = $(document.createElement('tr')).addClass("clickable-route").attr('data-route', key);
			//provera
			var $td1 = $(document.createElement('td'));
			var txt1 = key;
			$td1.text(txt1);
			$row.append($td1);


			var $td2 = $(document.createElement('td'));
			var txt2 = routeInfo["totalWeight"];
			$td2.text(txt2);
			$row.append($td2);

			var $td3 = $(document.createElement('td'));
			var txt3 = routeInfo["totalDistance"];
			$td3.text(txt3);
			$row.append($td3);

			var $td4 = $(document.createElement('td'));
			var txt4 = routeInfo["price"];
			$td4.text(txt4);
			$row.append($td4);

			$fragment.append($row);
		}
	}

	$tbody.html($fragment);
}

function resetTable() {
	$("#routesTable_body").html(""); //ili .empty();
}


//------------------brisi-------------------------
// function generateTable() {
// 	resetTable();

// 	var html;

// 	for (var key in routesData) {
// 		if (routesData.hasOwnProperty(key)) {
// 			var routeInfo = routesData[key];

// 			html += '<tr>';
// 			html += '<td>' + key + '</td>';

// 			html += '<td>' + routeInfo["totalWeight"] + '</td>';
// 			html += '<td>' + routeInfo["totalDistance"] + '</td>';
// 			html += '<td>' + routeInfo["price"] + '</td>';
// 			html += '</tr>';
// 		}

// 	}
// 	$("#routesTable_body").append(html);

// }