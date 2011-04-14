
<html>
	<head>		
		<meta name="layout" content="members" />

		<title><g:message code="fedreg.view.members.identityprovider.show.title" /></title>
		
		<r:script>
			var activeContact
			var contactCreateEndpoint = "${createLink(controller:'descriptorContact', action:'create', id:identityProvider.id )}";
			var contactDeleteEndpoint = "${createLink(controller:'descriptorContact', action:'delete' )}";
			var contactListEndpoint = "${createLink(controller:'descriptorContact', action:'list', id:identityProvider.id ) }";
			var contactSearchEndpoint = "${createLink(controller:'descriptorContact', action:'search')}";
			
			var certificateListEndpoint = "${createLink(controller:'roleDescriptorCrypto', action:'list', id:identityProvider.id )}";
			var certificateCreationEndpoint = "${createLink(controller:'roleDescriptorCrypto', action:'create', id:identityProvider.id)}";
			var certificateDeleteEndpoint = "${createLink(controller:'roleDescriptorCrypto', action:'delete')}";
			var certificateValidationEndpoint = "${createLink(controller:'coreUtilities', action:'validateCertificate')}";
			
			var endpointDeleteEndpoint = "${createLink(controller:'descriptorEndpoint', action:'delete')}";
			var endpointListEndpoint = "${createLink(controller:'descriptorEndpoint', action:'list', id:identityProvider.id)}";
			var endpointCreationEndpoint = "${createLink(controller:'descriptorEndpoint', action:'create', id:identityProvider.id)}";
			var endpointToggleStateEndpoint = "${createLink(controller:'descriptorEndpoint', action:'toggle')}";
			var endpointMakeDefaultEndpoint = "${createLink(controller:'descriptorEndpoint', action:'makeDefault')}";
			var endpointEditEndpoint = "${createLink(controller:'descriptorEndpoint', action:'edit')}";
			var endpointUpdateEndpoint = "${createLink(controller:'descriptorEndpoint', action:'update')}";
			
			var nameIDFormatRemoveEndpoint = "${createLink(controller:'descriptorNameIDFormat', action:'remove', id:identityProvider.id )}";
			var nameIDFormatListEndpoint = "${createLink(controller:'descriptorNameIDFormat', action:'list', id:identityProvider.id )}";
			var nameIDFormatAddEndpoint = "${createLink(controller:'descriptorNameIDFormat', action:'add', id:identityProvider.id )}";
			
			var attributeRemoveEndpoint = "${createLink(controller:'descriptorAttribute', action:'remove', id:identityProvider.id )}";
			var attributeListEndpoint = "${createLink(controller:'descriptorAttribute', action:'list', id:identityProvider.id )}";
			var attributeAddEndpoint = "${createLink(controller:'descriptorAttribute', action:'add', id:identityProvider.id )}";
			
			var descriptorFullAdministratorGrantEndpoint = "${createLink(controller:'descriptorAdministration', action:'grantFullAdministration', id:identityProvider.id)}";
			var descriptorFullAdministratorRevokeEndpoint = "${createLink(controller:'descriptorAdministration', action:'revokeFullAdministration', id:identityProvider.id)}";
			var descriptorFullAdministratorListEndpoint = "${createLink(controller:'descriptorAdministration', action:'listFullAdministration', id:identityProvider.id)}";
			var descriptorFullAdministratorSearchEndpoint = "${createLink(controller:'descriptorAdministration', action:'searchFullAdministration', id:identityProvider.id)}";
			
			var monitorDeleteEndpoint = "${createLink(controller:'roleDescriptorMonitor', action:'delete')}";
			var monitorListEndpoint = "${createLink(controller:'roleDescriptorMonitor', action:'list', id:identityProvider.id )}";
			var monitorCreateEndpoint = "${createLink(controller:'roleDescriptorMonitor', action:'create', id:identityProvider.id )}";
			
			var attributeFilterEndpoint = "${createLink(controller:'attributeFilter', action:'generate', id:identityProvider.id )}";
			
			var descriptorMetadataEndpoint = "${createLink(controller:'metadata', action:'entity', id:identityProvider.entityDescriptor.id )}";
			
			var idpReportsConnectivityEndpoint = "${createLink(controller:'idPReports', action:'connectivityjson', id:identityProvider.id)}"
			
			$(function() {
				$("#attrfilpolood").hide();
				fedreg.descriptor_metadata();
			});	
		</r:script>
	</head>
	<body>
		<section>
			
		<h2><g:message code="fedreg.view.members.identityprovider.show.heading" args="[identityProvider.displayName]"/></h2>
		<table>
			<tbody>		
				<tr>
					<th><g:message code="label.displayname"/></th>
					<td>${fieldValue(bean: identityProvider, field: "displayName")}</td>
				</tr>
				<tr>
					<th><g:message code="label.description"/></th>
					<td>${fieldValue(bean: identityProvider, field: "description")}</td>
				</tr>
				<tr>
					<th><g:message code="label.scope"/></th>
					<td>${fieldValue(bean: identityProvider, field: "scope")}</td>
				</tr>
				<tr>
					<th><g:message code="label.organization"/></th>
					<td><g:link controller="organization" action="show" id="${identityProvider.organization.id}">${fieldValue(bean: identityProvider, field: "organization.displayName")}</g:link></td>
				</tr>
				<n:hasPermission target="saml:advanced">
					<tr>
						<th><g:message code="label.entitydescriptor"/></th>
						<td><g:link controller="entityDescriptor" action="show" id="${identityProvider.entityDescriptor.id}">${fieldValue(bean: identityProvider, field: "entityDescriptor.entityID")}</g:link></td>
					</tr>
				</n:hasPermission>
				<n:lacksPermission target="saml:advanced">
					<tr>
						<th><g:message code="label.entitydescriptor"/></th>
						<td>${fieldValue(bean: identityProvider, field: "entityDescriptor.entityID")}</td>
					</tr>
				</n:lacksPermission>
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
					<td><a href="${identityProvider.errorURL}">${fieldValue(bean: identityProvider, field: "errorURL")}</a></td>
				</tr>
				</g:if>
				<tr>
					<th><g:message code="label.status"/></th>
					<td>
						<g:if test="${identityProvider.active}">
							<g:message code="label.active" />
						</g:if>
						<g:else>
							<g:message code="label.inactive" /> <div class="error"><g:message code="label.warningmetadata" /></div>
						</g:else>
					</td>
				</tr>
				<tr>
					<th><g:message code="label.approved"/></th>
					<td>
						<g:if test="${identityProvider.approved}">
							<g:message code="label.yes" />
						</g:if>
						<g:else>
							<g:message code="label.no" /> <div class="warning"><g:message code="label.undergoingapproval" /></div>
						</g:else>
					</td>
				</tr>
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
				<tr>
					<th><g:message code="label.datecreated" /></th>
					<td>${fieldValue(bean: identityProvider, field: "dateCreated")}</td>
				</tr>
			</tbody>
		</table>
			
			<div class="tabs">
				<ul>
					<li><a href="#tab-contacts" ><g:message code="label.contacts" /></a></li>
					<li><a href="#tab-crypto" ><g:message code="label.crypto" /></a></li>
					<li><a href="#tab-endpoints" ><g:message code="label.endpoints" /></a></li>
					<li><a href="#tab-attributes" ><g:message code="label.supportedattributes" /></a></li>
					<li><a href="#tab-nameidformats" ><g:message code="label.supportednameidformats" /></a></li>
					<li><a href="#tab-reports" ><g:message code="label.reporting" /></a></li>
					<li><a href="#tab-monitors" ><g:message code="label.monitoring" /></a></li>
					<li><a href="#tab-metadata" ><g:message code="label.metadata" /></a></li>
					<li><a href="#tab-attrfilpol" ><g:message code="label.attributefilter" /></a></li>
					<g:if test="${identityProvider.approved}">
						<li><a href="#tab-admins" ><g:message code="label.administrators" /></a></li>
					</g:if>
				</ul>
				
				<div id="tab-contacts" class="tabcontent">
					<h3><g:message code="label.contacts" /></h3>
					<div id="contacts">
						<g:render template="/templates/contacts/list" plugin="federationcore" model="[descriptor:identityProvider]" />
					</div>
					
					<g:render template="/templates/contacts/create" plugin="federationcore" model="[descriptor:identityProvider, contactTypes:contactTypes]"/>
				</div>
				<div id="tab-crypto" class="tabcontent">
					<h3><g:message code="label.publishedcertificates" plugin="federationcore" /></h3>
					<div id="certificates">
						<g:render template="/templates/certificates/list" plugin="federationcore" model="[descriptor:identityProvider]" />
					</div>
					<g:render template="/templates/certificates/create" plugin="federationcore" model="[descriptor:identityProvider]"/>
				</div>
				<div id="tab-endpoints" class="tabcontent">
					<h3><g:message code="label.supportedendpoints" /></h3>
					<div class="tabs">
						<ul>
							<li><a href="#tab-sso" ><g:message code="label.ssoservices" /></a></li>
							<li><a href="#tab-ars" ><g:message code="label.artifactresolutionservices" /></a></li>
							<g:if test="${identityProvider.collaborator}">
								<li><a href="#tab-attrs" ><g:message code="label.attributeservices" /></a></li>
							</g:if>
							<li><a href="#tab-slo" ><g:message code="label.sloservices" /></a></li>
						</ul>
						
						<div id="tab-sso" class="componentlist">
							<div id="ssoendpoints">
								<g:render template="/templates/endpoints/list" plugin="federationcore" model="[endpoints:identityProvider.singleSignOnServices, endpointType:'singleSignOnServices', containerID:'ssoendpoints', minEndpoints:1]" />
							</div>
							
							<g:render template="/templates/endpoints/create" plugin="federationcore" model="[descriptor:identityProvider, endpointType:'singleSignOnServices', containerID:'ssoendpoints']" />
							
						</div>
						<div id="tab-ars" class="componentlist">
							<div id="artifactendpoints">
								<g:render template="/templates/endpoints/list" plugin="federationcore" model="[endpoints:identityProvider.artifactResolutionServices, endpointType:'artifactResolutionServices', containerID:'artifactendpoints']" />
							</div>
							
							<g:render template="/templates/endpoints/create" plugin="federationcore" model="[descriptor:identityProvider, endpointType:'artifactResolutionServices', containerID:'artifactendpoints', indexed:true]" />
						</div>
						<div id="tab-slo" class="componentlist">
							<div id="singlelogoutendpoints">
								<g:render template="/templates/endpoints/list" plugin="federationcore" model="[endpoints:identityProvider.singleLogoutServices, endpointType:'singleLogoutServices', containerID:'singlelogoutendpoints']" />
							</div>
							
							<g:render template="/templates/endpoints/create" plugin="federationcore" model="[descriptor:identityProvider, endpointType:'singleLogoutServices', containerID:'singlelogoutendpoints']" />
						</div>
						<g:if test="${identityProvider.collaborator}">
							<div id="tab-attrs" class="componentlist">
								<div id="attributeserviceendpoints">
									<g:render template="/templates/endpoints/list" plugin="federationcore" model="[endpoints:identityProvider.collaborator.attributeServices, endpointType:'attributeServices', containerID:'attributeserviceendpoints']" />
								</div>
								
								<g:render template="/templates/endpoints/create" plugin="federationcore" model="[descriptor:identityProvider, endpointType:'attributeServices', containerID:'attributeserviceendpoints']" />
							</div>
						</g:if>
					</div>
				</div>
				<div id="tab-attributes" class="tabcontent">
					<h3><g:message code="label.supportedattributes" /></h3>
					<div id="attributes">
						<g:render template="/templates/attributes/list" plugin="federationcore" model="[descriptor:identityProvider, attrs:identityProvider.sortedAttributes(), containerID:'attributes']" />
					</div>
					<g:render template="/templates/attributes/add" plugin="federationcore" model="[descriptor:identityProvider, type:'idp', containerID:'attributes']"/>
				</div>
				<div id="tab-nameidformats" class="tabcontent">
					<h3><g:message code="label.supportednameidformats" /></h3>
					<div id="nameidformats">
						<g:render template="/templates/nameidformats/list" plugin="federationcore" model="[descriptor:identityProvider, nameIDFormats:identityProvider.nameIDFormats, containerID:'nameidformats']" />
					</div>
					
					<g:render template="/templates/nameidformats/add" plugin="federationcore" model="[descriptor:identityProvider, containerID:'nameidformats']"/>
				</div>
				<div id="tab-reports" class="tabcontent">
					<div id="reporting">
						<g:render template="/templates/reporting/idp/reports" plugin="federationreporting" model="[id:identityProvider.id]" />
					</div>
				</div>
				<div id="tab-attrfilpol">
					<h3><g:message code="label.attributefilter"/></h3>
					
					<p id="attrfilpolood" class="error" style="margin: 6px;"><g:message code="fedreg.view.members.identityprovider.show.attributefilter.outofdate" /> <a href="#" onClick="fedreg.attributefilter_refresh(); return false;"><strong><g:message code="label.refreshnow" />.</strong></a></p>
					
					<p><g:message code="fedreg.view.members.identityprovider.show.attributefilter.description" /></p>
					<p><g:message code="fedreg.view.members.identityprovider.show.attributefilter.configuration" /></p>
					<p><g:message code="fedreg.view.members.identityprovider.show.attributefilter.configurationurl" /> <strong><g:link controller="attributeFilter" action="generate" id="${identityProvider.id}">${createLink(controller:'attributeFilter', action:'generate', id:identityProvider.id, absolute:true)}</g:link></strong></p>
					
					<pre class="metadata"><code>${attributeFilter.encodeAsHTML()}</code></pre>

				</div>
				<div id="tab-metadata">
					<h3><g:message code="label.metadata" /></h3>
					<p><g:message code="fedreg.view.members.identityprovider.show.metadata.details" />
					<a href="#" onClick="fedreg.descriptor_metadata(); nimble.growl('success', '${g.message(code:'label.metadatareload')}'); false;" style="float:right;" class="redo-button"><g:message code="label.reload" /></a>
					</p>
					
					<div id="descriptormetadata"></div>
				</div>
				<g:if test="${identityProvider.approved}">
					<div id="tab-admins">
						<g:render template="/templates/descriptor/listfulladministration" plugin="federationcore" model="[descriptor:identityProvider, administrators:administrators]" />
						<n:hasPermission target="descriptor:${identityProvider.id}:manage:administrators">
							<g:render template="/templates/descriptor/searchfulladministration" plugin="federationcore" model="[descriptor:identityProvider]" />
						</n:hasPermission>
					</div>
				</g:if>
				<div id="tab-monitors">
					<div id="monitors">
						<g:render template="/templates/monitor/list" plugin="federationcore" model="[roleDescriptor:identityProvider]" />
					</div>
					<n:hasPermission target="descriptor:${identityProvider.id}:manage:monitors">
						<g:render template="/templates/monitor/create" plugin="federationcore" model="[descriptor:identityProvider]" />
					</n:hasPermission>
				</div>
			</div>
			
		</section>
		
	</body>
</html>
