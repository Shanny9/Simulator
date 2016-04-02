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
					var str = item.name;
					$("#pUserList").append("<li><a href=MainProfile.jsp?user="+item.user+"&catagory="+item.catagory+">"+str+"</a></li>");
			});
			 
        },
        error: function(e) {
			alert("error in getting users!");
        }
    });
}

function getClocks(){
	
}

function getGp()
{
    $.ajax({
        url: "HomeController?action=getGp",     
		dataType: "json",
        success: function(data) {
        		       		
        		var temp = data.runtime;
        		var hour = data.runTime%3600;
        		temp = temp - hour*3600;
        		var min = temp%60;
        		temp = temp - min*60;
        		var sec = temp;
        		
        		var simulator_clock = new Date(2016,3,2,0,0,0);
        		var remaining_clock = new Date (2016,3,2,hour,min,sec);
			});
			 
        },
        error: function(e) {
			alert("error in getting users!");
        }
    });
}