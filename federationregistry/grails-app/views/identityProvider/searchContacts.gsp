<script type="text/javascript">
	$(function() {
		$("#contactconfirmationdialog").dialog({
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
	});
	
	function confirmAndChooseContactType(id, contactID, name, email) {	
		$("#linkcontactform input[name=id]").val(id)
		$("#linkcontactform input[name=contactID]").val(contactID)
		$("#contactnameconfirmation").html(name);
		$("#contactemailconfirmation").html(email)
		$("#contactconfirmationdialog").dialog( "option", "hide", "drop" );
		$("#contactconfirmationdialog").dialog('open');
	}
	
	function linkContact(id, contactID, contactType) {
		
		var dataString = $("#linkcontactform").serialize();
		alert(dataString);
		$.ajax({
			type: "POST",
			url: linkContactEndpoint,
			data: dataString,
			success: function(res) {
				$("#contactconfirmationdialog").dialog('close');
				growl('success', res);
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				growl('error', xhr.responseText);
		    }
		});
	}
	
</script>
	<g:if test="${contacts}">
		<table>
			<thead>
				<tr>
					<th><g:message code="fedreg.label.givenname" /></th>
					<th><g:message code="fedreg.label.surname" /></th>
					<th><g:message code="fedreg.label.email" /></th>
					<th><g:message code="fedreg.label.organization" /></th>
					<th/>
				</tr>
			</thead>
			<tbody>
			<g:each in="${contacts}" var="contact" status="i">
				<tr>
					<td>${contact.givenName?.encodeAsHTML()}</td>
					<td>${contact.surname?.encodeAsHTML()}</td>
					<td>${contact.email?.uri?.encodeAsHTML()}</td>
					<td>${contact.organization?.displayName?.encodeAsHTML()}</td>
					<td>
						<a href="#" onClick="confirmAndChooseContactType('${roleDescriptor.id}', '${contact.id}', '${contact.givenName} ${contact.surname}', '${contact.email?.uri}');" class="button icon icon_add"><g:message code="fedreg.link.add" /></a>
					</td>
				</tr>
			</g:each>
			</tbody>
		</table>
	</g:if>
	<g:else>
		<p class="icon icon_information"><g:message code="fedreg.label.noresults"/></p>
		<p><a href="#" target="_blank" class="button icon icon_add"><g:message code="fedreg.link.newcontact" /></a></p>
	</g:else>
		
	
	<div id="contactconfirmationdialog" title="Add Contact">
		<div class="popup">
			<p><g:message code="fedreg.template.contacts.confirmaddition"/></p>
			<p><g:message code="fedreg.template.contacts.selecttype"/></p>
			<form id="linkcontactform">
				<input type="hidden" name="id" value="" />
				<input type="hidden" name="contactID" value="" />
				<g:select id="contactselectedtype" name="contactType" from="${contactTypes}" optionKey="name" optionValue="displayName"/>
			</form>
			<div class="buttons">
				<a href="#" class="modal_close button icon icon_accept" onClick="linkContact();">Accept</a>
				<a href="#" onClick="$('#contactconfirmationdialog').dialog('close');" class="modal_close button icon icon_cancel">Cancel</a>    
			</div>
		</div>
	</div>
