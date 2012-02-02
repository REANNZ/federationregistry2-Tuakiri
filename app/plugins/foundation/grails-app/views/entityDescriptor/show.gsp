
<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message code="fedreg.view.members.entity.show.title" /></title>    
  </head>
  <body>
      
    <h2><g:message code="fedreg.view.members.entity.show.heading" args="[entity.entityID]"/></h2>

    <g:if test="${!entity.functioning()}">
      <div class="alert-message block-message error">
        <p><g:message code="fedreg.view.members.entity.show.notfunctioning"/></p>
      </div>
    </g:if>

    <ul class="tabs">
      <li class="active"><a href="#tab-overview"><g:message code="label.overview" /></a></li>
      <li><a href="#tab-contacts"><g:message code="label.contacts" /></a></li>
      <li><a href="#tab-idp"><g:message code="label.identityproviders" /></a></li>
      <li><a href="#tab-sp"><g:message code="label.serviceproviders" /></a></li>
      
      <li><a href="#tab-admins"><g:message code="label.administrators" /></a></li>
      <g:if test="${entity.extensions}">
        <li><a href="#tab-ext"><g:message code="label.extensions" /></a></li>
      </g:if>
    </ul>

    <div class="tab-content">
      <div id="tab-overview" class="tab-pane active">
        <g:render template="/templates/entitydescriptor/overview" plugin="foundation" model="[entity:entity]" />
      </div>
      
      <div id="tab-contacts" class="tab-pane">
        <g:render template="/templates/contacts/list" plugin="foundation" model="[host:entity, allowremove:true]" />
        <hr>
        <g:render template="/templates/contacts/create" plugin="foundation" model="[host:entity, contactTypes:contactTypes]"/>
      </div>

      <div id="tab-idp" class="tab-pane">
        <g:render template="/templates/entitydescriptor/idp_list" plugin="foundation" model="[entity:entity]" />
      </div>

      <div id="tab-sp" class="tab-pane">
        <g:render template="/templates/entitydescriptor/sp_list" plugin="foundation" model="[entity:entity]" />
      </div>

      <g:if test="${entity.extensions}">
        <div id="tab-ext" class="tab-pane">
          <pre class="metadata">  
            ${fieldValue(bean: entity, field: "extensions")}
          </pre>
        </div>
      </g:if>

    </div>

    <r:script>
      var updateEntityDescriptorEndpoint = "${createLink(controller:'entityDescriptor', action:'update', id:entity.id )}";

      var contactCreateEndpoint = "${createLink(controller:'descriptorContact', action:'create', id:entity.id )}";
      var contactDeleteEndpoint = "${createLink(controller:'descriptorContact', action:'delete' )}";
      var contactListEndpoint = "${createLink(controller:'descriptorContact', action:'list', id:entity.id ) }";
      var contactSearchEndpoint = "${createLink(controller:'descriptorContact', action:'search')}";
      
      var descriptorFullAdministratorGrantEndpoint = "${createLink(controller:'descriptorAdministration', action:'grantFullAdministration', id:entity.id)}";
      var descriptorFullAdministratorRevokeEndpoint = "${createLink(controller:'descriptorAdministration', action:'revokeFullAdministration', id:entity.id)}";
      var descriptorFullAdministratorListEndpoint = "${createLink(controller:'descriptorAdministration', action:'listFullAdministration', id:entity.id)}";
      var descriptorFullAdministratorSearchEndpoint = "${createLink(controller:'descriptorAdministration', action:'searchFullAdministration', id:entity.id)}";
    </r:script>
  </body>
</html>
