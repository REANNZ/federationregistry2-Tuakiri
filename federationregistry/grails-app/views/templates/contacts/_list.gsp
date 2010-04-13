
	<g:if test="${descriptor.contacts}">
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
				<td><g:link controller="contacts" action="show" id="${contactPerson.contact.id}" class="button icon icon_user_go"><g:message code="fedreg.link.view"/></g:link>
				<g:if test="${allowremove}">
				<a href="#" onClick="fedreg.contact_delete(${contactPerson.id});" class="button icon icon_delete"><g:message code="fedreg.link.delete"/></td>
				</g:if>
			</tr>
			</g:each>
		</tbody>
	</table>
	</g:if>
	<g:else>
		<p><g:message code="fedreg.template.contacts.noresults" /></p>
	</g:else>