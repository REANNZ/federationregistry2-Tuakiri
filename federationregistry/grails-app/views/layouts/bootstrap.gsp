<!DOCTYPE html>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
		<title><g:message code="fedreg.title" /> | <g:layoutTitle /></title>

		<link rel="stylesheet" href="${resource(dir:'css',file:'jquery-ui-1.8.2.custom.css')}" />
		<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.jgrowl.css')}" />
		<link rel="stylesheet/less" href="${resource(dir:'css',file:'aaftheme.less')}" />

		<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery-1.4.2.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery-ui-1.8.2.custom.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: 'less-1.0.32.min.js')}"></script>
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
			<g:render template='/templates/aafheader' />
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
	</body>

</html>