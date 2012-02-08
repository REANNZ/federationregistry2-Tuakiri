<!DOCTYPE html>

<html>
	<head>
		<title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
		<r:require modules="jquery, bootstrap, app"/>
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
		
		<r:script>
		  if (typeof(Zenbox) !== "undefined") {
		    Zenbox.init({
		      dropboxID:   "6875",
		      url:         "australianaccessfederation.zendesk.com",
		      tabID:       "support",
		      hide_tab:	   true
		    });
		  }
		</r:script>
		<r:layoutResources/>
	</body>

</html>