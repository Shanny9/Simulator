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
var eventsData = new Object();
var finishRound;
var courseName = 'IDF-AMAM-01';
var clockInterval;
var incidentsOnScreen = 0;
var runPercentage;
var pausePercentage;

var winnerColor = '#00ff00';
var looserColor = '#ff0000';
var regularColor = '#FF9900';

var maromScore;
var rakiaScore;

$(document).ready(function() {
	$.backstretch("./css/home_images/runway.jpg"); // Fullscreen
	
	$("#startSimulator").click(startSimulator);
	$("#pause").click(pauseSimulator);
	$("#resume").click(resumeSimulator);
	console.log("END Document ready");
});

function setSolutionSource() {
    var eventSource = new EventSource("HomeController?action=solutionStream");

    eventSource.addEventListener('message', function(e) {
    	
		var data = JSON.parse(e.data);
		var column = (data.team=="Marom")? 0 : 1;
		
		for (var row = 1; row <= 12 ; row++){
			var eventOnBoard = $(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(1)").eq(column).html();
			if ($.inArray(eventOnBoard,data.events) > -1){
				$(".score-tbl tbody tr:nth-child(" + row + ")").eq(column).removeClass("danger");
				$(".score-tbl tbody tr:nth-child(" + row + ")").eq(column).addClass("success");
			}
		}
		
		console.log("team= " + data.team + ", events= " + data.events);
	}, false);
}

function setProfitSource() {
    var eventSource = new EventSource("HomeController?action=profitStream");

    eventSource.addEventListener('message', function(e) {
    	
		var data = JSON.parse(e.data);
		$.each(data, function(i, obj) {
			if (obj.team == 'marom'){
				maromScore = obj.profit;
			} else{
				rakiaScore = obj.profit;
			}
			var scoreId = '#' + obj.team + '-score';
			$(scoreId).html(obj.profit.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
		});
		
		var marom = '#marom-score';
		var rakia = '#rakia-score';
		
		if (maromScore > rakiaScore){
			$(marom).css('color', winnerColor);
			$(rakia).css('color', looserColor);
		} else if (maromScore < rakiaScore){
			$(rakia).css('color', winnerColor);
			$(marom).css('color', looserColor);
		} else{
			$(marom).css('color', regularColor);
			$(rakia).css('color', regularColor);
		}
	}, false);
}

/**
 * 
 */
function getEvents() {
	$.ajax({
		url : "HomeController?action=getEvents",
		dataType : "json",
		async : false,
		success : function(data) {
			$.each(data, function(key, value) {
				eventsData[key] = value;
			});
		},
		error : function(e) {
			console.log("js:getIncidents: Error in getting events.");
		}
	});
}

function showEventsInTime() {
	$.each(eventsData, function(i, item) {
		if (elapsedTime == item.time) {
			var row = incidentsOnScreen + 1;
			$(".score-tbl tbody tr:nth-child(" + row + ")").addClass("danger");
			$(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(1)").html(item.event_id);
			$(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(2)").html(item.time.toHHMMSS());
			incidentsOnScreen++;
		}
	});
}

function clearEvents(){
	for(var row=1; row<12; row++)
		{
			$(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(1)").html("&nbsp");
			$(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(2)").html("&nbsp");
			$(".score-tbl tbody tr:nth-child(" + row + ")").removeClass("success");
			$(".score-tbl tbody tr:nth-child(" + row + ")").addClass("danger");
			incidentsOnScreen =0;
		}
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
			getEvents();
			getGP(courseName);
			finishRound = gp["roundTime"] * (gp["currentRound"] + 1);
			getTime();
			clockInterval = setInterval(incrementClock, 1000);
			setSolutionSource();
			setProfitSource();
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
	
	showEventsInTime();
	
	elapsedTime++;
	
	if (isRunTime){
		runPercentage = (gp["runTime"] - showTime) / gp["sessionTime"] * 100;
	} else{
		pausePercentage = (gp["pauseTime"] - showTime) / gp["sessionTime"] * 100;
	}
	
	var idRun = '.progress-bar-success';
	var idPause = '.progress-bar-danger';
	
	$(idRun).css('width', runPercentage+'%').attr('aria-valuenow', runPercentage);
	$(idPause).css('width', pausePercentage+'%').attr('aria-valuenow', pausePercentage);

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
			runPercentage = 0;
			pausePercentage = 0;
			clearEvents();
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
