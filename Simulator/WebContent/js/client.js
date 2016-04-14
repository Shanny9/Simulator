/**
 * 
 */

var solutionsData;
var team = "Marom"; //TODO: change this
var courseName = 'IDF-AMAM-01'; //TODO: change this

$(document).ready(function() {
    /*
    initialize page
    */
	$.backstretch("./css/home_images/airplane-login.jpg"); //Fullscreen background
	getSolutions(); // init solutionsData
	$("#submit").click(sendSolution);
	
});

function sendSolution (){
	var eventID = $('#eventID').val();
	var sol = $('#solution').val();
	var isCorrect = false;
	
	$.each(solutionsData, function(i, item) {
		if(item.event == eventID && item.solution == sol){
			isCorrect = true;
			return false; // << break
		}
		
	});//end for each
	
	if(isCorrect){
/*		$.ajax({
			url : "ClientController?action=sendSolution",
			dataType : "json",
			data: {eventID: eventID, solution: sol},
			success : function(data) {
				
			},
			error : function(e) {
				console.log("js:sendSolution: Error in sendSolution.");
			}
		});*/
		console.log("Correct!");
	}
		
}

function getSolutions() {
	$.ajax({
		async: false,
		url : "ClientController?action=getSolutions",
		dataType : "json",
		
		data: {team: team},
		success : function(data) {
			solutionsData = data;
			console.log("js:getSolutions: Success!");
		},
		error : function(e) {
			console.log("js:getSolutions: Error in getSolutions.");
		}
	});
}