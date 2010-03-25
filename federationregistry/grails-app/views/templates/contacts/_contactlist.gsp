
	<table class="cleantable">
		<thead>
			<tr>
				<th><g:message code="fedreg.label.name" /></th>
				<th><g:message code="fedreg.label.email" /></th>
				<th><g:message code="fedreg.label.type" /></th>
				<th/>
			</tr>
		</thead>
		<tbody>
			<g:each in="${descriptor.contacts}" var="contactPerson" status="i">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				<td>${contactPerson.contact.givenName?.encodeAsHTML()} ${contactPerson.contact.surname?.encodeAsHTML()}</td>
				<td>${contactPerson.contact.email?.uri.encodeAsHTML()}</td>
				<td>${contactPerson.type.displayName.encodeAsHTML()}</td>
				<g:if test="${allowremove}">
				<td><a href="#" onClick="unlinkContact(${contactPerson.id});" class="button icon icon_delete"><g:message code="fedreg.link.remove"/></td>
				</g:if>
			</tr>
			</g:each>
		</tbody>
	</table>