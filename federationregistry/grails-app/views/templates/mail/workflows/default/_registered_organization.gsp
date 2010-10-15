<%@ page contentType="text/html"%>

<g:applyLayout name="email">
	<html>
		<head></head>
		<body>
			<p><g:message code="fedreg.templates.mail.workflow.org.registered.description" /></p>
			
			<table>
				<tr>
					<td>
						<g:message code="label.organization" />
					</td>
					<td>
						${fieldValue(bean: organization, field: "displayName")}
					</td>
				</tr>
				<tr>
					<td>
						<g:message code="label.internalid" />
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