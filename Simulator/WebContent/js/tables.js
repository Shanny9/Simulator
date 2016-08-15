/**
 * 
 */
$(document).ready(function() {
	
	supplierTable();
//	incidentTable();
//	solutionTable();
//	priorityCostTable();
//	serviceTable();
//	serviceDepTable();
//	serviceDivTable();
//	ciTable();
	
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
					create : true,
					inputClass: 'validate[required,maxSize[20]]'
				},
				solutionCost : {
					title : 'Solution Cost',
					width : '25%',
					edit : true,
					inputClass: 'validate[required,custom[number],min[0],max[1.7976931348623157E+308]]'
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
					options: { 'NIS': 'NIS', 'USD': 'USD', 'EUR' : 'EUR'},
					edit : true
				}
			},
            //Initialize validation logic when a form is created
            formCreated: function (event, data) {
                data.form.validationEngine();
                data.form.find('[name=supplierName]').attr('maxlength','20');
            },
            //Validate form when it is being submitted
            formSubmitting: function (event, data) {
                return data.form.validationEngine('validate');
            },
            //Dispose validation logic when form is closed
            formClosed: function (event, data) {
                data.form.validationEngine('hide');
                data.form.validationEngine('detach');
            }
//			formCreated: function (event, data) {
//				data.form.find('[name=solutionCost]').attr('type','number');
//				data.form.find('[name=solutionCost]').attr('min','0');
//				data.form.find('[name=solutionCost]').attr('max','1.7976931348623157E+308');				
//		    }
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
				create : true,
				inputClass: 'validate[required,custom[integer],min[1],max[255],maxSize[3]]'
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
		},
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
            data.form.find('[name=incidentId]').attr('maxlength','3');
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }
//		formCreated: function (event, data) {
//			
//			data.form.find('[name=incidentId]').attr('type','number');
//			data.form.find('[name=incidentId]').attr('min','1');
//			data.form.find('[name=incidentId]').attr('max','255');
//	    }
	    
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
		},
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
            data.form.find('[name=solutionId]').attr('maxlength','10');
            data.form.find('[name=solutionMarom]').attr('maxlength','11');
            data.form.find('[name=solutionRakia]').attr('maxlength','11');
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }
//		formCreated: function (event, data) {
//			
//			data.form.find('[name=solutionId]').attr('type','number');
//			data.form.find('[name=solutionId]').attr('min','1');
//			
//			
//			data.form.find('[name=solutionMarom]').attr('type','number');
//			data.form.find('[name=solutionMarom]').attr('min','1');
//			
//			
//			data.form.find('[name=solutionRakia]').attr('type','number');
//			data.form.find('[name=solutionRakia]').attr('min','1');
//	    }
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
		},
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
            data.form.find('[name=solutionId]').attr('maxlength','10');
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }
//		formCreated: function (event, data) {		
//			data.form.find('[name=solutionId]').attr('type','number');
//			data.form.find('[name=solutionId]').attr('min','0');
//			data.form.find('[name=solutionId]').attr('max','1.7976931348623157E+308');
//	    }
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
				create : true,
				inputClass: 'validate[required,custom[integer],min[1],max[255]]'
			},
			serviceCode : {
				title : 'Code',
				edit : true,
				inputClass: 'validate[maxSize[15]]'
			},
			serviceName : {
				title : 'Name',
				edit : true,
				inputClass: 'validate[maxSize[60]]'
			},
			fixedCost : {
				title : 'Fixed Cost',
				edit : true,
				inputClass: 'validate[required,custom[number],min[0],max[1.7976931348623157E+308]]'
			},
			fixedIncome : {
				title : 'Fixed Income',
				edit : true,
				inputClass: 'validate[required,custom[number],min[0],max[1.7976931348623157E+308]]'
			},
			isTechnical : {
				title : 'Technical?',
				type: 'checkbox',
                values: { 'false': 'No', 'true': 'Yes' },
//                defaultValue: 'true',
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
				type: 'checkbox',
                values: { 'false': 'No', 'true': 'Yes' },
				edit : true
			}
		},
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
            data.form.find('[name=serviceId]').attr('maxlength','3');
            data.form.find('[name=serviceCode]').attr('maxlength','15');
            data.form.find('[name=serviceName]').attr('maxlength','60');
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }
//		formCreated: function (event, data) {
//			data.form.find('[name=serviceId]').attr('maxlength','3');
//			data.form.find('[name=serviceId]').attr('type','number');
//			data.form.find('[name=serviceId]').attr('min','1');
//			data.form.find('[name=serviceId]').attr('max','255');
			
/*			data.form.find('[name=serviceCode]').attr('maxlength','15');
			
			data.form.find('[name=serviceName]').attr('maxlength','60');
						
			data.form.find('[name=fixedCost]').attr('type','number');
			data.form.find('[name=fixedCost]').attr('min','0');
			data.form.find('[name=fixedCost]').attr('max','1.7976931348623157E+308');
			
			data.form.find('[name=fixedIncome]').attr('type','number');
			data.form.find('[name=fixedIncome]').attr('min','0');
			data.form.find('[name=fixedIncome]').attr('max','1.7976931348623157E+308');*/
//	    }
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
			},
            //Initialize validation logic when a form is created
            formCreated: function (event, data) {
                data.form.validationEngine();
                data.form.find('[name=serviceId]').attr('maxlength','3');
            },
            //Validate form when it is being submitted
            formSubmitting: function (event, data) {
                return data.form.validationEngine('validate');
            },
            //Dispose validation logic when form is closed
            formClosed: function (event, data) {
                data.form.validationEngine('hide');
                data.form.validationEngine('detach');
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
			},
            //Initialize validation logic when a form is created
            formCreated: function (event, data) {
                data.form.validationEngine();
            },
            //Validate form when it is being submitted
            formSubmitting: function (event, data) {
                return data.form.validationEngine('validate');
            },
            //Dispose validation logic when form is closed
            formClosed: function (event, data) {
                data.form.validationEngine('hide');
                data.form.validationEngine('detach');
            }
		});
		$('#tableContainer').jtable('load');
	}

function ciTable(){
	
	$('#tableContainer').jtable({
		title : 'Configuration Items List',
		paging: true, //Set paging enabled
		pageSize: 7, //Set page size
		pageSizes: [7,10,20],
		selecting: true,
		selectingCheckboxes: true,
		multiselect: true,
		deleteConfirmation: true,
	//	editinline:{enable:true},
	//	toolbarsearch:true,
		actions : {
			listAction : 'DataController?action=list&table=ci',
			createAction : 'DataController?action=create&table=ci',
			updateAction : 'DataController?action=update&table=ci',
			deleteAction : 'DataController?action=delete&table=ci'
		},
		toolbar: {
	        items: [{
	            tooltip: 'Click here to export this table to excel',
	            text: 'Export to Excel',
	            icon: 'css/metro/Excel-icon.png',
	            click: function () {
	                $.ajax({
	                    url: 'DataController?action=excel&table=ci',     
	            		dataType: "json",
	                    success: function(data) {  
	                    	JSONToCSVConvertor(data.Records,"CIs",true);
	            			 
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
			ciId : {
				title : 'ID',
//				width : '30%',
				key : true,
				list : true,
				edit : false,
				create : true
			},
			CI_name : {
				title : 'Name',
//				width : '25%',
				edit : true
			},
			supplierName1 : { //supplier level 2
				title : 'Supplier Level 2',
//				width : '25%',
				edit : true
			},
			supplierName2 : { //supplier level 3
				title : 'Supplier Level 3',
//				width : '25%',
				edit : true
			},
			isActive : {
				title : 'Active?',
//				width : '20%',
				type: 'checkbox',
                values: { 'false': 'No', 'true': 'Yes' },
                defaultValue: 'true',
				edit : true
			}
		},
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
			data.form.find('[name=ciId]').attr('maxlength','3');
			data.form.find('[name=CI_name]').attr('maxlength','60');
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }

	});
	$('#tableContainer').jtable('load');
}

function CMDBTable(){
	
	$('#tableContainer').jtable({
		title : 'Configuration Items Management List',
		paging: true, //Set paging enabled
		pageSize: 7, //Set page size
		pageSizes: [7,10,20],
		selecting: true,
		selectingCheckboxes: true,
		multiselect: true,
		deleteConfirmation: true,
	//	editinline:{enable:true},
	//	toolbarsearch:true,
		actions : {
			listAction : 'DataController?action=list&table=cmdb',
			createAction : 'DataController?action=create&table=cmdb',
			updateAction : 'DataController?action=update&table=cmdb',
			deleteAction : 'DataController?action=delete&table=cmdb'
		},
		toolbar: {
	        items: [{
	            tooltip: 'Click here to export this table to excel',
	            text: 'Export to Excel',
	            icon: 'css/metro/Excel-icon.png',
	            click: function () {
	                $.ajax({
	                    url: 'DataController?action=excel&table=cmdb',     
	            		dataType: "json",
	                    success: function(data) {  
	                    	JSONToCSVConvertor(data.Records,"CMDBs",true);
	            			 
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
			ciId : {
				title : 'CI ID',
//				width : '30%',
				key : true,
				list : true,
				edit : false,
				create : true
			},
			serviceId : {
				title : 'Service ID',
//				width : '25%',
				edit : true
			},
			isActive : {
				title : 'Active?',
//				width : '20%',
				type: 'checkbox',
                values: { 'false': 'No', 'true': 'Yes'},
                defaultValue: 'true',
				edit : true
			}
		},
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }
	});
	$('#tableContainer').jtable('load');
}

function departmentTable(){
	
	$('#tableContainer').jtable({
		title : 'Departments List',
		paging: true, //Set paging enabled
		pageSize: 7, //Set page size
		pageSizes: [7,10,20],
		selecting: true,
		selectingCheckboxes: true,
		multiselect: true,
		deleteConfirmation: true,
	//	editinline:{enable:true},
	//	toolbarsearch:true,
		actions : {
			listAction : 'DataController?action=list&table=department',
			createAction : 'DataController?action=create&table=department',
			updateAction : 'DataController?action=update&table=department',
			deleteAction : 'DataController?action=delete&table=department'
		},
		toolbar: {
	        items: [{
	            tooltip: 'Click here to export this table to excel',
	            text: 'Export to Excel',
	            icon: 'css/metro/Excel-icon.png',
	            click: function () {
	                $.ajax({
	                    url: 'DataController?action=excel&table=department',     
	            		dataType: "json",
	                    success: function(data) {  
	                    	JSONToCSVConvertor(data.Records,"Departments",true);
	            			 
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
			departmentName : {
				title : 'Department Name',
//				width : '30%',
				key : true,
				list : true,
				edit : true,
				create : true
			},
			devisionName : {
				title : 'Devision Name',
				key : true,
//				width : '25%',
				list : true,
				edit : true,
				create : true
			},
			isActive : {
				title : 'Active?',
//				width : '20%',
				type: 'checkbox',
                values: { 'false': 'No', 'true': 'Yes'},
                defaultValue: 'true',
				edit : true
			}
		},
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
            data.form.find('[name=departmentName]').attr('maxlength','25');
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }

	});
	$('#tableContainer').jtable('load');
}

function divisionTable(){
	
	$('#tableContainer').jtable({
		title : 'Departments List',
		paging: true, //Set paging enabled
		pageSize: 7, //Set page size
		pageSizes: [7,10,20],
		selecting: true,
		selectingCheckboxes: true,
		multiselect: true,
		deleteConfirmation: true,
	//	editinline:{enable:true},
	//	toolbarsearch:true,
		actions : {
			listAction : 'DataController?action=list&table=division',
			createAction : 'DataController?action=create&table=division',
			updateAction : 'DataController?action=update&table=division',
			deleteAction : 'DataController?action=delete&table=division'
		},
		toolbar: {
	        items: [{
	            tooltip: 'Click here to export this table to excel',
	            text: 'Export to Excel',
	            icon: 'css/metro/Excel-icon.png',
	            click: function () {
	                $.ajax({
	                    url: 'DataController?action=excel&table=division',     
	            		dataType: "json",
	                    success: function(data) {  
	                    	JSONToCSVConvertor(data.Records,"Divisions",true);
	            			 
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
			devisionName : {
				title : 'Devision Name',
				key : true,
//				width : '25%',
				list : true,
				edit : true,
				create : true
			},
			isActive : {
				title : 'Active?',
//				width : '20%',
				type: 'checkbox',
                values: { 'false': 'No', 'true': 'Yes'},
                defaultValue: 'true',
				edit : true
			}
		},
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
            data.form.find('[name=devisionName]').attr('maxlength','25');
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }
	});
	$('#tableContainer').jtable('load');
}

function eventTable(){
	
	$('#tableContainer').jtable({
		title : 'Departments List',
		paging: true, //Set paging enabled
		pageSize: 7, //Set page size
		pageSizes: [7,10,20],
		selecting: true,
		selectingCheckboxes: true,
		multiselect: true,
		deleteConfirmation: true,
	//	editinline:{enable:true},
	//	toolbarsearch:true,
		actions : {
			listAction : 'DataController?action=list&table=event',
			createAction : 'DataController?action=create&table=event',
			updateAction : 'DataController?action=update&table=event',
			deleteAction : 'DataController?action=delete&table=event'
		},
		toolbar: {
	        items: [{
	            tooltip: 'Click here to export this table to excel',
	            text: 'Export to Excel',
	            icon: 'css/metro/Excel-icon.png',
	            click: function () {
	                $.ajax({
	                    url: 'DataController?action=excel&table=event',     
	            		dataType: "json",
	                    success: function(data) {  
	                    	JSONToCSVConvertor(data.Records,"Events",true);
	            			 
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
			eventId : {
				title : 'Event ID',
				key : true,
//				width : '25%',
				edit : true,
			},
			incidentId : {
				title : 'Incident ID',
				key : true,
//				width : '25%',
				edit : true,
			},
			serviceId : {
				title : 'Service ID',
				key : true,
//				width : '25%',
				edit : true,
			},
			isActive : {
				title : 'Active?',
//				width : '20%',
				type: 'checkbox',
                values: { 'false': 'No', 'true': 'Yes'},
                defaultValue: 'true',
				edit : true
			}
		},
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
            data.form.find('[name=eventId]').attr('maxlength','10');
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }

	});
	$('#tableContainer').jtable('load');
}

