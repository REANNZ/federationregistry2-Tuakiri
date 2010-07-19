
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
						<fr:button href="#" onclick="fedreg.contact_confirm('${contact.id}', '${contact.givenName} ${contact.surname}', '${contact.email?.uri}');" label="${message(code:'fedreg.link.add')}" icon="plus"/>
					</td>
				</tr>
			</g:each>
			</tbody>
		</table>
	</g:if>
	<g:else>
		<p class="icon icon_information"><g:message code="fedreg.label.noresults"/></p>
		<fr:button href="${createLink(controller:'contacts', action:'create')}" label="${message(code:'fedreg.link.newcontact')}" icon="plus"/>
	</g:else>
