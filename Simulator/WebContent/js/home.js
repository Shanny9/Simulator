/**
 * The elapsed RUN TIME from the beginning of the round.
 */
var elapsedRunTime = 0;
/**
 * The time to show (remaining time until nearest pause/run time).
 */
var showTime;
/**
 * The TOTAL TIME elapsed from the beginning of the round.
 */
var elapsedTime = 0;
/**
 * The current session in the simulated round.
 */
var session = 1;
/**
 * Indicates whether the current time is run time or pause time.
 */
var isRunTime = false;
/**
 * The settings of the course. see {@code Settings} class.
 */
var settings = new Object();
/**
 * The events sent from the servlet. contains attributes: {@code time} and
 * {@code event_id}.
 */
var eventsData = new Object();
/**
 * The timer of the clock (ticks every second).
 */
var clockInterval;
/**
 * The number of events currently appearing on the screen.
 */
var eventsOnScreen = 0;
/**
 * The proportion of the elapsed RUN TIME in current run time.
 */
var runPercentage;
/**
 * The proportion of the elapsed PAUSE TIME in current run time.
 */
var pausePercentage;
/**
 * The color of the winning team (green).
 */
var winnerColor = '#00ff00';
/**
 * The color the loosing team (red).
 */
var looserColor = '#ff0000';
/**
 * A neutral color (orange).
 */
var regularColor = '#FF9900';
/**
 * Indicates whether the simulation has already started.
 */
var isSimulatorStarted = false;
/**
 * The current score of Marom team.
 */
var maromScore;
/**
 * The current score of Rakia team.
 */
var rakiaScore;
/**
 * Indicates whether the round is finished.
 */
var isFinishedRound = false;
/**
 * The event source of solutions sent from the servlet.
 */
var solutionEventSource;
/**
 * The event source of profits/scores sent from the servlet.
 */
var profitEventSource;
/**
 * Listens to solutions sent from the servlet and updates the color of the
 * event.
 */
var solutionListener = function(e) {

	var data = JSON.parse(e.data);
	var column = (data.team == "Marom") ? 0 : 1;

	for (var row = 1; row <= 12; row++) {
		var eventOnBoard = $(
				".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(1)")
				.eq(column).html();
		if ($.inArray(eventOnBoard, data.events) > -1) {
			$(".score-tbl tbody tr:nth-child(" + row + ")").eq(column)
					.removeClass("danger");
			$(".score-tbl tbody tr:nth-child(" + row + ")").eq(column)
					.addClass("success");
		}
	}
	console.log("team= " + data.team + ", events= " + data.events);
};
/**
 * Listens to profits/scores sent from the servlet, updates the screen and
 * recolors them accordingly.
 */
var profitListener = function(e) {

	var data = JSON.parse(e.data);
	$.each(data, function(i, obj) {
		if (obj.team == 'marom') {
			maromScore = obj.profit;
		} else {
			rakiaScore = obj.profit;
		}
		var scoreId = '#' + obj.team + '-score';
		$(scoreId).html(obj.profit.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
	});

	var marom = '#marom-score';
	var rakia = '#rakia-score';

	if (maromScore > rakiaScore) {
		$(marom).css('color', winnerColor);
		$(rakia).css('color', looserColor);
	} else if (maromScore < rakiaScore) {
		$(rakia).css('color', winnerColor);
		$(marom).css('color', looserColor);
	} else {
		$(marom).css('color', regularColor);
		$(rakia).css('color', regularColor);
	}
};

$(document).ready(function() {

	// sets the background picture to fullscreen.
	$.backstretch("./css/home_images/runway.jpg");

	// checks whether the simulator has started in server side. If so, starts
	// the simulator here too.
	checkSimulator();

	// manages the mode of start/pause/resume button.
	$("#funcBtn").click(function() {
		var theClass = $("#funcBtn span").attr('class');
		if (theClass === "start") {
			startSimulator();
			setPauseBtn();
		}
		if (theClass === "pause" && !isFinishedRound) {
			pauseSimulator();
			setResumeBtn();
		}
		if (theClass === "resume" && !isFinishedRound) {
			resumeSimulator();
			setPauseBtn();
		}
		if (isFinishedRound) {
			setFinish();
		}
	});
	console.log("END Document ready");
});

/**
 * sets the start/pause/resume button to PAUSE mode.
 */
function setPauseBtn() {
	$(".start").removeClass("start").addClass("pause").html("PAUSE");
	$(".resume").removeClass("resume").addClass("pause").html("PAUSE");

}
/**
 * sets the start/pause/resume button to RESUME mode.
 */
function setResumeBtn() {
	$(".pause").removeClass("pause").addClass("resume").html("RESUME");
}

/**
 * sets the start/pause/resume button to FINISH mode.
 */
function setFinish() {
	$(".start").removeClass("start").addClass("finish").html("FINISHED");
	$(".resume").removeClass("resume").addClass("finish").html("FINISHED");
	$(".pause").removeClass("pause").addClass("finish").html("FINISHED");
	$("#funcBtn").css("cursor", "default");
}

/**
 * checks whether the simulator has started. If so, calls {@code startSimulator}
 * method.
 */
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

/**
 * Bonds the {@code solutionEventSource} variable to the solution stream sent
 * from the servlet.
 */
function setSolutionSource() {
	solutionEventSource = new EventSource(
			"HomeController?action=solutionStream");
	solutionEventSource.addEventListener('message', solutionListener, false);
}

/**
 * Bonds the {@code profitEventSource} variable to the profit/score stream sent
 * from the servlet.
 */
function setProfitSource() {
	profitEventSource = new EventSource("HomeController?action=profitStream");
	profitEventSource.addEventListener('message', profitListener, false);
}

/**
 * Fetches events from the servlet and puts them in {@code eventsData} variable.
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

/**
 * Presents the events that start NOW.
 */
function showEventsInTime() {
	$.each(eventsData, function(i, item) {
		if (elapsedRunTime == item.time) {
			var row = eventsOnScreen + 1;
			$(".score-tbl tbody tr:nth-child(" + row + ")").addClass("danger");
			$(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(1)")
					.html(item.event_id);
			$(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(2)")
					.html(item.time.toHHMMSS());
			delete eventsData[i];
			eventsOnScreen++;
		}
	});
}

/**
 * Presents events that should appear now (in case of refresh etc.).
 */
function showSessionEvents() {
	var sessionStartTime = (round - 1) * settings["roundTime"] + 1;
	$.each(eventsData, function(i, item) {
		if (item.time <= elapsedTime && item.time >= sessionStartTime) {
			var row = eventsOnScreen + 1;
			$(".score-tbl tbody tr:nth-child(" + row + ")").addClass("danger");
			$(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(1)")
					.html(item.event_id);
			$(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(2)")
					.html(item.time.toHHMMSS());
			delete eventsData[i];
			eventsOnScreen++;
		}
	});
}

/**
 * Clears all events from the screen (when session ends).
 */
function clearEvents() {
	for (var row = 1; row < 12; row++) {
		$(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(1)").html(
				"&nbsp");
		$(".score-tbl tbody tr:nth-child(" + row + ") td:nth-child(2)").html(
				"&nbsp");
		$(".score-tbl tbody tr:nth-child(" + row + ")").removeClass("success");
		$(".score-tbl tbody tr:nth-child(" + row + ")").addClass("danger");
		eventsOnScreen = 0;
	}
}

/**
 * Fetches the course settings from the servlet and pits them in
 * {@code settings} varivale.
 */
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

/**
 * Starts the simulation in the home screen (after it already started int the
 * server side). This method starts a chain of methods: {@code getEvents},
 * {@code getTime}, {@code showSessionEvents}, {@code setSolutionSource} and
 * {@code setProfitSource}.
 */
function startSimulator() {

	if (isSimulatorStarted) {
		return;
	}

	$.ajax({
		url : "HomeController",
		data : {
			action : "startSimulator",
			courseName : courseName,
			round : round
		},
		dataType : "text",
		async : false,
		success : function(data) {
			isSimulatorStarted = true;
			getSettings(courseName);
			$("#totalRounds").html(settings["rounds"]);
			$("#sessionsPerRound").html(settings["sessionsPerRound"]);
			$("#round").html(round);

			getEvents();
			getTime();
			showSessionEvents();

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

/**
 * Fetches clocks sent from the server ({@code elapsedClock},
 * {@code remainingClock}, {@code elapsedRunTime}, {@code isRunTime} and
 * {@code serverTime}), synchronizes the clinet-side clock with the server
 * clock and update the percentage bar.
 */
function getTime() {
	var start = (new Date).getTime();
	$
			.ajax({
				url : "HomeController",
				data : {
					action : "getTime"
				},
				dataType : "json",
				async : false,
				success : function(data) {
					var latency = Math
							.round((((new Date).getTime() - start) / 2) / 1000);
					var remainingClock = data.remainingClock;

					elapsedTime = Math.floor(data.elapsedClock + latency);
					elapsedRunTime = Math.floor(data.elapsedRunTime + latency);
					showTime = Math.floor(remainingClock + latency);

					var elaspedSessions = Math.floor(elapsedTime
							/ settings["sessionTime"]);
					var elapsedInSession = elapsedTime
							- (elaspedSessions * settings["sessionTime"]);
					isRunTime = (elapsedInSession > settings["pauseTime"]);

					// must be after getSettings()
					if (isRunTime) {
						pausePercentage = settings["pauseTime"]
								/ settings["sessionTime"] * 100;
						runPercentage = (elapsedInSession - settings["pauseTime"])
								/ settings["sessionTime"] * 100;
					} else {
						// in pause time
						runPercentage = 0;
						pausePercentage = (elapsedInSession / settings["sessionTime"]) * 100;
					}

					console.log("runPercentage: " + runPercentage);
					console.log("pausePercentage: " + pausePercentage);

				},
				error : function(e) {
					console.log("js:getTime: Error in getting time.");
				}
			});
}

/**
 * Sends the servlet a request to pause the simulator, and pauses it in this
 * screen.
 */
function pauseSimulator() {
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

/**
 * Sends the servlet a request to resume the simulator, and resumes it in this
 * screen.
 */
function resumeSimulator() {
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

/**
 * Include all actions that should occur every second.
 */
function incrementClock() {

	// console.log("incrementClock: show time=" + showTime);
	$('#main-time').html(showTime.toHHMMSS());
	showTime -= 1;

	showEventsInTime();
	elapsedTime++;
	console.log("incrementClock: elapsed time=" + elapsedRunTime);

	if (isRunTime) {
		runPercentage = (settings["runTime"] - showTime)
				/ settings["sessionTime"] * 100;
		elapsedRunTime++;
	} else {
		pausePercentage = (settings["pauseTime"] - showTime)
				/ settings["sessionTime"] * 100;
	}

	var idRun = '.progress-bar-success';
	var idPause = '.progress-bar-danger';

	$(idRun).css('width', runPercentage + '%').attr('aria-valuenow',
			runPercentage);
	$(idPause).css('width', pausePercentage + '%').attr('aria-valuenow',
			pausePercentage);

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
		if (session < settings["sessionsPerRound"]) {
			session++;
		}

		console.log("session: " + session);
		$('#session').html(session);

		if (elapsedTime % settings["roundTime"] == 0) {
			// finished round
			isFinishedRound = true;
			setFinish();
			console.log("finished");
			$('#main-time').html("00:00:00");
			clearInterval(clockInterval);
			solutionEventSource
					.removeEventListener('message', solutionListener);
			profitEventSource.removeEventListener('message', profitListener);
		}
	}
}

/**
 * converts a number in seconds to hh:mm:ss format (e.g.: 90 will be converted
 * to 1:30).
 */
Number.prototype.toHHMMSS = function() {
	var sec_num = parseInt(this, 10); // don't forget the second param
	var hours = Math.floor(sec_num / 3600);
	var minutes = Math.floor((sec_num - (hours * 3600)) / 60);
	var seconds = sec_num - (hours * 3600) - (minutes * 60);

	if (hours < 10) {
		hours = "0" + hours;
	}
	if (minutes < 10) {
		minutes = "0" + minutes;
	}
	if (seconds < 10) {
		seconds = "0" + seconds;
	}
	var time = hours + ':' + minutes + ':' + seconds;
	return time;
};

var connection = true;
window.setInterval(function() {
	$.ajax({
		cache : false,
		dataType : 'text',
		url : "HomeController",
		data : {
			action : "isAlive"
		},
		timeout : 1000,
		success : function(data) {
			if (!connection) {
				clockInterval = setInterval(incrementClock, 1000);
				connection = true;
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			if (connection) {
				alert("Lost connection to the server");
				clearInterval(clockInterval);
			}
			connection = false;
		}
	});
}, 3000);
