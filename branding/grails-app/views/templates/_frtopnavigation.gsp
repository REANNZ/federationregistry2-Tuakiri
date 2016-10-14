<ul class="level1">
  <fr:isNotLoggedIn>
    <li class="${controllerName == 'auth' ? 'directactive':''}">
      <g:link controller="auth"><g:message encodeAs="HTML"  code="fr.branding.nav.login" default="Login"/></g:link>
    </li>
    <li class="${controllerName == 'bootstrap' && ['organization', 'saveorganization','organizationregistered'].contains(actionName) ? 'directactive':''}">
      <g:link controller="bootstrap" action="organization"><g:message encodeAs="HTML"  code="fr.branding.nav.regorg" default="Create Organisation"/></g:link>
    </li>
    <li class="${controllerName == 'bootstrap' && ['idp', 'saveidp','idpregistered'].contains(actionName) ? 'directactive':''}">
      <g:link controller="bootstrap" action="idp"><g:message encodeAs="HTML"  code="fr.branding.nav.regidp" default="Create Identity Provider" /></g:link>
    </li>
    <li class="${controllerName == 'bootstrap' && ['sp', 'savesp','spregistered'].contains(actionName) ? 'directactive':''}">
      <g:link controller="bootstrap" action="sp"><g:message encodeAs="HTML"  code="fr.branding.nav.regsp" default="Create Service Provider" /></g:link>
    </li>
  </fr:isNotLoggedIn>
  <fr:isLoggedIn>
    <li class="${controllerName == 'dashboard' ? 'directactive':''}">
      <g:link controller="dashboard"><g:message encodeAs="HTML"  code="fr.branding.nav.dashboard" default="Dashboard" /></g:link>
    </li>
    <li class="${['organization', 'entityDescriptor', 'identityProvider', 'serviceProvider', 'contacts'].contains(controllerName) ? 'active' : ''}">
      <g:link controller="organization" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.subscribers" default="Subscribers" /></g:link>
    </li>
    <li class="${['federationReports', 'identityProviderReports', 'serviceProviderReports', 'complianceReports'].contains(controllerName) ? 'active' : ''}">
      <fr:hasPermission target="federation:management:reporting">
        <g:link controller="federationReports" action="summary"><g:message encodeAs="HTML"  code="fr.branding.nav.reporting" default="Reporting" /></g:link>
      </fr:hasPermission>
      <fr:lacksPermission target="federation:management:reporting">
        <g:link controller="complianceReports" action="attributesupport"><g:message encodeAs="HTML"  code="fr.branding.nav.reporting" default="Reporting" /></g:link>
      </fr:lacksPermission>
    </li>
    <fr:isAdministrator>
      <li class="${['metadata'].contains(controllerName) ? 'active' : ''}">
        <g:link controller="metadata" action="view"><g:message encodeAs="HTML"  code="fr.branding.nav.metadats" default="Metadata" /></g:link>
      </li>
    </fr:isAdministrator>
    <li class="${['workflowInstance', 'workflowProcess', 'workflowScript', 'workflowApproval'].contains(controllerName) ? 'active' : ''}">
      <g:link controller="workflowApproval"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow" default="Workflow" /></g:link>
    </li>
    <fr:isAdministrator>
      <li class="${request.forwardURI.contains("administration") ? 'active' : ''}">
        <g:link controller="adminDashboard"><g:message encodeAs="HTML"  code="fr.branding.nav.administration" default="Administration" /></g:link>
      </li>
    </fr:isAdministrator>
  </fr:isLoggedIn>
  <li><a style="color: #fff;" href="https://aaf.edu.au/support/"><g:message encodeAs="HTML"  code="fr.branding.nav.support" default="Support" /></a></li>
  <fr:isLoggedIn>
    <li>
      <g:link controller="auth" action="logout"><g:message encodeAs="HTML"  code="fr.branding.nav.logout" default="Logout" /></g:link>
    </li>
  </fr:isLoggedIn>
</ul>
