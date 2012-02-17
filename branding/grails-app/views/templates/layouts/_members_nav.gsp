<fr:isLoggedIn>
  <g:render template='/templates/frtopnavigation'/>

  <ul class="level2">
    <li class="${controllerName == 'organization' ? 'active':''}">
    <g:link controller="organization" action="list"><g:message code="label.organizations" /></g:link>
    </li>
    <fr:hasPermission target="saml:advanced">
    <li class="${controllerName == 'entityDescriptor' ? 'active':''}">
    <g:link controller="entityDescriptor" action="list"><g:message code="label.entitydescriptors" /></g:link>
    </li>
    </fr:hasPermission>
    <li class="${controllerName == 'identityProvider' ? 'active':''}">
    <g:link controller="identityProvider" action="list" ><g:message code="label.identityproviders" /></g:link>
    </li>
    <li class="${controllerName == 'serviceProvider' ? 'active':''}">
    <g:link controller="serviceProvider" action="list" ><g:message code="label.serviceproviders" /></g:link>
    </li>
    <li class="${controllerName == 'contacts' ? 'active':''}">
    <g:link controller="contacts" action="list"><g:message code="label.contacts" /></g:link>
    </li>
  </ul> 

  <g:if test="${controllerName == 'organization'}">
    <ul class="level3a">
      <li class="${actionName == 'list' ? 'active':''}"><g:link controller="organization" action="list"><g:message code="label.list"/></g:link></li>
      <fr:hasPermission target="saml:advanced">
      <li class="${actionName == 'listarchived' ? 'active':''}"><g:link controller="organization" action="listarchived"><g:message code="label.listarchived"/></g:link></li>
      </fr:hasPermission>
      <li class="${actionName in ['create', 'save'] ? 'active':''}"><g:link controller="organization" action="create"><g:message code="label.create"/></g:link></li>
      <g:if test="${actionName in ['show']}">
        <li class="${actionName == 'show' ? 'active':''}"><g:link controller="organization" action="show" id="${organization.id}"><g:message code="label.view"/></g:link></li>
      </g:if>
    </ul>
  </g:if>

  <fr:hasPermission target="saml:advanced">
    <g:if test="${controllerName == 'entityDescriptor'}">
      <ul class="level3a">
        <li class="${actionName == 'list' ? 'active':''}"><g:link controller="entityDescriptor" action="list"><g:message code="label.list"/></g:link></li>
        <fr:hasPermission target="saml:advanced">
        <li class="${actionName == 'listarchived' ? 'active':''}"><g:link controller="entityDescriptor" action="listarchived"><g:message code="label.listarchived"/></g:link></li>
        </fr:hasPermission>
        <li class="${actionName in ['create', 'save'] ? 'active':''}"><g:link controller="entityDescriptor" action="create"><g:message code="label.create"/></g:link></li>
        <g:if test="${actionName in ['show']}">
          <li class="${actionName == 'show' ? 'active':''}"><g:link controller="entityDescriptor" action="show" id="${entity.id}"><g:message code="label.view"/></g:link></li>
        </g:if>
      </ul>
    </g:if>
  </fr:hasPermission>

  <g:if test="${controllerName == 'identityProvider'}">
    <ul class="level3a">
    <li class="${actionName == 'list' ? 'active':''}"><g:link controller="identityProvider" action="list"><g:message code="label.list"/></g:link></li>
    <fr:hasPermission target="saml:advanced">
      <li class="${actionName == 'listarchived' ? 'active':''}"><g:link controller="identityProvider" action="listarchived"><g:message code="label.listarchived"/></g:link></li>
    </fr:hasPermission>
    <li class="${actionName in ['create', 'save'] ? 'active':''}"><g:link controller="identityProvider" action="create"><g:message code="label.create"/></g:link></li>
    <g:if test="${actionName in ['show']}">
      <li class="${actionName == 'show' ? 'active':''}"><g:message code="label.view"/></li>
    </g:if>
    </ul>
  </g:if>

  <g:if test="${controllerName == 'serviceProvider'}">
    <ul class="level3a">
      <li class="${actionName == 'list' ? 'active':''}"><g:link controller="serviceProvider" action="list"><g:message code="label.list"/></g:link></li>
      <fr:hasPermission target="saml:advanced">
        <li class="${actionName == 'listarchived' ? 'active':''}"><g:link controller="serviceProvider" action="listarchived"><g:message code="label.listarchived"/></g:link></li>
      </fr:hasPermission>
      <li class="${actionName in ['create', 'save'] ? 'active':''}"><g:link controller="serviceProvider" action="create"><g:message code="label.create"/></g:link></li>
      <g:if test="${actionName in ['show']}">
        <li class="active"><g:message code="label.view" /> </li>
      </g:if>
    </ul>
  </g:if>

  <g:if test="${controllerName == 'contacts'}">
    <ul class="level3a">
      <li class="${actionName == 'list' ? 'active':''}"><g:link controller="contacts" action="list"><g:message code="label.list"/></g:link></li>
      <li class="${actionName == 'create' ? 'active':''}"><g:link controller="contacts" action="create"><g:message code="label.create"/></g:link></li>
      <g:if test="${actionName in ['show', 'edit']}">
        <li> | </li>
        <li><g:message code="fedreg.view.members.contacts.show.heading" args="[contact.givenName, contact.surname]"/>: </li>
        <li class="${actionName == 'show' ? 'active':''}"><g:link controller="contacts" action="show" id="${contact.id}"><g:message code="label.view"/></g:link>
        <fr:hasPermission target="contact:${contact.id}:update">
          <li class="${actionName == 'edit' ? 'active':''}"><g:link controller="contacts" action="edit" id="${contact.id}"><g:message code="label.edit"/></g:link></li>
        </fr:hasPermission>
      </g:if>
    </ul>
  </g:if>
</fr:isLoggedIn>