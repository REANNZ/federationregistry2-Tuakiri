<script type="text/javascript">
	$(function() {
		$("#searchcontact").hide();
		$("#spinnercontacts").hide();
		$("#availablecontacts").hide();
		
		$("#addcontactlink").click(function() {
			$("#addcontact").hide();
			$("#searchcontact").show();
		});
		
		$("#closesearchcontactlink").click(function() {
			$("#searchcontact").hide();
			$("#availablecontacts").hide();
			$("#addcontact").show();
		});
	});
	
	var idpContactSearchEndpoint = "${createLink(controller:'identityProvider', action:'searchContacts')}";
	var idpContactLinkEndpoint = "${createLink(controller:'identityProvider', action:'linkContact')}";
	
	function searchContacts() {
		$("#availablecontacts").hide();
		$("#spinnercontacts").show();
		var dataString = "givenName=" + $('#givenName').val() + '&surname=' + $('#surname').val() + '&email=' + $('#email').val()
		$.ajax({
			type: "POST",
			url: idpContactSearchEndpoint,
			data: dataString,
			success: function(res) {
				$("#spinnercontacts").hide();
				$("#availablecontacts").empty();
				$("#availablecontacts").append(res);
				$("#availablecontacts").show();
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				$("#spinnercontacts").hide();
				growl('error', xhr.responseText);
		    }
		});
	}
</script>

<div id="addcontact">
	<a id="addcontactlink" href="#" class="button icon icon_add"><g:message code="fedreg.label.addcontact"/></a>
</div>
<div id="searchcontact">
	
		<h4>fedreg.templates.contactmanager.searchforcontacts.heading</h4>
		<table class="datatable buttons">
			<thead>
				<tr>
					<th><g:message code="fedreg.label.givenname"/></th>
					<th><g:message code="fedreg.label.surname"/></th>
					<th><g:message code="fedreg.label.email"/></th>
					<th/>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><input type="text" id="givenName" name="givenName" class="enhancedinput"/></td>
					<td><input type="text" id="surname" name="surname" class="enhancedinput"/></td>
					<td><input type="text" id="email" name="email" class="enhancedinput"/></td>
					<td>
						<a href="#" onClick="searchContacts();" class="button icon icon_magnifier"><g:message code="fedreg.link.search" /></a>
		                <a href="#" id="closesearchcontactlink" class="button icon icon_cross"><g:message code="fedreg.link.close" /></a>
		            </td>
				</tr>
			</tbody>
		</table>
		<div id="spinnercontacts">
			<img src="${resource(dir:'images', file:'spinner.gif')}" width="20" height="20">
		</div>

	<div id="availablecontacts">
	</div>
</div>