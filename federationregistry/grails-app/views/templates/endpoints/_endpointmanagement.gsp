<script type="text/javascript">

	function removeEndpoint(id, endpointType, containerID) {
		$("#working").trigger("fedreg.working");
		var dataString = "id=" + id;
		$.ajax({
			type: "POST",
			url: endpointDeleteEndpoint,
			data: dataString,
			success: function(res) {
				growl('success', res);
				listEndpoints(endpointType, containerID)
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

</script>