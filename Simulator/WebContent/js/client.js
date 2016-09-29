/**
 * 
 */
var finishRound;
var solutionsData = new Object();
var clockInterval;
var isRunTime = false;
var showTime;
var session;
var elapsedTime = 0;
var settings = new Object();
var currentRound; // init at startSimulator
//var team = "Marom"; // defined at the beginning of client.jsp
//var courseName = 'normalCourse'; // defined at the beginning of client.jsp

var pauseEventSource;
var pauseListener = function(e) {
	
	var data = e.data;
	console.log(data);
	if (data == 'pause'){
		clearInterval(clockInterval);
	} else if (data == 'resume'){
		clockInterval = setInterval(incrementClock, 1000);
	}
}

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
			getSolutions();
			setPauseSource();
			run(isRunTime);
			
			// when a key released in the ci field - updates if the menus could toggle
			$("#ciId").on('keyup', function () {
				// checks if ci field is empty
			    if ($('#ciId').val()==""){
			    	$(".collapse-menu").removeAttr('data-toggle'); //disables toggle
			    } else{
			    	$(".collapse-menu").attr('data-toggle',"collapse"); //enables toggle
			    }
			});
			
			// when the solve menu is clicked - handles the event 
			$("#solveMenu").click(function () {
				// checks if ci field is empty
				if ($("#ciId").val() == ""){
					$("#noCiId").slideToggle("slow").delay(2000).slideToggle("slow");
					return;
				}
				// checks if ci is currently open
				if (checkCi()==false){
					$("#wrongCiId").slideToggle("slow").delay(2000).slideToggle("slow");
					$(".collapse-menu").removeAttr('data-toggle'); //disables toggle
					return;
				}
				
				//checks if the menu is going to expand
				var willExpand = $("#solveMenu").attr("aria-expanded") != 'true';
				if (willExpand){
					$('#ciId').attr('readonly', 'readonly'); //locks the ci field
					$('#solutionID').val(""); //empties the solution field
					$('#submitSol').attr('disabled'); //disables the submit button
				} else{
					$('#ciId').removeAttr('readonly'); //unlocks the ci field
				}
			});
			
			// when the purchase menu is clicked - handles the event 
			$("#purchaseMenu").click(function () {
				
				// checks if ci field is empty
				if ($("#ciId").val() == ""){
					$("#noCiId").slideToggle("slow").delay(2000).slideToggle("slow");
					return;
				}
				
				// checks if ci is currently open
				if (checkCi()==false){
					$("#wrongCiId").slideToggle("slow").delay(2000).slideToggle("slow");
					$(".collapse-menu").removeAttr('data-toggle'); //disables toggle
					return;
				}

				//checks if the menu is going to expand
				var willExpand = $("#purchaseMenu").attr("aria-expanded") != 'true';
				if (willExpand){
					$('#ciId').attr('readonly', 'readonly');//locks the ci field
					showPrice();
				} else{
					$('#ciId').removeAttr('readonly'); //unlocks the ci field
				}
			});
			
			// when a key released in the solution field - updates if the submit button will be enabled
			$("#solutionID").on('keyup', function () {
				if ($('#solutionID').val() != ""){
					$('#submitSol').removeAttr('disabled');
				} else{
					$('#submitSol').attr('disabled');
				}	
			});
			
			// submitting solution
			$("#submitSol").click(function(){
				
				if (checkSolution()){
					sendSolution();
					id = '#success';
				} else{
					id = '#failure';
				}
				
				$("#gif").slideToggle("slow").css("display","block").delay(1000).slideToggle("slow");
				$(id).delay(3000).slideToggle("slow").delay(3000).slideToggle("slow"); // shows success/failure message
				setTimeout(function () {
					$('.panel-collapse.in').collapse('hide'); //collapses the menu
					$('#ciId').val(""); //empties the ci field
					$('#ciId').removeAttr('readonly'); // unlocks the ci field 
					$("#solveMenu").removeAttr('data-toggle'); //disables solve menu to toggle 
				}, 3000);
				
			});
			
			$("#buy").click(buySolution);
			checkSimulator();
//			startSimulator();

		});

function setPauseSource() {
	pauseEventSource = new EventSource("ClientController?action=pauseOrResume");
	pauseEventSource.addEventListener('message',pauseListener, false);
}

function run (isRun){
	
	disablePurchase(!isRun);
	disableSolve(!isRun);
}

function showPrice(){
	var ci_id = $('#ciId').val();
	var currencyTag;
	var cost;
	$.each(solutionsData, function(i, item) {
		if (ci_id == item.ci_id) {
			cost = item.solution_cost;
			switch (item.currency){
			case "NIS": currencyTag = 'ils';
				break;
			case "USD": currencyTag = 'dollar';
				break;
			case "EUR": currencyTag = 'euro';
				break;
			}
			$('#solCost').html(cost + ' <i class="fa fa-' + currencyTag + '"></i>');
		}
	});
}

function incrementClock() {
	
/*	// Disable purchasing during rounds 1 and 2.
	if(currentRound > 1)
		disablePurchase(true);
	else
		disablePurchase(false);*/
	
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
		if (session < settings["sessionsPerRound"]){
			session++;
		};

		if (elapsedTime % settings["roundTime"] == 0) {
			// finished round
			console.log("finished");
			$('#main-time').html("00:00:00");
			clearInterval(clockInterval);
		}
	}
}

function startSimulator() {
	getSettings(courseName); // must not be called outside&before startSimulator.
	currentRound = settings["currentRound"];//TODO:
	console.log(currentRound);
	finishRound = settings["roundTime"] * (settings["currentRound"] + 1);
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
			var latency = Math.round ( (((new Date).getTime() - start)/2)/1000 );
			var remainingClock = data.remainingClock;
//			var serverTime = new Date(data.serverTime);

//			client_time = new Date();
//			offset = (client_time.getTime() - serverTime.getTime())/1000;
//			if (offset < 0) {
//				offset = offset * -1;
//			}
			showTime =  Math.floor(remainingClock + latency);
			elapsedTime = Math.floor(data.elapsedClock + latency);
			console.log("remainingClock " +remainingClock);
			console.log("elapsed time " +elapsedTime);
			console.log("latency: "+ latency);
		},
		error : function(e) {
			console.log("js:getTime: Error in getting time.");
		}
	});
}

function checkCi(){
	var ci_id = $('#ciId').val();
	var doesExist = false;
	// first check - does the ci exist
	$.each(solutionsData, function(i, item) {
		if (i == item.ci_id) {
			doesExist = true;
			return;
		}
	});
	
	if (!doesExist){
		return false;
	}
	
	var isOpen = false;
	// second check - is the ci currently open
	$.ajax({
		url : "ClientController",
		dataType : "json",
		data : {
			action : "checkCi",
			team : team,
			ci_id : ci_id
		},
		async : false,
		success : function(data) {
			isOpen = data;
			console.log("checkCi: isOpen: "+ isOpen);
		},
		error : function(e) {
			console.log("js:checkCi: Error in checking cis.");
		}
	});
	return isOpen;
}

// checks if the solution is correct
function checkSolution(){
	var ci_id = $('#ciId').val();
	var sol = $('#solutionID').val();
	var isCorrect = false;
	$.each(solutionsData, function(i, item) {
		if (ci_id == item.ci_id) {
			if (team == "Marom" && item.solution_marom == sol){
				isCorrect = true;
				return;
			} else if (team == "Rakia" && item.solution_rakia == sol){
				isCorrect = true;
				return;
			}
		}
	});
	return isCorrect;
}

// gets the solutions from the server
function getSolutions() {
	$.ajax({
		url : "ClientController?action=getSolutions",
		dataType : "json",
		async : false,
		success : function(data) {
			$.each(data, function(key, value) {
				solutionsData[key] = value;
			});
		},
		error : function(e) {
			console.log("js:getSolutions: Error in getting solutions.");
		}
	});
}

//sends the solution to the server
function sendSolution() {
	var ci_id = $('#ciId').val();
	var sol = $('#solutionID').val();

	$.ajax({
		url : "ClientController?action=sendSolution",
		dataType : "json",
		data : {
			team : team,
			ci_id : ci_id,
		},
		success : function(msg) {
			console.log(msg);
		},
		error : function(e) {
			console.log("js:sendSolution: Error in sendSolution.");
		}
	});
}

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

// sends the purchase to the server
function buySolution() {

	var ci_id = $('#ciId').val();
	$.ajax({
		url : "ClientController",
		dataType : "json",
		data : {
			action : "buySolution",
			team : team,
			ci_id : ci_id
		},
		success : function(msg) {
			$('#success').delay(1000).slideToggle("slow").delay(3000).slideToggle("slow"); // shows success message
			setTimeout(function () {
				$('.panel-collapse.in').collapse('hide'); //collapses the menu
				$('#ciId').val(""); //empties the ci field
				$('#ciId').removeAttr('readonly'); // unlocks the ci field 
				$("#solveMenu").removeAttr('data-toggle'); //disables solve menu to toggle 
			}, 1000);
		},
		error : function(e) {
			//TODO: check why error
			$('#success').delay(1000).slideToggle("slow").delay(3000).slideToggle("slow"); // shows success message
			setTimeout(function () {
				$('.panel-collapse.in').collapse('hide'); //collapses the menu
				$('#ciId').val(""); //empties the ci field
				$('#ciId').removeAttr('readonly'); // unlocks the ci field 
				$("#solveMenu").removeAttr('data-toggle'); //disables solve menu to toggle 
			}, 1000);
			
			console.log("js:sendSolution: Error in buySolution.");
		}
	});
}

function disablePurchase(dis){
	if(dis){
		$(".btn-red").attr("disabled", true);
		$(".btn-red").css("background", "#727778");
	}
	else{
		$(".btn-red").attr("disabled", false);
		$(".btn-red").css("background", "#EC644B");
	}
}

function disableSolve(dis){
	if(dis){
		$(".btn-green").attr("disabled", true);
		$(".btn-green").css("background", "#727778");
	}
	else{
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