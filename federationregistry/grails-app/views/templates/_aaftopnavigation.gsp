
<div class="aafmenu">
	
	<ul id="topnavigation_">
		<li class="${['idpAttributeCompliance', 'attributeRelease', 'certifyingAuthorityUsage'].contains(controllerName) ? 'current' : ''}">
			<g:link controller="idpAttributeCompliance" action="summary" class="icon icon_award_star_gold_2"><g:message code="fedreg.navigation.compliance" /></g:link>
		</li>
		<li class="${['organization', 'entity', 'identityProvider', 'contacts'].contains(controllerName) ? 'current' : ''}">
			<g:link controller="organization" action="list" class="icon icon_chart_organisation"><g:message code="fedreg.navigation.membership" /></g:link>
		</li>
		<n:isAdministrator>
			<li class="${['admins', 'user', 'role', 'group'].contains(controllerName) ? 'current' : ''}">
				<g:link controller="user" class="icon icon_cog"><g:message code="fedreg.navigation.accesscontrol" /></g:link>
			</li>
			<li class="${['dataManagement'].contains(controllerName) ? 'current' : ''}">
				<g:link controller="dataManagement" class="icon icon_database"><g:message code="fedreg.navigation.datamanagement" /></g:link>
		  	</li>
			<li class="${['workflowInstance', 'workflowManager'].contains(controllerName) ? 'current' : ''}">
				<g:link controller="workflowManager" class="icon icon_time"><g:message code="fedreg.navigation.workflow" /></g:link>
			</li>
			<li class="${['monitor'].contains(controllerName) ? 'current' : ''}">
				<g:link controller="monitor" class="icon icon_time"><g:message code="fedreg.navigation.monitoring" /></g:link>
			</li>
			<li class="${['code'].contains(controllerName) ? 'current' : ''}">
				<g:link controller="code" class="icon icon_textfield"><g:message code="fedreg.navigation.codeconsole" /></g:link>
			</li>
		</n:isAdministrator>
		<li class="">
			<g:link controller="auth" action="logout" class="icon icon_lock_open"><g:message code="fedreg.label.logout" /></g:link>
		</li>
	</ul>
  
</div>
