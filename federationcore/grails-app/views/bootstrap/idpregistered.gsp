<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="members" />
        <title><g:message code="fedreg.view.members.bootstrap.identityprovider.registered.title" /></title>
    </head>
	
    <body>
		
        <section>
            <h2><g:message code="fedreg.view.members.bootstrap.identityprovider.registered.heading" /></h2>
			
			<p><g:message code="fedreg.view.members.bootstrap.identityprovider.registered.description" /></p>
			
			<table>
				<tbody>		
					<tr>
						<th><g:message code="label.id"/></th>
						<td>${fieldValue(bean: identityProvider, field: "id")}</td>
					</tr>
					<tr>
						<th><g:message code="label.displayname"/></th>
						<td>${fieldValue(bean: identityProvider, field: "displayName")}</td>
					</tr>
					<tr>
						<th><g:message code="label.description"/></th>
						<td>${fieldValue(bean: identityProvider, field: "description")}</td>
					</tr>
					<tr>
						<th><g:message code="label.organization"/></th>
						<td>${fieldValue(bean: identityProvider, field: "organization.displayName")}</td>
					</tr>
					<tr>
						<th><g:message code="label.entitydescriptor"/></th>
						<td><${fieldValue(bean: identityProvider, field: "entityDescriptor.entityID")}</td>
					</tr>
					<tr>
						<th><g:message code="label.protocolsupport"/></th>
						<td>
							<g:each in="${identityProvider.protocolSupportEnumerations}" status="i" var="pse">
							${pse.uri} <br/>
							</g:each>
						</td>
					<g:if test="${identityProvider.errorURL}">
					<tr>
						<th><g:message code="label.errorurl"/></th>
						<td>${fieldValue(bean: identityProvider, field: "errorURL")}</td>
					</tr>
					</g:if>
					<tr>
						<th><g:message code="label.status"/></th>
						<td>
							<g:if test="${identityProvider.active}">
								<g:message code="label.active" />
							</g:if>
							<g:else>
								<g:message code="label.inactive" />
							</g:else>
						</td>
					<tr>
						<th><g:message code="label.requiresignedauthn"/></th>
						<td>
							<g:if test="${identityProvider.wantAuthnRequestsSigned}">
								<g:message code="label.yes" />
							</g:if>
							<g:else>
								<g:message code="label.no" />
							</g:else>
						</td>
					</tr>
				</tbody>
			</table>

        </section>
    </body>
</html>