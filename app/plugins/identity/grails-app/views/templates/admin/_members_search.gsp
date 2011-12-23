<g:if test="${subjects != null && subjects.size() > 0}">
	<table>
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
					<g:if test="${subject?.profile?.displayName}">
						<td>${subject?.profile?.displayName.encodeAsHTML()}</td>
					</g:if>
					<g:else>
						<td>N/A</td>
					</g:else>
					<td>
						<n:button href="${createLink(controller:'subject', action:'show', id:subject.id)}" label="label.view" class="view-button" />
						<n:button onclick="nimble.addMember('${parent.id.encodeAsHTML()}', '${subject.id.encodeAsHTML()}', '${subject.subjectname.encodeAsHTML()}');" label="label.add" class="add-button"/></a>
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<div class="info">
		<strong><g:message code="nimble.template.members.add.subject.noresults" /></strong>
	</div>
</g:else>