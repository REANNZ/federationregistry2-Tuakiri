
<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message code="views.fr.foundation.organization.show.title" /></title>
  </head>
  <body>
    <h2><g:message code="views.fr.foundation.organization.show.heading" args="[organization.displayName]"/></h2>

    <g:if test="${!organization.functioning()}">
      <p class="alert alert-message alert-danger">
        <g:message code="views.fr.foundation.organization.show.notfunctioning"/>
      </p>
    </g:if>

    <g:render template="/templates/flash" plugin="foundation"/>
    
    <ul class="nav nav-tabs">
      <li class="active"><a href="#tab-overview" data-toggle="tab"><g:message code="label.overview" /></a></li>
      <li><a href="#tab-contacts" data-toggle="tab"><g:message code="label.contacts" /></a></li>
      <fr:hasPermission target="federation:management:saml:advanced">
        <li><a href="#tab-entities" data-toggle="tab"><g:message code="label.entities" /></a></li>
      </fr:hasPermission>
      <li><a href="#tab-idp" data-toggle="tab"><g:message code="label.identityproviders" /></a></li>
      <li><a href="#tab-sp" data-toggle="tab"><g:message code="label.serviceproviders" /></a></li>
      <li><a href="#tab-registrations" data-toggle="tab"><g:message code="label.registrations" /></a></li>
      <g:if test="${organization.approved}">
        <li><a href="#tab-admins" data-toggle="tab"><g:message code="label.administrators" /></a></li>
      </g:if>
    </ul>
    
    <div class="tab-content"> 
      <div id="tab-overview" class="tab-pane active">
        <g:render template="/templates/organization/overview" plugin="foundation" model="[organization:organization, organizationTypes:organizationTypes]" />
      </div>
      
      <div id="tab-contacts" class="tab-pane">
        <g:render template="/templates/contacts/list" plugin="foundation" model="[host:organization, hostType:'organization']" />
        <g:render template="/templates/contacts/create" plugin="foundation" model="[host:organization, hostType:'organization', contactTypes:contactTypes]"/>
      </div>

      <fr:hasPermission target="federation:management:saml:advanced">
        <div id="tab-entities" class="tab-pane">
          <g:render template="/templates/organization/entities" plugin="foundation" model="[entities:entities]" />
        </div>
      </fr:hasPermission>

      <div id="tab-idp" class="tab-pane">
        <g:render template="/templates/organization/idp" plugin="foundation" model="[identityproviders:identityproviders]" />
      </div>

      <div id="tab-sp" class="tab-pane">
        <g:render template="/templates/organization/sp" plugin="foundation" model="[serviceproviders:serviceproviders]" />
      </div>

      <div id="tab-registrations" class="tab-pane">
         <g:render template="/templates/organization/registrations" plugin="foundation" model="[registrations:registrations]" />         
      </div>

      <g:if test="${organization.approved}">
        <div id="tab-admins" class="tab-pane">
          <g:render template="/templates/organization/listfulladministration" plugin="foundation" />
        </div>
      </g:if>
    </div>

    <r:script>
      var updateOrganizationEndpoint = "${createLink(controller:'organization', action:'update', id:organization.id )}";

      var contactCreateEndpoint = "${createLink(controller:'organizationContact', action:'create', id:organization.id )}";
      var contactDeleteEndpoint = "${createLink(controller:'organizationContact', action:'delete' )}";
      var contactListEndpoint = "${createLink(controller:'organizationContact', action:'list', id:organization.id ) }";
      var contactSearchEndpoint = "${createLink(controller:'organizationContact', action:'search')}";
    
      var searchNewAdministratorsEndpoint = "${createLink(controller:'organization', action:'searchNewAdministrators', id:organization.id)}";
      var organizationFullAdministratorGrantEndpoint = "${createLink(controller:'organization', action:'grantFullAdministration', id:organization.id)}";
      var organizationFullAdministratorRevokeEndpoint = "${createLink(controller:'organization', action:'revokeFullAdministration', id:organization.id)}";
      var organizationFullAdministratorListEndpoint = "${createLink(controller:'organization', action:'listFullAdministration', id:organization.id)}";
      var organizationFullAdministratorSearchEndpoint = "${createLink(controller:'organization', action:'searchFullAdministration', id:organization.id)}";
    </r:script>
  </body>
</html>
