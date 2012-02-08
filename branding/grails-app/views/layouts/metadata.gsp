<!DOCTYPE html>

<html>
	<head>
		<title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
		<r:require modules="html5, tiptip, blockui, jgrowl, zenbox, app"/>
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
			
				<g:if test="${controllerName == 'metadata'}">
					<ul class="level2a">
						<li class="${actionName == 'view' ? 'active':''}"><g:link controller="metadata" action="view"><g:message code="label.currentmetadata"/></g:link></li>
						<li class="${actionName == 'viewall' ? 'active':''}"><g:link controller="metadata" action="viewall"><g:message code="label.allmetadata"/></g:link></li>
					</ul>
				</g:if>
			
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
