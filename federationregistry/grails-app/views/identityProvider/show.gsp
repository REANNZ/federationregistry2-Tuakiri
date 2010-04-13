
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />

		<title><g:message code="fedreg.view.members.identityprovider.show.title" /></title>
		
		<script type="text/javascript">
			var activeContact
			var contactCreateEndpoint = "${createLink(controller:'descriptorContact', action:'create', id:identityProvider.id )}";
			var contactDeleteEndpoint = "${createLink(controller:'descriptorContact', action:'delete' )}";
			var contactListEndpoint = "${createLink(controller:'descriptorContact', action:'list', id:identityProvider.id ) }";
			var contactSearchEndpoint = "${createLink(controller:'descriptorContact', action:'search')}";
			
			var certificateListEndpoint = "${createLink(controller:'descriptorKeyDescriptor', action:'list', id:identityProvider.id )}";
			var certificateCreationEndpoint = "${createLink(controller:'descriptorKeyDescriptor', action:'create', id:identityProvider.id)}";
			var certificateDeleteEndpoint = "${createLink(controller:'descriptorKeyDescriptor', action:'delete')}";
			var certificateValidationEndpoint = "${createLink(controller:'descriptorKeyDescriptor', action:'validateCertificate')}";
			
			var endpointDeleteEndpoint = "${createLink(controller:'descriptorEndpoint', action:'delete')}";
			var endpointListEndpoint = "${createLink(controller:'descriptorEndpoint', action:'list', id:identityProvider.id)}";
			var endpointCreationEndpoint = "${createLink(controller:'descriptorEndpoint', action:'create', id:identityProvider.id)}";
			var endpointToggleStateEndpoint = "${createLink(controller:'descriptorEndpoint', action:'toggle')}";
			
			var nameIDFormatRemoveEndpoint = "${createLink(controller:'descriptorNameIDFormat', action:'remove', id:identityProvider.id )}";
			var nameIDFormatListEndpoint = "${createLink(controller:'descriptorNameIDFormat', action:'list', id:identityProvider.id )}";
			var nameIDFormatAddEndpoint = "${createLink(controller:'descriptorNameIDFormat', action:'add', id:identityProvider.id )}";
			
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
				</ul>
				
				<div id="tab-contacts" class="tabcontent">
					<div id="contacts">
						<g:render template="/templates/contacts/list" model="[descriptor:identityProvider, allowremove:true]" />
					</div>
					<hr>
					<g:render template="/templates/contacts/create" model="[descriptor:identityProvider, contactTypes:contactTypes]"/>
				</div>
				<div id="tab-crypto" class="tabcontent">
					<div id="certificates">
						<g:render template="/templates/certificates/list" model="[descriptor:identityProvider, allowremove:true]" />
					</div>
					<hr>
					<g:render template="/templates/certificates/create" model="[descriptor:identityProvider]"/>
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
								<g:render template="/templates/endpoints/list" model="[endpoints:identityProvider.singleSignOnServices, allowremove:true, endpointType:'singleSignOnServices', containerID:'ssoendpoints']" />
							</div>
							<hr>
							<g:render template="/templates/endpoints/create" model="[endpointType:'singleSignOnServices', containerID:'ssoendpoints']" />
							
						</div>
						<div id="tab-ars" class="componentlist">
							<div id="artifactendpoints">
								<g:render template="/templates/endpoints/list" model="[endpoints:identityProvider.artifactResolutionServices, allowremove:true, endpointType:'artifactResolutionServices', containerID:'artifactendpoints']" />
							</div>
							<hr>
							<g:render template="/templates/endpoints/create" model="[endpointType:'artifactResolutionServices', containerID:'artifactendpoints']" />
						</div>
						<div id="tab-slo" class="componentlist">
							<div id="singlelogoutendpoints">
								<g:render template="/templates/endpoints/list" model="[endpoints:identityProvider.singleLogoutServices, allowremove:true, endpointType:'singleLogoutServices', containerID:'singlelogoutendpoints']" />
							</div>
							<hr>
							<g:render template="/templates/endpoints/create" model="[endpointType:'singleLogoutServices', containerID:'singlelogoutendpoints']" />
						</div>
					</div>
				</div>
				<div id="tab-attributes" class="tabcontent">
					<div id="attributes">
						<g:render template="/templates/attributes/list" model="[attrs:identityProvider.attributes, containerID:'attributes']" />
					</div>
				</div>
				<div id="tab-nameidformats" class="tabcontent">
					<div id="nameidformats">
						<g:render template="/templates/nameidformats/list" model="[nameIDFormats:identityProvider.nameIDFormats, containerID:'nameidformats']" />
					</div>
					<hr>
					<g:render template="/templates/nameidformats/add" model="[containerID:'nameidformats']"/>
				</div>
			</div>
			
		</div>
		<br/>
	</body>
</html>
