<g:if test="${permissions?.size() > 0}">
	<table>
		<thead>
			<tr>
				<th><g:message code="label.type" /></th>
				<th><g:message code="label.target" /></th>
				<th><g:message code="label.managed" /></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${permissions}" status="i" var="perm">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td>${perm.type.tokenize('.').last().encodeAsHTML()}</td>
					<td>${perm.target.encodeAsHTML()}</td>
					<td>
						<g:if test="${perm.managed}">
							<g:message code="label.yes" />
						</g:if>
						<g:else>
							<g:message code="label.no" />
						</g:else>
					</td>
					<td>
						<g:if test="${!perm.managed}">
							<n:confirmaction action="nimble.removePermission('${parent.id.encodeAsHTML()}', '${g.fieldValue(bean:perm, field:'id')}');" title="nimble.permission.remove.confirm.title" msg="nimble.permission.remove.confirm.descriptive" accept="${message(code:'label.accept')}" cancel="${message(code:'label.cancel')}" label="${message(code:'label.delete')}" class="delete-button" />
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
		<g:message code="nimble.template.permission.list.noresults" />
	</p>
</g:else>