var client_time;
var real_time;
var offset;
var runTime;
var elapsedRunTime = 0;
var pauseTime;
var showTime;
var elapsedTime = 0;
var remainingTime = 0;
var session = 1;
//var round = 1; //defined at the beginning of index.jsp
var isRunTime = false;
var settings = new Object();
var eventsData = new Object();
var finishRound;
//var courseName = 'normalCourse'; //defined at the beginning of index.jsp
var clockInterval;
var incidentsOnScreen = 0;
var runPercentage;
var pausePercentage;
var winnerColor = '#00ff00';
var looserColor = '#ff0000';
var regularColor = '#FF9900';
var isSimulatorStarted = false;
var maromScore;
var rakiaScore;

var solutionEventSource;
var profitEventSource;

var solutionListener = function(e) {
	
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
}

var profitListener = function(e) {
	
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
}

$(document).ready(function() {
	$.backstretch("./css/home_images/runway.jpg"); // Fullscreen
	
	checkSimulator();
		
	$("#startSimulator").click(startSimulator);
	$("#pause").click(pauseSimulator);
	$("#resume").click(resumeSimulator);
	console.log("END Document ready");
});

//checks if the simulator has started
function checkSimulator() {

	$.ajax({
		url : "ClientController",
		data : {
			action : "checkSimulator",
		},
		dataType : "json",
		timeout : 0,
		success : function(msg) {
			startSimulator();
		},
		error : function(e) {
			console.log("js:checkSimulator: Error in checkSimulator.");
		}
	});
}

function setSolutionSource() {
    solutionEventSource = new EventSource("HomeController?action=solutionStream");
    solutionEventSource.addEventListener('message',solutionListener, false);
}

function setProfitSource() {
    profitEventSource = new EventSource("HomeController?action=profitStream");
    profitEventSource.addEventListener('message', profitListener, false);
}

/**
 * 
 */
function getEvents() {
	$.ajax({
		url : "HomeController",
		data : {
			action : "getEvents",
		},
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
			delete eventsData[i];
			incidentsOnScreen++;
		}
	});
}

function showPastEvents() {
	$.each(eventsData, function(i, item) {
		if (elapsedTime >= item.time) {
			var row = incidentsOnScreen + 1;
			$(".score-tbl tbody tr:nth-child(" + row + ")").addClass("danger");
			$(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(1)").html(item.event_id);
			$(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(2)").html(item.time.toHHMMSS());
			delete eventsData[i];
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

function getSettings() {
	$.ajax({
		url : "HomeController",
		data : {
			action : "getSettings",
			courseName : courseName
		},
		dataType : "json",
		async : false,
		success : function(data) {
			$.each(data, function(key, value) {
				settings[key] = value;
			});
		},
		error : function(e) {
			console.log("js:getSettings: Error in getSettings: " + e.message);
		}
	});
}

function startSimulator() {
	
	if (isSimulatorStarted){
		return;
	}
	
	$.ajax({
		url : "HomeController",
		data: {
			action : "startSimulator",
			courseName: courseName, 
			round: round
		},
		dataType : "text",
		async : false,
		success : function(data) {
			isSimulatorStarted = true;			
//			$("#marom-score").html(settings["initCapital"]);
//			$("#rakia-score").html(settings["initCapital"]);			
			getSettings(courseName);
			$("#totalRounds").html(settings["rounds"]);
			$("#sessionsPerRound").html(settings["sessionsPerRound"]);
			$("#round").html(round);
			finishRound = settings["roundTime"] * (settings["lastRoundDone"] + 1);
			
			getEvents();
			getTime();
			showPastEvents();
			
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
	var start = (new Date).getTime();
	$.ajax({
		url : "HomeController",
		data : {
			action : "getTime"
		},
		dataType : "json",
		async : false,
		success : function(data) {
			var latency = Math.round ( (((new Date).getTime() - start)/2)/1000 );
			var remainingClock = data.remainingClock;
//			var serverTime = new Date(data.serverTime);
//
//			client_time = new Date();
//			offset = (client_time.getTime() - serverTime.getTime())/1000;
//			if (offset < 0) {
//				offset = offset * -1;
//			}
			
			elapsedTime = Math.floor(data.elapsedClock + latency); 
			elapsedRunTime = Math.floor(data.elapsedRunTime + latency);
			showTime = Math.floor(remainingClock + latency);
			
			var elaspedSessions =  Math.floor(elapsedTime / settings["sessionTime"]);
			var elapsedInSession = elapsedTime - (elaspedSessions * settings["sessionTime"]);
			isRunTime = (elapsedInSession > settings["pauseTime"]);
			
			// must be after getSettings()
			if (isRunTime){
				pausePercentage = settings["pauseTime"] / settings["sessionTime"] * 100;
				runPercentage = (elapsedInSession - settings["pauseTime"]) / settings["sessionTime"] * 100;
			} else {
				// in pause time
				runPercentage = 0;
				pausePercentage = (elapsedInSession / settings["sessionTime"]) * 100;
			}
			
			console.log("runPercentage: " + runPercentage);
			console.log("pausePercentage: " + pausePercentage);
			
//			console.log("remainingClock " +remainingClock);
//			console.log("latency: "+ latency);
		},
		error : function(e) {
			console.log("js:getTime: Error in getting time.");
		}
	});
}

function pauseSimulator(){
	$.ajax({
		url : "HomeController",
		data : {
			action : "pauseSimulator"
		},
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
		url : "HomeController",
		data : {
			action : "resumeSimulator"
		},
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
	
//	console.log("incrementClock: show time=" + showTime);
	$('#main-time').html(showTime.toHHMMSS());
	showTime -= 1;
	
	showEventsInTime();
	elapsedTime++;
	console.log("incrementClock: elapsed time=" + elapsedTime);
	
	if (isRunTime){
		runPercentage = (settings["runTime"] - showTime) / settings["sessionTime"] * 100;
		elapsedRunTime++;
	} else{
		pausePercentage = (settings["pauseTime"] - showTime) / settings["sessionTime"] * 100;
	}
	
	var idRun = '.progress-bar-success';
	var idPause = '.progress-bar-danger';
	
	$(idRun).css('width', runPercentage+'%').attr('aria-valuenow', runPercentage);
	$(idPause).css('width', pausePercentage+'%').attr('aria-valuenow', pausePercentage);

	if ((elapsedTime + settings["runTime"]) % (settings["sessionTime"]) == 0) {
		// finished pause time
		isRunTime = true;
		showTime = settings["runTime"];

	} else if (elapsedTime % (settings["sessionTime"]) == 0) {
		// finished run time
		isRunTime = false;
		showTime = settings["pauseTime"];


		// finished session
		runPercentage = 0;
		pausePercentage = 0;
		clearEvents();
		if (session < settings["sessionsPerRound"]){
			session++;
		};
		
		console.log("session: " + session);
		$('#session').html(session);

		if (elapsedTime % settings["roundTime"] == 0) {
			// finished round
			console.log("finished");
			$('#main-time').html("00:00:00");
			clearInterval(clockInterval);
			solutionEventSource.removeEventListener('message',solutionListener);
			profitEventSource.removeEventListener('message',profitListener);
		}
	}
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
};
