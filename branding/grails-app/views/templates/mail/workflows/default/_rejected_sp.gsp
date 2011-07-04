<%@ page contentType="text/html"%>

<g:applyLayout name="email">
	<html>
		<head></head>
		<body>
			<p><g:message code="fedreg.templates.mail.workflow.sp.rejected.description" /></p>
			
			<table>
				<tr>
					<td>
						<strong><g:message code="label.internalid" /></strong>: 
					</td>
					<td>
						${fieldValue(bean: serviceProvider, field: "id")}
					</td>
				</tr>
				<tr>
					<td>
						<strong><g:message code="label.displayname" /></strong>: 
					</td>
					<td>
						${fieldValue(bean: serviceProvider, field: "displayName")}
					</td>
				</tr>
				<tr>
					<td>
						<strong><g:message code="label.description" /></strong>: 
					</td>
					<td>
						${fieldValue(bean: serviceProvider, field: "description")}
					</td>
				</tr>
				<tr>
					<td>
						<strong><g:message code="label.entitydescriptor" /></strong>: 
					</td>
					<td>
						${fieldValue(bean: serviceProvider, field: "entityDescriptor.entityID")}
					</td>
				</tr>
				<tr>
					<td>
						<strong><g:message code="label.organization" /></strong>: 
					</td>
					<td>
						${fieldValue(bean: serviceProvider, field: "organization.displayName")}
					</td>
				</tr>
			</table>
		
			<p><g:message code="fedreg.templates.mail.get.support" /></p>
		
		</body>
	</html>
</g:applyLayout>