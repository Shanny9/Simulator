/**
 * 
 */

$(document).ready(function() {
	/*
	 * Fullscreen background
	 */
	$.backstretch("./css/home_images/runway.jpg");
	getCourses();
	disable($("#start"), true);
	disable($("#delete"), true);
	disable($("#form-round"), true);

	$('#delete').click(function(){
		deleteCourse($('#form-courseName option:selected').val());
	});
//	checkSubmit();
	$('#form-courseName').change(function() {
		checkLog($(this).val());
	});

	$('#form-round').change(function() {
		isRoundDone($(this).val() , $('#form-courseName').val());

	});
	
});

function isRoundDone(round, course){
	
	$.ajax({
		url : "HomeController?action=isRoundDone",
		data : {
			"filePrefix" : "round#" + round,
			"course" : course
		},
		dataType: "json", 
		success : function(msg) {
			if(msg){
				$("#start").html("Start Over");
				//TODO: change onSubmit action
			}
			else{
				$("#start").html("Start Simulator");
				//TODO: change onSubmit action
			}
				
		},
		error : function(e) {
			console.log("js: Error in isRoundDone");
		}
	});
	
}

function checkLog(directory) {

	$('#form-round').find('option').remove().end();
	
	if($("#form-courseName").val() == ""){
		setElements(true);
	}
	else{
		$.ajax({
			url : "HomeController?action=getRounds",
			data : {
				"directory" : directory
			},
			dataType: "json", 
			success : function(rounds) {
				if (rounds > 0) {
					setElements(false);
					setRounds(rounds);
					isRoundDone(1, directory);
					
				} else {
					setElements(true);
					if (directory != "") {
						$('#err').slideToggle("slow").delay(3000).slideToggle("slow"); // shows err message
					}
				}
			},
			error : function(e) {
				console.log("js: Error in checkLog");
			}
		});
	}
	
	
}

function setRounds(rounds) {
	for (var r = 1; r <= rounds; r++) {
		$('#form-round').append($("<option />").text(r).val(r));
	}
}

function disable(obj, dis) {
	obj.prop("disabled", dis);

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

function deleteCourse(value){
	console.log(value);
	$.ajax({
		url : "HomeController?action=deleteCourse",
		datatype : "text",
		data: {name: value},
		success : function(msg) {
			$("#form-courseName option[value='"+value+"']").remove();
			$("#form-courseName option[value='"+value+"']").attr('selected','selected');
			$("#form-round").empty();
			setElements(true);
			$('#msg').slideToggle("slow").delay(3000).slideToggle("slow"); // shows success message
		},
		error: function(xhr, status, error) {
			  var err = eval("(" + xhr.responseText + ")");
			  console.log(err.Message);
			}
	});
	
}
// sets buttons any dropdowns to disable/enable (dis = true -> disable,
// false->enable)
function setElements(dis) {
	if (dis) {
		disable($('#start'), true);
		disable($('#form-round'), true);
		disable($('#delete'), true);
		$(".form-round").addClass("disabled");
		$("#start").addClass("disabled");
		$("#delete").addClass("disabled");
	} else {
		disable($('#start'), false);
		disable($('#form-round'), false);
		disable($('#delete'), false);
		$(".form-round").removeClass("disabled");
		$("#start").removeClass("disabled");
		$("#delete").removeClass("disabled");
	}
	
}

function checkSubmit(){

    $('.create-form').on('submit', function(e) {
    	
    	if( $("#start").prop("disabled") == true )
    	{
    		console.log($("#start").prop("disabled"));
    		e.preventDefault(); 
    	}

    	
    });
	 
}