<g:if test="${administrators}">
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
				</td>
			</tr>
		</g:each>
	</tbody>
</table>
</g:if>
<g:else>
	<p><g:message code="fedreg.template.administrators.noresults" /></p>
</g:else>