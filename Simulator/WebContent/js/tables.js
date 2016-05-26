/**
 * 
 */
$(document).ready(function() {
	
	suppliersTable();
	
});


function suppliersTable(){

		$('#SupplierTableContainer').jtable({
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
				listAction : 'Controller?action=list',
				createAction : 'Controller?action=create',
				updateAction : 'Controller?action=update',
				deleteAction : 'Controller?action=delete'
			},
			toolbar: {
		        items: [{
		            tooltip: 'Click here to export this table to excel',
		            text: 'Export to Excel',
		            icon: 'css/metro/Excel-icon.png',
		            click: function () {
		                $.ajax({
		                    url: 'Controller?action=excel',     
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
		            	var $selectedRows = $('#SupplierTableContainer').jtable('selectedRows');
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
		      		                $('#SupplierTableContainer').jtable('deleteRows', $selectedRows);
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
		$('#SupplierTableContainer').jtable('load');

}