
<html>
	<head></head>
	<body>
		<p><g:message code="fedreg.templates.mail.idpssoroledescriptor.register.description" /></p>
			
		<table>
			<tr>
				<td>
					<g:message code="label.organization" />
				</td>
				<td>
					${fieldValue(bean: identityProvider, field: "organization.displayName")}
				</td>
			</tr>
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
					<g:message code="label.entitydescriptor" />
				</td>
				<td>
					${fieldValue(bean: identityProvider, field: "entityDescriptor.entityID")}
				</td>
			</tr>
			<tr>
				<td>
					<g:message code="label.httppostendpoint" />
				</td>
				<td>
					${fieldValue(bean: httpPost, field: "location.uri")}
				</td>
			</tr>
			<tr>
				<td>
					<g:message code="label.httpredirectendpoint" />
				</td>
				<td>
					${fieldValue(bean: httpRedirect, field: "location.uri")}
				</td>
			</tr>
			<tr>
				<td>
					<g:message code="label.soapartifactendpoint" />
				</td>
				<td>
					${fieldValue(bean: soapArtifact, field: "location.uri")}
				</td>
			</tr>
		</table>
		
		<p><g:message code="fedreg.templates.mail.get.support" /></p>
		
	</body>
</html>