<html>
	<head>
	  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	  <meta name="layout" content="main" />
	</head>
	
	<body>
		<h2>Requested Attributes Incomplete</h2>
		<p>Unfortunately your identity provider has not supplied all required attributes to this service</p>
		<br><br>
		<div class="error">
			<ul>
				<g:each in="${errors}">
					<li><g:message code="${it}" /></li>
				</g:each>
			</ul>
		</div>
		<br><br>
		<h3>Request Details</h3>
		<g:include controller="auth" action="echo" />
	</body>
</html>