<!DOCTYPE html>

<html>
	<head>
		<title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
		<r:use modules="html5, tiptip, jgrowl, zenbox, app"/>
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
