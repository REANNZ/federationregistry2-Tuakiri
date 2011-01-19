<!DOCTYPE html>

<html>
	<head>
		<title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
		<r:use modules="html5, tiptip, jgrowl, datatables, validate, zenbox, alphanumeric, formwizard, zenbox, app"/>
		<r:layoutResources/>
		<g:layoutHead />
	</head>

	<body>
		<header>
			<g:render template='/templates/frheader' />
		</header>

		<nav>
			<g:render template='/templates/frtopnavigation'/>
		</nav>

		<section>
			<section>
				<g:layoutBody/>
			</section>
		</section>
		
		<footer>
			Federation Registry <strong>version <g:meta name="app.version"/></strong>
			<br>
			Developed for the <a href="http://www.aaf.edu.au">Australian Access Federation</a> by <a href="http://bradleybeddoes.com">Bradley Beddoes</a>
			<br>
			Powered by Grails <g:meta name="app.grails.version"/>
		</footer>
		
		<r:layoutResources/>
	</body>

</html>