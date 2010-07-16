
	<g:if test="${descriptor.contacts}">
	<table>
		<thead>
			<tr>
				<th><g:message code="fedreg.label.name" /></th>
				<th><g:message code="fedreg.label.email" /></th>
				<th><g:message code="fedreg.label.type" /></th>
				<th/>
			</tr>
		</thead>
		<tbody>
			<g:each in="${descriptor.contacts.sort{it.contact.surname}}" var="contactPerson" status="i">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				<td>${contactPerson.contact.givenName?.encodeAsHTML()} ${contactPerson.contact.surname?.encodeAsHTML()}</td>
				<td>${contactPerson.contact.email?.uri.encodeAsHTML()}</td>
				<td>${contactPerson.type.displayName.encodeAsHTML()}</td>
				<td>
					<fr:button href="${createLink(controller:'contacts', action:'show', id: contactPerson.contact.id)}" label="${message(code:'fedreg.link.view')}" icon="arrowthick-1-ne"/>
				<g:if test="${allowremove}">
					<fr:button href="#" onclick="fedreg.contact_delete(${contactPerson.id});" label="${message(code:'fedreg.link.delete')}" icon="trash"/>
				</td>
				</g:if>
			</tr>
			</g:each>
		</tbody>
	</table>
	</g:if>
	<g:else>
		<p><g:message code="fedreg.template.contacts.noresults" /></p>
	</g:else>