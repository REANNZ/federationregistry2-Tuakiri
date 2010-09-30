<%@ page contentType="text/html"%>
<html>
	<head></head>
	<body>
		<p><g:message code="fedreg.templates.mail.workflow.org.activated.description" /></p>
			
		<table>
			<tr>
				<td>
					<g:message code="label.internalid" />
				</td>
				<td>
					${fieldValue(bean: serviceProvider, field: "id")}
				</td>
			</tr>
			<tr>
				<td>
					<g:message code="label.displayname" />
				</td>
				<td>
					${fieldValue(bean: organization, field: "displayName")}
				</td>
			</tr>
		</table>
		
		<g:message code="fedreg.templates.mail.workflow.org.activated.nextsteps" args="[createLink(controller:'invitation', action:'claim', params:[code:invitation.inviteCode], absolute:true)]"/>
		
		<p><g:message code="fedreg.templates.mail.get.support" /></p>
		
	</body>
</html>