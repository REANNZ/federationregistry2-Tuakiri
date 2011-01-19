<!DOCTYPE html>

<html>
	<head>
		<title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
		<r:use modules="html5, jquery-ui, tiptip, jgrowl, datatables, validate, zenbox, alphanumeric, formwizard, zenbox, app"/>
		<r:layoutResources/>
		<g:layoutHead />
	</head>

	<body>
		<header>
			<g:render template='/templates/frheader' />
		</header>

		<nav>
			<ul class="level1">
				<li class="${controllerName == 'initialBootstrap' ? 'directactive':''}">
					<g:link controller="initialBootstrap"><g:message code="fedreg.navigation.bootstrap" /></g:link>
				</li>
			</ul>
		</nav>

		<section>
			<section>
				<g:layoutBody/>
			</section>
		</section>
		
		<footer>
			<g:render template='/templates/frfooter' />
		</footer>
		
	</body>

</html>