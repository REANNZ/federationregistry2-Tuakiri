<h4><g:message code="nimble.template.members.list.subjects.heading" /></h4>
<g:if test="${subjects?.size() > 0}">
	<table class="details">
		<thead>
			<tr>
				<th><g:message code="label.subjectname" /></th>
				<th><g:message code="label.fullname" /></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${subjects}" status="i" var="subject">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<g:if test="${subject.subjectname.length() > 30}">
						<td>${subject.subjectname?.substring(0,30).encodeAsHTML()}...</td>
					</g:if>
					<g:else>
						<td>${subject.subjectname?.encodeAsHTML()}</td>
					</g:else>
					<g:if test="${subject.profile?.fullName}">
						<td>${subject.profile?.fullName?.encodeAsHTML()}</td>
					</g:if>
					<g:else>
						<td>N/A</td>
					</g:else>
					<td>
						<n:button href="${createLink(controller:'subject', action:'show', id:subject.id)}" label="label.view" class="view-button" />
						<g:if test="${!protect}">
							<n:confirmaction action="nimble.removeMember('${parent.id.encodeAsHTML()}', '${subject.id.encodeAsHTML()}', '${subject.subjectname.encodeAsHTML()}');" title="nimble.role.subject.remove.confirm.title" msg="nimble.role.subject.remove.confirm.descriptive" accept="label.accept" cancel="label.cancel" label="label.remove" class="revoke-button" />
						</g:if>
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<p>
		<g:message code="nimble.template.members.list.subjects.noresults" />
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
							<n:button href="${createLink(controller:'group', action:'show', id:group.id)}" label="label.view" class="view-button" />
							<g:if test="${!protect}">
								<n:confirmaction action="nimble.removeGroupMember('${parent.id.encodeAsHTML()}', '${group.id.encodeAsHTML()}', '${group.name.encodeAsHTML()}');" title="nimble.role.group.remove.confirm.title" msg="nimble.role.group.remove.confirm.descriptive" accept="label.accept" cancel="label.cancel" label="label.remove" class="revoke-button" />
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