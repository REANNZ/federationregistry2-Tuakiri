<script type="text/javascript">

	$(function() {
		contactDialogInit();
		$("#searchcontact").hide();
		$("#availablecontacts").hide();
	
		$("#add${endpointType}").click(function() {
			$("#addcontact").hide();
			$("#searchcontact").show('slide');
			$("#email").focus();
		});
	
		$("#closesearchcontactlink").click(function() {
			$("#searchcontact").hide('slide');
			$("#availablecontacts").hide();
			$("#addcontact").show('slide');
			$("#availablecontacts").empty();
		});
	});

	function removeEndpoint(id, endpointType, containerID) {
		$("#working").trigger("fedreg.working");
		var dataString = "id=" + id;
		$.ajax({
			type: "POST",
			url: endpointDeleteEndpoint,
			data: dataString,
			success: function(res) {
				growl('success', res);
				listEndpoints(endpointType, containerID);
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}
	
	function listEndpoints(endpointType, containerID) {
		var dataString = "endpointType=" + endpointType + "&containerID=" + containerID;
		$.ajax({
			type: "GET",
			url: endpointListEndpoint,
			data: dataString,
			success: function(res) {
				$("#"+containerID).html(res)
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}
	
	function createEndpoint(endpointType, containerID) {
		$("#working").trigger("fedreg.working");
		var dataString = "endpointType=" + endpointType + "&containerID=" + containerID + "&" + $("#new" + endpointType + "data").serialize();
		$.ajax({
			type: "POST",
			url: endpointCreationEndpoint,
			data: dataString,
			success: function(res) {
				$(':input', "#new" + endpointType + "data")
				 	.not(':button, :submit, :reset, :hidden, select[name=binding]')
				 	.val('')
				listEndpoints(endpointType, containerID);
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}
	
	function toggleEndpointState(id, endpointType, containerID) {
		$("#working").trigger("fedreg.working");
		var dataString = "id=" + id;
		$.ajax({
			type: "POST",
			url: endpointToggleStateEndpoint,
			data: dataString,
			success: function(res) {
				growl('success', res);
				listEndpoints(endpointType, containerID);
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}

</script>