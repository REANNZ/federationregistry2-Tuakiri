
<%@ page import="aaf.fr.foundation.Organization" %>
<html>
	<head>
		
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.organization.show.title" /></title>
		
		<r:script>
			var activeContact
			var contactCreateEndpoint = "${createLink(controller:'organizationContact', action:'create', id:organization.id )}";
			var contactDeleteEndpoint = "${createLink(controller:'organizationContact', action:'delete' )}";
			var contactListEndpoint = "${createLink(controller:'organizationContact', action:'list', id:organization.id ) }";
			var contactSearchEndpoint = "${createLink(controller:'organizationContact', action:'search')}";
		
			var organizationFullAdministratorGrantEndpoint = "${createLink(controller:'organizationAdministration', action:'grantFullAdministration', id:organization.id)}";
			var organizationFullAdministratorRevokeEndpoint = "${createLink(controller:'organizationAdministration', action:'revokeFullAdministration', id:organization.id)}";
			var organizationFullAdministratorListEndpoint = "${createLink(controller:'organizationAdministration', action:'listFullAdministration', id:organization.id)}";
			var organizationFullAdministratorSearchEndpoint = "${createLink(controller:'organizationAdministration', action:'searchFullAdministration', id:organization.id)}";
		</r:script>
		
	</head>
	<body>
			<h2><g:message code="fedreg.view.members.organization.show.heading" args="[organization.displayName]"/></h2>

      <g:if test="${!organization.functioning()}">
        <div class="alert alert-message alert-danger">
          <p><g:message code="fedreg.view.members.organization.show.notfunctioning"/></p>
        </div>
      </g:if>
			
      <ul class="tabs">
        <li class="active"><a href="#tab-overview"><g:message code="label.overview" /></a></li>
        <li><a href="#tab-contacts"><g:message code="label.contacts" /></a></li>
        <fr:hasPermission target="saml:advanced">
          <li><a href="#tab-entities"><g:message code="label.entities" /></a></li>
        </fr:hasPermission>
        <li><a href="#tab-idp"><g:message code="label.identityproviders" /></a></li>
        <li><a href="#tab-sp"><g:message code="label.serviceproviders" /></a></li>
        <li><a href="#tab-statistics"><g:message code="label.statistics" /></a></li>
        <g:if test="${organization.approved}">
          <li><a href="#tab-admins"><g:message code="label.administrators" /></a></li>
        </g:if>
        <g:if test="${organization.extensions}">
          <li><a href="#tab-ext" class="icon icon_cog"><g:message code="label.extensions" /></a></li>
        </g:if>
      </ul>
			
      <div class="tab-content">	
        <div id="tab-overview" class="tab-pane active">
          <table class="borderless">
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
              <td><a href="${organization.url}">${organization.url}</a></td>
              </tr>
              <tr>
              <th><g:message code="label.status"/></th>
              <td>
              <g:if test="${organization.active}">
              <g:message code="label.active" />
              </g:if>
              <g:else>
              <span class="more-info not-in-federation" rel="twipsy" data-original-title="${g.message(code:'label.warningmetadata')}" data-placement="right"><g:message code="label.inactive" /></span>
              </g:else>
              </td>
              </tr>

              <tr>
              <th><g:message code="label.archived"/></th>
              <td>
              <g:if test="${organization.archived}"> 
              <span class="alert-message warning"><g:message code="label.yes" /></span>
              <span class="more-info not-in-federation"  rel="twipsy" data-original-title="${g.message(code:'label.warningmetadataarchived')}" data-placement="right"><g:message code="label.no" /></span>
              </g:if>
              <g:else>
              <g:message code="label.no" /> 
              </g:else>
              </td>
              </tr>
              <tr>
              <th><g:message code="label.approved"/></th>
              <td>
              <g:if test="${organization.approved}">
              <g:message code="label.yes" />
              </g:if>
              <g:else>
              <span class="more-info not-in-federation" rel="twipsy" data-original-title="${g.message(code:'label.undergoingapproval')}" data-placement="right"><g:message code="label.no" /></span>
              </g:else>
              </td>
              </tr>
              <tr>
              <th><g:message code="label.primarytype" /></th>
              <td>${fieldValue(bean: organization, field: "primary.displayName")}</td>
              </tr>
              <g:if test="${organization.types}">
              <tr>
              <th><g:message code="label.organizationsecondarytypes" /></th>
              <td>
              <ul class="clean">
              <g:each in="${organization.types}" var="t">
              <li>${fieldValue(bean: t, field: "displayName")}</li>
              </g:each>ß
              </ul>
              </td>
              </tr>
              </g:if> 
              <tr>
              <th><g:message code="label.datecreated" /></th>
              <td>${fieldValue(bean: organization, field: "dateCreated")}</td>
              </tr>     
            </tbody>
          </table>
        </div>

				<div id="tab-statistics" class="tab-pane">
					<table>
						<thead>
							<tr>
								<th></th>
								<th><g:message code="label.total" /></th>
								<th><g:message code="label.active" /></th>
								<th><g:message code="label.inactive" /></th>
								<th><g:message code="label.approved" /></th>
								<th><g:message code="label.unapproved" /></th>
							</tr>
						</thead>
						<tbody>
							<tr class="odd">
								<th><g:message code="label.entitydescriptors" /></th>
								<td>${statistics.entityDescriptors.total.encodeAsHTML()}</td>
								<td>${statistics.entityDescriptors.activeEntityDescriptors.encodeAsHTML()}</td>
								<td>${statistics.entityDescriptors.inactiveEntityDescriptors.encodeAsHTML()}</td>
								<td>${statistics.entityDescriptors.approvedEntityDescriptors.encodeAsHTML()}</td>
								<td>${statistics.entityDescriptors.unapprovedEntityDescriptors.encodeAsHTML()}</td>
							</tr>
							<tr>
								<th><g:message code="label.identityproviders" /></th>
								<td>${statistics.idpSSODescriptors.total.encodeAsHTML()}</td>
								<td>${statistics.idpSSODescriptors.activeIDPSSODescriptors.encodeAsHTML()}</td>
								<td>${statistics.idpSSODescriptors.inactiveIDPSSODescriptors.encodeAsHTML()}</td>
								<td>${statistics.idpSSODescriptors.approvedIDPSSODescriptors.encodeAsHTML()}</td>
								<td>${statistics.idpSSODescriptors.unapprovedIDPSSODescriptors.encodeAsHTML()}</td>
							</tr>
							<tr>
								<th><g:message code="label.serviceproviders" /></th>
								<td>${statistics.spSSODescriptors.total.encodeAsHTML()}</td>
								<td>${statistics.spSSODescriptors.activeSPSSODescriptors.encodeAsHTML()}</td>
								<td>${statistics.spSSODescriptors.inactiveSPSSODescriptors.encodeAsHTML()}</td>
								<td>${statistics.spSSODescriptors.approvedSPSSODescriptors.encodeAsHTML()}</td>
								<td>${statistics.spSSODescriptors.unapprovedSPSSODescriptors.encodeAsHTML()}</td>
							</tr>
						</tbody>
					</table>					
				</div>
				
				<div id="tab-contacts" class="tab-pane">
					<g:render template="/templates/contacts/list" plugin="foundation" model="[host:organization]" />
          
					<g:render template="/templates/contacts/create" plugin="foundation" model="[host:organization, contactTypes:contactTypes]"/>
				</div>

        <fr:hasPermission target="saml:advanced">
        <div id="tab-entities" class="tab-pane">
          <g:if test="${entities}">
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
                      <div><g:message code="label.active" /></div>
                    </g:if>
                    <g:else>
                      <div><g:message code="label.inactive" /></div>
                    </g:else>
                  </td>
                  <td><n:button href="${createLink(controller:'entityDescriptor', action:'show', id:ent.id)}" label="label.view" class="view-button" /></td>
                </tr>
              </g:each>
              </tbody>
            </table>
          </g:if>
          <g:else>
            <p><g:message code="fedreg.view.members.organization.no.entities" /></p>
          </g:else>
        </div>
        </fr:hasPermission>

				<div id="tab-idp" class="tab-pane">
					<g:if test="${identityproviders}">
						<table>
							<thead>
								<tr>
									<th><g:message code="label.name" /></th>
									<th><g:message code="label.entitydescriptor" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
								<g:each in="${identityproviders}" var="idp" status ="i">
									<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
										<td>${idp.displayName.encodeAsHTML()}</td>
										<td>${idp.entityDescriptor.entityID.encodeAsHTML()}</td>
										<td><n:button href="${createLink(controller:'IDPSSODescriptor', action:'show', id:idp.id)}" label="label.view" class="view-button" /></td>
									</tr>
								</g:each>
							</tbody>
						</table>
					</g:if>
					<g:else>
						<p><g:message code="fedreg.view.members.organization.no.identityproviders" /></p>
					</g:else>
				</div>

				<div id="tab-sp" class="tab-pane">
					<g:if test="${serviceproviders}">
						<table>
							<thead>
								<tr>
									<th><g:message code="label.name" /></th>
									<th><g:message code="label.entitydescriptor" /></th>
									<th/>
								</tr>
							</thead>
							<tbody>
								<g:each in="${serviceproviders}" var="sp">
									<tr>
										<td>${(sp.displayName?:"N/A").encodeAsHTML()}</td>
										<td>${sp.entityDescriptor.entityID.encodeAsHTML()}</td>
										<td><n:button href="${createLink(controller:'SPSSODescriptor', action:'show', id:sp.id)}" label="label.view" class="view-button" /></td>
									</tr>
								</g:each>
							</tbody>
						</table>
					</g:if>
					<g:else>
						<p><g:message code="fedreg.view.members.organization.no.serviceproviders" /></p>
					</g:else>
				</div>

        <g:if test="${organization.approved}">
        <div id="tab-admins" class="tab-pane">
          <g:render template="/templates/organization/listfulladministration" plugin="foundation" model="[organization:organization, administrators:administrators]" />
          <fr:hasPermission target="organization:${organization.id}:manage:administrators">
            <g:render template="/templates/organization/searchfulladministration" plugin="foundation" model="[organization:organization, administrators:administrators]" />
          </fr:hasPermission>
        </div>
        </g:if>
        <g:if test="${organization.extensions}">
          <div id="tab-ext" class="tab-pane">  
            ${fieldValue(bean: organization, field: "extensions")}
          </div>
        </g:if>

    </div>
	</body>
</html>
