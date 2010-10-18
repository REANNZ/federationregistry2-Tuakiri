<g:if test="${roles?.size() > 0}">
	<table>
	<thead>
	<tr>
		<th><g:message code="label.name" /></th>
		<th><g:message code="label.description" /></th>
		<th/>
	</tr>
	</thead>
		<tbody>
			<g:each in="${roles}" status="i" var="role">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td>${role.name?.encodeAsHTML()}</td>
					<td>${role.description?.encodeAsHTML()}</td>
					<td>
						<n:button href="${createLink(controller:'role', action:'show', id:role.id)}" label="label.view" icon="arrowthick-1-ne" />
						<g:if test="${role.protect == false}">
							<n:confirmaction action="nimble.removeRole('${ownerID.encodeAsHTML()}', '${role.id.encodeAsHTML()}');" title="nimble.role.remove.confirm.title" msg="nimble.role.remove.confirm.descriptive" accept="label.accept" cancel="label.cancel"  label="label.remove" icon="minus" />
						</g:if>
						<g:else>&nbsp;</g:else>
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<p>
		<g:message code="nimble.template.roles.list.noresults" />
	</p>
</g:else>