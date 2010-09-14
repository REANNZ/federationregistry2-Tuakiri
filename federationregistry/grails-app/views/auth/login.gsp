<html>
	<head>
	  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	  <meta name="layout" content="public" />
	</head>
	
	<body>
	  	<h2>This page is for development purposes only and not available in production</h2>
		<table>
			<tbody>
				<tr>
					<td>
						<h3>User 1</h3>
						<p>UniqueID: https://idp.qut.edu.au/idp/shibboleth! https://manager.aaf.edu.au/shibboleth! d2404817-6fb9-4165-90d8-1</p>
					  	<g:form action="devauth" method="post">
							<g:hiddenField name="uniqueID" value="https://idp.qut.edu.au/idp/shibboleth!https://manager.aaf.edu.au/shibboleth!d2404817-6fb9-4165-90d8-1" />
							<g:hiddenField name="givenName" value="Mega" />
							<g:hiddenField name="surname" value="Tron" />
							<g:hiddenField name="email" value="megatron@decepticons.com" />
							<g:hiddenField name="entityID" value="https://auth-test-idp.qut.edu.au/idp/shibboleth" />
							<g:hiddenField name="homeOrganization" value="qut.edu.au" />
							<g:hiddenField name="homeOrganizationType" value="university:australia" />
							<g:submitButton name="Login" />
						</g:form>
					</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp; <strong>or</strong> &nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>
						<h3>User 2</h3>
						<p>UniqueID: https://idp.qut.edu.au/idp/shibboleth! https://manager.aaf.edu.au/shibboleth! d2404817-6fb9-4165-90d8-2</p>
					  	<g:form action="devauth" method="post">
							<g:hiddenField name="uniqueID" value="https://idp.qut.edu.au/idp/shibboleth!https://manager.aaf.edu.au/shibboleth!d2404817-6fb9-4165-90d8-2" />
							<g:hiddenField name="givenName" value="Optimus" />
							<g:hiddenField name="surname" value="Prime" />
							<g:hiddenField name="email" value="optimus.prime@autobots.com" />
							<g:hiddenField name="entityID" value="https://auth-test-idp.qut.edu.au/idp/shibboleth" />
							<g:hiddenField name="homeOrganization" value="qut.edu.au" />
							<g:hiddenField name="homeOrganizationType" value="university:australia" />
							<g:submitButton name="Login" />
						</g:form>
					</td>
				</tr>
			</tbody>
		</table>

	</body>
</html>