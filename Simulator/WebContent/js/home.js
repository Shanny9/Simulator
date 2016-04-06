var client_time;
var real_time;
var offset;
var runTime;
var pauseTime;
var showTime;
var gp = new Object();

$(document).ready(function() {
	$("#startSimulator").click(startSimulator);
});

/**
 * 
 */
function getIncidents() {
	$.ajax({
		url : "HomeController?action=getIncidents",
		dataType : "json",
		success : function(data) {

			$.each(data, function(j, item) {
				// .....
			});

		},
		error : function(e) {
			console.log("js:getIncidents: Error in getting incidents.");
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
			console.log("gp: " + gp);
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
			for (var s = 0; s < gp["sessionsPerRound"]; s++) {
				console.log("session: " + s);
				if (showTime == 0){
					console.log("starting run time");
					showTime = gp["runTime"];
				}
				for (var rTime = 0; rTime < gp["runTime"]; rTime++) {
					setTimeout(incrementClock(),1000);
				}
				console.log("finished run time. showTime:" + showTime);
				if (showTime == 0){
					console.log("starting pause time");
					showTime = gp["pauseTime"];
				}
				for (var pTime = 0; pTime < gp["pauseTime"]; pTime++) {
					setTimeout(incrementClock(),1000);
				}
				console.log("finished pause time. showTime:" + showTime);
			}
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
			showTime = (remainingClock.getTime() + offset)/1000;
		},
		error : function(e) {
			console.log("js:getTime: Error in getting time.");
		}
	});
}

function incrementClock() {
	var d = new Date(showTime*1000);
	var fShowTime = dateFormat(d, "HH:MM:ss");
	$('#main-time').html(fShowTime);
	// console.log("real_time: " + real_time);
	showTime--;
}
