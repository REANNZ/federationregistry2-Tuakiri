<g:if test="${users != null && users.size() > 0}">
	<table>
		<thead>
			<tr>
				<th><g:message code="label.username" /></th>
				<th><g:message code="label.fullname" /></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${users}" status="i" var="user">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<g:if test="${user.username.length() > 30}">
						<td>${user.username?.substring(0,30).encodeAsHTML()}...</td>
					</g:if>
					<g:else>
						<td>${user.username?.encodeAsHTML()}</td>
					</g:else>
					<g:if test="${user?.profile?.fullName}">
						<td>${user?.profile?.fullName.encodeAsHTML()}</td>
					</g:if>
					<g:else>
						<td>N/A</td>
					</g:else>
					<td>
						<n:button href="${createLink(controller:'user', action:'show', id:user.id)}" label="label.view" class="view-button" />
						<n:button onclick="nimble.addMember('${parent.id.encodeAsHTML()}', '${user.id.encodeAsHTML()}', '${user.username.encodeAsHTML()}');" label="label.add" class="add-button"/></a>
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<div class="info">
		<strong><g:message code="nimble.template.members.add.user.noresults" /></strong>
	</div>
</g:else>