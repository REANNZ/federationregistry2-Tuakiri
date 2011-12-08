<g:if test="${roles?.size() > 0}">
	<table>
		<thead>
			<tr>
				<th><g:message code="label.name" /></th>
				<th><g:message code="label.description" /></th>
				<th />
			</tr>
		</thead>
		<tbody>
			<g:each in="${roles}" status="i" var="role">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td>${role.name?.encodeAsHTML()}</td>
					<td>${role.description?.encodeAsHTML()}</td>
					<td>
						<n:button href="${createLink(controller:'role', action:'show', id:role.id)}" label="label.view" class="view-button" />
						<n:button onclick="nimble.grantRole('${ownerID.encodeAsHTML()}', '${role.id.encodeAsHTML()}');" label="label.add" class="add-button" />
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<p>
		<strong><g:message code="nimble.template.roles.add.noresults" /></strong>
	</p>
</g:else>