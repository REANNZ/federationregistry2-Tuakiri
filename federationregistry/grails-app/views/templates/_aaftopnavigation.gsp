
	
	<ul class="level1">
		<n:isNotLoggedIn>
			<li class="${controllerName == 'auth' ? 'directactive':''}">
				<g:link controller="auth"><g:message code="fedreg.navigation.login" /></g:link>
			</li>
			<li class="${controllerName == 'bootstrap' && ['organization','organizationregistered'].contains(actionName) ? 'directactive':''}">
				<g:link controller="bootstrap" action="organization" class=""><g:message code="fedreg.navigation.registerorganization" /></g:link>
			</li>
			<li class="${controllerName == 'bootstrap' && ['idp','idpregistered'].contains(actionName) ? 'directactive':''}">
				<g:link controller="bootstrap" action="idp" class=""><g:message code="fedreg.navigation.registeridentityprovider" /></g:link>
			</li>
		</n:isNotLoggedIn>
		<n:isLoggedIn>
			<li class="${['idpAttributeCompliance', 'attributeRelease', 'certifyingAuthorityUsage'].contains(controllerName) ? 'active' : ''}">
				<g:link controller="idpAttributeCompliance" action="summary" class=""><g:message code="fedreg.navigation.compliance" /></g:link>
			</li>
			<li class="${['organization', 'entity', 'IDPSSODescriptor', 'SPSSODescriptor', 'contacts'].contains(controllerName) ? 'active' : ''}">
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
				<li class="${['monitor'].contains(controllerName) ? 'directactive' : ''}">
					<g:link controller="monitor" class=""><g:message code="fedreg.navigation.monitoring" /></g:link>
				</li>
				<li class="${['code'].contains(controllerName) ? 'directactive' : ''}">
					<g:link controller="code" class=""><g:message code="fedreg.navigation.codeconsole" /></g:link>
				</li>
			</n:isAdministrator>
			<li class="">
				<g:link controller="auth" action="logout" class=""><g:message code="label.logout" /></g:link>
			</li>
		</n:isLoggedIn>
	</ul>
  
