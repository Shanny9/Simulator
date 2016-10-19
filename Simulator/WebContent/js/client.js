/**
 * 
 */
var finishRound;
var priceList = new Object();
var clockInterval;
var isRunTime = false;
var showTime;
var session;
var elapsedTime = 0;
var settings = new Object();
var currentRound;
var isSolved = false;
// init at startSimulator
// var team = "Marom"; // defined at the beginning of client.jsp
// var courseName = 'normalCourse'; // defined at the beginning of client.jsp

var pauseEventSource;
var pauseListener = function(e) {

	var data = e.data;
	console.log(data);
	if (data == 'pause') {
		clearInterval(clockInterval);
	} else if (data == 'resume') {
		clockInterval = setInterval(incrementClock, 1000);
	}
};

$(document).ready(

		function() {
			/*
			 * initialize page
			 */
			$.backstretch("./css/home_images/airplane-login.jpg"); // Fullscreen
			// background
			$('.login-form input[type="text"], .login-form textarea').on(
					'focus', function() {
						$(this).removeClass('input-error');
					});
			setPauseSource();
			run(isRunTime);

			// when a key released in the ci field - updates if the menus could
			// toggle
			$("#questionId").on('keyup', function() {
				// checks if ci field is empty
				if ($('#questionId').val() == "") {
					// disables toggle
					$(".collapse-menu").removeAttr('data-toggle');
				} else {
					// enables toggle
					$(".collapse-menu").attr('data-toggle', "collapse");
				}
			});

			// when the solve menu is clicked - handles the event
			$("#solveMenu")
					.click(
							function() {
								// checks if ci field is empty
								if ($("#questionId").val() == "") {
									$("#noQuestionId").slideToggle("slow")
											.delay(2000).slideToggle("slow");
									return;
								}

								// checks if the menu is going to expand
								var willExpand = $("#solveMenu").attr(
										"aria-expanded") != 'true';
								if (willExpand) {
									// locks the ci field
									$('#questionId').attr('readonly', 'readonly');
									// empties the solution filed
									$('#solutionID').val("");
									// disables the submit button
									$('#submitSol').attr('disabled');
								} else {
									// unlocks the ci field
									$('#questionId').removeAttr('readonly');
								}
							});

			// when the purchase menu is clicked - handles the event
			$("#purchaseMenu").click(
					function() {
						// checks if ci field is empty
						if ($("#questionId").val() == "") {
							$("#noQuestionId").slideToggle("slow").delay(2000)
									.slideToggle("slow");
							return;
						}

						// checks if the menu is going to expand
						var willExpand = $("#purchaseMenu").attr(
								"aria-expanded") != 'true';
						if (willExpand) {
							// locks the question field
							$('#questionId').attr('readonly', 'readonly');
							showPrice();
						} else {
							// unlocks the question field
							$('#questionId').removeAttr('readonly');
						}
					});

			// when a key released in the solution field - updates if the submit
			// button will be enabled
			$("#solutionID").on('keyup', function() {
				if ($('#solutionID').val() != "") {
					$('#submitSol').removeAttr('disabled');
				} else {
					$('#submitSol').attr('disabled');
				}
			});

			// submitting solution
			$("#submitSol").click(
					function() {
						sendSolution();
						id = isSolved ? '#success' : '#failure';
						$("#gif").slideToggle("slow").css("display", "block")
								.delay(1000).slideToggle("slow");
						// shows success/failure message
						$(id).delay(3000).slideToggle("slow").delay(3000)
								.slideToggle("slow");
						setTimeout(function() {
							// collapses the menu
							$('.panel-collapse.in').collapse('hide');
							// empties the question field
							$('#questionId').val("");
							// unlocks the question field
							$('#questionId').removeAttr('readonly');
							// disables solve menu to toggle
							$("#solveMenu").removeAttr('data-toggle');
						}, 3000);

					});

			$("#buy").click(buySolution);
			getPriceList();
			checkSimulator();
		});

function setPauseSource() {
	pauseEventSource = new EventSource("ClientController?action=pauseOrResume");
	pauseEventSource.addEventListener('message', pauseListener, false);
}

function run(isRun) {

	disablePurchase(!isRun);
	disableSolve(!isRun);
}

function showPrice() {
	var question_id = $('#questionId').val();
	var currencyTag = "";
	var cost;
	$.each(priceList, function(i, item) {
		if (question_id == item.question) {
			cost = item.cost;
			switch (item.currency) {
			case "NIS":
				currencyTag = 'ils';
				break;
			case "USD":
				currencyTag = 'dollar';
				break;
			case "EUR":
				currencyTag = 'euro';
				break;
			}
			$('#solCost').html(
					cost + ' <i class="fa fa-' + currencyTag + '"></i>');
		}
	});
}

function incrementClock() {

	/*
	 * // Disable purchasing during rounds 1 and 2. if(currentRound > 1)
	 * disablePurchase(true); else disablePurchase(false);
	 */

	$('#main-time').html(showTime.toHHMMSS());
	showTime = (showTime - 1);
	elapsedTime++;

	if ((elapsedTime + settings["runTime"]) % (settings["sessionTime"]) == 0) {
		// finished pause time
		showTime = settings["runTime"];
		disablePurchase(false);
		disableSolve(false);

	} else if (elapsedTime % (settings["sessionTime"]) == 0) {
		// finished run time
		showTime = settings["pauseTime"];
		disablePurchase(true);
		disableSolve(true);

		// finished session
		if (session < settings["sessionsPerRound"]) {
			session++;
		}
		;

		if (elapsedTime % settings["roundTime"] == 0) {
			// finished round
			console.log("finished");
			$('#main-time').html("00:00:00");
			clearInterval(clockInterval);
		}
	}
}

function startSimulator() {
	// must not be called outside&before startSimulator.
	getSettings(courseName);
	getTime();
	clockInterval = setInterval(incrementClock, 1000);
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
			isRunTime = data.isRunTime;
			run(isRunTime);
			var latency = Math
					.round((((new Date).getTime() - start) / 2) / 1000);
			var remainingClock = data.remainingClock;
			showTime = Math.floor(remainingClock + latency);
			elapsedTime = Math.floor(data.elapsedClock + latency);

			currentRound = data["round"];
			console.log(currentRound);
			finishRound = settings["roundTime"] * (currentRound + 1);

			console.log("remainingClock " + remainingClock);
			console.log("elapsed time " + elapsedTime);
			console.log("latency: " + latency);
		},
		error : function(e) {
			console.log("js:getTime: Error in getting time.");
		}
	});
}

// get the prices of the questions
function getPriceList() {

	$.ajax({
		url : "ClientController",
		data : {
			action : "getPriceList",
		},
		dataType : "json",
		async : false,
		success : function(msg) {
			priceList = msg;
		},
		error : function(e) {
			console.log("js:getPriceList: Error in getPriceList.");
		}
	});
}

// sends the solution to the server
function sendSolution() {
	var question_id = $('#questionId').val();
	var sol = $('#solutionID').val();
	$.ajax({
		url : "ClientController?action=sendSolution",
		dataType : "json",
		async : false,
		data : {
			team : team,
			question_id : question_id,
			solution : sol,
		},
		success : function(msg) {
			isSolved = msg.message;
		},
		error : function(e) {
			console.log("js:sendSolution: Error in sendSolution.");
		}
	});
}

// checks if the simulator has started
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

// sends the purchase to the server
function buySolution() {

	var question_id = $('#questionId').val();
	$.ajax({
		url : "ClientController",
		dataType : "json",
		data : {
			action : "buySolution",
			team : team,
			question_id : question_id
		},
		success : function(msg) {
			// shows success message
			$('#success').delay(1000).slideToggle("slow").delay(3000)
					.slideToggle("slow");
			setTimeout(function() {
				// collapses the menu
				$('.panel-collapse.in').collapse('hide');
				// empties the ci field
				$('#questionId').val("");
				// unlocks the ci field
				$('#questionId').removeAttr('readonly');
				// disables solve menu to toggle
				$("#solveMenu").removeAttr('data-toggle');
			}, 1000);
		},
		error : function(e) {
			// shows success message
			$('#success').delay(1000).slideToggle("slow").delay(3000)
					.slideToggle("slow");
			setTimeout(function() {
				// collapses the menu
				$('.panel-collapse.in').collapse('hide');
				// empties the ci field
				$('#questionId').val("");
				// unlocks the ci field
				$('#questionId').removeAttr('readonly');
				// disables solve menu to toggle
				$("#solveMenu").removeAttr('data-toggle');
			}, 1000);
			console.log("js:sendSolution: Error in buySolution.");
		}
	});
}

function disablePurchase(dis) {
	if (dis) {
		$(".btn-red").attr("disabled", true);
		$(".btn-red").css("background", "#727778");
	} else {
		$(".btn-red").attr("disabled", false);
		$(".btn-red").css("background", "#EC644B");
	}
}

function disableSolve(dis) {
	if (dis) {
		$(".btn-green").attr("disabled", true);
		$(".btn-green").css("background", "#727778");
	} else {
		$(".btn-green").attr("disabled", false);
		$(".btn-green").css("background", "#2ECC71");
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