<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.admins.title" /></title>
		<script type="text/javascript">
			<njs:admin />
		</script>
	</head>

	<body>

		<h2><g:message code="nimble.view.admins.heading" /></h2>
		<p>
			<g:message code="nimble.view.admins.descriptive" />
		</p>

		<div id="admins">
		</div>

		<h3><g:message code="nimble.view.admins.addadmin.heading" /></h3>
		<p>
			<g:message code="nimble.view.admins.addadmin.descriptive" />
		</p>

		<g:textField name="q" size="30"/>
		<n:button onclick="nimble.searchAdministrators();" label="label.search" icon="search"/>

		<div id="searchresponse" class="clear">
		</div>

	</body>
</html>