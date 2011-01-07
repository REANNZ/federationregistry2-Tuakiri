
<g:if test="${users}">
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
		<g:each in="${users}" var="user" status="i">
			<tr>
				
				<td>${user.profile.fullName?.encodeAsHTML()}</td>
				<td>${user.profile.email?.encodeAsHTML()}</td>
				<td><g:link controller='organization' action='show' id="${user.contact?.organization?.id}">${user.contact?.organization?.displayName?.encodeAsHTML()}</g:link></td>
				<td>						
				<n:confirmaction action="fedreg.descriptor_fulladministrator_grant(${user.id});" title="${message(code: 'fedreg.templates.descriptor.full.administrator.grant.confirm.title')}" msg="${message(code: 'fedreg.templates.descriptor.full.administrator.grant.confirm.descriptive')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" class="grant-button" label="${message(code: 'label.grant')}"/>
				</td>
			</tr>
		</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<p><g:message code="fedreg.templates.descriptor.full.administrator.search.noresults" /></p>
</g:else>
