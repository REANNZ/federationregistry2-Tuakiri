<g:if test="${groups?.size() > 0}">
	<table class="details">
		<thead>
			<tr>
				<th><g:message code="label.name" /></th>
				<th><g:message code="label.description" /></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${groups}" status="i" var="group">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td>${group.name?.encodeAsHTML()}</td>
					<td>${group.description?.encodeAsHTML()}</td>
					<td>
						<n:button href="${createLink(controller:'group', action:'show', id:group.id)}" label="label.view" icon="arrowthick-1-ne" />
						<n:button onclick="nimble.grantGroup('${ownerID.encodeAsHTML()}', '${group.id.encodeAsHTML()}');" label="label.add" icon="plus" />
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<p>
		<strong><g:message code="nimble.template.groups.add.noresults" /></strong>
	</p>
</g:else>