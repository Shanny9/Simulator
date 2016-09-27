/**
 * 
 */
var isSettingOk;
var isNumbersOk =true;
var isExist;
$(document).ready(function() {
	
    /*
    Fullscreen background
    */
	$.backstretch("./css/home_images/runway.jpg");
	
	 $('.create-form input[type="text"], .create-form input[type="number"], .create-form textarea').on('focus', function() {
		    var id = "#"+$(this).attr('id')+"Err";
	    	$(this).removeClass('input-error');
	    	hideErrMsg($(id));
	    });
	    
	    $('.create-form').on('submit', function(e) {
	    	//check for empty text fields
	    	$(this).find('input[type="text"], textarea').each(function(){
	    		if( $(this).val() == "" ) {
	    			e.preventDefault();
	    			$(this).addClass('input-error');   			
	    		}
	    		else{
	    	    	//check course name
	    			if($("#form-courseName").val() != ""){
	    				checkLog($("#form-courseName").val());
	    				if(isExist){
	    	    			e.preventDefault();
	    	    			showErrMsg($("#form-courseNameErr"), "This course name is already taken. Please choose a different name");
	    	    			$("#form-courseName").addClass('input-error');
	    				}
	    			}
	    		}
	    	});
	    	//check for empty number fields
	    	$(this).find('input[type="number"]').each(function(){
	    		if( $(this).val() == "" ) {
	    			e.preventDefault();
	    			$(this).addClass('input-error');
	    			if($(this) != $("#form-courseName")){
	    				isFieldEmpty = true;
	    			}
	    			
	    		}
	    		else{
	    			//check numbers
			    		var id = "#"+$(this).attr('id')+"Err";
			    		if( $(this).val()<=0 ) {
			    			e.preventDefault();
			    			$(this).addClass('input-error');
			    			if($(this) == $("#form-initCapital")){
			    				showErrMsg($("#form-initCapitalErr"), "Field should be greater than 9999.");
			    			}
			    			else{
				    			showErrMsg($(id), "Field should be a number greater than 0.");
			    			}
			    			isNumbersOk = false;
			    		}
			    		else{
			    			
							if($(this).attr('id') == $("#form-initCapital").attr('id') && $(this).val()<10000){
								$(this).addClass('input-error');
								showErrMsg($("#form-initCapitalErr"), "Field should be greater than 9999.");
								e.preventDefault();
							}
			    		}
	    		}
	    	});
	    	//check for settings
	    	if(isNumbersOk){
				checkSettings();
				if(!isSettingOk){
					e.preventDefault();
				}
	    	}
	    	// init
	    	isNumbersOk = true;
	    	isSettingOk = undefined;
	    	isExist = undefined;
	    });
	
}); // END doc ready

function showErrMsg(input, msg){
	input.html(msg);
}

function hideErrMsg(input){
	input.html("");
}

function checkLog(directory) {

	$.ajax({
		url : "HomeController",
		data : {
			action : "checkLog",
			directory : directory
		},
		async: false,
		dataType: "json", 
		success : function(courseExists) {
			if (courseExists) {
				//the course already exists
				isExist = true;
			}
			else
				isExist = false;
		},
		error : function(e) {
			console.log("js: Error in checkLog");
		}
	});
}

function checkSettings(){
	$.ajax({
		url : "HomeController",
		data : {
			action : "checkSettings",
			courseName : $("#form-courseName").val(),
			numOfRounds : $("#form-numOfRounds").val(),
			runTime : $("#form-runTime").val(),
			pauseTime : $("#form-pauseTime").val(),
			sessions : $("#form-sessions").val(),
			initCapital : $("#form-initCapital").val()
			
		},
		async: false,
		dataType: "text", 
		success : function(msg) {
			if (msg == "") {
				isSettingOk = true;
			}
			else{
				isSettingOk = false;
				mscAlert({
					title: "",
					subtitle: msg,
					 dismissOverlay: true
				});
				$(".msc-sub").css("margin-right","3px");
			}
		},
		error: function(xhr, status, error) {
			  console.log(error.Message);
			}
	});
}

function ok(){
	$("#msg").slideToggle("slow").delay(2000).slideToggle("slow");
}