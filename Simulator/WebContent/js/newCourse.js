/**
 * 
 */
var isExist;
$(document).ready(function() {
	
    /*
    Fullscreen background
    */
	$.backstretch("./css/home_images/runway.jpg");
	
	 $('.create-form input[type="text"], .create-form input[type="number"], .create-form textarea').on('focus', function() {
	    	$(this).removeClass('input-error');
	    });
	    
	    $('.create-form').on('submit', function(e) {
	    	
	    	$(this).find('input[type="text"], input[type="number"], textarea').each(function(){
	    		if( $(this).val() == "" ) {
	    			e.preventDefault();
	    			$(this).addClass('input-error');
	    		}
	    		else {
	    			$(this).removeClass('input-error');
	    		}
	    	});
			$(this).find('input[type="number"]').each(function(){
				if($(this).val() == "" || $(this).val() < 0)
					{
    	    			e.preventDefault();
    	    			$(this).addClass('input-error');
					}
				else
					$(this).removeClass('input-error');
			});
			
			if($("#form-courseName").val() != ""){
				checkLog($("#form-courseName").val());
				if(isExist){
	    			e.preventDefault();
	    			$("#form-courseName").addClass('input-error');
				}
				else
					$("#form-courseName").removeClass('input-error');
			}
			
	    });
	
}); // END doc ready

function checkLog(directory) {

	$.ajax({
		url : "HomeController",
		data : {
			action : "checkLog",
			directory : directory
		},
		async: false,
		dataType: "json", 
		success : function(rounds) {
			if (rounds>0) {
				//the course already exists
				//TODO: fix error pop-up
				$('#err').slideToggle("slow").delay(3000).slideToggle("slow"); // shows err message
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

function ok(){
	$("#msg").slideToggle("slow").delay(2000).slideToggle("slow");
}