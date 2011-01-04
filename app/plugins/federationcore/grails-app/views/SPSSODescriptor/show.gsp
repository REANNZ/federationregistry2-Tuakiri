
<html>
	<head>
		<meta name="layout" content="members" />

		<title><g:message code="fedreg.view.members.serviceprovider.show.title" /></title>
		
		<script type="text/javascript">
			var activeContact
			var contactCreateEndpoint = "${createLink(controller:'descriptorContact', action:'create', id:serviceProvider.id )}";
			var contactDeleteEndpoint = "${createLink(controller:'descriptorContact', action:'delete' )}";
			var contactListEndpoint = "${createLink(controller:'descriptorContact', action:'list', id:serviceProvider.id ) }";
			var contactSearchEndpoint = "${createLink(controller:'descriptorContact', action:'search')}";
			
			var acsAddAttr = "${createLink(controller:'attributeConsumingService', action:'addRequestedAttribute')}";
			var acsListAttr = "${createLink(controller:'attributeConsumingService', action:'listRequestedAttributes')}";
			var acsRemoveAttr = "${createLink(controller:'attributeConsumingService', action:'removeRequestedAttribute')}";
			var acsUpdateAttr = "${createLink(controller:'attributeConsumingService', action:'updateRequestedAttribute')}";
			var acsAddSpecAttrVal = "${createLink(controller:'attributeConsumingService', action:'addSpecifiedAttributeValue')}";
			var acsRemoveSpecAttrVal = "${createLink(controller:'attributeConsumingService', action:'removeSpecifiedAttributeValue')}";
			var acsListSpecAttrVal = "${createLink(controller:'attributeConsumingService', action:'listSpecifiedAttributeValue')}";
			var acsListSpecAttrsVal = "${createLink(controller:'attributeConsumingService', action:'listSpecifiedAttributes')}";
			
			var certificateListEndpoint = "${createLink(controller:'roleDescriptorCrypto', action:'list', id:serviceProvider.id )}";
			var certificateCreationEndpoint = "${createLink(controller:'roleDescriptorCrypto', action:'create', id:serviceProvider.id)}";
			var certificateDeleteEndpoint = "${createLink(controller:'roleDescriptorCrypto', action:'delete')}";
			var certificateValidationEndpoint = "${createLink(controller:'coreUtilities', action:'validateCertificate')}";
			
			var endpointDeleteEndpoint = "${createLink(controller:'descriptorEndpoint', action:'delete')}";
			var endpointListEndpoint = "${createLink(controller:'descriptorEndpoint', action:'list', id:serviceProvider.id)}";
			var endpointCreationEndpoint = "${createLink(controller:'descriptorEndpoint', action:'create', id:serviceProvider.id)}";
			var endpointToggleStateEndpoint = "${createLink(controller:'descriptorEndpoint', action:'toggle')}";
			var endpointMakeDefaultEndpoint = "${createLink(controller:'descriptorEndpoint', action:'makeDefault')}";
			var endpointEditEndpoint = "${createLink(controller:'descriptorEndpoint', action:'edit')}";
			var endpointUpdateEndpoint = "${createLink(controller:'descriptorEndpoint', action:'update')}";
			
			var nameIDFormatRemoveEndpoint = "${createLink(controller:'descriptorNameIDFormat', action:'remove', id:serviceProvider.id )}";
			var nameIDFormatListEndpoint = "${createLink(controller:'descriptorNameIDFormat', action:'list', id:serviceProvider.id )}";
			var nameIDFormatAddEndpoint = "${createLink(controller:'descriptorNameIDFormat', action:'add', id:serviceProvider.id )}";
			
			var serviceCategoryListEndpoint = "${createLink(controller:'SPSSODescriptor', action:'listCategories', id:serviceProvider.id )}";
			var serviceCategoryAddEndpoint = "${createLink(controller:'SPSSODescriptor', action:'addCategory', id:serviceProvider.id )}";
			var serviceCategoryRemoveEndpoint = "${createLink(controller:'SPSSODescriptor', action:'removeCategory', id:serviceProvider.id )}";
			
			var attributeRemoveEndpoint = "${createLink(controller:'descriptorAttribute', action:'remove', id:serviceProvider.id )}";
			var attributeListEndpoint = "${createLink(controller:'descriptorAttribute', action:'list', id:serviceProvider.id )}";
			var attributeAddEndpoint = "${createLink(controller:'descriptorAttribute', action:'add', id:serviceProvider.id )}";
			
			var descriptorFullAdministratorGrantEndpoint = "${createLink(controller:'descriptorAdministration', action:'grantFullAdministration', id:serviceProvider.id)}";
			var descriptorFullAdministratorRevokeEndpoint = "${createLink(controller:'descriptorAdministration', action:'revokeFullAdministration', id:serviceProvider.id)}";
			var descriptorFullAdministratorListEndpoint = "${createLink(controller:'descriptorAdministration', action:'listFullAdministration', id:serviceProvider.id)}";
			var descriptorFullAdministratorSearchEndpoint = "${createLink(controller:'descriptorAdministration', action:'searchFullAdministration', id:serviceProvider.id)}";
			
			var monitorDeleteEndpoint = "${createLink(controller:'roleDescriptorMonitor', action:'delete')}";
			var monitorListEndpoint = "${createLink(controller:'roleDescriptorMonitor', action:'list', id:serviceProvider.id )}";
			var monitorCreateEndpoint = "${createLink(controller:'roleDescriptorMonitor', action:'create', id:serviceProvider.id )}";
			
			$(function() {
				$("#tabs").tabs();
				$("#tabs2").tabs();
			});
		</script>
	</head>
	<body>
		<section>
			
		<h2><g:message code="fedreg.view.members.serviceprovider.show.heading" args="[serviceProvider.displayName]"/></h2>
		<table>
			<tbody>		
				<tr>
					<th><g:message code="label.displayname"/></th>
					<td>${fieldValue(bean: serviceProvider, field: "displayName")}</td>
				</tr>
				<tr>
					<th><g:message code="label.organization"/></th>
					<td><g:link controller="organization" action="show" id="${serviceProvider.organization.id}">${fieldValue(bean: serviceProvider, field: "organization.displayName")}</g:link></td>
				</tr>
				<n:hasPermission target="saml:advanced">
					<tr>
						<th><g:message code="label.entitydescriptor"/></th>
						<td><g:link controller="entityDescriptor" action="show" id="${serviceProvider.entityDescriptor.id}">${fieldValue(bean: serviceProvider, field: "entityDescriptor.entityID")}</g:link></td>
					</tr>
				</n:hasPermission>
				<n:lacksPermission target="saml:advanced">
					<tr>
						<th><g:message code="label.entitydescriptor"/></th>
						<td>${fieldValue(bean: serviceProvider, field: "entityDescriptor.entityID")}</td>
					</tr>
				</n:lacksPermission>
				<tr>
					<th><g:message code="label.protocolsupport"/></th>
					<td>
						<g:each in="${serviceProvider.protocolSupportEnumerations}" status="i" var="pse">
						${pse.uri} <br/>
						</g:each>
					</td>
				<g:if test="${serviceProvider.errorURL}">
				<tr>
					<th><g:message code="label.errorurl"/></th>
					<td><a href="${serviceProvider.errorURL}">${fieldValue(bean: serviceProvider, field: "errorURL")}</a></td>
				</tr>
				</g:if>
				<tr>
					<th><g:message code="label.status"/></th>
					<td>
						<g:if test="${serviceProvider.active}">
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
						<g:if test="${serviceProvider.approved}">
							<g:message code="label.yes" />
						</g:if>
						<g:else>
							<g:message code="label.no" /> <div class="warning"><g:message code="label.undergoingapproval" /></div>
						</g:else>
					</td>
				</tr>
				<tr>
					<th><g:message code="label.datecreated" /></th>
					<td>${fieldValue(bean: serviceProvider, field: "dateCreated")}</td>
				</tr>
			</tbody>
		</table>
			
			<div id="tabs">
				<ul>
					<li><a href="#tab-details"><g:message code="label.details" /></a></li>
					<li><a href="#tab-categories"><g:message code="label.categories" /></a></li>
					<li><a href="#tab-contacts"><g:message code="label.contacts" /></a></li>
					<li><a href="#tab-crypto"><g:message code="label.crypto" /></a></li>
					<li><a href="#tab-endpoints"><g:message code="label.endpoints" /></a></li>
					<li><a href="#tab-attributes"><g:message code="label.attributeconsumingservices" /></a></li>
					<li><a href="#tab-nameidformats"><g:message code="label.supportednameidformats" /></a></li>
					<g:if test="${serviceProvider.approved}">
						<li><a href="#tab-admins" class="icon icon_database_key"><g:message code="label.administrators" /></a></li>
					</g:if>
					<li><a href="#tab-monitors" class="icon icon_database_key"><g:message code="label.monitoring" /></a></li>
				</ul>
				
				<div id="tab-details" class="tabcontent">
					<h3><g:message code="label.details" /></h3>
					<div id="details">
						<g:render template="/templates/spssodescriptor/details" plugin="federationcore" model="[serviceProvider:serviceProvider]" />
					</div>
				</div>
				<div id="tab-categories" class="tabcontent">
					<h3><g:message code="label.categories" /></h3>
					<div id="categories">
						<g:render template="/templates/servicecategories/list" plugin="federationcore" model="[descriptor:serviceProvider, categories:serviceProvider.serviceCategories, containerID:'categories']" />
					</div>
					
					<g:render template="/templates/servicecategories/add" plugin="federationcore" model="[descriptor:serviceProvider, containerID:'categories']"/>
				</div>
				<div id="tab-contacts" class="tabcontent">
					<h3><g:message code="label.contacts" /></h3>
					<div id="contacts">
						<g:render template="/templates/contacts/list" plugin="federationcore" model="[descriptor:serviceProvider, allowremove:true]" />
					</div>
					
					<g:render template="/templates/contacts/create" plugin="federationcore" model="[descriptor:serviceProvider, contactTypes:contactTypes]"/>
				</div>
				<div id="tab-crypto" class="tabcontent">
					<h3><g:message code="label.publishedcertificates" plugin="federationcore" /></h3>
					<div id="certificates">
						<g:render template="/templates/certificates/list" plugin="federationcore" model="[descriptor:serviceProvider, allowremove:true]" />
					</div>
					
					<g:render template="/templates/certificates/create" plugin="federationcore" model="[descriptor:serviceProvider]"/>
				</div>
				<div id="tab-endpoints" class="tabcontent">
					<h3><g:message code="label.supportedendpoints" /></h3>
					<div id="tabs2">
						<ul>
							<li><a href="#tab-acs"><g:message code="label.assertionconsumerservice" /></a></li>
							<li><a href="#tab-ars"><g:message code="label.artifactresolutionservices" /></a></li>
							<li><a href="#tab-slo"><g:message code="label.sloservices" /></a></li>
							<li><a href="#tab-drs"><g:message code="label.drsservices" /></a></li>
							<li><a href="#tab-nim"><g:message code="label.nimservices" /></a></li>
						</ul>
						<div id="tab-acs" class="componentlist">
							<div id="assertionconsumerendpoints">
								<g:render template="/templates/endpoints/list" plugin="federationcore" model="[endpoints:serviceProvider.assertionConsumerServices, allowremove:true, endpointType:'assertionConsumerServices', containerID:'assertionconsumerendpoints']" />
							</div>
							
							<g:render template="/templates/endpoints/create" plugin="federationcore" model="[descriptor:serviceProvider, endpointType:'assertionConsumerServices', containerID:'assertionconsumerendpoints']" />
						</div>
						<div id="tab-ars" class="componentlist">
							<div id="artifactendpoints">
								<g:render template="/templates/endpoints/list" plugin="federationcore" model="[endpoints:serviceProvider.artifactResolutionServices, allowremove:true, endpointType:'artifactResolutionServices', containerID:'artifactendpoints']" />
							</div>
							
							<g:render template="/templates/endpoints/create" plugin="federationcore" model="[descriptor:serviceProvider, endpointType:'artifactResolutionServices', containerID:'artifactendpoints']" />
						</div>
						<div id="tab-slo" class="componentlist">
							<div id="singlelogoutendpoints">
								<g:render template="/templates/endpoints/list" plugin="federationcore" model="[endpoints:serviceProvider.singleLogoutServices, allowremove:true, endpointType:'singleLogoutServices', containerID:'singlelogoutendpoints']" />
							</div>
							
							<g:render template="/templates/endpoints/create" plugin="federationcore" model="[descriptor:serviceProvider, endpointType:'singleLogoutServices', containerID:'singlelogoutendpoints']" />
						</div>
						<div id="tab-drs" class="componentlist">
							<div id="discoveryresponseservices">
								<g:render template="/templates/endpoints/list" plugin="federationcore" model="[endpoints:serviceProvider.discoveryResponseServices, allowremove:true, endpointType:'discoveryResponseServices', containerID:'discoveryresponseservices']" />
							</div>
							
							<g:render template="/templates/endpoints/create" plugin="federationcore" model="[descriptor:serviceProvider, endpointType:'discoveryResponseServices', containerID:'discoveryresponseservices']" />
						</div>
						<div id="tab-nim" class="componentlist">
							<div id="managenameidservices">
								<g:render template="/templates/endpoints/list" plugin="federationcore" model="[endpoints:serviceProvider.manageNameIDServices, allowremove:true, endpointType:'manageNameIDServices', containerID:'managenameidservices']" />
							</div>
							
							<g:render template="/templates/endpoints/create" plugin="federationcore" model="[descriptor:serviceProvider, endpointType:'manageNameIDServices', containerID:'managenameidservices']" />
						</div>
					</div>
				</div>
				<div id="tab-attributes" class="tabcontent">
					<h3><g:message code="label.attributeconsumingservices" /></h3>
					<g:render template="/templates/acs/list" plugin="federationcore" model="[attributeConsumingServices:serviceProvider.attributeConsumingServices, allowremove:true]"/>
				</div>
				<div id="tab-nameidformats" class="tabcontent">
					<h3><g:message code="label.supportednameidformats" /></h3>
					<div id="nameidformats">
						<g:render template="/templates/nameidformats/list" plugin="federationcore" model="[descriptor:serviceProvider, nameIDFormats:serviceProvider.nameIDFormats, containerID:'nameidformats']" />
					</div>
					
					<g:render template="/templates/nameidformats/add" plugin="federationcore" model="[descriptor:serviceProvider, containerID:'nameidformats']"/>
				</div>
				<g:if test="${serviceProvider.approved}">
					<div id="tab-admins">
						<g:render template="/templates/descriptor/listfulladministration" plugin="federationcore" model="[descriptor:serviceProvider, administrators:administrators]" />
						<n:hasPermission target="descriptor:${serviceProvider.id}:manage:administrators">
							<g:render template="/templates/descriptor/searchfulladministration" plugin="federationcore" model="[descriptor:serviceProvider]" />
						</n:hasPermission>
					</div>
				</g:if>
				<div id="tab-monitors">
					<div id="monitors">
						<g:render template="/templates/monitor/list" plugin="federationcore" model="[roleDescriptor:serviceProvider]" />
					</div>
					<n:hasPermission target="descriptor:${serviceProvider.id}:manage:monitors">
						<g:render template="/templates/monitor/create" plugin="federationcore" model="[descriptor:serviceProvider]" />
					</n:hasPermission>
				</div>
			</div>
			
		</section>
		
	</body>
</html>
