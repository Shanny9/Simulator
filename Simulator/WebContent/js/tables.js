/**
 * 
 */
$(document).ready(function() {
	
//	supplierTable();
//	incidentTable();
//	solutionTable();
//	priorityCostTable();
//	serviceTable();
	serviceDepTable();
//	serviceDivTable();
	
});


function supplierTable(){

		$('#tableContainer').jtable({
			title : 'Suppliers List',
			paging: true, //Set paging enabled
			pageSize: 7, //Set page size
			pageSizes: [7,10,15],
			selecting: true,
			selectingCheckboxes: true,
			multiselect: true,
			deleteConfirmation: true,
		//	editinline:{enable:true},
		//	toolbarsearch:true,
			actions : {
				listAction : 'DataController?action=list&table=supplier',
				createAction : 'DataController?action=create&table=supplier',
				updateAction : 'DataController?action=update&table=supplier',
				deleteAction : 'DataController?action=delete&table=supplier'
			},
			toolbar: {
		        items: [{
		            tooltip: 'Click here to export this table to excel',
		            text: 'Export to Excel',
		            icon: 'css/metro/Excel-icon.png',
		            click: function () {
		                $.ajax({
		                    url: 'DataController?action=excel&table=suppliers',     
		            		dataType: "json",
		                    success: function(data) {  
		                    	JSONToCSVConvertor(data.Records,"Suppliers",true);
		            			 
		                    },
		                    error: function(e) {
		            			alert("error in getting users!");
		                    }
		                });
		            }
		        }, {
		            icon: 'css/metro/delete_toolbar2.png',
		            text: 'Delete Selected',
		            click: function () {
		            	var $selectedRows = $('#tableContainer').jtable('selectedRows');
		            	//show confirmation dialog
		            	$('<div></div>').appendTo('body')
		            	  .html('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><span class="jtable-delete-confirm-message">'+$selectedRows.length+ ' record(s) will be deleted. Are you sure?</span></p></div>')
		            	  .dialog({
		            	      modal: true, title: 'Are you sure?', zIndex: 10000, autoOpen: true,
		            	      width: 'auto', resizable: false,
		            	      buttons: {
		            	          Cancel: function () {
			            	              $(this).dialog("close");
			            	          },
		            	          Delete: function () {
		            	        	 //call the delete function from jtable 
		      		                $('#tableContainer').jtable('deleteRows', $selectedRows);
		            	              $(this).dialog("close");
		            	          }

		            	      },
		            	      close: function (event, ui) {
		            	          $(this).remove();
		            	      }
		            	});
		            	
		            }//end click function
		        }//end delete button
		          ]
		    },
			fields : {
				supplierName : {
					title : 'Supplier Name',
					width : '30%',
					key : true,
					list : true,
					edit : true,
					create : true
				},
				solutionCost : {
					title : 'Solution Cost',
					width : '25%',
					key : true,
					edit : true
				},
				isActive : {
					title : 'Active?',
					width : '20%',
					options: { '1': 'Yes', '0': 'No'},
					edit : true
				},
				currency : {
					title : 'Currency',
					width : '25%',
					options: { 'NIS': 'NIS', 'USD': 'USD'},
					edit : true
				}
			}
		});
		$('#tableContainer').jtable('load');
}

function incidentTable(){

	$('#tableContainer').jtable({
		title : 'Incidents List',
		paging: true, //Set paging enabled
		pageSize: 7, //Set page size
		pageSizes: [7,10,15],
		selecting: true,
		selectingCheckboxes: true,
		multiselect: true,
		deleteConfirmation: true,
	//	editinline:{enable:true},
	//	toolbarsearch:true,
		actions : {
			listAction : 'DataController?action=list&table=incident',
			createAction : 'DataController?action=create&table=incident',
			updateAction : 'DataController?action=update&table=incident',
			deleteAction : 'DataController?action=delete&table=incident'
		},
		toolbar: {
	        items: [{
	            tooltip: 'Click here to export this table to excel',
	            text: 'Export to Excel',
	            icon: 'css/metro/Excel-icon.png',
	            click: function () {
	                $.ajax({
	                    url: 'DataController?action=excel&table=incident',     
	            		dataType: "json",
	                    success: function(data) {  
	                    	JSONToCSVConvertor(data.Records,"Incidents",true);
	            			 
	                    },
	                    error: function(e) {
	            			alert("error in getting users!");
	                    }
	                });
	            }
	        }, {
	            icon: 'css/metro/delete_toolbar2.png',
	            text: 'Delete Selected',
	            click: function () {
	            	var $selectedRows = $('#tableContainer').jtable('selectedRows');
	            	//show confirmation dialog
	            	$('<div></div>').appendTo('body')
	            	  .html('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><span class="jtable-delete-confirm-message">'+$selectedRows.length+ ' record(s) will be deleted. Are you sure?</span></p></div>')
	            	  .dialog({
	            	      modal: true, title: 'Are you sure?', zIndex: 10000, autoOpen: true,
	            	      width: 'auto', resizable: false,
	            	      buttons: {
	            	          Cancel: function () {
		            	              $(this).dialog("close");
		            	          },
	            	          Delete: function () {
	            	        	 //call the delete function from jtable 
	      		                $('#tableContainer').jtable('deleteRows', $selectedRows);
	            	              $(this).dialog("close");
	            	          }

	            	      },
	            	      close: function (event, ui) {
	            	          $(this).remove();
	            	      }
	            	});
	            	
	            }//end click function
	        }//end delete button
	          ]
	    },
		fields : {
			incidentId : {
				title : 'Incident ID',
				key : true,
				list : true,
				edit : false,
				create : true
			},
			incidentTime : {
				title : 'Incident Time',
				edit : true
			},
			ciId : {
				title : 'Configuration Item',
				edit : true
			},
			solutionId : {
				title : 'Solution',
				edit : true
			},
			isActive : {
				title : 'Active?',
				options: { 1: 'Yes', 0: 'No'},
				edit : true
			}
		}
	});
	$('#tableContainer').jtable('load');

}

function solutionTable(){

	$('#tableContainer').jtable({
		title : 'Solution List',
		paging: true, //Set paging enabled
		pageSize: 7, //Set page size
		pageSizes: [7,10,15],
		selecting: true,
		selectingCheckboxes: true,
		multiselect: true,
		deleteConfirmation: true,
	//	editinline:{enable:true},
	//	toolbarsearch:true,
		actions : {
			listAction : 'DataController?action=list&table=solution',
			createAction : 'DataController?action=create&table=solution',
			updateAction : 'DataController?action=update&table=solution',
			deleteAction : 'DataController?action=delete&table=solution'
		},
		toolbar: {
	        items: [{
	            tooltip: 'Click here to export this table to excel',
	            text: 'Export to Excel',
	            icon: 'css/metro/Excel-icon.png',
	            click: function () {
	                $.ajax({
	                    url: 'DataController?action=excel&table=solution',     
	            		dataType: "json",
	                    success: function(data) {  
	                    	JSONToCSVConvertor(data.Records,"Solutions",true);
	            			 
	                    },
	                    error: function(e) {
	            			alert("error in getting users!");
	                    }
	                });
	            }
	        }, {
	            icon: 'css/metro/delete_toolbar2.png',
	            text: 'Delete Selected',
	            click: function () {
	            	var $selectedRows = $('#tableContainer').jtable('selectedRows');
	            	//show confirmation dialog
	            	$('<div></div>').appendTo('body')
	            	  .html('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><span class="jtable-delete-confirm-message">'+$selectedRows.length+ ' record(s) will be deleted. Are you sure?</span></p></div>')
	            	  .dialog({
	            	      modal: true, title: 'Are you sure?', zIndex: 10000, autoOpen: true,
	            	      width: 'auto', resizable: false,
	            	      buttons: {
	            	          Cancel: function () {
		            	              $(this).dialog("close");
		            	          },
	            	          Delete: function () {
	            	        	 //call the delete function from jtable 
	      		                $('#tableContainer').jtable('deleteRows', $selectedRows);
	            	              $(this).dialog("close");
	            	          }

	            	      },
	            	      close: function (event, ui) {
	            	          $(this).remove();
	            	      }
	            	});
	            	
	            }//end click function
	        }//end delete button
	          ]
	    },
		fields : {
			solutionId : {
				title : 'Solution ID',
				width : '30%',
				key : true,
				list : true,
				edit : true,
				create : true
			},
			solutionMarom : {
				title : 'Marom',
				width : '25%',
				edit : true
			},
			solutionRakia : {
				title : 'Rakia',
				width : '20%',
				edit : true
			},
			isActive : {
				title : 'Active?',
				width : '25%',
	//			type : 'checkbox',
				options: { '1': 'Yes','0': 'No' },
				edit : true
			}
		}
	});
	$('#tableContainer').jtable('load');
}

function priorityCostTable(){

	$('#tableContainer').jtable({
		title : 'Priority List',
		paging: true, //Set paging enabled
		pageSize: 7, //Set page size
		pageSizes: [7,10,15],
		selecting: true,
		selectingCheckboxes: true,
		multiselect: true,
		deleteConfirmation: true,
	//	editinline:{enable:true},
	//	toolbarsearch:true,
		actions : {
			listAction : 'DataController?action=list&table=priority',
			createAction : 'DataController?action=create&table=priority',
			updateAction : 'DataController?action=update&table=priority',
			deleteAction : 'DataController?action=delete&table=priority'
		},
		toolbar: {
	        items: [{
	            tooltip: 'Click here to export this table to excel',
	            text: 'Export to Excel',
	            icon: 'css/metro/Excel-icon.png',
	            click: function () {
	                $.ajax({
	                    url: 'DataController?action=excel&table=priority',     
	            		dataType: "json",
	                    success: function(data) {  
	                    	JSONToCSVConvertor(data.Records,"Priority Cost",true);
	            			 
	                    },
	                    error: function(e) {
	            			alert("error in getting users!");
	                    }
	                });
	            }
	        }, {
	            icon: 'css/metro/delete_toolbar2.png',
	            text: 'Delete Selected',
	            click: function () {
	            	var $selectedRows = $('#tableContainer').jtable('selectedRows');
	            	//show confirmation dialog
	            	$('<div></div>').appendTo('body')
	            	  .html('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><span class="jtable-delete-confirm-message">'+$selectedRows.length+ ' record(s) will be deleted. Are you sure?</span></p></div>')
	            	  .dialog({
	            	      modal: true, title: 'Are you sure?', zIndex: 10000, autoOpen: true,
	            	      width: 'auto', resizable: false,
	            	      buttons: {
	            	          Cancel: function () {
		            	              $(this).dialog("close");
		            	          },
	            	          Delete: function () {
	            	        	 //call the delete function from jtable 
	      		                $('#tableContainer').jtable('deleteRows', $selectedRows);
	            	              $(this).dialog("close");
	            	          }

	            	      },
	            	      close: function (event, ui) {
	            	          $(this).remove();
	            	      }
	            	});
	            	
	            }//end click function
	        }//end delete button
	          ]
	    },
		fields : {
			pName : {
				title : 'Priority Name',
				width : '30%',
				key : true,
				list : true,
				edit : true,
//				options: { 'Low': 'Low', 'Medium': 'Medium', 'High': 'High','Critical': 'Critical','Major': 'Major'},
				create : true
			},
			pCost : {
				title : 'Cost',
				width : '25%',
				edit : true
			},
			isActive : {
				title : 'Active?',
				width : '25%',
	//			type : 'checkbox',
				options: { '1': 'Yes','0': 'No' },
				edit : true
			}
		}
	});
	$('#tableContainer').jtable('load');
}

function serviceTable(){

	$('#tableContainer').jtable({
		title : 'Service List',
		paging: true, //Set paging enabled
		pageSize: 7, //Set page size
		pageSizes: [7,10,15],
		selecting: true,
		selectingCheckboxes: true,
		multiselect: true,
		deleteConfirmation: true,
	//	editinline:{enable:true},
	//	toolbarsearch:true,
		actions : {
			listAction : 'DataController?action=list&table=service',
			createAction : 'DataController?action=create&table=service',
			updateAction : 'DataController?action=update&table=service',
			deleteAction : 'DataController?action=delete&table=service'
		},
		toolbar: {
	        items: [{
	            tooltip: 'Click here to export this table to excel',
	            text: 'Export to Excel',
	            icon: 'css/metro/Excel-icon.png',
	            click: function () {
	                $.ajax({
	                    url: 'DataController?action=excel&table=service',     
	            		dataType: "json",
	                    success: function(data) {  
	                    	JSONToCSVConvertor(data.Records,"Services",true);
	            			 
	                    },
	                    error: function(e) {
	            			alert("error in getting users!");
	                    }
	                });
	            }
	        }, {
	            icon: 'css/metro/delete_toolbar2.png',
	            text: 'Delete Selected',
	            click: function () {
	            	var $selectedRows = $('#tableContainer').jtable('selectedRows');
	            	//show confirmation dialog
	            	$('<div></div>').appendTo('body')
	            	  .html('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><span class="jtable-delete-confirm-message">'+$selectedRows.length+ ' record(s) will be deleted. Are you sure?</span></p></div>')
	            	  .dialog({
	            	      modal: true, title: 'Are you sure?', zIndex: 10000, autoOpen: true,
	            	      width: 'auto', resizable: false,
	            	      buttons: {
	            	          Cancel: function () {
		            	              $(this).dialog("close");
		            	          },
	            	          Delete: function () {
	            	        	 //call the delete function from jtable 
	      		                $('#tableContainer').jtable('deleteRows', $selectedRows);
	            	              $(this).dialog("close");
	            	          }

	            	      },
	            	      close: function (event, ui) {
	            	          $(this).remove();
	            	      }
	            	});
	            	
	            }//end click function
	        }//end delete button
	          ]
	    },
		fields : {
			serviceId : {
				title : 'ID',
				key : true,
				list : true,
				edit : true,
				create : true
			},
			serviceCode : {
				title : 'Code',
				edit : true
			},
			serviceName : {
				title : 'Name',
				edit : true
			},
			fixedCost : {
				title : 'Fixed Cost',
				edit : true
			},
			fixedIncome : {
				title : 'Fixed Income',
				edit : true
			},
			isTechnical : {
				title : 'Technical?',
				options: { '1': 'Yes','0': 'No' },
				edit : true
			},
			supplierLevel2 : {
				title : 'Supplier Level 2',
				edit : true
			},
			supplierLevel3 : {
				title : 'Supplier Level 3',
				edit : true
			},
			urgency : {
				title : 'Urgency',
				options: { 'Low': 'Low','Medium': 'Medium', 'High': 'High'},
				edit : true
			},
			impact : {
				title : 'Impact',
				options: { 'Low': 'Low','Medium': 'Medium', 'High': 'High'},
				edit : true
			},
			isActive : {
				title : 'Active?',
	//			type : 'checkbox',
				options: { '1': 'Yes','0': 'No' },
				edit : true
			}
		}
	});
	$('#tableContainer').jtable('load');
}
	function serviceDepTable(){

		$('#tableContainer').jtable({
			title : 'Service-Department List',
			paging: true, //Set paging enabled
			pageSize: 7, //Set page size
			pageSizes: [7,10,15],
			selecting: true,
			selectingCheckboxes: true,
			multiselect: true,
			deleteConfirmation: true,
		//	editinline:{enable:true},
		//	toolbarsearch:true,
			actions : {
				listAction : 'DataController?action=list&table=serviceDepartment',
				createAction : 'DataController?action=create&table=serviceDepartment',
				updateAction : 'DataController?action=update&table=serviceDepartment',
				deleteAction : 'DataController?action=delete&table=serviceDepartment'
			},
			toolbar: {
		        items: [{
		            tooltip: 'Click here to export this table to excel',
		            text: 'Export to Excel',
		            icon: 'css/metro/Excel-icon.png',
		            click: function () {
		                $.ajax({
		                    url: 'DataController?action=excel&table=serviceDepartment',     
		            		dataType: "json",
		                    success: function(data) {  
		                    	JSONToCSVConvertor(data.Records,"Service-Department",true);
		            			 
		                    },
		                    error: function(e) {
		            			alert("error in getting users!");
		                    }
		                });
		            }
		        }, {
		            icon: 'css/metro/delete_toolbar2.png',
		            text: 'Delete Selected',
		            click: function () {
		            	var $selectedRows = $('#tableContainer').jtable('selectedRows');
		            	//show confirmation dialog
		            	$('<div></div>').appendTo('body')
		            	  .html('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><span class="jtable-delete-confirm-message">'+$selectedRows.length+ ' record(s) will be deleted. Are you sure?</span></p></div>')
		            	  .dialog({
		            	      modal: true, title: 'Are you sure?', zIndex: 10000, autoOpen: true,
		            	      width: 'auto', resizable: false,
		            	      buttons: {
		            	          Cancel: function () {
			            	              $(this).dialog("close");
			            	          },
		            	          Delete: function () {
		            	        	 //call the delete function from jtable 
		      		                $('#tableContainer').jtable('deleteRows', $selectedRows);
		            	              $(this).dialog("close");
		            	          }

		            	      },
		            	      close: function (event, ui) {
		            	          $(this).remove();
		            	      }
		            	});
		            	
		            }//end click function
		        }//end delete button
		          ]
		    },
			fields : {
				service_ID : {
					title : 'Service ID',
					width : '15%',
					key : true,
					list : true,
					edit : true,
					create : true
				},
				departmentName : {
					title : 'Department Name',
					width : '30%',
					key : true,
					list : true,
					edit : true,
					create : true
				},
				divisionName : {
					title : 'Division Name',
					width : '30%',
					key : true,
					list : true,
					edit : true,
					create : true
				},
				isActive : {
					title : 'Active?',
					width : '25%',
		//			type : 'checkbox',
					options: { '1': 'Yes','0': 'No' },
					edit : true
				}
			}
		});
		$('#tableContainer').jtable('load');
	}

	function serviceDivTable(){

		$('#tableContainer').jtable({
			title : 'Service-Division List',
			paging: true, //Set paging enabled
			pageSize: 7, //Set page size
			pageSizes: [7,10,15],
			selecting: true,
			selectingCheckboxes: true,
			multiselect: true,
			deleteConfirmation: true,
		//	editinline:{enable:true},
		//	toolbarsearch:true,
			actions : {
				listAction : 'DataController?action=list&table=serviceDivision',
				createAction : 'DataController?action=create&table=serviceDivision',
				updateAction : 'DataController?action=update&table=serviceDivision',
				deleteAction : 'DataController?action=delete&table=serviceDivision'
			},
			toolbar: {
		        items: [{
		            tooltip: 'Click here to export this table to excel',
		            text: 'Export to Excel',
		            icon: 'css/metro/Excel-icon.png',
		            click: function () {
		                $.ajax({
		                    url: 'DataController?action=excel&table=serviceDivision',     
		            		dataType: "json",
		                    success: function(data) {  
		                    	JSONToCSVConvertor(data.Records,"Service-Division",true);
		            			 
		                    },
		                    error: function(e) {
		            			alert("error in getting users!");
		                    }
		                });
		            }
		        }, {
		            icon: 'css/metro/delete_toolbar2.png',
		            text: 'Delete Selected',
		            click: function () {
		            	var $selectedRows = $('#tableContainer').jtable('selectedRows');
		            	//show confirmation dialog
		            	$('<div></div>').appendTo('body')
		            	  .html('<div><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><span class="jtable-delete-confirm-message">'+$selectedRows.length+ ' record(s) will be deleted. Are you sure?</span></p></div>')
		            	  .dialog({
		            	      modal: true, title: 'Are you sure?', zIndex: 10000, autoOpen: true,
		            	      width: 'auto', resizable: false,
		            	      buttons: {
		            	          Cancel: function () {
			            	              $(this).dialog("close");
			            	          },
		            	          Delete: function () {
		            	        	 //call the delete function from jtable 
		      		                $('#tableContainer').jtable('deleteRows', $selectedRows);
		            	              $(this).dialog("close");
		            	          }

		            	      },
		            	      close: function (event, ui) {
		            	          $(this).remove();
		            	      }
		            	});
		            	
		            }//end click function
		        }//end delete button
		          ]
		    },
			fields : {
				service_ID : {
					title : 'Service ID',
					width : '25%',
					key : true,
					list : true,
					edit : true,
					create : true
				},
				divisionName : {
					title : 'Division Name',
					width : '30%',
					key : true,
					list : true,
					edit : true,
					create : true
				},
				isActive : {
					title : 'Active?',
					width : '25%',
		//			type : 'checkbox',
					options: { '1': 'Yes','0': 'No' },
					edit : true
				}
			}
		});
		$('#tableContainer').jtable('load');
	}