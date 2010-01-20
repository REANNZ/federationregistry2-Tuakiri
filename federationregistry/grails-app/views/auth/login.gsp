<html>
	<head>
	  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	  <meta name="layout" content="login" />
	</head>
	
	<body>
	  	<h2>This page is for development purposes only and should not be available in production</h2>
		<h3>CHOOSE YOUR HERO</h3>
		<table>
			<tbody>
				<tr>
					<td>
						<h3>Megatron</h3>
						<img src="http://upload.wikimedia.org/wikipedia/en/7/7f/Megatron.jpg" width="140px" height="140px">
						<p>UniqueID: https://idp.qut.edu.au/idp/shibboleth! https://manager.aaf.edu.au/shibboleth! d2404817-6fb9-4165-90d8-1</p>
					  	<g:form action="devauth" method="post">
							<g:hiddenField name="uniqueID" value="https://idp.qut.edu.au/idp/shibboleth!https://manager.aaf.edu.au/shibboleth!d2404817-6fb9-4165-90d8-1" />
							<g:hiddenField name="givenName" value="Mega" />
							<g:hiddenField name="surname" value="Tron" />
							<g:hiddenField name="email" value="megatron@decepticons.com" />
							<g:hiddenField name="entityID" value="https://idp.qut.edu.au/idp/shibboleth" />
							<g:submitButton name="Login" />
						</g:form>
					</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp; <strong>VS</strong> &nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>
						<h3>Optimus Prime</h3>
						<img src="http://upload.wikimedia.org/wikipedia/en/thumb/b/b2/Optimusprime-originaltoy.jpg/240px-Optimusprime-originaltoy.jpg"  width="140px" height="140px">
						<p>UniqueID: https://idp.qut.edu.au/idp/shibboleth! https://manager.aaf.edu.au/shibboleth! d2404817-6fb9-4165-90d8-2</p>
					  	<g:form action="devauth" method="post">
							<g:hiddenField name="uniqueID" value="https://idp.qut.edu.au/idp/shibboleth!https://manager.aaf.edu.au/shibboleth!d2404817-6fb9-4165-90d8-2" />
							<g:hiddenField name="givenName" value="Optimus" />
							<g:hiddenField name="surname" value="Prime" />
							<g:hiddenField name="email" value="optimus@autobots.com" />
							<g:hiddenField name="entityID" value="https://idp.qut.edu.au/idp/shibboleth" />
							<g:submitButton name="Login" />
						</g:form>
					</td>
				</tr>
			</tbody>
		</table>

	</body>
</html>