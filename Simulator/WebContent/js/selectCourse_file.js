/**
 * 
 */

$(document).ready(function() {
	/*
	 * Fullscreen background
	 */
	$.backstretch("./css/home_images/runway.jpg");
	getCourses();
	disable($("#start"), true); //submit button
	$('#form-courseName').change(function() {
		if($(this).val() !== ""){
			disable($("#start"), false);
		}
		else
			disable($("#start"), true);
	});

});

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

function disable(obj, dis) {
	obj.prop("disabled", dis);
	if(dis){
		$("#start").addClass("disabled");
	}
	else{

		$("#start").removeClass("disabled");
	}

}
