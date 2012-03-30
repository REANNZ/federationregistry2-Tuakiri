<ul class="level1">
  <fr:isNotLoggedIn>
    <li class="${controllerName == 'auth' ? 'directactive':''}">
      <g:link controller="auth"><g:message code="fedreg.navigation.login" /></g:link>
    </li>
    <li class="${controllerName == 'bootstrap' && ['organization', 'saveorganization','organizationregistered'].contains(actionName) ? 'directactive':''}">
      <g:link controller="bootstrap" action="organization"><g:message code="fedreg.navigation.registerorganization" /></g:link>
    </li>
    <li class="${controllerName == 'bootstrap' && ['idp', 'saveidp','idpregistered'].contains(actionName) ? 'directactive':''}">
      <g:link controller="bootstrap" action="idp"><g:message code="fedreg.navigation.registeridentityprovider" /></g:link>
    </li>
    <li class="${controllerName == 'bootstrap' && ['sp', 'savesp','spregistered'].contains(actionName) ? 'directactive':''}">
      <g:link controller="bootstrap" action="sp"><g:message code="fedreg.navigation.registerserviceprovider" /></g:link>
    </li>
  </fr:isNotLoggedIn>
  <fr:isLoggedIn>
    <li class="${controllerName == 'dashboard' ? 'directactive':''}">
      <g:link controller="dashboard"><g:message code="fedreg.navigation.dashboard" /></g:link>
    </li>
    <li class="${['organization', 'entityDescriptor', 'identityProvider', 'serviceProvider', 'contacts'].contains(controllerName) ? 'active' : ''}">
      <g:link controller="organization" action="list"><g:message code="fedreg.navigation.membership" /></g:link>
    </li>
    <li class="${['federationReports', 'identityProviderReports', 'serviceProviderReports', 'complianceReports'].contains(controllerName) ? 'active' : ''}">
      <fr:hasPermission target="federation:reporting">
        <g:link controller="federationReports" action="summary"><g:message code="fedreg.navigation.reporting" /></g:link>
      </fr:hasPermission>
      <fr:lacksPermission target="federation:reporting">
        <g:link controller="complianceReports" action="attributesupport"><g:message code="fedreg.navigation.reporting" /></g:link>
      </fr:lacksPermission>
    </li>
    <li class="${['metadata'].contains(controllerName) ? 'active' : ''}">
      <g:link controller="metadata" action="view"><g:message code="fedreg.navigation.metadata" /></g:link>
    </li>
    <li class="${['workflowInstance', 'workflowProcess', 'workflowScript', 'workflowApproval'].contains(controllerName) ? 'active' : ''}">
      <g:link controller="workflowApproval"><g:message code="fedreg.navigation.workflow" /></g:link>
    </li>
    <fr:isAdministrator>
      <li class="${request.forwardURI.contains("administration") ? 'active' : ''}">
        <g:link controller="adminDashboard"><g:message code="fedreg.navigation.administration" /></g:link>
      </li>
    </fr:isAdministrator>
  </fr:isLoggedIn>
  <li><a style="color: #fff;" href="http://support.aaf.edu.au/forums"><g:message code="fedreg.navigation.help" /></a></li>
  <li><a style="color: #fff;" href="#" onClick="script: Zenbox.show(); return false;"><g:message code="fedreg.navigation.support" /></a></li>
  <fr:isLoggedIn>
    <li>
      <g:link controller="auth" action="logout"><g:message code="label.logout" /></g:link>
    </li>
  </fr:isLoggedIn>
</ul>