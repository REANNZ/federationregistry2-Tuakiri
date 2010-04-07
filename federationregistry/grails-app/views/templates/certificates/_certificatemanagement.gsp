<script type="text/javascript">
	$(function() {
		certificateDialogInit();
		$("#newcertificate").hide();
		
		$("#addcertficatelink").click(function() {
			$("#addcertificate").hide();
			$("#newcertificate").show('slide');
		});
		
		$("#closenewcertificatelink").click(function() {
			$("#newcertificate").hide('slide');
			$("#addcertificate").show('slide');
		});
		
		$("#newcertificatedata").bind('paste', function() { setTimeout(function() {verifyNewCertificateData();}, 100); });
	});
	
	function certificateDialogInit() {
		$("#certificateconfirmationdialog").dialog({
			bgiframe: true,
			resizable: false,
			modal: true,
			autoOpen: false,
			width: 400,
			overlay: {
				backgroundColor: '#000',
				opacity: 0.5
			}
		});
	}
	
	function verifyNewCertificateData() {
		$("#working").trigger("fedreg.working");
		var dataString = "cert=" + $("#newcertificatedata").val();
		$.ajax({
			type: "POST",
			url: certificateValidationEndpoint,
			data: dataString,
			success: function(res) {
				$("#newcertificatedetails").html(res);
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}
	
	function createNewCertificate() {
		$("#working").trigger("fedreg.working");
		var dataString = "cert=" + $("#newcertificatedata").val() + "&certname=" + $("#newcertificatename").val();
		$.ajax({
			type: "POST",
			url: certificateCreationEndpoint,
			data: dataString,
			success: function(res) {
				$("#newcertificatedata").val('');
				$("#newcertificatedetails").html('');
				$("#newcertificate").hide('slide');
				$("#addcertificate").show('slide');
				growl('success', res);
				listCertificates();
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}
	
	function removeKeyDescriptor(id) {
		$("#working").trigger("fedreg.working");
		var dataString = "id=" + id;
		$.ajax({
			type: "POST",
			url: certificateDeleteEndpoint,
			data: dataString,
			success: function(res) {
				growl('success', res);
				listCertificates();
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}
	
	function listCertificates() {
		$.ajax({
			type: "GET",
			url: certificateListEndpoint,
			success: function(res) {
				$("#certificates").html(res)
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}
</script>

<div id="certificatemanagement">
	<div id="addcertificate" class="searcharea">
		<a id="addcertficatelink" href="#" class="button icon icon_add"><g:message code="fedreg.link.addcertificate"/></a>
	</div>
	
	<div id="newcertificate">
		<h3><g:message code="fedreg.template.certificates.certificatemanagement.addnew.heading"/></h3>
		<p>
			<g:message code="fedreg.template.certificates.certificatemanagement.addnew.requestformat" /> &nbsp;&nbsp;&nbsp; <a href="#" onClick="$('#newcertificate').hide('slide'); $('#addcertificate').show('slide');" class="modal_close button icon icon_cancel"><g:message code="fedreg.link.cancel"/></a>
			<br/>
			<g:textArea name="newcertificatedata" rows="50" cols="120"/>
		</p>
		
		<div id="newcertificatedetails">
		</div>
	</div>

	<div id="certificateconfirmationdialog" title="Add Certificate">
		<div class="popup">
			<p><g:message code="fedreg.template.certificate.confirmaddition"/></p>
			<div class="buttons">
				<a href="#" class="modal_close button icon icon_accept" onClick="addCertificate();"><g:message code="fedreg.link.accept"/></a>
				<a href="#" onClick="$('#certificateconfirmationdialog').dialog('close');" class="modal_close button icon icon_cancel"><g:message code="fedreg.link.cancel"/></a>   
			</div>
		</div>
	</div>
</div>