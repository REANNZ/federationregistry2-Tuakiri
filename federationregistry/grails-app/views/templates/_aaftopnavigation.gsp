
	
	<ul class="level1">
		<li class="${['idpAttributeCompliance', 'attributeRelease', 'certifyingAuthorityUsage'].contains(controllerName) ? 'active' : ''}">
			<g:link controller="idpAttributeCompliance" action="summary" class=""><g:message code="fedreg.navigation.compliance" /></g:link>
		</li>
		<li class="${['organization', 'entity', 'IDPSSODescriptor', 'contacts'].contains(controllerName) ? 'active' : ''}">
			<g:link controller="organization" action="list" class=""><g:message code="fedreg.navigation.membership" /></g:link>
		</li>
		<n:isAdministrator>
			<li class="${['admins', 'user', 'role', 'group'].contains(controllerName) ? 'active' : ''}">
				<g:link controller="user" class=""><g:message code="fedreg.navigation.accesscontrol" /></g:link>
			</li>
			<li class="${['dataManagement'].contains(controllerName) ? 'active' : ''}">
				<g:link controller="dataManagement" class=""><g:message code="fedreg.navigation.datamanagement" /></g:link>
		  	</li>
			<li class="${['workflowInstance', 'workflowProcess'].contains(controllerName) ? 'active' : ''}">
				<g:link controller="workflowProcess" class=""><g:message code="fedreg.navigation.workflow" /></g:link>
			</li>
			<li class="${['monitor'].contains(controllerName) ? 'active' : ''}">
				<g:link controller="monitor" class=""><g:message code="fedreg.navigation.monitoring" /></g:link>
			</li>
			<li class="${['code'].contains(controllerName) ? 'active' : ''}">
				<g:link controller="code" class=""><g:message code="fedreg.navigation.codeconsole" /></g:link>
			</li>
		</n:isAdministrator>
		<li class="">
			<g:link controller="auth" action="logout" class=""><g:message code="fedreg.label.logout" /></g:link>
		</li>
	</ul>
  
