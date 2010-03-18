
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />

		<title><g:message code="fedreg.view.members.identityprovider.show.title" /></title>
		
		<script type="text/javascript">
			$(function() {
				$("#tabs").tabs();
			});
		</script>
	</head>
	<body>
		
		<h2><g:message code="fedreg.view.members.identityprovider.show.heading" args="[identityProvider.displayName]"/></h2>
		<div id="identityprovider">
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
							<th><g:message code="fedreg.label.entitydescriptor"/></th>
							<td><g:link controller="entity" action="show" id="${identityProvider.entityDescriptor.id}">${fieldValue(bean: identityProvider, field: "entityDescriptor")}</g:link></td>
						</tr>
						<g:if test="${identityProvider.errorURL}">
						<tr>
							<th><g:message code="fedreg.label.errorurl"/></th>
							<td><a href="${identityProvider.errorURL}">${fieldValue(bean: identityProvider, field: "errorURL")}</a></td>
						</tr>
						</g:if>
						<tr>
							<th><g:message code="fedreg.label.status"/></th>
							<td>
								<g:if test="${identityProvider.active}">
									<div class="icon icon_tick"><g:message code="fedreg.label.active" /></div>
								</g:if>
								<g:else>
									<div class="icon icon_cross"><g:message code="fedreg.label.inactive" /></div>
								</g:else>
							</td>
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
			
			<div id="tabs">
				<ul>
					<li><a href="#tab-contacts" class="icon icon_user_comment"><g:message code="fedreg.label.contacts" /></a></li>
					<li><a href="#tab-crypto" class="icon icon_cog"><g:message code="fedreg.label.crypto" /></a></li>
					<li><a href="#tab-sso" class="icon icon_cog"><g:message code="fedreg.label.sso" /></a></li>
					<li><a href="#tab-attributes" class="icon icon_cog"><g:message code="fedreg.label.attributes" /></a></li>
					<li><a href="#tab-protocols" class="icon icon_cog"><g:message code="fedreg.label.protocolsupport" /></a></li>
					<li><a href="#tab-nameid" class="icon icon_cog"><g:message code="fedreg.label.nameidmapping" /></a></li>
					<li><a href="#tab-assertionid" class="icon icon_cog"><g:message code="fedreg.label.assertionidrequest" /></a></li>
					<li><a href="#tab-attrprof" class="icon icon_cog"><g:message code="fedreg.label.attributeprofiles" /></a></li>
					<g:if test="${identityProvider.extensions}">
					<li><a href="#tab-ext" class="icon icon_cog"><g:message code="fedreg.label.extensions" /></a></li>
					</g:if>
				</ul>
				
				<div id="tab-contacts" class="tabcontent">
					<table>
						<thead>
							<tr>
								<th><g:message code="fedreg.label.name" /></th>
								<th><g:message code="fedreg.label.email" /></th>
								<th><g:message code="fedreg.label.type" /></th>
								<th/>
							</tr>
						</thead>
						<tbody>
						<g:each in="${identityProvider.contacts}" var="contactPerson" status="i">
							<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
								<td>${contactPerson.contact.givenName?.encodeAsHTML()} ${contactPerson.contact.surname?.encodeAsHTML()}</td>
								<td>${contactPerson.contact.email?.uri.encodeAsHTML()}
								</td>
								<td>${contactPerson.type.encodeAsHTML()}</td>
								<td/>
							</tr>
						</g:each>
						<tr>
							<td colspan="3"><h4><g:message code="fedreg.view.members.identityprovider.show.contacts.from.entity" /></h4></td>
						</tr>
						<g:each in="${identityProvider.entityDescriptor.contacts}" var="contactPerson" status="i">
							<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
								<td>${contactPerson.contact.givenName?.encodeAsHTML()} ${contactPerson.contact.surname?.encodeAsHTML()}</td>
								<td>${contactPerson.contact.email?.uri.encodeAsHTML()}
								</td>
								<td>${contactPerson.type.encodeAsHTML()}</td>
								<td/>
							</tr>
						</g:each>
						
						</tbody>
					</table>
				</div>
				<div id="tab-crypto" class="tabcontent">
				</div>
				<div id="tab-sso" class="tabcontent">	
				</div>
				<div id="tab-attributes" class="tabcontent">
				</div>
				<div id="tab-protocols" class="tabcontent">
				</div>
				<div id="tab-nameid" class="tabcontent">
				</div>
				<div id="tab-assertionid" class="tabcontent">
				</div>
				<div id="tab-attrprof" class="tabcontent">
				</div>
				<g:if test="${identityProvider.extensions}">
				<div id="tab-ext" class="tabcontent">
				</div>
				</g:if>
			</div>
			
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
