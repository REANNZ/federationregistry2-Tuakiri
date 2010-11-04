<!DOCTYPE html>

<html>
	<head>
		<title><g:message code="fedreg.title" /> | <g:layoutTitle /></title>

		<!--[if lt IE 9]>
			<script type="text/javascript" src="${resource(dir: 'js', file: '/html5.js')}"></script>
		<![endif]-->
		<script type="text/javascript" src="${resource(dir: 'js', file: '/modernizr-1.5.min.js')}"></script>
		
		<link rel="stylesheet" href="${resource(dir:'css',file:'jquery-ui-1.8.2.custom.css')}" />
		<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.jgrowl.css')}" />
		<link rel="stylesheet/less" href="${resource(dir:'css',file:'frtheme.less')}" />

		<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery-1.4.2.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery-ui-1.8.2.custom.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: 'less-1.0.35.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.jgrowl.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.validate.pack.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.validate.additional-methods.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.form.wizard-2.0.1-min.js')}"></script>

		<script type="text/javascript" src="${resource(dir: 'js', file: '/fedreg-members.js')}"></script>
		<nh:nimbleui/>

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
	</body>

</html>