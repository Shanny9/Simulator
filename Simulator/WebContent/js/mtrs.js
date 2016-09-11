/**
 * 
 */
// Pie Chart(ranges) -MTRS
var mtrsPieData;
function getMTRSPieData(team, round, service) {
	var labels = [ "1-2", "3-4", "5-6", "7-8" ];
	$.ajax({
		url : "DashboardController",
		data : {
			action : "getMTRSPieData",
			ranges : labels,
			team : team,
			round : round,
			service : service,
			courseName : courseName
		},
		dataType : "json",
		async : false,
		success : function(data) {

			mtrsPieData = data;
		},
		error : function(e) {
			console.log("Error in getMTRSPieData");
		}
	});
}

function setMTRSpie(team, round, service) {
	getMTRSPieData(team, round, service);
	var labels = [ "1-2", "3-4", "5-6", "7-8" ];
	var tempData = [ 20, 30, 40, 80 ];
	/*
	 * var options = { legend : true, responsive : false };
	 */

	var mtrsPie = new Chart(document.getElementById("canvas_mtrsPie"), {
		type : 'pie',
		tooltipFillColor : "rgba(51, 51, 51, 0.55)",
		data : {
			labels : labels,
			datasets : [ {
				data : mtrsPieData,
				backgroundColor : [ "#BDC3C7", "#9B59B6", "#26B99A", "#E74C3C",
						"#3498DB" ],
				hoverBackgroundColor : [ "#CFD4D8", "#B370CF", "#36CAAB",
						"#E95E4F", "#49A9EA" ]
			} ]
		},
		options : {

		}
	});

}

$(document).ready(function() {
	setMTRSpie("both", 0, 0);
	
	$("#roundSelectMTRS").select2({
		allowClear : true,
		data : roundsForSelection
	}).on("change", function(e) {
		var roundSelectMTRS = $("#roundSelectMTRS").val();
		// clear old data
		resetCanvas($("#canvas_mtrsPie"));
		setMTRSpie("both", roundSelectMTRS, 0);
	});

	$("#serviceSelectMTRS").select2({
		allowClear : true,
		data : servicesForSelection
	}).on("change", function(e) {
		var serSelectMTRS = $("#serviceSelectMTRS").val();
		// clear old data
		resetCanvas($("#canvas_mtrsPie"));
		setMTRSpie("both", 0, serSelectMTRS);
	});

	$("#teamSelectMTRS").select2({
		allowClear : true
	}).on("change", function(e) {
		var teamSelectMTRS = $("#teamSelectMTRS").val();
		// clear old data
		resetCanvas($("#canvas_mtrsPie"));
		setMTRSpie(teamSelectMTRS, 0, 0);
	});

});
