$(document).ready(function() {
	getHeader();
});

/**
 * converts a number in seconds to hh:mm:ss format (e.g.: 90 will be converted
 * to 1:30).
 */
Number.prototype.toHHMMSS = function() {
	var sec_num = parseInt(this, 10); // don't forget the second param
	var hours = Math.floor(sec_num / 3600);
	var minutes = Math.floor((sec_num - (hours * 3600)) / 60);
	var seconds = sec_num - (hours * 3600) - (minutes * 60);

	if (hours < 10) {
		hours = "0" + hours;
	}
	if (minutes < 10) {
		minutes = "0" + minutes;
	}
	if (seconds < 10) {
		seconds = "0" + seconds;
	}
	var time = hours + ':' + minutes + ':' + seconds;
	return time;
};

function formatDate(date){
	var monthNames = [
	                  "January", "February", "March",
	                  "April", "May", "June", "July",
	                  "August", "September", "October",
	                  "November", "December"
	                ];
	var day = date.getDate();
	var monthIndex = date.getMonth()+1;
	var year = date.getFullYear();

	return day+"/"+ monthIndex+"/"+ year;
}

function getHeader() {
	$.ajax({
		url : "DashboardController",
		data : {
			action : "getHeaderData",
			course : courseName
		},
		dataType : "json",
		success : function(data) {
			$('#hDate').html(formatDate(new Date()));
			$('#hRoundsNum').html(data.rounds_done+"/"+data.rounds);
			$('#hRoundTime').html(data.round_time.toHHMMSS());
			$('#hCi').html(data.cis);
			$('#hServices').html(data.services);
			$('#hInci').html(data.incidents);
		},
		error : function(e) {
			console.log("js:getHeader: Error in getting data.");
		}
	});
}