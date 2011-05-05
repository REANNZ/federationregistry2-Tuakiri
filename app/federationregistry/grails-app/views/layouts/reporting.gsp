<!DOCTYPE html>

<html>
	<head>
		<title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
		<r:use modules="html5, tiptip, blockui, jgrowl, datatables, zenbox, app"/>
		<r:layoutResources/>
		<g:layoutHead />
	</head>

	<body>

	    <header>
			<g:render template='/templates/frheader' />
	    </header>
	
		<nav>
		<n:isLoggedIn>
			<g:render template='/templates/frtopnavigation'/>
			
			<ul class="level2">
				<n:hasPermission target="federation:reporting">
					<li class="${controllerName == '' ? 'active':''}"><g:link controller="" action=""><g:message code="fedreg.navigation.federationreports"/></g:link></li>
					<li class="${controllerName == '' ? 'active':''}"><g:link controller="" action=""><g:message code="fedreg.navigation.idpreports"/></g:link></li>
					<li class="${controllerName == '' ? 'active':''}"><g:link controller="" action=""><g:message code="fedreg.navigation.spreports"/></g:link></li>
				</n:hasPermission>
				<li class="${controllerName in ['IDPSSODescriptorAttributeCompliance', 'attributeRelease', 'certifyingAuthorityUsage'] ? 'active':''}"><g:link controller="IDPSSODescriptorAttributeCompliance" action="summary"><g:message code="fedreg.navigation.compliance"/></g:link></li>
			</ul>
			
			<ul class="level3a">
				<li class="${controllerName == 'IDPSSODescriptorAttributeCompliance' ? 'active':''}">
					<g:link controller="IDPSSODescriptorAttributeCompliance" action="summary"><g:message code="label.attributesummary" /></g:link>
			 	</li>
				<li class="${controllerName == 'attributeRelease' ? 'active':''}">
					<g:link controller="attributeRelease" action="index"><g:message code="label.attributerelease" /></g:link>
			 	</li>
				<li class="${controllerName == 'certifyingAuthorityUsage' ? 'active':''}">
					<g:link controller="certifyingAuthorityUsage" action="index"><g:message code="label.cautilization" /></g:link>
				</li>
			</ul>	
		</n:isLoggedIn>
		</nav>

		<section>
			<g:layoutBody/>
	    </section>

		<footer>
			<g:render template='/templates/frfooter' />
		</footer>
		
		<r:layoutResources/>
	</body>
</html>
