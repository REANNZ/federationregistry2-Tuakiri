<%@ page contentType="text/html"%>

<g:applyLayout name="email">
	<html>
		<head></head>
		<body>
			<p><g:message code="fr.branding.mail.workflow.org.rejected.description" /></p>
			
			<table>
				<tr>
					<td>
						<strong><g:message code="label.internalid" /></strong>: 
					</td>
					<td>
						${fieldValue(bean: organization, field: "id")}
					</td>
				</tr>
				<tr>
					<td>
						<strong><g:message code="label.displayname" /></strong>: 
					</td>
					<td>
						${fieldValue(bean: organization, field: "displayName")}
					</td>
				</tr>
			</table>
		
			<p><g:message code="fr.branding.mail.get.support" /></p>
		
		</body>
	</html>
</g:applyLayout>