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
			getSolutions(); // init solutionsData
			
			$("#incidentID").on('keyup', function () {
			    if ($('#incidentID').val()==""){
			    	$("#solveMenu").removeAttr('data-toggle');
			    } else{
			    	$("#solveMenu").attr('data-toggle',"collapse");
			    }
			});
			
			$("#solveMenu").click(function () {
				var isReadOnly = ($('#incidentID').attr('readonly'));
				if (isReadOnly != undefined){
					$('#incidentID').removeAttr('readonly');
				} else{
					$('#incidentID').attr('readonly', 'readonly');
				}
			});

			$("#submitSol").click(sendSolution);
			$("#buy").click(buySolution);
			startSimulator();

		});

function sendSolution() {
	console.log("client.js: sent solution");
	var ciID = $('#incidentID').val();
	var sol = $('#solution').val();
	var isCorrect = false;

	if (ciID != "" && sol != "") {
		$.each(solutionsData, function(i, item) {
			if (item.ciID == ciID && item.solution == sol) {
				isCorrect = true; // TODO: is necessary?
				return false; // << break
			}

		});// end for each

		if (isCorrect) {
			$.ajax({
				url : "ClientController?action=sendSolution",
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
					console.log("js:sendSolution: Error in sendSolution.");
				}
			});

		} else {
			$("#feedback").addClass('wrong');
			$("#feedback").html("Wrong");
			return false;
		}
	}// end <if fields are empty>
//	else {
//		if (ciID == "") {
//			$('#ciID').addClass('input-error');
//		}
//		if (sol == "") {
//			$('#solution').addClass('input-error');
//		}
//
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