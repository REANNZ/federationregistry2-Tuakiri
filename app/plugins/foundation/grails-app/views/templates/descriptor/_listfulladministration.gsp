<div id="descriptorfulladministratorlist">
	<g:if test="${administrators}">
		<h3><g:message code="fedreg.templates.descriptor.full.administrators" /></h3>
		<table>
			<thead>
				<tr>
					<th><g:message code="label.name" /></th>
					<th><g:message code="label.email" /></th>
					<th><g:message code="label.organization" /></th>
					<th/>
				</tr>
			</thead>
			<tbody>
				<g:each in="${administrators.sort{it.principal}}" var="admin" status="i">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${admin.profile.displayName?.encodeAsHTML()}</td>
						<td><a href="mailto:${admin.profile.email?.encodeAsHTML()}">${admin.profile.email?.encodeAsHTML()}</a></td>
						<td><g:link controller='organization' action='show' id="${admin.contact?.organization?.id}">${admin.contact?.organization?.displayName?.encodeAsHTML()}</g:link></td>
						<td>
							<n:isAdministrator>
								<n:button href="${createLink(controller:'user', action:'show', id: admin.id)}" label="${message(code:'label.view')}" class="view-button"/>
							</n:isAdministrator>
							<fr:hasPermission target="descriptor:${descriptor.id}:manage:administrators">
								<n:confirmaction action="fedreg.descriptor_fulladministrator_revoke(${admin.id});" title="${message(code: 'fedreg.templates.descriptor.full.administrator.revoke.confirm.title')}" msg="${message(code: 'fedreg.templates.descriptor.full.administrator.revoke.confirm.descriptive')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" class="revoke-button" label="${message(code: 'label.revoke')}"/>
							</fr:hasPermission>
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</g:if>
	<g:else>
		<p><g:message code="fedreg.templates.descriptor.administrator.noresults" /></p>
	</g:else>
</div>