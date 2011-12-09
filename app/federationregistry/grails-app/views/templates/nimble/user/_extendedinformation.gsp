<h3><g:message code="nimble.view.user.show.extendedinformation.heading" /></h3>

<table class="datatable">
 	<tbody>
		<tr>
	    	<th><g:message code="label.fullname" /></th>
			<td>${user.profile?.displayName?.encodeAsHTML()}</td>
		</tr>
		<tr>
	    	<th><g:message code="label.email" /></th>
			<td>${user.profile?.email?.encodeAsHTML()}</td>
		</tr>
		<tr>
	    	<th><g:message code="label.organization" /></th>
			<td>
				<g:link controller="organization" action="show" id="${user?.entityDescriptor?.organization?.id}">${user?.entityDescriptor?.organization?.displayName?.encodeAsHTML()}</g:link>
			</td>
		</tr>
		<g:if test="${user?.contact}">
		<tr>
	    	<th><g:message code="label.contact" /></th>
			<td>
				<n:button href="${createLink(controller:'contacts', action:'show', id:user.contact.id)}" label="label.show" plain="true" />
		</tr>
		</g:if>
	</tbody>
</table>