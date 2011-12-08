<g:if test="${subjects != null && subjects.size() > 0}">
	<table>
		<thead>
			<tr>
				<th><g:message code="label.subjectname" /></th>
				<th><g:message code="label.fullname" /></th>
				<th />
			</tr>
		</thead>
		<tbody>
			<g:each in="${subjects}" status="i" var="subject">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<g:if test="${subject.subjectname.length() > 50}">
						<td>${subject.subjectname?.substring(0,50).encodeAsHTML()}...</td>
					</g:if>
					<g:else>
						<td>${subject.subjectname?.encodeAsHTML()}</td>
					</g:else>
					<td>${subject?.profile?.fullName?.encodeAsHTML()}</td>
					<td>
						<n:button href="${createLink(controller:'subject', action:'show', id:subject.id)}" label="label.view" class="view-button" />
						<n:button onclick="nimble.grantAdministrator('${subject.id.encodeAsHTML()}', '${subject.subjectname.encodeAsHTML()}');" label="label.grant" class="add-button" />
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