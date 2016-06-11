/**
 * 
 */
$(document).ready(function() {
	
	supplierTable();
//	incidentTable();
	
});


function supplierTable(){

		$('#tableContainer').jtable({
			title : 'Suppliers List',
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
					edit : false,
					create : true
				},
				solutionCost : {
					title : 'Solution Cost',
					width : '25%',
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
		pageSize: 10, //Set page size
		pageSizes: [10,20,30],
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
				options: { '1': 'Yes', '0': 'No'},
				edit : true
			},
			solutionId : {
				title : 'Solution',
				options: { 'NIS': 'NIS', 'USD': 'USD'},
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