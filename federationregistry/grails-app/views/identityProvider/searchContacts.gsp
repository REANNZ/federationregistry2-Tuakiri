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
	
	var activeContact;
	function confirmAndChooseContactType(contactname, email) {
		$("#contactnameconfirmation").html(contactname); 
		$("#contactemailconfirmation").html(email); 
	
		$( "#contactconfirmationdialog" ).dialog( "option", "hide", 'drop' );
		$("#contactconfirmationdialog").dialog('open');		
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
					<td><a href="#" onClick="activeContact = ${contact.id}; confirmAndChooseContactType('${contact.givenName?.encodeAsHTML()} ${contact.surname?.encodeAsHTML()}', '${contact.email?.uri?.encodeAsHTML()}'); return false;" class="button icon icon_add"><g:message code="fedreg.link.add" /></a></td>
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
			<p id="contactconfirmationcontent"><g:message code="fedreg.template.contacts.confirmaddition"/></p>
			<p><g:message code="fedreg.template.contacts.selecttype"/><g:select id="contactselectedtype" name="contacttype" from="${contactTypes}" optionKey="name" optionValue="displayName"/></p>
			<div>
				<a href="#" class="modal_close button icon icon_accept" onClick="linkContact();">Accept</a>
				<a href="#" onClick="$('#contactconfirmationdialog').dialog('close');" class="modal_close button icon icon_cancel">Cancel</a>    
			</div>
		</div>
	</div>
