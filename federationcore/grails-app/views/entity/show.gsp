
<%@ page import="fedreg.core.Organization" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.entity.show.title" /></title>
		
		<script type="text/javascript">
			$(function() {
				$("#tabs").tabs();
			});
		</script>
	</head>
	<body>
		<h2><g:message code="fedreg.view.members.entity.show.heading" args="[entity.entityID]"/></h2>
		<div id="entity">
			<div class="details">
				<table class="datatable">
					<tbody>		
						<tr>
							<th><g:message code="fedreg.label.entitydescriptor"/></th>
							<td>${fieldValue(bean: entity, field: "entityID")}</td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.status"/></th>
							<td>
								<g:if test="${entity.active}">
									<div class="icon icon_tick"><g:message code="fedreg.label.active" /></div>
								</g:if>
								<g:else>
									<div class="icon icon_cross"><g:message code="fedreg.label.inactive" /></div>
								</g:else>
							</td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.organization"/></th>
							<td>${fieldValue(bean: entity, field: "organization.displayName")}</td>
						</tr>			
					</tbody>
				</table>
			
				<div id="tabs">
					<ul>
						<li><a href="#tab-contacts" class="icon icon_user_comment"><g:message code="fedreg.label.contacts" /></a></li>
						<li><a href="#tab-idp" class="icon icon_cog"><g:message code="fedreg.label.identityproviders" /></a></li>
						<li><a href="#tab-aa" class="icon icon_cog"><g:message code="fedreg.label.attributeauthorities" /></a></li>
						<li><a href="#tab-sp" class="icon icon_cog"><g:message code="fedreg.label.serviceproviders" /></a></li>
						<g:if test="${entity.extensions}">
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
							<g:each in="${entity.contacts}" var="contactPerson" status="i">
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
					<div id="tab-idp" class="tabcontent">
						<table class="datatable">
							<thead>
								<tr>
									<th><g:message code="fedreg.label.name" /></th>
									<th><g:message code="fedreg.label.status" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
								<g:if test="${entity.idpDescriptors}">
									<g:each in="${entity.idpDescriptors}" var="idp" status="i">
										<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
											<td>${idp.displayName.encodeAsHTML()}</td>
											<td>
												<g:if test="${idp.active}">
													<div class="icon icon_tick"><g:message code="fedreg.label.active" /></div>
												</g:if>
												<g:else>
													<div class="icon icon_cross"><g:message code="fedreg.label.inactive" /></div>
												</g:else>
											</td>
											<td><g:link controller="identityProvider" action="show" id="${idp.id}" class="button icon icon_view"><g:message code="fedreg.link.view" /></g:link></td>
										</tr>
									</g:each>
								</g:if>
							</tbody>
						</table>
					</div>
					<div id="tab-aa" class="tabcontent">
						<table class="datatable">
							<thead>
								<tr>
									<th><g:message code="fedreg.label.name" /></th>
									<th><g:message code="fedreg.label.status" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
								<g:if test="${entity.attributeAuthorityDescriptors}">
									<g:each in="${entity.attributeAuthorityDescriptors}" var="aa">
										<tr>
											<td>${(aa.displayName?:"N/A").encodeAsHTML()}</td>
											<td>
												<g:if test="${aa.active}">
													<div class="icon icon_tick"><g:message code="fedreg.label.active" /></div>
												</g:if>
												<g:else>
													<div class="icon icon_cross"><g:message code="fedreg.label.inactive" /></div>
												</g:else>
											</td>
											<td><g:link controller="attributeAuthority" action="show" id="${aa.id}" class="button icon icon_view"><g:message code="fedreg.link.view" /></g:link></td>
										</tr>
									</g:each>
								</g:if>
							</tbody>
						</table>
					</div>
					<div id="tab-sp" class="tabcontent">
						<table class="datatable">
							<thead>
								<tr>
									<th><g:message code="fedreg.label.name" /></th>
									<th><g:message code="fedreg.label.status" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
								<g:if test="${entity.spDescriptors}">
									<g:each in="${entity.spDescriptors}" var="sp">
										<tr>
											<td>${(sp.displayName?:"N/A").encodeAsHTML()}</td>
											<td>
												<g:if test="${sp.active}">
													<div class="icon icon_tick"><g:message code="fedreg.label.active" /></div>
												</g:if>
												<g:else>
													<div class="icon icon_cross"><g:message code="fedreg.label.inactive" /></div>
												</g:else>
											</td>
											<td><g:link controller="serviceProvider" action="show" id="${sp.id}" class="button icon icon_view"><g:message code="fedreg.link.view" /></g:link></td>
										</tr>
									</g:each>
								</g:if>
							</tbody>
						</table>
					</div>
					<g:if test="${entity.extensions}">
					<div id="tab-ext">	
						${fieldValue(bean: entity, field: "extensions")}
					</div>
					</g:if>
				</div>
			</div>
		</div>
	</body>
</html>
