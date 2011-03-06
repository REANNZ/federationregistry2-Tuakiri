<n:hasPermission target="organization:${organization.id}:contact:add">
	<script type="text/javascript">
		$(function() {
			fedreg.contact_dialogInit();
			$("#searchcontact").hide();
			$("#availablecontacts").hide();
		
			$("#closesearchcontactlink").click(function() {
				$("#searchcontact").fadeOut();
				$("#availablecontacts").fadeOut();
				$("#addcontact").fadeIn();
				$("#availablecontacts").empty();
			});
		});
	</script>
	<hr>
	<div id="addcontact" class="searcharea">
		<n:button onclick="\$('#addcontact').fadeOut(); \$('#searchcontact').fadeIn(); \$('#email').focus();" label="${message(code:'label.addcontact')}" class="add-button"/>
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
						<n:button href="#" onclick="fedreg.orgcontact_search(${organization.id});" label="${message(code:'label.search')}" class="search-button"/>
						<n:button onclick="\$('#searchcontact').fadeOut(); \$('#availablecontacts').fadeOut(); \$('#addcontact').fadeIn(); \$('#availablecontacts').empty();" label="${message(code:'label.close')}" class="close-button"/>
		            </td>
				</tr>
			</tbody>
		</table>		

		<div id="availablecontacts">
		</div>
	</div>

	<div id="contactconfirmationdialog" title="Add Contact">
		<div class="popup">
			<p><g:message code="fedreg.templates.contacts.confirmaddition"/></p>
			<p><g:message code="fedreg.templates.contacts.selecttype"/></p>
			<g:select id="contactselectedtype" name="contactType" from="${contactTypes}" optionKey="name" optionValue="displayName"/>
			<div class="buttons">
				<n:button href="#" onclick="fedreg.orgcontact_create();" label="${message(code:'label.accept')}" class="save-button modal_close"/>
				<n:button href="#" onclick="\$('#contactconfirmationdialog').dialog('close');" label="${message(code:'label.cancel')}" class="close-button modal_close"/>
			</div>
		</div>
	</div>
</n:hasPermission>
