<script type="text/javascript">
	$(function() {
		fedreg.contact_dialogInit();
		$("#searchcontact").hide();
		$("#availablecontacts").hide();
		
		$("#addcontactlink").click(function() {
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
</script>

<div>
	<div id="addcontact" class="searcharea">
		<n:button id="addcontactlink" href="#" label="${message(code:'label.addcontact')}" icon="plus"/>
	</div>
	<div id="searchcontact" class="searcharea">
		<h3><g:message code="fedreg.templates.contactmanager.searchforcontacts.heading"/></h3>
		<table class="datatable buttons">
			<thead>
				<tr>
					<th><g:message code="label.givenname"/></th>
					<th><g:message code="label.surname"/></th>
					<th><g:message code="label.email"/></th>
					<th/>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><input type="text" id="givenName" name="givenName" class="enhancedinput"/></td>
					<td><input type="text" id="surname" name="surname" class="enhancedinput"/></td>
					<td><input type="text" id="email" name="email" class="enhancedinput"/></td>
					<td>
						<n:button href="#" onclick="fedreg.contact_search(${descriptor.id});" label="${message(code:'label.search')}" icon="search"/>
						<n:button id="closesearchcontactlink" href="#" label="${message(code:'label.close')}" icon="close"/>
		            </td>
				</tr>
			</tbody>
		</table>		

		<div id="availablecontacts">
		</div>
	</div>

	<div id="contactconfirmationdialog" title="Add Contact">
		<div class="popup">
			<p><g:message code="fedreg.template.contacts.confirmaddition"/></p>
			<p><g:message code="fedreg.template.contacts.selecttype"/></p>
			<g:select id="contactselectedtype" name="contactType" from="${contactTypes}" optionKey="name" optionValue="displayName"/>
			<div class="buttons">
				<n:button href="#" onclick="fedreg.contact_create();" label="${message(code:'label.accept')}" icon="check" class="modal_close"/>
				<n:button href="#" onclick="\$('#contactconfirmationdialog').dialog('close');" label="${message(code:'label.cancel')}" icon="cancel" class="modal_close"/>
			</div>
		</div>
	</div>
</div>