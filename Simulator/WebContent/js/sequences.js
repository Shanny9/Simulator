function ColorLuminance(hex, lum) {
	// validate hex string
	hex = String(hex).replace(/[^0-9a-f]/gi, '');
	if (hex.length < 6) {
		hex = hex[0]+hex[0]+hex[1]+hex[1]+hex[2]+hex[2];
	}
	lum = lum || 0;

	// convert to decimal and change luminosity
	var rgb = "#", c, i;
	for (i = 0; i < 3; i++) {
		c = parseInt(hex.substr(i*2,2), 16);
		c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
		rgb += ("00"+c).substr(c.length);
	}

	return rgb;
}
var csv;
function generateFile(round, service, team){
	$.ajax({
		url : "DashboardController",
		data : {
			action : "generateITBudgetBreakdown",
			courseName: courseName,
			round: round,
			service: service,
			team: team
		},
		dataType : "json",
		async : false,
		success : function(data) {
			console.log("generateITBudgetBreakdown:OK");
			csv = data;

		},
		error: function(xhr, status, error) {
			  console.log(xhr.responseText);
			}
	});

}

var titles = new Array();
function getTitles(){
	$.ajax({
		url : "DashboardController",
		data : {
			action : "getBizUnitsTitles"
		},
		dataType : "json",
		async : false,
		success : function(data) {
/*			console.log("getTitles data:");
			console.log(data);*/
			titles = data;

		},
		error : function(e) {
			console.log("Error in getTitles");
		}
	});
}

var catagory1 = new Object();
var catagory2 = new Object();
var catagory3 = new Object();
var titles1 = new Array();
var titles2 = new Array();
function divideLegend(catagories, titles){
	var length = Math.floor((Object.keys(catagories).length)/2);
	var remain = (Object.keys(catagories).length)%2;
	Object.keys(catagories).forEach(function(key,index) {
	    // key: the name of the object key
	    // index: the ordinal position of the key within the object 		
			if(index >= 0 && index < length)
				catagory1[key] = catagories[key];
			if(index >= length && index<( (length*2) + (remain) ))
				catagory2[key] = catagories[key];
	/*		if(index >= length*2 && index<( (length*3) + (length%3) ))
				catagory3[key] = catagories[key];*/

	});
	
	for(var i=0;i<titles.length;i++){
		if(i >= 0 && i< length)
			titles1.push(titles[i]);
		if(i >= length && i<( (length*2) + (remain) ))
			titles2.push(titles[i]);
	}
	
}

function getCatagories(){
	var colorsTry = ["#1f78b4", "#33a02c", "#fb9a99",
	                 "#e31a1c", "#ff7f00", "#6a3d9a", "#8a1321"
	                 ,"#b15928","#04703c", "#bc3ab1", "#b40845","#8d8d8d"];
	var dJson;
	/* get catagories from server */
	$.ajax({
		url : "DashboardController",
		data : {
			action : "getBizUnitsHierachical"
		},
		dataType : "json",
		async : false,
		success : function(data) {
			console.log("getCatagories data:");
			console.log(data);
			dJson = data;

		},
		error : function(e) {
			console.log("Error in getCatagories");
		}
	});
/*	var dJson =[ {
			"division":"Cust Service & Sales",
			"departments":[
			"dep1",
			"dep2",
			"dep3"] },
			{
				"division":"div2",
				"departments":[
				"dep4",
				"dep5",
				"dep6"] },	{
					"division":"div3",
					"departments":[
					"dep7",
					"dep8",
					"dep9"] },	{
						"division":"div4",
						"departments":[
						"dep10",
						"dep11",
						"dep12"] },
						{
							"division":"div5",
							"departments":[
							"dep14",
							"dep15",
							"dep16"] },
							{
								"division":"div6",
								"departments":[
								{"department":"dep114",
									"services":["s1","s2"]},
								{"department":"dep115",
									"services":["s3","s4"]},
								{"department":"dep116",
									"services":["s5","s6"]},
								{"department":"dep117",
									"services":["s7","s8"]}] }
		];*/
	var catagory = new Object();
	$.each(dJson, function(key, item){
		var div = item.division;
		var divColor = colorsTry[key];
		
		catagory[div] = divColor;
		
		$.each(item.departments, function(key, item){
				catagory[item.department] = ColorLuminance(divColor, 0.33);
				var depColor = ColorLuminance(divColor, 0.33);
				$.each(item.services, function(key, val){
				//	catagory[val] = ColorLuminance(depColor, 0.2);
					catagory[val] = '#8d8d8d';
				});
				
		});
		
	});

	return catagory;
}


/*******  Creates the visualization   *******/
function setVisualization(round, service, team, isDrawLegend){
	// Dimensions of sunburst.
	/*var*/ width = 750;
	/*var*/ height = 600;
	/*var*/ radius = Math.min(width, height) / 2;

	// Breadcrumb dimensions: width, height, spacing, width of tip/tail.
	/*var*/ b = {
	  w: /*75*/125, h: 30, s: 3, t: 10
	};

	// Mapping of step names to colors.
	/*var colors = {
	  "home": "#5687d1",
	  "product": "#7b615c",
	  "search": "#de783b",
	  "account": "#6ab975",
	  "other": "#a173d1",
	  "end": "#bbbbbb"
	};*/
	/*var*/ colors = getCatagories();
	/* Legend Divided */
	/* Hover Titles */
	if(isDrawLegend){
		getTitles();
		divideLegend(colors, titles);
	}

	// Total size of all segments; we set this later, after loading the data.
	/*var*/ totalSize = 0; 

	/*var*/ vis = d3.select("#chart").append("svg:svg")
	    .attr("width", width)
	    .attr("height", height)
	    .append("svg:g")
	    .attr("id", "container")
	    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

	/*var*/ partition = d3.layout.partition()
	    .size([2 * Math.PI, radius * radius])
	    .value(function(d) { return d.size; });

	/*var*/ arc = d3.svg.arc()
	    .startAngle(function(d) { return d.x; })
	    .endAngle(function(d) { return d.x + d.dx; })
	    .innerRadius(function(d) { return Math.sqrt(d.y); })
	    .outerRadius(function(d) { return Math.sqrt(d.y + d.dy); });
	/*Generate CSV file */
	generateFile(round, service, team); //sets var csv!
	// Use d3.text and d3.csv.parseRows so that we do not need to have a header
	// row, and can receive the csv as an array of arrays.
//	d3.text("ITBudgetBreakdown.csv", function(text) {
//	  var csv = d3.csv.parseRows(text);
	  var json = buildHierarchy(csv);
	  createVisualization(json, isDrawLegend);
//	});
}
// Main function to draw and set up the visualization, once we have the data.
function createVisualization(json, isDrawLegend) {

  if(isDrawLegend){
	  // Basic setup of page elements.
	  initializeBreadcrumbTrail();
	  drawLegend("#legend1", catagory1, titles1);
	  drawLegend("#legend2", catagory2, titles2);
	  /*drawLegend("#legend3", catagory3);*/
	  d3.select("#togglelegend").on("click", toggleLegend);
  }
  // Bounding circle underneath the sunburst, to make it easier to detect
  // when the mouse leaves the parent g.
  vis.append("svg:circle")
      .attr("r", radius)
      .style("opacity", 0);

  // For efficiency, filter nodes to keep only those large enough to see.
  var nodes = partition.nodes(json)
      .filter(function(d) {
      return (d.dx > 0.005); // 0.005 radians = 0.29 degrees
      });

  var path = vis.data([json]).selectAll("path")
      .data(nodes)
      .enter().append("svg:path")
      .attr("display", function(d) { return d.depth ? null : "none"; })
      .attr("d", arc)
      .attr("fill-rule", "evenodd")
      .style("fill", function(d) { return colors[d.name]; })
      .style("opacity", 1)
      .on("mouseover", mouseover);

  // Add the mouseleave handler to the bounding circle.
  d3.select("#container").on("mouseleave", mouseleave);

  // Get total size of the tree = value of root node from partition.
  totalSize = path.node().__data__.value;
 };

// Fade all but the current sequence, and show it in the breadcrumb trail.
function mouseover(d) {

  var percentage = (100 * d.value / totalSize).toPrecision(3);
  var percentageString = percentage + "%";
  if (percentage < 0.1) {
    percentageString = "< 0.1%";
  }

  d3.select("#percentage")
      .text(percentageString);

  d3.select("#explanation")
      .style("visibility", "");

  var sequenceArray = getAncestors(d);
  updateBreadcrumbs(sequenceArray, percentageString);

  // Fade all the segments.
  d3.selectAll("path")
      .style("opacity", 0.3);

  // Then highlight only those that are an ancestor of the current segment.
  vis.selectAll("path")
      .filter(function(node) {
                return (sequenceArray.indexOf(node) >= 0);
              })
      .style("opacity", 1);
}

// Restore everything to full opacity when moving off the visualization.
function mouseleave(d) {

  // Hide the breadcrumb trail
  d3.select("#trail")
      .style("visibility", "hidden");

  // Deactivate all segments during transition.
  d3.selectAll("path").on("mouseover", null);

  // Transition each segment to full opacity and then reactivate it.
  d3.selectAll("path")
      .transition()
      .duration(1000)
      .style("opacity", 1)
      .each("end", function() {
              d3.select(this).on("mouseover", mouseover);
            });

  d3.select("#explanation")
    .transition()
  .duration(1000)
      .style("visibility", "hidden");
}

// Given a node in a partition layout, return an array of all of its ancestor
// nodes, highest first, but excluding the root.
function getAncestors(node) {
  var path = [];
  var current = node;
  while (current.parent) {
    path.unshift(current);
    current = current.parent;
  }
  return path;
}

function initializeBreadcrumbTrail() {
  // Add the svg area.
  var trail = d3.select("#sequence").append("svg:svg")
      .attr("width", width)
      .attr("height", 50)
      .attr("id", "trail");
  // Add the label at the end, for the percentage.
  trail.append("svg:text")
    .attr("id", "endlabel")
    .style("fill", "#000");
}

// Generate a string that describes the points of a breadcrumb polygon.
function breadcrumbPoints(d, i) {
  var points = [];
  points.push("0,0");
  points.push(b.w + ",0");
  points.push(b.w + b.t + "," + (b.h / 2));
  points.push(b.w + "," + b.h);
  points.push("0," + b.h);
  if (i > 0) { // Leftmost breadcrumb; don't include 6th vertex.
    points.push(b.t + "," + (b.h / 2));
  }
  return points.join(" ");
}

// Update the breadcrumb trail to show the current sequence and percentage.
function updateBreadcrumbs(nodeArray, percentageString) {

  // Data join; key function combines name and depth (= position in sequence).
  var g = d3.select("#trail")
      .selectAll("g")
      .data(nodeArray, function(d) { return d.name + d.depth; });

  // Add breadcrumb and label for entering nodes.
  var entering = g.enter().append("svg:g");

  entering.append("svg:polygon")
      .attr("points", breadcrumbPoints)
      .style("fill", function(d) { return colors[d.name]; });

  entering.append("svg:text")
      .attr("x", (b.w + b.t) / 2)
      .attr("y", b.h / 2)
      .attr("dy", "0.35em")
      .attr("text-anchor", "middle")
      .text(function(d) { return d.name; });

  // Set position for entering and updating nodes.
  g.attr("transform", function(d, i) {
    return "translate(" + i * (b.w + b.s) + ", 0)";
  });

  // Remove exiting nodes.
  g.exit().remove();

  // Now move and update the percentage at the end.
  d3.select("#trail").select("#endlabel")
      .attr("x", (nodeArray.length + 0.5) * (b.w + b.s))
      .attr("y", b.h / 2)
      .attr("dy", "0.35em")
      .attr("text-anchor", "middle")
      .text(percentageString);

  // Make the breadcrumb trail visible, if it's hidden.
  d3.select("#trail")
      .style("visibility", "");

}

function drawLegend(selector, catagory, titleArr) {

  // Dimensions of legend item: width, height, spacing, radius of rounded rect.
  var li = {
    w: /*75*/125, h: 30, s: 3, r: 3
  };

  var legend = d3.select(selector).append("svg:svg")
      .attr("width", li.w)
      .attr("height", d3.keys(catagory).length * (li.h + li.s));

  var g = legend.selectAll("g")
      .data(d3.entries(catagory))
      .enter().append("svg:g")
      .attr("transform", function(d, i) {
              return "translate(0," + i * (li.h + li.s) + ")";
           });

  g.append("svg:rect")
      .attr("rx", li.r)
      .attr("ry", li.r)
      .attr("width", li.w)
      .attr("height", li.h)
      .style("fill", function(d) { return d.value; })
      /*was added for tooltip*/
      .append("svg:title").text(function(d, i) { return titleArr[i]; });

  g.append("svg:text")
      .attr("x", li.w / 2)
      .attr("y", li.h / 2)
      .attr("dy", "0.35em")
      .attr("text-anchor", "middle")
      .text(function(d) { return d.key; });
}

function toggleLegend() {
  var legend = d3.select("#legend");
  if (legend.style("visibility") == "hidden") {
    legend.style("visibility", "");
  } else {
    legend.style("visibility", "hidden");
  }
}

// Take a 2-column CSV and transform it into a hierarchical structure suitable
// for a partition layout. The first column is a sequence of step names, from
// root to leaf, separated by hyphens. The second column is a count of how 
// often that sequence occurred.
function buildHierarchy(csv) {
  var root = {"name": "root", "children": []};
  for (var i = 0; i < csv.length; i++) {
    var sequence = csv[i][0]; //first word in the row (unit name hierarchy)
    var size = +csv[i][1]; //second word in the row (percentage)
    if (isNaN(size)) { // e.g. if this is a header row
      continue;
    }
    var parts = sequence.split("-");
    var currentNode = root;
    for (var j = 0; j < parts.length; j++) {
      var children = currentNode["children"];
      var nodeName = parts[j];
      var childNode;
      if (j + 1 < parts.length) {
   // Not yet at the end of the sequence; move down the tree.
 	var foundChild = false;
 	for (var k = 0; k < children.length; k++) {
 	  if (children[k]["name"] == nodeName) {
 	    childNode = children[k];
 	    foundChild = true;
 	    break;
 	  }
 	}
  // If we don't already have a child node for this branch, create it.
 	if (!foundChild) {
 	  childNode = {"name": nodeName, "children": []};
 	  children.push(childNode);
 	}
 	currentNode = childNode;
      } else {
 	// Reached the end of the sequence; create a leaf node.
 	childNode = {"name": nodeName, "size": size};
 	children.push(childNode);
      }
    }
  }
  return root;
};