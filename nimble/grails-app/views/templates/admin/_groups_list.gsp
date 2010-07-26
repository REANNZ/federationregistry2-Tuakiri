<g:if test="${groups?.size() > 0}">
	<table>
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
					<td>${group.name?.encodeAsHTML()}</td>
					<td>${group.description?.encodeAsHTML()}</td>
					<td>
						<n:button href="${createLink(controller:'group', action:'show', id:group.id)}" label="label.view" icon="arrowthick-1-ne" />
						<g:if test="${group.protect == false}">
							<n:confirmaction action="nimble.removeGroup('${ownerID.encodeAsHTML()}', '${group.id.encodeAsHTML()}');" title="nimble.group.remove.confirm.title" msg="nimble.group.remove.confirm.descriptive" accept="label.accept" cancel="label.cancel" label="label.remove" icon="minus" />
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
	<g:message code="nimble.template.groups.list.noresults" />
</p>
</g:else>