/**
 * 
 */
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
			
			checkLog($("#form-courseName").val());
			
	    });
	
}); // END doc ready

function checkLog(directory) {

	$.ajax({
		url : "HomeController?action=checkLog",
		data : {
			"directory" : directory
		},
		dataType: "json", 
		success : function(rounds) {
			if (rounds>0) {
				//the course already exists
				//TODO: fix error pop-up
				$('#err').slideToggle("slow").delay(3000).slideToggle("slow"); // shows err message
			}
		},
		error : function(e) {
			console.log("js: Error in checkLog");
		}
	});
}