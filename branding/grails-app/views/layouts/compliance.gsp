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
		
			<ul class="level2a">
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
			<fr:working/>

			<g:layoutBody/>
	    </section>

		<footer>
			<g:render template='/templates/frfooter' />
		</footer>
		
		<r:layoutResources/>
	</body>
</html>
