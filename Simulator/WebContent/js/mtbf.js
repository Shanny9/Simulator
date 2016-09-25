/**
 * 
 */
		var marom, rakia, labels;
		function getMTBFPerService(roundId) { //has to be called before select2 script
			$.ajax({
				url : "DashboardController",
				data : {
					action : "getMTBFPerService",
					round : roundId,
					courseName : courseName
				},
				dataType : "json",
				async : false,
				success : function(data) {

					marom = data.maromData;
					rakia = data.rakiaData;
					labels = data.labels;

				},
				error : function(e) {
					console.log("Error in getMTBFPerService");
				}
			});
		}
		function setBarChartPerService(roundId) {
			getMTBFPerService(roundId);
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
							        labelString: 'Mean Time Between Failures (Seconds)'
							      }
							    }]
						  },
						  tooltips: {
							   mode: 'label'
							 } 
						}
			});
	
		}

	
	//MTBF Per Team
	var colors = new Array("#D56AA0", "#7D82B8", "#613F75","#993955","#271F30"); //5 color for 5 rounds MAX
	var rounds = new Array();
	var teams;
	function getMTBFPerTeam(serviceId) { //has to be before select2 script
		$.ajax({
			url : "DashboardController",
			data : {
				action : "getMTBFPerTeam",
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
				console.log("Error in getMTBFPerTeam");
			}
		});
	}
	function setBarChartPerTeam(serviceId) {
		getMTBFPerTeam(serviceId);
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
/*		for(var i=0;i<rounds.length;i++){
			var item = new Object();
			item.label = "Round "+(i+1);
			item.backgroundColor =colors[i];
			item.data = rounds[i];
			dataArr.push(item);
		}*/
		
		var barDataTeams = {
			labels : teams,
			datasets : dataArr
		};
		console.log(barDataTeams);
		var ctx = document.getElementById('canvas_team');
		new Chart(ctx, {
			type : 'bar',
			data : barDataTeams,
			options : {
					  scales: {
					    yAxes: [{
					      scaleLabel: {
					        display: true,
					        labelString: 'Mean Time Between Failures (Seconds)'
					      }
					    }]
					  }     
					}
		});
	}

	//MTBF Per Round
		var marom, rakia, labels;
		function getMTBFPerRound(serviceId) { //has to be before select2 script
			$.ajax({
				url : "DashboardController",
				data : {
					action : "getMTBFPerRound",
					service : serviceId,
					courseName : courseName
				},
				dataType : "json",
				async : false,
				success : function(data) {

					marom = data.maromData;
					rakia = data.rakiaData;
					labels = data.labels;

				},
				error : function(e) {
					console.log("Error in getMTBFPerRound");
				}
			});
		}
		function setBarChartPerRound(serviceId) {
			getMTBFPerRound(serviceId);
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
						        labelString: 'Mean Time Between Failures (Seconds)'
						      }
						    }]
						  }     
						}
			});
		}

	//Select functions
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

					roundsForSelection = data;

				},
				error : function(e) {
					console.log("Error in getRounds");
				}
			});
		}
		
		var resetCanvas = function(element){
			var id = element.attr('id');
			var parent = element.parent();
			var width = element.css("width");
			var height = element.css("height");
/* 			console.log("w "+ width + "h "+ height); */
			  element.remove(); // this is my <canvas> element
			  parent.append('<canvas id="' + id + '"></canvas>');
			  var canvas = document.getElementById(id);
			  $('#'+id).addClass("demo-placeholder");
			  var ctx = canvas.getContext('2d');
			  ctx.canvas.style.width = width; // resize width
			  ctx.canvas.style.height = height; // resize height

			  
			};
// *** document ready - for graphs with Select ***	
		$(document).ready(function() {
			//get data for combo boxes
			getServices();
			getRounds();
			
			//set charts
			setBarChartPerRound(0); //all services default 
			setBarChartPerTeam(0);
			setBarChartPerService(0);

			
			$("#serviceSelection").select2({
				allowClear : true,
				data : servicesForSelection
			}).on("change", function(e) {
				var serSelection = $("#serviceSelection").val();
				console.log("change val(service)=" + serSelection);
				//MTBF per round
				resetCanvas($("#canvas_dahs"));
				setBarChartPerRound(serSelection);
				//MTBF per team
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


		});//end doc ready


