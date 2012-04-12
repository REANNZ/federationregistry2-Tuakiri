<fr:isLoggedIn>
  <g:render template='/templates/frtopnavigation'/>

  <ul class="level2">
    <li class="${controllerName == 'workflowApproval' ? 'active':''}">
      <g:link controller="workflowApproval" action="list"><g:message code="label.approvals" /></g:link>
    </li>
    <fr:hasPermission target="workflow:processes:view">
      <li class="${controllerName == 'workflowProcess' ? 'active':''}">
        <g:link controller="workflowProcess" action="list"><g:message code="label.processes" /></g:link>
      </li>
    </fr:hasPermission>
    <fr:hasPermission target="workflow:scripts:view">
      <li class="${controllerName == 'workflowScript' ? 'active':''}">
        <g:link controller="workflowScript" action="list"><g:message code="label.scripts" /></g:link>
      </li>
    </fr:hasPermission>
  </ul>

  <g:if test="${controllerName == 'workflowApproval'}">
    <ul class="level3a">
      <li class="${actionName == 'list' ? 'active':''}"><g:link controller="workflowApproval" action="list"><g:message code="label.pending"/></g:link></li>
      <fr:hasPermission target="workflow:approval:administrator">
        <li class="${actionName == 'administrative' ? 'active':''}"><g:link controller="workflowApproval" action="administrative"><g:message code="label.all"/></g:link></li>
      </fr:hasPermission>
    </ul>
  </g:if>

  <g:if test="${controllerName == 'workflowProcess'}">
    <ul class="level3a">
      <li class="${actionName == 'list' ? 'active':''}"><g:link controller="workflowProcess" action="list"><g:message code="label.list"/></g:link></li>
      <li class="${actionName == 'create' ? 'active':''}"><g:link controller="workflowProcess" action="create"><g:message code="label.create"/></g:link></li>
      <g:if test="${actionName in ['show', 'edit']}">
        <li class="${actionName == 'show' ? 'active':''}"><g:message code="label.view"/></li>
        <li class="${actionName == 'edit' ? 'active':''}"><g:link controller="workflowProcess" action="edit" id="${process.id}" class="${actionName == 'edit' ? 'active':''}"><g:message code="label.edit"/></g:link></li>
      </g:if>
    </ul>
  </g:if>

  <g:if test="${controllerName == 'workflowScript'}">
    <ul class="level3a">
      <li class="${actionName == 'list' ? 'active':''}"><g:link controller="workflowScript" action="list"><g:message code="label.list"/></g:link></li>
      <li class="${actionName == 'create' ? 'active':''}"><g:link controller="workflowScript" action="create"><g:message code="label.create"/></g:link></li>
      <g:if test="${actionName in ['show', 'edit']}">
      <li class="${actionName == 'show' ? 'active':''}"><g:message code="label.view"/></li>
      <li class="${actionName == 'edit' ? 'active':''}"><g:link controller="workflowScript" action="edit" id="${script.id}" class="${actionName == 'edit' ? 'active':''}"><g:message code="label.edit"/></g:link></li>
      </g:if>
    </ul>
  </g:if>

</fr:isLoggedIn>