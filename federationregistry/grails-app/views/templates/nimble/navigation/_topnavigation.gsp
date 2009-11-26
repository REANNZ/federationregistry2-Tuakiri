
<div>
  <ul id="topnavigation_" class="horizmenu mlhorizmenu">

    <n:isAdministrator>
      <li class="${['admins', 'user', 'role', 'group'].contains(controllerName) ? 'current' : ''}">
      	<a href="#" class="icon icon_cog"><g:message code="navigation.accesscontrol.label" /></a>
	      <ul class="submenu">
				<li>
			  		<g:link controller="user" action="list" class="icon icon_user"><g:message code="navigation.accesscontrol.user.label" /></g:link>
				</li>
				<li>
					<g:link controller="role" action="list" class="icon icon_cog"><g:message code="navigation.accesscontrol.roles.label" /></g:link>
				</li>
				<li>
					<g:link controller="group" action="list" class="icon icon_group"><g:message code="navigation.accesscontrol.groups.label" /></g:link>
				</li>
				<li>
	          		<g:link controller="admins" action="index" class="icon icon_user_suit"><g:message code="navigation.accesscontrol.administrators.label" /></g:link>
				</li>
	        </li>
	      </ul>
      </li>
    </n:isAdministrator>
  </ul>
  
</div>
