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
var eventsData;
var finishRound;
var courseName = 'IDF-AMAM-01';
var clockInterval;

$(document).ready(function() {
//	setSource();
	$("#startSimulator").click(getEvents);
	$("#startSimulator").click(startSimulator);
	$("#pause").click(pauseSimulator);
	$("#resume").click(resumeSimulator);
	console.log("END Document ready");
});

//function setSource() {
//    var eventSource = new EventSource("ScoreController");
//    eventSource.onmessage = function(event) {
//        document.getElementById('marom-score').innerHTML = event.data;
//    };
//}

/**
 * 
 */
function getEvents() {
	$.ajax({
		url : "HomeController?action=getEvents",
		dataType : "json",
		async : false,
		success : function(data) {
			eventsData = data;
		},
		error : function(e) {
			console.log("js:getIncidents: Error in getting events.");
		}
	});
}

function showEventsInTime(elapsedTime) {
	$.each(eventsData, function(i, item) {
		if (elapsedTime == item.time) {
			$(".score-tbl tbody tr:nth-child(" + i+1 + ")").addClass("danger");
			$(".score-tbl tbody tr:nth-child(" + i+1 + ") td:nth-child(1)").html(item.event_id.toHHMMSS());
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
			showTime = Math.floor(remainingClock + offset);
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
	console.log("incrementClock: show time=" + showTime);
	$('#main-time').html(showTime.toHHMMSS());
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

Number.prototype.toHHMMSS = function () {
    var sec_num = parseInt(this, 10); // don't forget the second param
    var hours   = Math.floor(sec_num / 3600);
    var minutes = Math.floor((sec_num - (hours * 3600)) / 60);
    var seconds = sec_num - (hours * 3600) - (minutes * 60);

    if (hours   < 10) {hours   = "0"+hours;}
    if (minutes < 10) {minutes = "0"+minutes;}
    if (seconds < 10) {seconds = "0"+seconds;}
    var time    = hours+':'+minutes+':'+seconds;
    return time;
}
