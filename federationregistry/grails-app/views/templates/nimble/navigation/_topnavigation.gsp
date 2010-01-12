
<div class="aafmenu">
  <ul id="topnavigation_">
	<li class="${['attributeCompliance'].contains(controllerName) ? 'current' : ''}">
      	<a href="/federationregistry/attributeCompliance/summary" class="icon icon_award_star_gold_2"><g:message code="fedreg.navigation.compliance" /></a>
	</li>
    <n:isAdministrator>
      <li class="${['admins', 'user', 'role', 'group'].contains(controllerName) ? 'current' : ''}">
      	<g:link controller="user" class="icon icon_cog"><g:message code="fedreg.navigation.accesscontrol" /></g:link>
      </li>
    </n:isAdministrator>
  </ul>
  
</div>
