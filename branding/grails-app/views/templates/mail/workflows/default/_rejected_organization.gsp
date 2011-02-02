<%@ page contentType="text/html"%>

<g:applyLayout name="email">
	<html>
		<head></head>
		<body>
			<p><g:message code="fedreg.templates.mail.workflow.org.rejected.description" /></p>
			
			<table>
				<tr>
					<td>
						<strong><g:message code="label.organization" /></strong>: 
					</td>
					<td>
						${fieldValue(bean: organization, field: "displayName")}
					</td>
				</tr>
				<tr>
					<td>
						<strong><g:message code="label.internalid" /></strong>: 
					</td>
					<td>
						${fieldValue(bean: organization, field: "id")}
					</td>
				</tr>
			</table>
		
			<p><g:message code="fedreg.templates.mail.get.support" /></p>
		
		</body>
	</html>
</g:applyLayout>