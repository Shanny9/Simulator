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
    $('.login-form input[type="text"], .login-form textarea').on('focus', function() {
    	$(this).removeClass('input-error');
    });
	getSolutions(); // init solutionsData
	$("#submit").click(sendSolution);
	
});

function sendSolution (){
	var ciID = $('#ciID').val();
	var sol = $('#solution').val();
	var isCorrect = false;
	
	if(ciID != "" && sol!=""){
		$.each(solutionsData, function(i, item) {
			if(item.ciID == ciID && item.solution == sol){
				isCorrect = true;
				return false; // << break
			}
			
		});//end for each
		
		if(isCorrect){
	/*		$.ajax({
				url : "ClientController?action=sendSolution",
				dataType : "json",
				data: {team: team, course: courseName, ciID: ciID, solution: sol},
				success : function(data) {
					
				},
				error : function(e) {
					console.log("js:sendSolution: Error in sendSolution.");
				}
			});*/

		}
		else{	    	
			$("#feedback").addClass('wrong');
			$("#feedback").html("Wrong");
			return false;
		}
	}//end <if fields are empty>	
	else{
    	if( ciID == "" ) {
    		$('#ciID').addClass('input-error');
    	}
    	if( sol == "" ) {
    		$('#solution').addClass('input-error');
    	}

    	return false;
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