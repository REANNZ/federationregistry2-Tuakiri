
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />

		<title><g:message code="fedreg.view.members.identityprovider.show.title" /></title>
		
		<script type="text/javascript">
			var activeContact
			var linkContactEndpoint = "${createLink(controller:'contacts', action:'linkDescriptorContact', id:identityProvider.id )}";
			var unlinkContactEndpoint = "${createLink(controller:'contacts', action:'unlinkDescriptorContact' )}";
			var listContactsEndpoint = "${createLink(controller:'contacts', action:'listDescriptorContacts', id:identityProvider.id ) }";
			var contactSearchEndpoint = "${createLink(controller:'contacts', action:'searchContacts')}";
			var certificateListEndpoint = "${createLink(controller:'keyDescriptor', action:'listCertificates', id:identityProvider.id )}";
			var certificateCreationEndpoint = "${createLink(controller:'keyDescriptor', action:'createCertificate', id:identityProvider.id)}";
			var certificateDeleteEndpoint = "${createLink(controller:'keyDescriptor', action:'delete')}";
			var certificateValidationEndpoint = "${createLink(controller:'keyDescriptor', action:'validateCertificate')}";
			var endpointDeleteEndpoint = "${createLink(controller:'endpoint', action:'delete')}";
			var endpointListEndpoint = "${createLink(controller:'endpoint', action:'listEndpoints', id:identityProvider.id)}";
			
			$(function() {
				$("#tabs").tabs();
				$("#tabs2").tabs();
			});
		</script>
	</head>
	<body>
		<h2><g:message code="fedreg.view.members.identityprovider.show.heading" args="[identityProvider.displayName]"/></h2>
		<div id="identityprovider">
			<div class="details">
				<table class="datatable buttons">
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
							<th><g:message code="fedreg.label.organization"/></th>
							<td><g:link controller="organization" action="show" id="${identityProvider.organization.id}" class="icon icon_arrow_branch">${fieldValue(bean: identityProvider, field: "organization")}</g:link></td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.entitydescriptor"/></th>
							<td><g:link controller="entity" action="show" id="${identityProvider.entityDescriptor.id}" class="icon icon_arrow_branch">${fieldValue(bean: identityProvider, field: "entityDescriptor")}</g:link></td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.protocolsupport"/></th>
							<td>
								<g:each in="${identityProvider.protocolSupportEnumerations}" status="i" var="pse">
								${pse.uri} <br/>
								</g:each>
							</td>
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
					<li><a href="#tab-endpoints" class="icon icon_cog"><g:message code="fedreg.label.endpoints" /></a></li>
					<li><a href="#tab-attributes" class="icon icon_cog"><g:message code="fedreg.label.supportedattributes" /></a></li>
					<li><a href="#tab-nameidformats" class="icon icon_cog"><g:message code="fedreg.label.supportednameidformats" /></a></li>
					<li><a href="#tab-ext" class="icon icon_cog"><g:message code="fedreg.label.extensions" /></a></li>
				</ul>
				
				<div id="tab-contacts" class="tabcontent">
					<div id="contacts">
						<g:render template="/templates/contacts/contactlist" model="[descriptor:identityProvider, allowremove:true]" />
					</div>
					<hr>
					<g:render template="/templates/contacts/contactmanagement" model="[descriptor:identityProvider, contactTypes:contactTypes]"/>
				</div>
				<div id="tab-crypto" class="tabcontent">
					<div id="certificates">
						<g:render template="/templates/certificates/certificatelist" model="[descriptor:identityProvider, allowremove:true]" />
					</div>
					<hr>
					<g:render template="/templates/certificates/certificatemanagement" model="[descriptor:identityProvider]"/>
				</div>
				<div id="tab-endpoints" class="tabcontent">
					
					<div id="tabs2">
						<ul>
							<li><a href="#tab-sso" class="icon icon_cog"><g:message code="fedreg.label.ssoservices" /></a></li>
							<li><a href="#tab-ars" class="icon icon_cog"><g:message code="fedreg.label.artifactresolutionservices" /></a></li>
							<li><a href="#tab-slo" class="icon icon_cog"><g:message code="fedreg.label.sloservices" /></a></li>
						</ul>
						
						<div id="tab-sso" class="componentlist">
							<div id="ssoendpoints">
								<g:render template="/templates/endpoints/endpointlist" model="[endpoints:identityProvider.singleSignOnServices, allowremove:true, endpointType:'singleSignOnServices', containerID:'ssoendpoints']" />
							</div>
							<g:render template="/templates/endpoints/endpointmanagement" />
						</div>
						<div id="tab-ars" class="componentlist">
							<div id="artifactendpoints">
								<g:render template="/templates/endpoints/endpointlist" model="[endpoints:identityProvider.artifactResolutionServices, allowremove:true, endpointType:'artifactResolutionServices', containerID:'artifactendpoints']" />
								<hr/>
							</div>
						</div>
						<div id="tab-slo" class="componentlist">
							<div id="singlelogoutendpoints">
								<g:render template="/templates/endpoints/endpointlist" model="[endpoints:identityProvider.singleLogoutServices, allowremove:true, endpointType:'singleLogoutServices', containerID:'singlelogoutendpoints']"]" />
								<hr/>
							</div>
						</div>
					</div>
				</div>
				<div id="tab-attributes" class="tabcontent">
					<div class="categorydetail">
						<table class="cleantable">
							<thead>
								<tr>
									<th><g:message code="fedreg.label.attribute" /></th>
									<th><g:message code="fedreg.label.description" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
							<g:each in="${identityProvider.attributes}" status="i" var="attr">
								<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
									<td>${attr.friendlyName.encodeAsHTML()}</td>
									<td> ${attr.description.encodeAsHTML()}</td>
								</tr>
							</g:each>
							</tbody>
						</table>	
					</div>
				</div>
				<div id="tab-nameidformats" class="tabcontent">
					<table class="cleantable">
						<thead>
							<tr>
								<th><g:message code="fedreg.label.supportedformat" /></th>
								<th><g:message code="fedreg.label.description" /></th>
								<th/>
							</tr>
						</thead>
						<tbody>
						<g:each in="${identityProvider.nameIDFormats}" status="i" var="nidf">
							<tr>
								<td>${nidf.uri.encodeAsHTML()}</td>
								<td>${nidf.description?.encodeAsHTML()}</td>
							</tr>
						</g:each>
						</tbody>
					</table>
				</div>
				<div id="tab-ext" class="tabcontent">
					${identityProvider.extensions?.encodeAsHTML()}
				</div>
			</div>
			
		</div>
		<br/>
	</body>
</html>
