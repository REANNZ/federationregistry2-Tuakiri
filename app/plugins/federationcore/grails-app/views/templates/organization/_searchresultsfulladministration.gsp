
<g:if test="${users}">
	<table>
		<thead>
			<tr>
				<th><g:message code="label.name" /></th>
				<th><g:message code="label.email" /></th>
				<th/>
			</tr>
		</thead>
		<tbody>
		<g:each in="${users}" var="user" status="i">
			<tr>
				<td>${user.profile.fullName?.encodeAsHTML()}</td>
				<td>${user.profile.email?.encodeAsHTML()}</td>
				<td>						
				<n:confirmaction action="fedreg.organization_fulladministrator_grant(${user.id});" title="${message(code: 'fedreg.templates.organization.full.administrator.grant.confirm.title')}" msg="${message(code: 'fedreg.templates.organization.full.administrator.grant.confirm.descriptive')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" class="grant-button" label="${message(code: 'label.grant')}"/>
				</td>
			</tr>
		</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<p><g:message code="fedreg.templates.organization.full.administrator.search.noresults" /></p>
</g:else>
