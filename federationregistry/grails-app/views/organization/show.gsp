
<%@ page import="fedreg.core.Organization" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.organization.show.title" /></title>
	</head>
	<body>
		<div class="breadcrumb">
			<g:link controller="organization" action="show" id="${organization.id}">${organization.displayName.encodeAsHTML()}</g:link>
		</div>
		
			<h2><g:message code="fedreg.view.members.organization.show.heading" args="[organization.displayName]"/></h2>

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
							<td><g:link controller="organizationType" action="show" id="${organization?.primary?.id}">${organization?.primary?.encodeAsHTML()}</g:link></td>
						</tr>
						<g:if test="${organization.types}">
						<tr>
							<th><g:message code="fedreg.label.secondarytypes" /></th>
							<td valign="top" style="text-align: left;" class="value">
								<ul>
								<g:each in="${organization.types}" var="t">
									<li><g:link controller="organizationType" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></li>
								</g:each>
								</ul>
							</td>
						</tr>
						</g:if>
						<g:if test="${organization.extensions}">
							<tr>
								<th><g:message code="fedreg.label.extensions" /></th>
								<td>${fieldValue(bean: organization, field: "extensions")}</td>
							</tr>
						</g:if>					
					</tbody>
				</table>
				
				<div id="organizationentities">
					<h3><g:message code="fedreg.view.members.organization.show.entities.heading" /></h3>
					<g:each in="${entities}" var="ent">
					
						<div class="entity">
							<h4>${ent.entityID}</h4>

							<g:if test="${ent.idpDescriptors}">
								<table>
									<tbody>
										<tr><td colspan="2"><h5><g:message code="fedreg.label.identityproviders" /></h5></td></tr>
									<g:each in="${ent.idpDescriptors}" var="idp">
										<tr>
											<td>${idp.displayName.encodeAsHTML()} </td>
											<td><g:link controller="identityProvider" action="show" id="${idp.id}" class="button icon icon_view"><g:message code="fedreg.link.view" /></g:link></td>
										</tr>
									</g:each>
									</tbody>
								</table>
							</g:if>

							<g:if test="${ent.spDescriptors}">
								<table>
									<tbody>
										<tr><td colspan="2"><h5><g:message code="fedreg.label.serviceproviders" /></h5></td></tr>
									<g:each in="${ent.spDescriptors}" var="sp">
										<tr>
											<td>${sp.displayName.encodeAsHTML()}</td>
											<td><g:link controller="serviceProvider" action="show" id="${sp.id}" class="button icon icon_view"><g:message code="fedreg.link.view" /></g:link></td>
										</tr>
									</g:each>
									</tbody>
								</table>
							</g:if>

							<g:if test="${ent.attributeAuthorityDescriptors}">
								<table>
									<tbody>
										<tr><td colspan="2"><h5><g:message code="fedreg.label.attributeauthority" /></h5></td></tr>
									<g:each in="${ent.idpDescriptors}" var="aa">
										<tr>
											<td>AA</td>
											<td><g:link controller="attributeAuthority" action="show" id="${aa.id}" class="button icon icon_view"><g:message code="fedreg.link.view" /></g:link></td>
										</tr>
									</g:each>
									</tbody>
								</table>
							</g:if>

						</div>

					</g:each>
				</div>
				
			</div>

	</body>
</html>
