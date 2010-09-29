<h4><g:message code="nimble.template.members.list.users.heading" /></h4>
<g:if test="${users?.size() > 0}">
	<table class="details">
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
					<g:if test="${user.profile?.fullName}">
						<td>${user.profile?.fullName?.encodeAsHTML()}</td>
					</g:if>
					<g:else>
						<td>N/A</td>
					</g:else>
					<td>
						<n:button href="${createLink(controller:'user', action:'show', id:user.id)}" label="label.view" icon="arrowthick-1-ne" />
						<g:if test="${!protect}">
							<n:confirmaction action="nimble.removeMember('${parent.id.encodeAsHTML()}', '${user.id.encodeAsHTML()}', '${user.username.encodeAsHTML()}');" title="nimble.role.user.remove.confirm.title" msg="nimble.role.user.remove.confirm.descriptive" accept="label.accept" cancel="label.cancel" label="label.remove" icon="minus" />
						</g:if>
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<p>
		<g:message code="nimble.template.members.list.users.noresults" />
	</p>
</g:else>

<g:if test="${groupmembers}">
	<h4><g:message code="nimble.template.members.list.groups.heading" /></h4>
	<g:if test="${groups?.size() > 0}">
		<table class="details">
			<thead>
				<tr>
					<th class="first"><g:message code="label.name" /></th>
					<th class=""><g:message code="label.description" /></th>
					<th class="last"></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${groups}" status="i" var="group">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${group.name.encodeAsHTML()}</td>
						<td>${group.description.encodeAsHTML()}</td>
						<td>
							<n:button href="${createLink(controller:'group', action:'show', id:group.id)}" label="label.view" icon="arrowthick-1-ne" />
							<g:if test="${!protect}">
								<n:confirmaction action="nimble.removeGroupMember('${parent.id.encodeAsHTML()}', '${group.id.encodeAsHTML()}', '${group.name.encodeAsHTML()}');" title="nimble.role.group.remove.confirm.title" msg="nimble.role.group.remove.confirm.descriptive" accept="label.accept" cancel="label.cancel" label="label.remove" icon="minus" />
							</g:if>
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</g:if>
	<g:else>
		<p>
			<g:message code="nimble.template.members.list.groups.noresults" />
		</p>
	</g:else>
</g:if>