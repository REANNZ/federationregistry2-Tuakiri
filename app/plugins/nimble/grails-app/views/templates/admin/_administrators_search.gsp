<g:if test="${users != null && users.size() > 0}">
	<table>
		<thead>
			<tr>
				<th><g:message code="label.username" /></th>
				<th><g:message code="label.fullname" /></th>
				<th />
			</tr>
		</thead>
		<tbody>
			<g:each in="${users}" status="i" var="user">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<g:if test="${user.username.length() > 50}">
						<td>${user.username?.substring(0,50).encodeAsHTML()}...</td>
					</g:if>
					<g:else>
						<td>${user.username?.encodeAsHTML()}</td>
					</g:else>
					<td>${user?.profile?.fullName.encodeAsHTML()}</td>
					<td>
						<n:button href="${createLink(controller:'user', action:'show', id:user.id)}" label="label.view" class="view-button" />
						<n:button onclick="nimble.grantAdministrator('${user.id.encodeAsHTML()}', '${user.username.encodeAsHTML()}');" label="label.grant" class="add-button" />
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<p>
		<strong><g:message code="nimble.view.admins.search.notfound" /></strong>
	</p>
</g:else>