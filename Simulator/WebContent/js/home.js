var client_time;
var real_time;
var offset;
var runTime;
var pauseTime;
var gp;

$(document).ready(function(){
	$("#startSimulator").click(startSimulator);
	console.log(isRunning);
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
			console.log("js:getIncidents: Error in getting incidents.");
        }
    });
}

function getGP(){
	$.ajax({
        url: "HomeController?action=getGP", 
        dataType: "json",
        success: function(data) {
        	$.each(data, function(j, item) {
				//TODO: save data to gp var
        	});
        },
        error: function(e) {
			console.log("js:startSimulator: Error in starting simulator.");
        }
    });
}

function startSimulator()
{
    $.ajax({
        url: "HomeController?action=startSimulator", 
        dataType: "text",
        success: function(data) {
        	getGP();
        	getTime();
    		setInterval(incrementClock, 1000);
        	console.log("js:startSimulator: Success.");
        },
        error: function(e) {
			console.log("js:startSimulator: Error in starting simulator.");
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
        		var remainingClock = new Date(data.remainingClock);
        		var serverTime  = new Date(data.serverTime);
        		
        		client_time = new Date();
    			offset = client_time.getTime()-serverTime.getTime();
    			if (offset < 0){
    				offset = offset*-1;
    			}
        		real_time = new Date(remainingClock.getTime()+offset);
        },
        error: function(e) {
			console.log("js:getTime: Error in getting time.");
        }
    });
}

function incrementClock(){
	//TODO: update clock according to gp
	var rTime= dateFormat(real_time, "HH:MM:ss");
	$('#main-time').html(rTime);
	real_time.setSeconds(real_time.getSeconds()-1);
}

