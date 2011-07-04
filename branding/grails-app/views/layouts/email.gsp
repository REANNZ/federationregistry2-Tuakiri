<!DOCTYPE html>

<html>
	<head>
		<title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /></title>
	</head>

	<body style="padding:0; margin:0; font-family: Helvetica, Arial, sans-serif; font-size: 1.0em;">
		<div style="padding: 6px; height: 70px; background-color: #495666; color: #ffffff">
			<img style="float:left; padding-right: 6px;" src="${resource(absolute: true, dir:'images', file:'logo.jpg', plugin:'federationregistry')}" border="0" />
			<h1 style="font-size: 1.3em;">&nbsp;&nbsp;<g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /></h1>
		</div>

		<div style="padding: 12px;">
				<g:layoutBody/>
		</div>
	</body>

</html>