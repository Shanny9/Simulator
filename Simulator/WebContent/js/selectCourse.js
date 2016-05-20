/**
 * 
 */

$(document).ready(function() {
	/*
	 * Fullscreen background
	 */
	$.backstretch("./css/home_images/runway.jpg");
	getCourses();

	$('#form-courseName').change(function() {
		checkLog($(this).val());
	});

});

function checkLog(directory) {

	$('#form-round').find('option').remove().end();

	$.ajax({
		url : "HomeController?action=checkLog",
		data : {
			"directory" : directory
		},
		dataType: "json", 
		success : function(rounds) {
			if (rounds > 0) {
				disable($('#start'), false);
				disable($('#form-round'), false);
				setRounds(rounds);
				
			} else {
				disable($('#start'), true);
				disable($('#form-round'), true);
				if (directory != "") {
					$('#err').slideToggle("slow").delay(3000).slideToggle("slow"); // shows success message
				}
			}
		},
		error : function(e) {
			console.log("js: Error in checkLog");
		}
	});
}

function setRounds(rounds) {
	for (var r = 1; r <= rounds; r++) {
		$('#form-round').append($("<option />").text(r).val(r));
	}
}

function disable(obj, dis) {
	if (dis) {
		obj.attr("disabled", true);
	} else {
		obj.attr("disabled", false);
	}
}

function getCourses() {

	$.ajax({
		url : "HomeController?action=getCourses",
		datatype : "json",
		success : function(names) {
			$.each(names, function(i, name) {
				$('#form-courseName').append(
						$("<option />").text(name).val(name));
			});
		},
		error : function(e) {
			console.log("js: Error in getCourses");
		}
	});
}