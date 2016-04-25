/**
 * 
 */
var finishRound;
var solutionsData;
var clockInterval;
var isRunTime;
var showTime;
var session;
var elapsedTime;
var gp = new Object();
var team = "Marom"; // TODO: change this
var courseName = 'IDF-AMAM-01';

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
			
			// when a key released in the incident field - updates if the menus could toggle
			$("#incidentID").on('keyup', function () {
				// checks if incident field is empty
			    if ($('#incidentID').val()==""){
			    	$(".collapse-menu").removeAttr('data-toggle'); //disables toggle
			    } else{
			    	$(".collapse-menu").attr('data-toggle',"collapse"); //enables toggle
			    }
			});
			
			// when the solve menu is clicked - handles the event 
			$("#solveMenu").click(function () {
				// checks if incident field is empty
				if ($("#incidentID").val() == ""){
					$("#noIncId").slideToggle("slow").delay(2000).slideToggle("slow");
					return;
				}
				// checks if incident is currently open
				if (checkIncident()==false){
					$("#wrongIncId").slideToggle("slow").delay(2000).slideToggle("slow");
					return;
				}
				
				//checks if the menu is going to expand
				var willExpand = $("#solveMenu").attr("aria-expanded") != 'true';
				if (willExpand){
					$('#incidentID').attr('readonly', 'readonly'); //locks the incident field
					$('#solutionID').val(""); //empties the solution field
					$('#submitSol').attr('disabled'); //disables the submit button
				} else{
					$('#incidentID').removeAttr('readonly'); //unlocks the incident field
				}
			});
			
			// when the purchase menu is clicked - handles the event 
			$("#purchaseMenu").click(function () {
				// checks if incident field is empty
				if ($("#incidentID").val() == ""){
					$("#noIncidentId").slideToggle("slow").delay(2000).slideToggle("slow");
					return;
				}
				
				//checks if the menu is going to expand
				var willExpand = $("#purchaseMenu").attr("aria-expanded") != 'true';
				if (willExpand){
					$('#incidentID').attr('readonly', 'readonly');//locks the incident field
					showPrice();
				} else{
					$('#incidentID').removeAttr('readonly'); //unlocks the incident field
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
				var id = (checkSolution())? "#success" : "#failure";
				$("#gif").slideToggle("slow").css("display","block").delay(1000).slideToggle("slow");
				$(id).delay(3000).slideToggle("slow").delay(3000).slideToggle("slow"); // shows success/failure message
				setTimeout(function () {
					$('.panel-collapse.in').collapse('hide'); //collapses the menu
					$('#incidentID').val(""); //empties the incident field
					$('#incidentID').removeAttr('readonly'); // unlocks the incident field 
					$("#solveMenu").removeAttr('data-toggle'); //disables solve menu to toggle 
				}, 3000);
				
			});
			
			$("#buy").click(buySolution);
			startSimulator();

		});

function showPrice(){
	var inc_id = $('#incidentID').val();
	var cost;
	var currency;
	var currencyTag;
	
	$.each(solutionsData, function(i, item) {
		if (i == item.incident_id) {
			cost = item.solution_cost;
			currency = item.currency;
			switch (currency){
			case "NIS": currencyTag = 'ils';
				break;
			case "USD": currencyTag = 'dollar';
				break;
			case "EUR": currencyTag = 'euro';
				break;
			}
			$('solCost').html(solution_cost + ' <i class="fa fa-' + currencyTag + '></i>')
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

function incrementClock() {
//	console.log("incrementClock: elapsed time=" + elapsedTime);
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

			if (elapsedTime % gp["roundTime"] == 0) {
				// finished round
				console.log("finished");
				$('#main-time').html("00:00:00");
				clearInterval(clockInterval);
			}
		}
	}
}

function startSimulator() {
	getGP(courseName);
	finishRound = gp["roundTime"] * (gp["currentRound"] + 1);
	getTime();
	clockInterval = setInterval(incrementClock, 1000);
}

function checkIncident(){
	var inc_id = $('#incidentID').val();
	
	// first check - does the incident exist
	$.each(solutionsData, function(i, item) {
		if (i == item.incident_id) {
			return true;
			}
	});
	
	// second check - is the incident currently open
	$.ajax({
		url : "ClientController?action=checkIncident",
		dataType : "json",
		data : {
			team : team,
			inc_id : inc_id,
			time : elapsedTime
		},
		async : false,
		success : function(data) {
			if (data=="true"){
				return true;
			}
			return false;
		},
		error : function(e) {
			console.log("js:checkIncident: Error in checking incidents.");
		}
	});
	return false;
}

// checks if the solution is correct
function checkSolution(){
	var inc_id = $('#incidentID').val();
	var sol = $('#solutionID').val();
	$.each(solutionsData, function(i, item) {
		if (i == item.incident_id) {
			if (team == "Marom" && item.solution_marom == sol){
				return true;
			} else if (team == "Rakia" && item.solution_rakia == sol){
				return true;
			}
		}
	});
	return false;
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
	var inc_id = $('#incidentID').val();
	var sol = $('#solutionID').val();

	$.ajax({
		url : "ClientController?action=sendSolution",
		dataType : "json",
		data : {
			team : team,
			inc_id : inc_id,
			time : elapsedTime
		},
		success : function(msg) {
			console.log(msg);
		},
		error : function(e) {
			console.log("js:sendSolution: Error in sendSolution.");
		}
	});
		return false;
}

// sends the purchase to the server
function buySolution() {

	$.ajax({
		url : "ClientController?action=buySolution",
		dataType : "json",
		data : {
			team : team,
			inc_id : inc_id,
			time : elapsedTime
		},
		success : function(msg) {
			$('#success').delay(3000).slideToggle("slow").delay(3000).slideToggle("slow"); // shows success message
		},
		error : function(e) {
			console.log("js:sendSolution: Error in buySolution.");
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