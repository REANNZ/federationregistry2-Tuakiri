<fr:isLoggedIn>
  <g:render template='/templates/frtopnavigation'/>

  <ul class="level2">
    <li class="${['attributeBase', 'attributeCategory', 'CAKeyInfo', 'contactType', 'monitorType', 'organizationType', 'samlURI'].contains(controllerName) ? 'directactive':''}">
      <g:link controller="attributeBase" action="list"><g:message code="fedreg.navigation.admin.datamanagement" /></g:link>
    </li>
    <li class="${controllerName == 'adminConsole' ? 'directactive':''}">
      <g:link controller="adminConsole" action="index"><g:message code="fedreg.navigation.admin.console" /></g:link>
    </li>
  </ul>
</fr:isLoggedIn>