
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
					<table id="crypto">
						<tbody>
						<g:each in="${identityProvider.keyDescriptors}" status="i" var="kd">
							<tr><td colspan="2"><h4>Certificate ${i+1}</h4></td></tr>
							<tr>
								<th><g:message code="fedreg.label.keytype" /></th>
								<td>${kd.keyType.encodeAsHTML()}</td>
							</tr>
							<tr>
								<th><g:message code="fedreg.label.created" /></th>
								<td>${kd.dateCreated.encodeAsHTML()}</td>
							</tr>
							<tr>
								<th><g:message code="fedreg.label.lastupdated" /></th>
								<td>${kd.lastUpdated.encodeAsHTML()}</td>
							</tr>
							<tr>
								<th><g:message code="fedreg.label.name" /></th>
								<td>${(kd.keyInfo.keyName?:"N/A").encodeAsHTML()}</td>
							</tr>
							<tr>
								<th><g:message code="fedreg.label.issuer" /></th>
								<td>${kd.keyInfo.certificate.issuer.encodeAsHTML()}</td>
							</tr>
							<tr>
								<th><g:message code="fedreg.label.subject" /></th>
								<td>${kd.keyInfo.certificate.subject.encodeAsHTML()}</td>
							</tr>
							<tr>
								<th><g:message code="fedreg.label.expirydate" /></th>
								<td>
									${kd.keyInfo.certificate.expiryDate.encodeAsHTML()}
									<g:if test="${kd.keyInfo.certificate.criticalAlert()}">
										<div class="critical">
											<p class="icon icon_exclamation"><g:message code="fedreg.label.certificatecritical"/></p>
										</div>
									</g:if>
									<g:else>
										<g:if test="${kd.keyInfo.certificate.warnAlert()}">
											<div class="warning">
												<p class="icon icon_error"><g:message code="fedreg.label.certificatewarning"/></p>
											</div>
										</g:if>
										<g:else>
											<g:if test="${kd.keyInfo.certificate.infoAlert()}">
												<div class="information">
													<p class="icon icon_information"><g:message code="fedreg.label.certificateinfo"/></p>
												</div>
											</g:if>
										</g:else>
									</g:else>
								</td>
							</tr>
							<tr>
								<th><g:message code="fedreg.label.selfsigned" /></th>
								<td>
									<g:if test="${kd.keyInfo.certificate.subject.equals(kd.keyInfo.certificate.issuer)}">
										<div class="icon icon_tick"><g:message code="fedreg.label.yes" /></div>
									</g:if>
									<g:else>
										<div class="icon icon_cross"><g:message code="fedreg.label.no" /></div>
									</g:else>
								</td>
							</tr>
							<tr>
								<th><g:message code="fedreg.label.certificatedata" /></th>
								<td>${(kd.keyInfo.certificate.data).encodeAsHTML()}</td>
							</tr>
						</g:each>
						</tbody>
					</table>
				</div>
				<div id="tab-endpoints" class="tabcontent">
					<div class="endpoints">
						<h3><g:message code="fedreg.label.ssoservices" /></h3>
						<table id="ssoservices">
							<tbody>
							<g:each in="${identityProvider.singleSignOnServices}" status="i" var="sso">
								<tr><td colspan="2"><h4><g:message code="fedreg.label.ssoservice" /> ${i+1}</h4></td></tr>
								<tr>
									<th><g:message code="fedreg.label.binding" /></th>
									<td>${sso.binding.uri.encodeAsHTML()}</td>
								</tr>
								<tr>
									<th><g:message code="fedreg.label.location" /></th>
									<td>${sso.location.uri.encodeAsHTML()}</td>
								</tr>
								<tr>
									<th><g:message code="fedreg.label.responselocation" /></th>
									<td>${(sso.responseLocation?.uri ?:sso.location.uri).encodeAsHTML()}</td>
								</tr>
								<tr>
									<th><g:message code="fedreg.label.status" /></th>
									<td>
									<g:if test="${sso.active}">
										<div class="icon icon_tick"><g:message code="fedreg.label.active" /></div>
									</g:if>
									<g:else>
										<div class="icon icon_cross"><g:message code="fedreg.label.inactive" /></div>
									</g:else>
									<td>
								</tr>
							</g:each>
							</tbody>
						</table>
					</div>
					<div class="endpoints">
						<h3><g:message code="fedreg.label.artifactresolutionservices" /></h3>
						<table id="artresservices">
							<tbody>
							<g:each in="${identityProvider.artifactResolutionServices}" status="i" var="art">
								<tr><td colspan="2"><h4><g:message code="fedreg.label.artifactresolutionservice" /> ${i+1}</h4></td></tr>
								<tr>
									<th><g:message code="fedreg.label.binding" /></th>
									<td>${art.binding.uri.encodeAsHTML()}</td>
								</tr>
								<tr>
									<th><g:message code="fedreg.label.index" /></th>
									<td>${art.endpointIndex.encodeAsHTML()}</td>
								</tr>
								<tr>
									<th><g:message code="fedreg.label.location" /></th>
									<td>${art.location.uri.encodeAsHTML()}</td>
								</tr>
								<tr>
									<th><g:message code="fedreg.label.defaultendpoint" /></th>
									<td>
									<g:if test="${art.isDefault}">
										<div class="icon icon_tick"><g:message code="fedreg.label.yes" /></div>
									</g:if>
									<g:else>
										<div class="icon icon_cross"><g:message code="fedreg.label.no" /></div>
									</g:else>	
									</td>
								</tr>
								<tr>
									<th><g:message code="fedreg.label.status" /></th>
									<td>
									<g:if test="${art.active}">
										<div class="icon icon_tick"><g:message code="fedreg.label.active" /></div>
									</g:if>
									<g:else>
										<div class="icon icon_cross"><g:message code="fedreg.label.inactive" /></div>
									</g:else>
									<td>
								</tr>
							</g:each>
							</tbody>
						</table>
					</div>
					<div  class="endpoints">
						<h3><g:message code="fedreg.label.sloservices" /></h3>
						<table id="sloservices">
							<tbody>
							<g:each in="${identityProvider.singleLogoutServices}" status="i" var="slo">
								<tr><td colspan="2"><h4><g:message code="fedreg.label.sloservice" /> ${i+1}</h4></td></tr>
								<tr>
									<th><g:message code="fedreg.label.binding" /></th>
									<td>${slo.binding.uri.encodeAsHTML()}</td>
								</tr>
								<tr>
									<th><g:message code="fedreg.label.location" /></th>
									<td>${slo.location.uri.encodeAsHTML()}</td>
								</tr>
								<tr>
									<th><g:message code="fedreg.label.status" /></th>
									<td>
									<g:if test="${slo.active}">
										<div class="icon icon_tick"><g:message code="fedreg.label.active" /></div>
									</g:if>
									<g:else>
										<div class="icon icon_cross"><g:message code="fedreg.label.inactive" /></div>
									</g:else>
									<td>
								</tr>
							</g:each>
							</tbody>
						</table>
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
							<td>${nidf.uri.encodeAsHTML()}</td>
							<td>${nidf.description?.encodeAsHTML()}</td>
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
