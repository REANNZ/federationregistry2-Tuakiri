
<%@ page import="fedreg.core.Organization" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.organization.show.title" /></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.members.organization.show.heading" args="[organization.displayName]"/></h2>

			<table>
				<tbody>		
					<tr>
						<th><g:message code="label.name"/></th>
						<td>${fieldValue(bean: organization, field: "name")}</td>
					</tr>
					<tr>
						<th><g:message code="label.displayname" /></th>
						<td>${fieldValue(bean: organization, field: "displayName")}</td>
					</tr>
					<tr>
						<th><g:message code="label.lang" /></th>
						<td>${fieldValue(bean: organization, field: "lang")}</td>
					</tr>
					<tr>
						<th><g:message code="label.url" /></th>
						<td><a href="${organization.url.uri}">${organization.url.uri}</a></td>
					</tr>
					<tr>
						<th><g:message code="label.primarytype" /></th>
						<td>${fieldValue(bean: organization, field: "primary.displayName")}</td>
					</tr>
					<g:if test="${organization.types}">
					<tr>
						<th><g:message code="label.secondarytypes" /></th>
						<td valign="top"  class="value">
							<ul>
							<g:each in="${organization.types}" var="t">
								<li>${fieldValue(bean: t, field: "displayName")}</li>
							</g:each>
							</ul>
						</td>
					</tr>
					</g:if>				
				</tbody>
			</table>
		
			<div id="tabs">
				<ul>
					<li><a href="#tab-contacts"><g:message code="label.contacts" /></a></li>
					<li><a href="#tab-entities"><g:message code="label.entities" /></a></li>
					<li><a href="#tab-idp"><g:message code="label.identityproviders" /></a></li>
					<li><a href="#tab-sp"><g:message code="label.serviceproviders" /></a></li>
					<g:if test="${organization.extensions}">
						<li><a href="#tab-ext" class="icon icon_cog"><g:message code="label.extensions" /></a></li>
					</g:if>
				</ul>
				
				<div id="tab-contacts">
					<table>
						<thead>
							<tr>
								<th><g:message code="label.name" /></th>
								<th><g:message code="label.email" /></th>
								<th/>
							</tr>
						</thead>
						<tbody>
						<g:each in="${contacts}" var="contact" status="i">
							<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
								<td>${contact.givenName?.encodeAsHTML()} ${contact.surname?.encodeAsHTML()}</td>
								<td><a href="mailto:${contact.email?.uri.encodeAsHTML()}">${contact.email?.uri.encodeAsHTML()}</a></td>
								<td></td>
							</tr>
						</g:each>
						</tbody>
					</table>
				</div>
				<div id="tab-entities">
					<table>
						<thead>
							<tr>
								<th><g:message code="label.entitydescriptor" /></th>
								<th><g:message code="label.status" /></th>
								<th/>
							</tr>
						</thead>
						<tbody>
						<g:each in="${entities}" var="ent" status="i">
							<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
								<td>${ent.entityID.encodeAsHTML()}</td>
								<td>
									<g:if test="${ent.active}">
										<div class="icon icon_tick"><g:message code="label.active" /></div>
									</g:if>
									<g:else>
										<div class="icon icon_cross"><g:message code="label.inactive" /></div>
									</g:else>
								</td>
								<td><n:button href="${createLink(controller:'entity', action:'show', id:ent.id)}" label="label.view" icon="arrowthick-1-ne" /></td>
							</tr>
						</g:each>
						</tbody>
					</table>
				</div>
				<div id="tab-idp">
					<table>
						<thead>
							<tr>
								<th><g:message code="label.name" /></th>
								<th><g:message code="label.entitydescriptor" /></th>
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
										<td><n:button href="${createLink(controller:'IDPSSODescriptor', action:'show', id:idp.id)}" label="label.view" icon="arrowthick-1-ne" /></td>
									</tr>
								</g:each>
							</g:if>
						</g:each>
						</tbody>
					</table>
				</div>
				<div id="tab-sp">
					<table>
						<thead>
							<tr>
								<th><g:message code="label.name" /></th>
								<th><g:message code="label.entitydescriptor" /></th>
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
										<td><n:button href="${createLink(controller:'SPSSODescriptor', action:'show', id:sp.id)}" label="label.view" icon="arrowthick-1-ne" /></td>
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
			
		</section>
	</body>
</html>
