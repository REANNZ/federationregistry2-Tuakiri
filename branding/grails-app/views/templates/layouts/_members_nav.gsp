<fr:isLoggedIn>
  <g:render template='/templates/frtopnavigation'/>

  <ul class="level2">
    <li class="${controllerName == 'organization' ? 'active':''}">
    <g:link controller="organization" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.members.organizations" default="Organisations"/></g:link>
    </li>
    <fr:hasPermission target="saml:advanced">
    <li class="${controllerName == 'entityDescriptor' ? 'active':''}">
    <g:link controller="entityDescriptor" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.members.entitydescriptors" default="Entity Descriptors"/></g:link>
    </li>
    </fr:hasPermission>
    <li class="${controllerName == 'identityProvider' ? 'active':''}">
    <g:link controller="identityProvider" action="list" ><g:message encodeAs="HTML"  code="fr.branding.nav.members.identityproviders" default="Identity Providers"/></g:link>
    </li>
    <li class="${controllerName == 'serviceProvider' ? 'active':''}">
    <g:link controller="serviceProvider" action="list" ><g:message encodeAs="HTML"  code="fr.branding.nav.members.serviceproviders" default="Service Providers"/></g:link>
    </li>
    <li class="${controllerName == 'contacts' ? 'active':''}">
    <g:link controller="contacts" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.members.contacts" default="Contacts"/></g:link>
    </li>
  </ul> 

  <g:if test="${controllerName == 'organization'}">
    <ul class="level3a">
      <li class="${actionName == 'list' ? 'active':''}"><g:link controller="organization" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.members.list" default="List"/></g:link></li>
      <fr:hasPermission target="saml:advanced">
      <li class="${actionName == 'listarchived' ? 'active':''}"><g:link controller="organization" action="listarchived"><g:message encodeAs="HTML"  code="fr.branding.nav.members.listarchived" default="List Archived"/></g:link></li>
      </fr:hasPermission>
      <li class="${actionName in ['create', 'save'] ? 'active':''}"><g:link controller="organization" action="create"><g:message encodeAs="HTML"  code="fr.branding.nav.members.create" default="Create"/></g:link></li>
      <g:if test="${actionName in ['show']}">
        <li class="${actionName == 'show' ? 'active':''}"><g:link controller="organization" action="show" id="${organization.id}"><g:message encodeAs="HTML"  code="fr.branding.nav.members.view" default="View"/></g:link></li>
      </g:if>
    </ul>
  </g:if>

  <fr:hasPermission target="saml:advanced">
    <g:if test="${controllerName == 'entityDescriptor'}">
      <ul class="level3a">
        <li class="${actionName == 'list' ? 'active':''}"><g:link controller="entityDescriptor" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.members.list" default="List"/></g:link></li>
        <fr:hasPermission target="saml:advanced">
        <li class="${actionName == 'listarchived' ? 'active':''}"><g:link controller="entityDescriptor" action="listarchived"><g:message encodeAs="HTML"  code="fr.branding.nav.members.listarchived" default="List Archived"/></g:link></li>
        </fr:hasPermission>
        <li class="${actionName in ['create', 'save'] ? 'active':''}"><g:link controller="entityDescriptor" action="create"><g:message encodeAs="HTML"  code="fr.branding.nav.members.create" default="Create"/></g:link></li>
        <g:if test="${actionName in ['show']}">
          <li class="${actionName == 'show' ? 'active':''}"><g:link controller="entityDescriptor" action="show" id="${entity.id}"><g:message encodeAs="HTML"  code="fr.branding.nav.members.view" default="View"/></g:link></li>
        </g:if>
      </ul>
    </g:if>
  </fr:hasPermission>

  <g:if test="${controllerName == 'identityProvider'}">
    <ul class="level3a">
    <li class="${actionName == 'list' ? 'active':''}"><g:link controller="identityProvider" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.members.list" default="List"/></g:link></li>
    <fr:hasPermission target="saml:advanced">
      <li class="${actionName == 'listarchived' ? 'active':''}"><g:link controller="identityProvider" action="listarchived"><g:message encodeAs="HTML"  code="fr.branding.nav.members.listarchived" default="List Archived"/></g:link></li>
    </fr:hasPermission>
    <li class="${actionName in ['create', 'save'] ? 'active':''}"><g:link controller="identityProvider" action="create"><g:message encodeAs="HTML"  code="fr.branding.nav.members.create" default="Create"/></g:link></li>
    <g:if test="${actionName in ['show']}">
      <li class="${actionName == 'show' ? 'active':''}"><g:message encodeAs="HTML"  code="fr.branding.nav.members.view" default="View"/></li>
    </g:if>
    </ul>
  </g:if>

  <g:if test="${controllerName == 'serviceProvider'}">
    <ul class="level3a">
      <li class="${actionName == 'list' ? 'active':''}"><g:link controller="serviceProvider" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.members.list" default="List"/></g:link></li>
      <fr:hasPermission target="saml:advanced">
        <li class="${actionName == 'listarchived' ? 'active':''}"><g:link controller="serviceProvider" action="listarchived"><g:message encodeAs="HTML"  code="fr.branding.nav.members.listarchived" default="List Archived"/></g:link></li>
      </fr:hasPermission>
      <li class="${actionName in ['create', 'save'] ? 'active':''}"><g:link controller="serviceProvider" action="create"><g:message encodeAs="HTML"  code="fr.branding.nav.members.create" default="Create"/></g:link></li>
      <g:if test="${actionName in ['show']}">
        <li class="active"><g:message encodeAs="HTML"  code="fr.branding.nav.members.view" default="View"/></li>
      </g:if>
    </ul>
  </g:if>

  <g:if test="${controllerName == 'contacts'}">
    <ul class="level3a">
      <li class="${actionName == 'list' ? 'active':''}"><g:link controller="contacts" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.members.list" default="List"/></g:link></li>
      <li class="${actionName in ['create', 'save'] ? 'active':''}"><g:link controller="contacts" action="create"><g:message encodeAs="HTML"  code="fr.branding.nav.members.create" default="Create"/></g:link></li>
      <g:if test="${actionName in ['show']}">
        <li class="active"><g:message encodeAs="HTML"  code="fr.branding.nav.members.view" default="View"/></li>
      </g:if>
    </ul>
  </g:if>
</fr:isLoggedIn>
