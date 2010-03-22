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
	
	function confirmAndChooseContactType(contactname, email) {
		$("#contactnameconfirmation").html(contactname); 
		$("#contactemailconfirmation").html(email); 
	

		$("#contactconfirmationdialog").dialog('open');		
	}
	
</script>

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
		<g:if test="${contacts}">
		<g:each in="${contacts}" var="contact" status="i">
			<tr>
				<td>${contact.givenName?.encodeAsHTML()}</td>
				<td>${contact.surname?.encodeAsHTML()}</td>
				<td>${contact.email?.uri?.encodeAsHTML()}</td>
				<td>${contact.organization?.displayName?.encodeAsHTML()}</td>
				<td><a href="#" onClick="confirmAndChooseContactType('${contact.givenName?.encodeAsHTML()} ${contact.surname?.encodeAsHTML()}', '${contact.email?.uri?.encodeAsHTML()}'); return false;" class="button icon icon_add"><g:message code="fedreg.link.add" /></a></td>
			</tr>
		</g:each>
		</g:if>
		<g:else>
			<tr>
				<td colspan="5">
					<div class="information">
						<p class="icon icon_information"><g:message code="fedreg.label.noresults"/></p>
					</div>
				</td>
			</tr>
		</g:else>
		</tbody>
	</table>
	
	<div id="contactconfirmationdialog" title="Add Contact">
		<div class="popup">
			<p id="contactconfirmationcontent">You're about to add <span id="contactnameconfirmation"> </span> (<span id="contactemailconfirmation"> </span>) as a contact.</p>
			<p>How do you want to classify this contact: <g:select name="contacttype" from="['Technical', 'Support', 'Administrative', 'Billing', 'Other']"/></p>
			<div>
				<a href="#" class="modal_close button icon icon_accept" onClick="confirmAction();">Accept</a>
				<a href="#" onClick="$('#contactconfirmationdialog').dialog('close');" class="modal_close button icon icon_cancel">Cancel</a>    
			</div>
		</div>
	</div>
