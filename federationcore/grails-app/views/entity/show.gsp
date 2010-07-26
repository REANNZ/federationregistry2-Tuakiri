
<%@ page import="fedreg.core.Organization" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.entity.show.title" /></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.members.entity.show.heading" args="[entity.entityID]"/></h2>

			<table>
				<tbody>		
					<tr>
						<th><g:message code="label.entitydescriptor"/></th>
						<td>${fieldValue(bean: entity, field: "entityID")}</td>
					</tr>
					<tr>
						<th><g:message code="label.status"/></th>
						<td>
							<g:if test="${entity.active}">
								<div class="icon icon_tick"><g:message code="label.active" /></div>
							</g:if>
							<g:else>
								<div class="icon icon_cross"><g:message code="label.inactive" /></div>
							</g:else>
						</td>
					</tr>
					<tr>
						<th><g:message code="label.organization"/></th>
						<td>${fieldValue(bean: entity, field: "organization.displayName")}</td>
					</tr>			
				</tbody>
			</table>
		
			<div id="tabs">
				<ul>
					<li><a href="#tab-contacts" class="icon icon_user_comment"><g:message code="label.contacts" /></a></li>
					<li><a href="#tab-idp" class="icon icon_cog"><g:message code="label.identityproviders" /></a></li>
					<li><a href="#tab-sp" class="icon icon_cog"><g:message code="label.serviceproviders" /></a></li>
					<g:if test="${entity.extensions}">
					<li><a href="#tab-ext" class="icon icon_cog"><g:message code="label.extensions" /></a></li>
					</g:if>
				</ul>
				
				<div id="tab-contacts" class="tabcontent">
					<table>
						<thead>
							<tr>
								<th><g:message code="label.name" /></th>
								<th><g:message code="label.email" /></th>
								<th><g:message code="label.type" /></th>
							</tr>
						</thead>
						<tbody>
						<g:each in="${entity.contacts}" var="contactPerson" status="i">
							<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
								<td>${contactPerson.contact.givenName?.encodeAsHTML()} ${contactPerson.contact.surname?.encodeAsHTML()}</td>
								<td><a href="mailto:${contactPerson.contact.email?.uri.encodeAsHTML()}">${contactPerson.contact.email?.uri.encodeAsHTML()}</a></td>
								<td>${contactPerson.type.displayName.encodeAsHTML()}</td>
							</tr>
						</g:each>
						</tbody>
					</table>
				</div>
				<div id="tab-idp" class="tabcontent">
					<g:if test="${entity.idpDescriptors}">
						<table>
							<thead>
								<tr>
									<th><g:message code="label.name" /></th>
									<th><g:message code="label.description" /></th>
									<th><g:message code="label.status" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
									<g:each in="${entity.idpDescriptors}" var="idp" status="i">
										<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
											<td>${idp.displayName.encodeAsHTML()}</td>
											<td>${idp.description.encodeAsHTML()}</td>
											<td>
												<g:if test="${idp.active}">
													<div class="icon icon_tick"><g:message code="label.active" /></div>
												</g:if>
												<g:else>
													<div class="icon icon_cross"><g:message code="label.inactive" /></div>
												</g:else>
											</td>
											<td><n:button href="${createLink(controller:'IDPSSODescriptor', action:'show', id:idp.id)}" label="label.view" icon="arrowthick-1-ne"/></td>
										</tr>
									</g:each>
							</tbody>
						</table>
					</g:if>
					<g:else>
						<p class="info"><g:message code="fedreg.view.members.entity.show.no.identityproviders" />
					</g:else>
				</div>
				<div id="tab-sp" class="tabcontent">
					<g:if test="${entity.spDescriptors}">
						<table>
							<thead>
								<tr>
									<th><g:message code="label.name" /></th>
									<th><g:message code="label.description" /></th>
									<th><g:message code="label.status" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
								<g:each in="${entity.spDescriptors}" var="sp">
									<tr>
										<td>${sp.displayName.encodeAsHTML()}</td>
										<td>${sp.description.encodeAsHTML()}</td>
										<td>
											<g:if test="${sp.active}">
												<div class="icon icon_tick"><g:message code="label.active" /></div>
											</g:if>
											<g:else>
												<div class="icon icon_cross"><g:message code="label.inactive" /></div>
											</g:else>
										</td>
										<td><n:button href="${createLink(controller:'SPSSODescriptor', action:'show', id:sp.id)}" label="label.view" icon="arrowthick-1-ne"/></td>
									</tr>
								</g:each>
							</tbody>
						</table>
					</g:if>
					<g:else>
						<p class="info"><g:message code="fedreg.view.members.entity.show.no.serviceproviders" />
					</g:else>
				</div>
				<g:if test="${entity.extensions}">
				<div id="tab-ext">	
					${fieldValue(bean: entity, field: "extensions")}
				</div>
				</g:if>
			</div>

		</section>
	</body>
</html>
