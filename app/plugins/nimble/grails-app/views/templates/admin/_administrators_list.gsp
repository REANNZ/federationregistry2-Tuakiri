<table class="adminslist">
	<thead>
		<tr>
			<th><g:message code="label.username" /></th>
			<th><g:message code="label.fullname" /></th>
			<th />
		</tr>
	</thead>
	<tbody>
		<g:each in="${admins.sort{it.username}}" status="i" var="admin">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				<g:if test="${admin.username.length() > 50}">
					<td>${admin.username?.substring(0,50).encodeAsHTML()}...</td>
				</g:if>
				<g:else>
					<td>${admin.username?.encodeAsHTML()}</td>
				</g:else>
				<td>${admin?.profile?.fullName.encodeAsHTML()}</td>
				<td>
					<n:button href="${createLink(controller:'user', action:'show', id:admin.id)}" label="label.view" class="view-button" />
					<g:if test="${currentAdmin != admin}">
						<n:confirmaction action="nimble.deleteAdministrator(${admin.id});" label="label.remove" class="revoke-button" title="nimble.admin.remove.confirm.title" msg="nimble.admin.remove.confirm.descriptive" accept="label.accept" cancel="label.cancel" />
					</g:if>
				</td>
			</tr>
		</g:each>
	</tbody>
</table>