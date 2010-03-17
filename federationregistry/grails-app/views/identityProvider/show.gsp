
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />

		<title><g:message code="fedreg.view.members.identityprovider.show.title" /></title>
	</head>
	<body>
		
		<h2><g:message code="fedreg.view.members.identityprovider.show.heading" /></h2>
		
		<div class="details">
			<table class="datatable">
				<tbody>		
					<tr>
						<th><g:message code="fedreg.label.displayname"/></th>
						<td>${fieldValue(bean: identityProvider, field: "displayName")}</td>
					</tr>
					<tr>
						<th><g:message code="fedreg.label.description"/></th>
						<td>${fieldValue(bean: identityProvider, field: "description")}</td>
					</tr>
					<tr>
						<th><g:message code="fedreg.label.created"/></th>
						<td>${fieldValue(bean: identityProvider, field: "dateCreated")}</td>
					</tr>
					<tr>
						<th><g:message code="fedreg.label.lastupdated"/></th>
						<td>${fieldValue(bean: identityProvider, field: "lastUpdated")}</td>
					</tr>
					<tr>
						<th><g:message code="fedreg.label.organization"/></th>
						<td><g:link controller="organization" action="show" id="${identityProvider.organization.id}">${fieldValue(bean: identityProvider, field: "organization")}</g:link></td>
					</tr>
					<tr>
						<th><g:message code="fedreg.label.entity"/></th>
						<td><g:link controller="entity" action="show" id="${identityProvider.entityDescriptor.id}">${fieldValue(bean: identityProvider, field: "entityDescriptor")}</g:link></td>
					</tr>
					<g:if test="${identityProvider.errorURL}">
					<tr>
						<th><g:message code="fedreg.label.errorurl"/></th>
						<td><a href="${identityProvider.errorURL}">${fieldValue(bean: identityProvider, field: "errorURL")}</a></td>
					</tr>
					</g:if>
					<tr>
						<th><g:message code="fedreg.label.requiresignedauthn"/></th>
						<td>
							<g:if test="${identityProvider.wantAuthnRequestsSigned}">
								<div class="icon icon_tick"><g:message code="fedreg.label.yes" /></div>
							</g:if>
							<g:else>
								<div class="icon icon_cross"><g:message code="fedreg.label.no" /></div>
							</g:else>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<!--
		BREADCRUMB: ORGANIZATION >> ENTITYID
		
		// Single
		Date dateCreated
		Date lastUpdated
		String displayName
		String description
		
		Organization organization 
		UrlURI errorURL
				
		boolean wantAuthnRequestsSigned
		
		String extensions
		
		// Many
		contacts: ContactPerson
		
		keyDescriptors: KeyDescriptor
		
		singleSignOnServices: SingleSignOnService
		singleLogoutServices: SingleLogoutService
		
		attributes: Attribute
		attributeProfiles: SamlURI

		nameIDFormats: SamlURI
		protocolSupportEnumerations: SamlURI
		
		assertionIDRequestServices: AssertionIDRequestService
		artifactResolutionServices: ArtifactResolutionService
		nameIDMappingServices: NameIDMappingService
		manageNameIDServices: ManageNameIDService
		-->

	</body>
</html>
