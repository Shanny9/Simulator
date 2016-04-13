/**
 * 
 */

var solutionsData;
var team = "Marom";

$(document).ready(function() {
	$("#submit").click(sendSolution);
});

function sendSolution (){
	var eventID;
	var sol;
	
	$.ajax({
		url : "ClientController?action=sendSolution",
		dataType : "json",
		data: {eventID: eventID, solution: sol},
		success : function(data) {
			
		},
		error : function(e) {
			console.log("js:sendSolution: Error in sendSolution.");
		}
	});
}

function getSolutions() {
	$.ajax({
		url : "ClientController?action=getSolutions",
		dataType : "json",
		data: {team: team},
		success : function(data) {
			solutionsData = data;
		},
		error : function(e) {
			console.log("js:getSolutions: Error in getSolutions.");
		}
	});
}