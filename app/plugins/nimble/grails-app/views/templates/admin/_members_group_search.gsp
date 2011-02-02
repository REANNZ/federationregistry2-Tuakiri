<g:if test="${groups != null && groups.size() > 0}">
	<table>
		<thead>
			<tr>
				<th><g:message code="label.name" /></th>
				<th><g:message code="label.description" /></th>
				<th />
			</tr>
		</thead>
		<tbody>
			<g:each in="${groups}" status="i" var="group">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td>${group.name.encodeAsHTML()}</td>
					<td>${group.description.encodeAsHTML()}</td>
					<td>
						<n:button href="${createLink(controller:'group', action:'show', id:group.id)}" label="label.view" class="view-button" />
						<n:button onclick="nimble.addGroupMember('${parent.id.encodeAsHTML()}', '${group.id.encodeAsHTML()}', '${group.name.encodeAsHTML()}');" label="label.add" class="add-button" />
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<div class="info">
		<strong><g:message code="nimble.template.members.add.group.noresults" /></strong>
	</div>
</g:else>