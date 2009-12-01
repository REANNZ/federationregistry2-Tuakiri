
<div class="aafmenu">
  <ul id="topnavigation_">
	<li class="${['attributeCompliance'].contains(controllerName) ? 'current' : ''}">
      	<a href="/federationregistry/attributeCompliance/summary" class="icon icon_award_star_gold_2"><g:message code="navigation.compliance.label" /></a>
	</li>
    <n:isAdministrator>
      <li class="${['admins', 'user', 'role', 'group'].contains(controllerName) ? 'current' : ''}">
      	<g:link controller="admins" class="icon icon_cog"><g:message code="navigation.accesscontrol.label" /></g:link>
      </li>
    </n:isAdministrator>
  </ul>
  
</div>
