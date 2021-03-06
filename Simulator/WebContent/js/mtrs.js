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
			$.each(Object.keys(data), function(key, item){
				if(item!="labels"){
					var r = new Object();
					r[item] = data[item];
					rounds.push(r);
				}
			});
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
	for (var i = 0; i < rounds.length; i++) { // rounds is array of objects: {round#:data}
		$.each(Object.keys(rounds[i]), function(key, val) { // val = round#... the only field of the object.
			var item = new Object();
			var rNum = val.replace( /^\D+/g, '');
			item.label = "Round "+ rNum;
			item.backgroundColor = colors[i];
			item.data = rounds[i][val]; // ex. rounds[0][round#1] --> array of data for round 1.
			dataArr.push(item);
		});
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
	var labels = [ "0-50", "50-100", "100-200", "200-400"];
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
	var labels = [ "0-50", "50-100", "100-200", "200-400"];
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

// *** little pie chart ***//
var pieData;
function getPieData() {
	$.ajax({
		url : "DashboardController",
		data : {
			action : "getPieData",
			courseName : courseName
		},
		dataType : "json",
		async : false,
		success : function(data) {

			pieData = data;
			/* 					for (var i = 0; i < pieData.marom.labels.length; i++) {
			
			 pieData.marom.labels[i] = pieData.marom.labels[i] +" " +
			 pieData.marom.percentages[i]*100 + "%";
			 } */
		},
		error : function(e) {
			console.log("Error in getPieData");
		}
	});
}

function makeLabels(percentages) {
	var i = 0;
	$('#my-doughnut-legend').find('li').each(function() {
		var current = $(this);
		current.append("&nbsp;" + percentages[i] * 100 + "%");
		i++;
	});
}

function setAvailabiltyPie(){
	getPieData();

	var options = {
		legend : true,
		responsive : false
	};

	var dougnut = new Chart(document
			.getElementById("canvas1"), {
		type : 'pie',
		tooltipFillColor : "rgba(51, 51, 51, 0.55)",
		data : {
			labels : pieData.marom.labels,
			datasets : [ {
				data : pieData.marom.percentages,
				backgroundColor : [
				//            "#BDC3C7",
				//            "#9B59B6",
				"#26B99A", "#E74C3C"
				//          "#3498DB"
				],
				hoverBackgroundColor : [
				//            "#CFD4D8",
				//            "#B370CF",
				"#36CAAB", "#E95E4F"
				//              "#49A9EA"
				]
			} ]
		},
		options : options
	});
	document.getElementById('my-doughnut-legend').innerHTML = dougnut
			.generateLegend();
	makeLabels(pieData.marom.percentages);
}

// ******** Doc Ready - Set Select2 + Graphs with combos ********
$(document).ready(function() {
	//set little pie chart
	setAvailabiltyPie();
	
	//get data for combo boxes
	getServices();
	getRounds();
	
	//get selections for mtrs pie
	var roundSelectMTRS = $("#roundSelectMTRS").val();
	var serSelectMTRS = $("#serviceSelectMTRS").val();
	var teamSelectMTRS = $("#teamSelectMTRS").val();
	//set charts
	setBarChartPerRound(0); //all services default 
	setBarChartPerTeam(0);
	setBarChartPerService(0);
	setMTRSpie(teamSelectMTRS, roundSelectMTRS, serSelectMTRS);
	
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
		roundSelectMTRS = $("#roundSelectMTRS").val();
		// clear old data
		resetCanvas($("#canvas_mtrsPie"));
		setMTRSpie(teamSelectMTRS, roundSelectMTRS, serSelectMTRS);
	});

	$("#serviceSelectMTRS").select2({
		allowClear : true,
		data : servicesForSelection
	}).on("change", function(e) {
		serSelectMTRS = $("#serviceSelectMTRS").val();
		// clear old data
		resetCanvas($("#canvas_mtrsPie"));
		setMTRSpie(teamSelectMTRS, roundSelectMTRS, serSelectMTRS);
	});

	$("#teamSelectMTRS").select2({
		allowClear : true
	}).on("change", function(e) {
		teamSelectMTRS = $("#teamSelectMTRS").val();
		// clear old data
		resetCanvas($("#canvas_mtrsPie"));
		setMTRSpie(teamSelectMTRS, roundSelectMTRS, serSelectMTRS);
	});
	
// ***** END Select for Pie Chart MTRS ******
	
});
