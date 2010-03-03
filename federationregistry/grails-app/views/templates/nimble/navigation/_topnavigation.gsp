
<div class="aafmenu">
	
	<ul id="topnavigation_">
		<li class="${['idpAttributeCompliance', 'attributeRelease', 'certifyingAuthorityUsage'].contains(controllerName) ? 'current' : ''}">
			<g:link controller="idpAttributeCompliance" action="summary" class="icon icon_award_star_gold_2"><g:message code="fedreg.navigation.compliance" /></g:link>
		</li>
		<n:isAdministrator>

			<li class="${['admins', 'user', 'role', 'group'].contains(controllerName) ? 'current' : ''}">
				<g:link controller="user" class="icon icon_cog"><g:message code="fedreg.navigation.accesscontrol" /></g:link>
			</li>
			<li class="${['dataManagement'].contains(controllerName) ? 'current' : ''}">
				<g:link controller="dataManagement" class="icon icon_database"><g:message code="fedreg.navigation.datamanagement" /></g:link>
		  	</li>
			<li class="${['monitor'].contains(controllerName) ? 'current' : ''}">
				<g:link controller="monitor" class="icon icon_time"><g:message code="fedreg.navigation.monitoring" /></g:link>
			</li>
		</n:isAdministrator>
	</ul>
  
</div>
