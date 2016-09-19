var servicesForSelection;
function getServices() {
	$.ajax({
		url : "DashboardController",
		data : {
			action : "getServiceList",
			courseName : courseName
		},
		dataType : "json",
		async : false,
		success : function(data) {

			servicesForSelection = data;

		},
		error : function(e) {
			console.log("Error in getServices");
		}
	});
}

//*** document ready - for graphs with Select ***	
$(document).ready(function() {
	//get data for combo boxes
	getServices();
	
	//set charts

	
	$("#serviceSelection").select2({
		allowClear : true,
		data : servicesForSelection
	}).on("change", function(e) {
		var serSelection = $("#serviceSelection").val();
		console.log("change val(service)=" + serSelection);

//		resetCanvas($("#canvas_dahs"));
//		setBarChartPerRound(serSelection);

	});


});//end doc ready