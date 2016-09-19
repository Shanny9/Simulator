/**
 * 
 */
// ******** Select(combobox) Functions ************	
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
			console.log("getServices data:");
			console.log(data);
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
		
// ******** Reset Canvas ************

		
		var resetCanvas = function(element) {
	var id = element.attr('id');
	var parent = element.parent();
	var width = element.css("width");
	var height = element.css("height");
	/* console.log("w "+ width + "h "+ height); */
	element.remove(); // this is my <canvas> element
	parent.append('<canvas id="' + id + '"></canvas>');
	var canvas = document.getElementById(id);
	$('#' + id).addClass("demo-placeholder");
	var ctx = canvas.getContext('2d');
	ctx.canvas.style.width = width; // resize width
	ctx.canvas.style.height = height; // resize height
};

//MTRS Per Team
var colors = new Array("#bff0d4", "#70a288", "#048a81","#028090","#114b5f"); //5 color for 5 rounds MAX
var rounds = new Array();
var teams;
function getMTRSPerTeam(serviceId) { //has to be before select2 script
	$.ajax({
		url : "DashboardController",
		data : {
			action : "getMTRSPerTeam",
			service : serviceId,
			courseName : courseName
		},
		dataType : "json",
		async : false,
		success : function(data) {
			console.log("getMTRSPerTeam data:");
			console.log(data);
			for(var i=0; i<Object.keys(data).length-1;i++){
				var name = "round#"+ (i+1);
				rounds.push(data[name]);
			}
			teams = data["labels"];
		},
		error : function(e) {
			console.log("Error in getMTRSPerTeam");
		}
	});
}
function setBarChartPerTeam(serviceId) {
	getMTRSPerTeam(serviceId);
	var dataArr = new Array();
	for(var i=0;i<rounds.length;i++){
		var item = new Object();
		item.label = "Round "+(i+1);
		item.backgroundColor =colors[i];
		item.data = rounds[i];
		dataArr.push(item);
	}
	
	var barDataTeams = {
		labels : teams,
		datasets : dataArr
	};
	console.log(barDataTeams);
	var ctx = document.getElementById('canvas_team').getContext('2d');
	new Chart(ctx, {
		type : 'bar',
		data : barDataTeams,
		options : {
				  scales: {
				    yAxes: [{
				      scaleLabel: {
				        display: true,
				        labelString: 'Mean Time to Restore Service (Seconds)'
				      }
				    }]
				  }     
				}
	});
}


// Pie Chart(ranges) -MTRS
var mtrsPieData = new Array();
function getMTRSPieData(team, round, service) {
	// first range must start at 0
	var labels = [ "0-9", "9-18", "18-27", "27-36" , "36-45" ];
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
			console.log("getMTRSPieData data:");
			console.log(data);
			mtrsPieData = data.data;
		},
		error : function(e) {
			console.log("Error in getMTRSPieData");
		}
	});
}

function setMTRSpie(team, round, service) {
	getMTRSPieData(team, round, service);
	var labels = [ "0-9", "9-18", "18-27", "27-36" , "36-45" ];
	var tempData = [ 20, 30, 40, 80 ];
	/*
	 * var options = { legend : true, responsive : false };
	 */
	console.log("labels: "+labels+" data: "+mtrsPieData+ " tempData: " + tempData);
	var ctx = document.getElementById("canvas_mtrsPie").getContext('2d');
	ctx.canvas.width = 350;
	ctx.canvas.height = 350;
	var mtrsPie = new Chart(ctx, {
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

//MTRF Per Round
var marom, rakia, labels;
function getMTRFPerRound(serviceId) { //has to be before select2 script
	$.ajax({
		url : "DashboardController",
		data : {
			action : "getMTRSPerRound",
			service : serviceId,
			courseName : courseName
		},
		dataType : "json",
		async : false,
		success : function(data) {
			console.log("getMTRFPerRound data:");
			console.log(data);
			marom = data.maromData;
			rakia = data.rakiaData;
			labels = data.labels;

		},
		error : function(e) {
			console.log("Error in getMTRFPerRound");
		}
	});
}
function setBarChartPerRound(serviceId) {
	getMTRFPerRound(serviceId);
	var barData = {
		labels : labels,
		datasets : [ {
			label : "Marom",
			backgroundColor : "rgba(76, 164, 224, 1)",
			data : marom
		},

		{
			label : "Rakia",
			backgroundColor : "rgba(28, 97, 142, 1)",
			data : rakia
		} ]
	};
	console.log(barData);
	var ctx = document.getElementById('canvas_dahs');
	new Chart(ctx, {
		type : 'bar',
		data : barData,
		options : {
				  scales: {
				    yAxes: [{
				      scaleLabel: {
				        display: true,
				        labelString: 'Mean Time to Restore Service (Seconds)'
				      }
				    }]
				  }     
				}
	});
}

var marom, rakia, labels;
function getMTRSPerService(roundId) { //has to be called before select2 script
	$.ajax({
		url : "DashboardController",
		data : {
			action : "getMTRSPerService",
			round : roundId,
			courseName : courseName
		},
		dataType : "json",
		async : false,
		success : function(data) {
			console.log("getMTRSPerService data:");
			console.log(data);
			marom = data.maromData;
			rakia = data.rakiaData;
			labels = data.labels;

		},
		error : function(e) {
			console.log("Error in getMTRSPerService");
		}
	});
}
function setBarChartPerService(roundId) {
	getMTRSPerService(roundId);
	var barData = {
		labels : labels,
		datasets : [ {
			label : "Marom",
			backgroundColor : "rgba(76, 164, 224, 1)",
			data : marom
		},

		{
			label : "Rakia",
			backgroundColor : "rgba(28, 97, 142, 1)",
			data : rakia
		} ]
	};
	console.log(barData);
	var ctx = document.getElementById('canvas_service').getContext('2d');
	ctx.canvas.width = 500;
	ctx.canvas.height = 500;
	new Chart(ctx, {
		type : 'horizontalBar',
		data : barData,
		options : {
				  scales: {
				    yAxes: [{
				      scaleLabel: {
				        display: true,
				        labelString: 'Services'
				      }
				    }],
				    xAxes: [{
					      scaleLabel: {
					        display: true,
					        labelString: 'Mean Time to Restore Service (Seconds)'
					      }
					    }]
				  },
				  tooltips: {
					   mode: 'label'
					 } 
				}
	});

}

// ******** Doc Ready - Set Select2 + Graphs with combos ********
$(document).ready(function() {
	//get data for combo boxes
	getServices();
	getRounds();
	
	//set charts
	setBarChartPerRound(0); //all services default 
	setBarChartPerTeam(0);
	setBarChartPerService(0);
	setMTRSpie("both", 0, 0);
	
	$("#serviceSelection").select2({
		allowClear : true,
		data : servicesForSelection
	}).on("change", function(e) {
		var serSelection = $("#serviceSelection").val();
		console.log("change val(service)=" + serSelection);
		//MTRS per round
		resetCanvas($("#canvas_dahs"));
		setBarChartPerRound(serSelection);
		//MTRS per team
		//clear old data
		rounds = new Array();
		resetCanvas($("#canvas_team"));
		
		setBarChartPerTeam(serSelection);
	});
	
	$("#roundSelection").select2({
		allowClear : true,
		data : roundsForSelection
	}).on("change", function(e) {
		var rSelection = $("#roundSelection").val();
		console.log("change val(round)=" + rSelection);
		//clear old data
		resetCanvas($("#canvas_service"));
		setBarChartPerService(rSelection);
	});
	
// ***** Select for Pie Chart MTRS ******
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
// ***** END Select for Pie Chart MTRS ******
	
});
