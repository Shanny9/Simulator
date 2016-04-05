var flag = true;
var client_time;
var offset;

$(document).ready(function(){
	startSimulator();
	setInterval(getTime, 1000);
});

/**
 * 
 */
function getIncidents()
{
    $.ajax({
        url: "HomeController?action=getIncidents",     
		dataType: "json",
        success: function(data) {  
        	
				$.each(data, function(j, item) {
					//.....
			});
			 
        },
        error: function(e) {
			alert("error in getting incidents.");
        }
    });
}

function startSimulator()
{
    $.ajax({
        url: "HomeController?action=startSimulator",     
        dataType: "json",
        success: function(data) {
        	alert("success");
        },
        error: function(e) {
			alert("error in starting simulator");
        }
    });
}

function getTime()
{
    $.ajax({
        url: "HomeController?action=getTime",     
		dataType: "json",
        success: function(data) {
        		
        		//alert(data.elapsedClock);
        		var elapsedClock  = new Date(data.elapsedClock);
        		var serverTime  = new Date(data.serverTime);

        		if (flag){
        			client_time = new Date();
        			offset = client_time.getTime()-serverTime.getTime();
        			flag = false;
        		}
        		
        		console.log(offset)
        		var real_time = new Date(elapsedClock.getTime()-offset);
        		console.log("realTime: " + real_time);
        		var rTime= dateFormat(real_time, "HH:MM:ss");
        		console.log("formatted realTime: " + real_time);
        		$('#main-time').html(rTime);
  
//        		var temp = data.runtime;
//        		var hour = data.runTime%3600;
//        		temp = temp - hour*3600;
//        		var min = temp%60;
//        		temp = temp - min*60;
//        		var sec = temp;
        		
//        		var simulator_clock = new Date(2016,3,2,0,0,0);
//        		var remaining_clock = new Date (2016,3,2,hour,min,sec);
//			});
			 
        },
        error: function(e) {
			console.log("js: error in getting time!");
        	
//			$('#main-time').html(e.responseText);
        }
    });
}