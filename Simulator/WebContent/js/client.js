/**
 * 
 */

var solutionsData;
var team = "Marom"; // TODO: change this

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
			
			// lets toggle the solve menu only if there is an incident id
			$("#incidentID").on('keyup', function () {
			    if ($('#incidentID').val()==""){
			    	$(".collapse-menu").removeAttr('data-toggle');
			    } else{
			    	//TODO: check if the incident is open right now
			    	$(".collapse-menu").attr('data-toggle',"collapse");
			    }
			});
			
			// locks the incident input field when solve menu is open
			$("#solveMenu").click(function () {
				if ($("#incidentID").val() == ""){
					$("#noIncidentId").slideToggle("slow").delay(2000).slideToggle("slow");
					return;
				}
				var willExpand = $("#solveMenu").attr("aria-expanded") != 'true';
				if (willExpand){
					$('#incidentID').attr('readonly', 'readonly');
					$('#solutionID').val("");
					$('#submitSol').attr('disabled');
				} else{
					$('#incidentID').removeAttr('readonly');
				}
			});
			
			// locks the incident input field when purchase menu is open
			$("#purchaseMenu").click(function () {
				if ($("#incidentID").val() == ""){
					$("#noIncidentId").slideToggle("slow").delay(2000).slideToggle("slow");
					return;
				}
				var willExpand = $("#purchaseMenu").attr("aria-expanded") != 'true';
				if (willExpand){
					$('#incidentID').attr('readonly', 'readonly');
					showPrice();
				} else{
					$('#incidentID').removeAttr('readonly');
				}
			});
			
			// enables sending solution only when solution id is not empty
			$("#solutionID").on('keyup', function () {
				if ($('#solutionID').val() != ""){
					$('#submitSol').removeAttr('disabled');
				} else{
					$('#submitSol').attr('disabled');
				}	
			});
			
			// submitting solution
			$("#submitSol").click(function(){
//				var isCorrect = checkSolution();
				isCorrect = true;
				var id = (isCorrect)? "#success" : "failure";
				$("#gif").slideToggle("slow").css("display","block").delay(1000).slideToggle("slow");
				$(id).delay(3000).slideToggle("slow").delay(3000).slideToggle("slow");
				setTimeout(function () {
					$('.panel-collapse.in').collapse('hide');
					$('#incidentID').val("");
					$("#solveMenu").removeAttr('data-toggle');
					$('#incidentID').removeAttr('readonly');
				}, 3000);
				
			});
			
//			$("#buy").click(buySolution);
//			startSimulator();

		});

function showPrice(){
	var incID = $('#incidentID').val();
	var cost;
	var currency;
	var currencyTag;
	
	$.each(solutionsData, function(i, item) {
		if (i == item.incID) {
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

function checkSolution(){
	var incID = $('#incidentID').val();
	var sol = $('#solutionID').val();
	$.each(solutionsData, function(i, item) {
		if (i == item.incID) {
			if (team == "Marom" && item.solution_marom == sol){
				return true;
			} else if (team == "Rakia" && item.solution_rakia == sol){
				return true;
			}
		}
	});
	return false;
}

function getSolutions() {
	$.ajax({
		url : "ClientController?action=getSolutions",
		dataType : "json",
		async : false,
		success : function(data) {
			solutionsData = data;
		},
		error : function(e) {
			console.log("js:getSolutions: Error in getting solutions.");
		}
	});
}

function sendSolution() {
	var incID = $('#incidentID').val();
	var sol = $('#solutionID').val();

	$.ajax({
		url : "ClientController?action=sendSolution",
		dataType : "json",
		data : {
			team : team,
			incID : ciID,
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
//	}
}

function buySolution() {

	if (ciID != "") {
		$.each(solutionsData, function(i, item) {
			if (item.ciID == ciID) {
				isCorrect = true; // TODO: is necessary?
				return false; // << break
			}

		});// end for each

	}

	if (isCorrect) {
		$.ajax({
			url : "ClientController?action=buySolution",
			dataType : "json",
			data : {
				team : team,
				ciID : ciID,
				time : elapsedTime
			},
			success : function(msg) {
				console.log(msg);
			},
			error : function(e) {
				console.log("js:sendSolution: Error in buySolution.");
			}
		});
	}// end <if fields are empty>
	else {
		$('#ciID').addClass('input-error');
		return false;
	}
}

function getSolutions() {
	$.ajax({
		async : false,
		url : "ClientController?action=getSolutions",
		dataType : "json",

		data : {
			team : team
		},
		success : function(data) {
			solutionsData = data;
			console.log("js:getSolutions: Success!");
		},
		error : function(e) {
			console.log("js:getSolutions: Error in getSolutions.");
		}
	});
}