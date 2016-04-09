var client_time;
var real_time;
var offset;
var runTime;
var pauseTime;
var showTime;
var elapsedTime = 0;
var remainingTime = 0;
var session = 0;
var round = 1;
var isRunTime = true;
var gp = new Object();
var incidentsData; //initialize by getIncidents()

$(document).ready(function() {
	$("#startSimulator").click(startSimulator);
	$("#startSimulator").click(getIncidents);
	

});

/**
 * 
 */
function getIncidents() {
	$.ajax({
		url : "HomeController?action=getIncidents",
		dataType : "json",
		success : function(data) {
			incidentsData = data;
		},
		error : function(e) {
			console.log("js:getIncidents: Error in getting incidents.");
		}
	});
}

//after calling getIncidents
function showIncidentsInTime(elapsedTime) {
	$.each(incidentsData, function(i, item) {
		if( elapsedTime==item.time){ 
			var str =  "<tr class='danger'>"
			+"	<td>"+item.event+"</td>"
			+"	<td>"+item.time+"</td>"
			+"</tr>";
			$(".event-tbl").append(str);
			console.log("showIncidentsInTime:eventID: "+ item.event);
		}
	});
}

function getGP() {
	$.ajax({
		url : "HomeController?action=getGP",
		dataType : "json",
		success : function(data) {
			$.each(data, function(key, value) {
				gp[key] = value;

			});
//			console.log("gp: " + gp);
		},
		error : function(e) {
			console.log("js:getGP: Error in getGP: " + e.message);
		}
	});
}

function startSimulator() {
	console.log("method startSimulator started");
	$.ajax({
		url : "HomeController?action=startSimulator",
		dataType : "text",
		success : function(data) {

			getGP();
			getTime();
			console.log("sessionsPerRound: " + gp["sessionsPerRound"]);
			setInterval(incrementClock, 1000);
			// console.log("js:startSimulator: Success.");
		},
		error : function(e) {
			console.log("js:startSimulator: Error in starting simulator... "
					+ e.message);
		}
	});
}

function getTime() {
	$.ajax({
		url : "HomeController?action=getTime",
		dataType : "json",
		async : false,
		success : function(data) {

			var remainingClock = new Date(data.remainingClock);
			var serverTime = new Date(data.serverTime);

			client_time = new Date();
			offset = client_time.getTime() - serverTime.getTime();
			if (offset < 0) {
				offset = offset * -1;
			}
			showTime = new Date(remainingClock.getTime() + offset);
		},
		error : function(e) {
			console.log("js:getTime: Error in getting time.");
		}
	});
}

function incrementClock() {
	var fShowTime = dateFormat(showTime, "HH:MM:ss");
	$('#main-time').html(fShowTime);
	//
	showIncidentsInTime(dateFormat(secToDate(elapsedTime), "HH:MM:ss"));
	//
	showTime.setSeconds(showTime.getSeconds()-1);
	elapsedTime++;
	
	console.log('elapsedTime % gp["sessionTime"]=' + elapsedTime+1 % gp["sessionTime"]);
	if (elapsedTime % gp["sessionTime"]==0){
		session++;
		$('#session').html(session);
		console.log('elapsedTime % gp["roundTime"]=' + elapsedTime+1 % gp["roundTime"]);
		if (elapsedTime % gp["roundTime"]==0){
			round++;
			$('#round').html(round);
		}
	}
	
	if ((elapsedTime+gp["pauseTime"])%(gp["sessionTime"])==0){
		// finished runTime
		isRunTime = false;
		showTime = secToDate(gp["pauseTime"]);
	} else if (elapsedTime % (gp["sessionTime"]) == 0){
		// finished pause time
		isRunTime = true;
		showTime = secToDate(gp["runTime"]);
	}
}
