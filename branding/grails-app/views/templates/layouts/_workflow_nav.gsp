<fr:isLoggedIn>
  <g:render template='/templates/frtopnavigation'/>

  <ul class="level2">
    <li class="${controllerName == 'workflowApproval' ? 'active':''}">
      <g:link controller="workflowApproval" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.approvals" default="Approvals"/></g:link>
    </li>
    <fr:hasPermission target="workflow:processes:view">
      <li class="${controllerName == 'workflowProcess' ? 'active':''}">
        <g:link controller="workflowProcess" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.processes" default="Processes"/></g:link>
      </li>
    </fr:hasPermission>
    <fr:hasPermission target="workflow:scripts:view">
      <li class="${controllerName == 'workflowScript' ? 'active':''}">
        <g:link controller="workflowScript" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.scripts" default="Scripts"/></g:link>
      </li>
    </fr:hasPermission>
  </ul>

  <g:if test="${controllerName == 'workflowApproval'}">
    <ul class="level3a">
      <li class="${actionName == 'list' ? 'active':''}"><g:link controller="workflowApproval" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.pending" default="Pending"/></g:link></li>
      <fr:hasPermission target="workflow:approval:administrator">
        <li class="${actionName == 'administrative' ? 'active':''}"><g:link controller="workflowApproval" action="administrative"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.all" default="All"/></g:link></li>
      </fr:hasPermission>
    </ul>
  </g:if>

  <g:if test="${controllerName == 'workflowProcess'}">
    <ul class="level3a">
      <li class="${actionName == 'list' ? 'active':''}"><g:link controller="workflowProcess" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.list" default="List"/></g:link></li>
      <li class="${actionName == 'create' ? 'active':''}"><g:link controller="workflowProcess" action="create"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.create" default="Create"/></g:link></li>
      <g:if test="${actionName in ['show', 'edit']}">
        <li class="${actionName == 'show' ? 'active':''}"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.view" default="View"/></li>
        <li class="${actionName == 'edit' ? 'active':''}"><g:link controller="workflowProcess" action="edit" id="${process.id}" class="${actionName == 'edit' ? 'active':''}"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.edit" default="Edit"/></g:link></li>
      </g:if>
    </ul>
  </g:if>

  <g:if test="${controllerName == 'workflowScript'}">
    <ul class="level3a">
      <li class="${actionName == 'list' ? 'active':''}"><g:link controller="workflowScript" action="list"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.list" default="List"/></g:link></li>
      <li class="${actionName == 'create' ? 'active':''}"><g:link controller="workflowScript" action="create"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.create" default="Create"/></g:link></li>
      <g:if test="${actionName in ['show', 'edit']}">
      <li class="${actionName == 'show' ? 'active':''}"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.view" default="View"/></li>
      <li class="${actionName == 'edit' ? 'active':''}"><g:link controller="workflowScript" action="edit" id="${script.id}" class="${actionName == 'edit' ? 'active':''}"><g:message encodeAs="HTML"  code="fr.branding.nav.workflow.edit" default="Edit"/></g:link></li>
      </g:if>
    </ul>
  </g:if>

</fr:isLoggedIn>
