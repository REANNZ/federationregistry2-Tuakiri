
	
	<ul class="level1">
		<n:isNotLoggedIn>
			<li class="${controllerName == 'auth' ? 'directactive':''}">
				<g:link controller="auth"><g:message code="fedreg.navigation.login" /></g:link>
			</li>
			<li class="${controllerName == 'bootstrap' && ['organization', 'saveorganization','organizationregistered'].contains(actionName) ? 'directactive':''}">
				<g:link controller="bootstrap" action="organization" class=""><g:message code="fedreg.navigation.registerorganization" /></g:link>
			</li>
			<li class="${controllerName == 'bootstrap' && ['idp', 'saveidp','idpregistered'].contains(actionName) ? 'directactive':''}">
				<g:link controller="bootstrap" action="idp" class=""><g:message code="fedreg.navigation.registeridentityprovider" /></g:link>
			</li>
			<li class="${controllerName == 'bootstrap' && ['sp', 'savesp','spregistered'].contains(actionName) ? 'directactive':''}">
				<g:link controller="bootstrap" action="sp" class=""><g:message code="fedreg.navigation.registerserviceprovider" /></g:link>
			</li>
		</n:isNotLoggedIn>
		<n:isLoggedIn>
			<li class="${controllerName == 'dashboard' ? 'directactive':''}">
				<g:link controller="dashboard"><g:message code="label.dashboard" /></g:link>
			</li>
			<li class="${['organization', 'entityDescriptor', 'IDPSSODescriptor', 'SPSSODescriptor', 'contacts'].contains(controllerName) ? 'active' : ''}">
				<g:link controller="organization" action="list" class=""><g:message code="fedreg.navigation.membership" /></g:link>
			</li>
			<li class="${['IDPSSODescriptorAttributeCompliance', 'attributeRelease', 'certifyingAuthorityUsage'].contains(controllerName) ? 'active' : ''}">
				<g:link controller="IDPSSODescriptorAttributeCompliance" action="summary" class=""><g:message code="fedreg.navigation.compliance" /></g:link>
			</li>
			<li class="${['metadata'].contains(controllerName) ? 'active' : ''}">
				<g:link controller="metadata" action="view" class=""><g:message code="fedreg.navigation.metadata" /></g:link>
			</li>
			<li class="${['workflowInstance', 'workflowProcess', 'workflowScript', 'workflowApproval'].contains(controllerName) ? 'active' : ''}">
				<g:link controller="workflowApproval" class=""><g:message code="fedreg.navigation.workflow" /></g:link>
			</li>
			<n:isAdministrator>
				<li class="${['admins', 'user', 'role', 'group'].contains(controllerName) ? 'active' : ''}">
					<g:link controller="user" class=""><g:message code="fedreg.navigation.accesscontrol" /></g:link>
				</li>
				<li class="${['code'].contains(controllerName) ? 'directactive' : ''}">
					<g:link controller="code" class=""><g:message code="fedreg.navigation.codeconsole" /></g:link>
				</li>
			</n:isAdministrator>
		</n:isLoggedIn>
			<li><a style="color: #fff;" href="http://support.aaf.edu.au/forums"><g:message code="fedreg.navigation.help" /></a></li>
			<li><a style="color: #fff;" href="#" onClick="script: Zenbox.show(); return false;"><g:message code="fedreg.navigation.support" /></a></li>
		<n:isLoggedIn>
			<li class="">
				<g:link controller="auth" action="logout" class=""><g:message code="label.logout" /></g:link>
			</li>
		</n:isLoggedIn>
	</ul>
  
