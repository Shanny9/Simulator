var isEmtpy = 0;
var isCorrect;
jQuery(document).ready(function() {
	
    /*
        Fullscreen background
    */
    $.backstretch("./css/home_images/airplane-login.jpg");
    
    /*
        Form validation
    */
    $('.login-form input[type="text"], .login-form input[type="password"], .login-form textarea').on('focus', function() {
    	$(this).removeClass('input-error');
    });
    
    $('.login-form').on('submit', function(e) {
    	
    	$(this).find('input[type="text"], input[type="password"], textarea').each(function(){
    		if( $(this).val() == "" ) {
    			e.preventDefault();
    			$(this).addClass('input-error');
    			isEmtpy++;
    		}
    		else {
    			$(this).removeClass('input-error');
    		}
    	});
    	
    	if(isEmtpy == 0 ) {
    		authenticate($("#form-password").val());
    		console.log(isCorrect);
    		e.preventDefault();
    	}
    	else
    		isEmtpy = 0;
    	
    });
    
    
}); //END doc ready

function authenticate(password){

	$.ajax({
		url : "HomeController?action=authenticate",
		dataType : "json",
		async: false,
		data: { pass: password },
		success : function(msg) {
			if(msg){
			//	console.log("Password OK.");
				isCorrect = true;
			}
			else
			{
			//	console.log("Password not OK.");
				isCorrect = false;
			}
		},
		error : function(e) {
			console.log("js:authenticate: Incorrect Password.");
		}
	});

}
