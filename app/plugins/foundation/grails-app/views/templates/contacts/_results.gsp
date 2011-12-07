
	<g:if test="${contacts}">
		<table>
			<thead>
				<tr>
					<th><g:message code="label.givenname" /></th>
					<th><g:message code="label.surname" /></th>
					<th><g:message code="label.email" /></th>
					<th><g:message code="label.organization" /></th>
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
						<n:button href="#" onclick="fedreg.contact_confirm('${contact.id}', '${contact.givenName} ${contact.surname}', '${contact.email?.uri}');" label="${message(code:'label.add')}" class="add-button"/>
					</td>
				</tr>
			</g:each>
			</tbody>
		</table>
	</g:if>
	<g:else>
		<p class="icon icon_information"><g:message code="label.noresults"/></p>
		<n:button href="${createLink(controller:'contacts', action:'create')}" label="${message(code:'label.newcontact')}" class="add-button"/>
	</g:else>
