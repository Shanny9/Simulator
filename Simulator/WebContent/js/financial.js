var servicesForSelection, roundsForSelection;
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

function getRounds() {
	$.ajax({
		url : "DashboardController",
		data : {
			action : "getRoundList",
			courseName : courseName
		},
		dataType : "json",
		async : false,
		success : function(data) {
			console.log("getRounds data:");
			console.log(data);
			roundsForSelection = data;

		},
		error : function(e) {
			console.log("Error in getRounds");
		}
	});
}

//*** document ready - for graphs with Select ***	
$(document).ready(function() {
	//get data for combo boxes
	getServices();
	getRounds();
	//get selections
	var serSelection = $("#serviceSelection").val();
	var rSelection = $("#roundSelection").val();
	var teamSelection = $("#teamSelection").val();
	//set chart
	setVisualization(rSelection, serSelection, teamSelection, true);
	
	$("#serviceSelection").select2({
		allowClear : true,
		data : servicesForSelection
	}).on("change", function(e) {
		serSelection = $("#serviceSelection").val();
		console.log("change val(service)=" + serSelection);
		// clear old data
		$("#chart > svg").remove();
		setVisualization(rSelection, serSelection, teamSelection, false);

	});
	
	$("#roundSelection").select2({
		allowClear : true,
		data : roundsForSelection
	}).on("change", function(e) {
		rSelection = $("#roundSelection").val();
		console.log("change val(round)=" + rSelection);
		//clear old data
		$("#chart > svg").remove();
		setVisualization(rSelection, serSelection, teamSelection, false);
	});
	
	$("#teamSelection").select2({
		allowClear : true
	}).on("change", function(e) {
		teamSelection = $("#teamSelection").val();
		// clear old data
		$("#chart > svg").remove();
		setVisualization(rSelection, serSelection, teamSelection, false);
	});


});//end doc ready