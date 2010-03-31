
<%@ page import="fedreg.core.Organization" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.organization.show.title" /></title>
		
		<script type="text/javascript">
			$(function() {
				$("#tabs").tabs();
			});
		</script>
	</head>
	<body>
			<h2><g:message code="fedreg.view.members.organization.show.heading" args="[organization.displayName]"/></h2>
		<div id="organization">
			<div class="details">
				<table class="datatable">
					<tbody>		
						<tr>
							<th><g:message code="fedreg.label.name"/></th>
							<td>${fieldValue(bean: organization, field: "name")}</td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.displayname" /></th>
							<td>${fieldValue(bean: organization, field: "displayName")}</td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.lang" /></th>
							<td>${fieldValue(bean: organization, field: "lang")}</td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.url" /></th>
							<td><a href="${organization.url.uri}">${organization.url.uri}</a></td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.primarytype" /></th>
							<td>${organization?.primary?.encodeAsHTML()}</td>
						</tr>
						<g:if test="${organization.types}">
						<tr>
							<th><g:message code="fedreg.label.secondarytypes" /></th>
							<td valign="top" style="text-align: left;" class="value">
								<ul>
								<g:each in="${organization.types}" var="t">
									<li>${t?.encodeAsHTML()}</li>
								</g:each>
								</ul>
							</td>
						</tr>
						</g:if>				
					</tbody>
				</table>
			
				<div id="tabs">
					<ul>
						<li><a href="#tab-members" class="icon icon_user_comment"><g:message code="fedreg.label.members" /></a></li>
						<li><a href="#tab-entities" class="icon icon_cog"><g:message code="fedreg.label.entities" /></a></li>
						<li><a href="#tab-idp" class="icon icon_cog"><g:message code="fedreg.label.identityproviders" /></a></li>
						<li><a href="#tab-aa" class="icon icon_cog"><g:message code="fedreg.label.attributeauthorities" /></a></li>
						<li><a href="#tab-sp" class="icon icon_cog"><g:message code="fedreg.label.serviceproviders" /></a></li>
						<g:if test="${organization.extensions}">
						<li><a href="#tab-ext" class="icon icon_cog"><g:message code="fedreg.label.extensions" /></a></li>
						</g:if>
					</ul>
					
					<div id="tab-members" class="tabcontent">
						<table>
							<thead>
								<tr>
									<th><g:message code="fedreg.label.name" /></th>
									<th><g:message code="fedreg.label.email" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
							<g:each in="${contacts}" var="contact" status="i">
								<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
									<td>${contact.givenName?.encodeAsHTML()} ${contact.surname?.encodeAsHTML()}</td>
									<td>${contact.email?.uri.encodeAsHTML()}</td>
									<td></td>
								</tr>
							</g:each>
							</tbody>
						</table>
					</div>
					<div id="tab-entities" class="tabcontent">
						<table>
							<thead>
								<tr>
									<th><g:message code="fedreg.label.entitydescriptor" /></th>
									<th><g:message code="fedreg.label.status" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
							<g:each in="${entities}" var="ent" status="i">
								<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
									<td>${ent.entityID.encodeAsHTML()}</td>
									<td>
										<g:if test="${ent.active}">
											<div class="icon icon_tick"><g:message code="fedreg.label.active" /></div>
										</g:if>
										<g:else>
											<div class="icon icon_cross"><g:message code="fedreg.label.inactive" /></div>
										</g:else>
									</td>
									<td><g:link controller="entity" action="show" id="${ent.id}" class="button icon icon_view"><g:message code="fedreg.link.view" /></g:link></td>
								</tr>
							</g:each>
							</tbody>
						</table>
					</div>
					<div id="tab-idp" class="tabcontent">
						<table class="datatable">
							<thead>
								<tr>
									<th><g:message code="fedreg.label.name" /></th>
									<th><g:message code="fedreg.label.entitydescriptor" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
							<g:each in="${entities}" var="ent">
								<g:if test="${ent.idpDescriptors}">
									<g:each in="${ent.idpDescriptors}" var="idp" status="i">
										<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
											<td>${idp.displayName.encodeAsHTML()}</td>
											<td>${ent.entityID.encodeAsHTML()}</td>
											<td><g:link controller="identityProvider" action="show" id="${idp.id}" class="button icon icon_view"><g:message code="fedreg.link.view" /></g:link></td>
										</tr>
									</g:each>
								</g:if>
							</g:each>
							</tbody>
						</table>
					</div>
					<div id="tab-aa" class="tabcontent">
						<table class="datatable">
							<thead>
								<tr>
									<th><g:message code="fedreg.label.name" /></th>
									<th><g:message code="fedreg.label.entitydescriptor" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
							<g:each in="${entities}" var="ent">
								<g:if test="${ent.attributeAuthorityDescriptors}">
									<g:each in="${ent.attributeAuthorityDescriptors}" var="aa">
										<tr>
											<td>${(aa.displayName?:"N/A").encodeAsHTML()}</td>
											<td>${ent.entityID.encodeAsHTML()}</td>
											<td><g:link controller="attributeAuthority" action="show" id="${aa.id}" class="button icon icon_view"><g:message code="fedreg.link.view" /></g:link></td>
										</tr>
									</g:each>
								</g:if>
							</g:each>
							</tbody>
						</table>
					</div>
					<div id="tab-sp" class="tabcontent">
						<table class="datatable">
							<thead>
								<tr>
									<th><g:message code="fedreg.label.name" /></th>
									<th><g:message code="fedreg.label.entitydescriptor" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
							<g:each in="${entities}" var="ent">
								<g:if test="${ent.spDescriptors}">
									<g:each in="${ent.spDescriptors}" var="sp">
										<tr>
											<td>${(sp.displayName?:"N/A").encodeAsHTML()}</td>
											<td>${ent.entityID.encodeAsHTML()}</td>
											<td><g:link controller="serviceProvider" action="show" id="${sp.id}" class="button icon icon_view"><g:message code="fedreg.link.view" /></g:link></td>
										</tr>
									</g:each>
								</g:if>
							</g:each>
							</tbody>
						</table>
					</div>
					<g:if test="${organization.extensions}">
					<div id="tab-ext">	
						${fieldValue(bean: organization, field: "extensions")}
					</div>
					</g:if>
				</div>
			</div>
		</div>
	</body>
</html>
