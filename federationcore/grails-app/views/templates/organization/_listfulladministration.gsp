<div id="organizationfulladministratorlist">
	<g:if test="${administrators}">
		<h3><g:message code="fedreg.templates.organization.full.administrators" /></h3>
		<table>
			<thead>
				<tr>
					<th><g:message code="label.name" /></th>
					<th><g:message code="label.username" /></th>
					<th><g:message code="label.email" /></th>
					<th/>
				</tr>
			</thead>
			<tbody>
				<g:each in="${administrators.sort{it.username}}" var="admin" status="i">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${admin.profile.fullName?.encodeAsHTML()}</td>
						<td>${admin.username?.encodeAsHTML()}</td>
						<td><a href="mailto:${admin.profile.email?.encodeAsHTML()}">${admin.profile.email?.encodeAsHTML()}</a></td>
						<td>
							<n:isAdministrator>
								<n:button href="${createLink(controller:'user', action:'show', id: admin.id)}" label="${message(code:'label.view')}" icon="arrowthick-1-ne"/>
							</n:isAdministrator>
							<n:hasPermission target="organization:${organization.id}:manage:administrators">
								<n:confirmaction action="fedreg.organization_fulladministrator_revoke(${admin.id});" title="${message(code: 'fedreg.templates.organization.full.administrator.revoke.confirm.title')}" msg="${message(code: 'fedreg.templates.organization.full.administrator.revoke.confirm.descriptive')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" icon="circle-close" label="${message(code: 'label.revoke')}"/>
							</n:hasPermission>
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</g:if>
	<g:else>
		<p><g:message code="fedreg.templates.organization.administrator.noresults" /></p>
	</g:else>
</div>