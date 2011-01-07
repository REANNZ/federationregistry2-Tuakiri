
<table>
	<thead>
		<tr>
			<th><g:message code="label.name" /></th>
			<th><g:message code="label.email" /></th>
			<th/>
		</tr>
	</thead>
	<tbody>
		<g:each in="${contacts.sort{it.surname}}" var="contact" status="i">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				<td>${contact.givenName?.encodeAsHTML()} ${contact.surname?.encodeAsHTML()}</td>
				<td><a href="mailto:${contact.email?.uri.encodeAsHTML()}">${contact.email?.uri.encodeAsHTML()}</a></td>
				<td>
					<n:button href="${createLink(controller:'contacts', action:'show', id: contact.id)}" label="${message(code:'label.view')}" class="view-button"/>
				</td>
			</tr>
		</g:each>
	</tbody>
</table>
