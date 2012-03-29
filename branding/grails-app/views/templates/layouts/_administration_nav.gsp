<fr:isLoggedIn>
  <g:render template='/templates/frtopnavigation'/>

  <ul class="level2">
    <li class="${controllerName == 'adminDashboard' ? 'directactive':''}">
      <g:link controller="adminDashboard" action="index"><g:message code="fedreg.navigation.admin.dashboard" default="Dashboard"/></g:link>
    </li>
    <li class="${controllerName == 'subject' ? 'active':''}">
      <g:link controller="subject" action="list"><g:message code="fedreg.navigation.admin.subjects" default="Subjects"/></g:link>
    </li>
    <li class="${controllerName == 'role' ? 'active':''}">
      <g:link controller="role" action="list"><g:message code="fedreg.navigation.admin.roles" default="Roles"/></g:link>
    </li>
    <li class="${['attributeBase', 'attributeCategory', 'CAKeyInfo', 'contactType', 'monitorType', 'organizationType', 'samlURI'].contains(controllerName) ? 'directactive':''}">
      <g:link controller="attributeBase" action="list"><g:message code="fedreg.navigation.admin.datamanagement" /></g:link>
    </li>
    <li class="${controllerName == 'adminConsole' ? 'directactive':''}">
      <g:link controller="adminConsole" action="index"><g:message code="fedreg.navigation.admin.console" /></g:link>
    </li>
  </ul>

  <g:if test="${controllerName == 'subject'}">
    <ul class="level3">
      <li class="${actionName == 'list' ? 'directactive':''}">
        <g:link controller="subject" action="list"><g:message code="fedreg.navigation.subject.subjects.list" default="List"/></g:link>
      </li>
      <g:if test="${actionName == 'show'}">
        <li class="directactive">
          <g:message code="fedreg.navigation.admin.subjects.view" default="View"/>
        </li>
      </g:if>
    </ul>
  </g:if>

  <g:if test="${controllerName == 'role'}">
    <ul class="level3">
      <li class="${actionName == 'list' ? 'directactive':''}">
        <g:link controller="role" action="list"><g:message code="fedreg.navigation.admin.roles.list" default="List"/></g:link>
      </li>
      <li class="${actionName == 'create' ? 'directactive':''}">
        <g:link controller="role" action="create"><g:message code="fedreg.navigation.admin.roles.create" default="Create"/></g:link>
      </li>
      <g:if test="${actionName == 'show'}">
        <li class="directactive">
          <g:message code="fedreg.navigation.admin.roles.view" default="View"/>
        </li>
      </g:if>
    </ul>
  </g:if>
</fr:isLoggedIn>