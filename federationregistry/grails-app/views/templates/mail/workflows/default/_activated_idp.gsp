
<html>
	<head></head>
	<body>
		<p><g:message code="fedreg.templates.mail.workflow.idp.activated.description" /></p>
			
		<table>
			<tr>
				<td>
					<g:message code="label.internalid" />
				</td>
				<td>
					${fieldValue(bean: identityProvider, field: "id")}
				</td>
			</tr>
			<tr>
				<td>
					<g:message code="label.displayname" />
				</td>
				<td>
					${fieldValue(bean: identityProvider, field: "displayName")}
				</td>
			</tr>
			<tr>
				<td>
					<g:message code="label.description" />
				</td>
				<td>
					${fieldValue(bean: identityProvider, field: "description")}
				</td>
			</tr>
			<tr>
				<td>
					<g:message code="label.organization" />
				</td>
				<td>
					${fieldValue(bean: identityProvider, field: "organization.displayName")}
				</td>
			</tr>
		</table>
		
		<g:message code="fedreg.templates.mail.workflow.idp.activated.nextsteps" args="[createLink(controller:'invitation', action:'claim', params:[code:invitation.inviteCode], absolute:true)]"/>
		
		<p><g:message code="fedreg.templates.mail.get.support" /></p>
		
	</body>
</html>