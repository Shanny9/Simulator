var client_time;
var real_time;
var offset;
var runTime;
var pauseTime;
var showTime;
var elapsedTime = 0;
var remainingTime = 0;
var session = 1;
var round = 1;
var isRunTime = true;
var gp = new Object();
var incidentsData; // initialize by getIncidents()
var finishRound;
var courseName = 'IDF-AMAM-01';
var clockInterval;

$(document).ready(function() {
	$("#startSimulator").click(getIncidents);
	$("#startSimulator").click(startSimulator);
	$("#pause").click(pauseSimulator);
	$("#resume").click(resumeSimulator);
	console.log("END Document ready");
});

/**
 * 
 */
function getIncidents() {
	$.ajax({
		url : "HomeController?action=getIncidents",
		dataType : "json",
		async : false,
		success : function(data) {
			incidentsData = data;
			console.log(incidentsData);
		},
		error : function(e) {
			console.log("js:getIncidents: Error in getting incidents.");
		}
	});
}

// after calling getIncidents
function showIncidentsInTime(elapsedTime) {
	$.each(incidentsData, function(i, item) {
		if (elapsedTime == item.time) {
			console.log("i= " + i + ", showIncidentsInTime:ciID: " + item.ciID);
			$(".score-tbl tbody tr:nth-child(" + i+1 + ")").addClass("danger");
			$(".score-tbl tbody tr:nth-child(" + i+1 + ") td:nth-child(1)").html(item.ciID);
			$(".score-tbl tbody tr:nth-child(" + i+1 + ") td:nth-child(2)").html(item.time);
		}
	});
}

function getGP() {
	$.ajax({
		url : "HomeController?action=getGP&courseName=" + courseName,
		dataType : "json",
		async : false,
		success : function(data) {
			$.each(data, function(key, value) {
				gp[key] = value;
				// console.log("key= " + key + ", value= " + value);
			});
		},
		error : function(e) {
			console.log("js:getGP: Error in getGP: " + e.message);
		}
	});
}

function startSimulator() {
	$.ajax({
		url : "HomeController?action=startSimulator&courseName=" + courseName,
		dataType : "text",
		async : false,
		success : function(data) {
			getGP(courseName);
			finishRound = gp["roundTime"] * (gp["currentRound"] + 1);
			getTime();
			clockInterval = setInterval(incrementClock, 1000);
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

			var remainingClock = data.remainingClock;
			var serverTime = new Date(data.serverTime);

			client_time = new Date();
			offset = (client_time.getTime() - serverTime.getTime())/1000;
			if (offset < 0) {
				offset = offset * -1;
			}
			showTime = (remainingClock + offset)
			console.log("remainingClock " +remainingClock);
			console.log("offset: "+ offset);
		},
		error : function(e) {
			console.log("js:getTime: Error in getting time.");
		}
	});
}

function pauseSimulator(){
	$.ajax({
		url : "HomeController?action=pauseSimulator",
		dataType : "text",
		async : false,
		success : function(data) {
			clearInterval(clockInterval);
		},
		error : function(e) {
			console.log("js:pauseSimulator: Error in pauseSimulator.");
		}
	});
}

function resumeSimulator(){
	$.ajax({
		url : "HomeController?action=resumeSimulator",
		dataType : "text",
		async : false,
		success : function(data) {
			clockInterval = setInterval(incrementClock, 1000);
		},
		error : function(e) {
			console.log("js:resumeSimulator: Error in resumeSimulator.");
		}
	});
}

function incrementClock() {
	console.log("incrementClock: elapsed time=" + elapsedTime);
	$('#main-time').html(dateFormat(secToDate(showTime), "HH:MM:ss"));
	// console.log("fShowTime: " + fShowTime);
	//
	//showIncidentsInTime(dateFormat(secToDate(elapsedTime), "HH:MM:ss"));
	//
	showTime = (showTime - 1);
	elapsedTime++;

	if ((elapsedTime + gp["pauseTime"]) % (gp["sessionTime"]) == 0) {
		// finished runTime
		isRunTime = false;
		showTime = gp["pauseTime"];

	} else if (elapsedTime % (gp["sessionTime"]) == 0) {
		// finished pause time
		isRunTime = true;
		showTime = gp["runTime"];

		if (elapsedTime % gp["sessionTime"] == 0) {
			// finished session
			if (session < gp["sessionsPerRound"]){
				session++;
			};
			console.log("session: " + session);
			$('#session').html(session);

			if (elapsedTime % gp["roundTime"] == 0) {
				// finished round
				console.log("finished");
				$('#main-time').html("00:00:00");
				clearInterval(clockInterval);
			}
		}
	}//
}
