<%@ page contentType="text/html"%>
<html>
	<head></head>
	<body>
		<p><g:message code="fedreg.templates.mail.workflow.sp.registered.description" /></p>
			
		<table>
			<tr>
				<td>
					<g:message code="label.organization" />
				</td>
				<td>
					${fieldValue(bean: serviceProvider, field: "organization.displayName")}
				</td>
			</tr>
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
					${fieldValue(bean: serviceProvider, field: "displayName")}
				</td>
			</tr>
			<tr>
				<td>
					<g:message code="label.description" />
				</td>
				<td>
					${fieldValue(bean: serviceProvider, field: "description")}
				</td>
			</tr>
			<tr>
				<td>
					<g:message code="label.entitydescriptor" />
				</td>
				<td>
					${fieldValue(bean: serviceProvider, field: "entityDescriptor.entityID")}
				</td>
			</tr>
		</table>
		
		<p><g:message code="fedreg.templates.mail.get.support" /></p>
		
	</body>
</html>