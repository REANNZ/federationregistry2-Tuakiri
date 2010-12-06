$(function() {
	$('#${tableID}').dataTable( {
			"sPaginationType": "full_numbers",
			"bLengthChange": false,
			"iDisplayLength": 10,
			"aaSorting": [[${sortColumn}, "asc"]],
			"oLanguage": {
				"sSearch": "${g.message(code:'label.filter')}",
				"sZeroRecords": "${g.message(code:'label.nomatches')}",
				"sInfo": "${g.message(code:'label.recordcount')}",
				"sInfoEmpty": "${g.message(code:'label.recordshowing')}",
				"sInfoFiltered": "${g.message(code:'label.recordfiltered')}",
				"oPaginate": {
					"sFirst": "${g.message(code:'label.first')}",
					"sLast": "${g.message(code:'label.last')}",
					"sNext": "${g.message(code:'label.next')}",
					"sPrevious": "${g.message(code:'label.previous')}",
				}
			}
		} );
});