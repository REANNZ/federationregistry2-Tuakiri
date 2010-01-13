<html>
	<head>
	  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	</head>
	
	<body>
	  	<h2>This page is for development purposes only and should not be available in production</h2>
	  	<g:form action="devauth" method="post">
			<g:hiddenField name="uniqueID" value="https://idp.qut.edu.au/idp/shibboleth!https://manager.aaf.edu.au/shibboleth!d2404817-6fb9-4165-90d8-1" />
			<g:hiddenField name="givenName" value="Joe" />
			<g:hiddenField name="surname" value="Bloggs" />
			<g:hiddenField name="email" value="joe@bloggs.com" />
			<g:submitButton name="login as id 1" />
		</g:form>

	</body>
</html>