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
						<h3>Fred Bloggs</h3>
						<p>UniqueID: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-1</p>
						<p>Federation Registry wide administrator</p>
					  	<g:form action="devauth" method="post">
							<g:hiddenField name="uniqueID" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-1" />
							<g:hiddenField name="givenName" value="Fred" />
							<g:hiddenField name="surname" value="Bloggs" />
							<g:hiddenField name="email" value="fredbloggs@one.edu.au" />
							<g:hiddenField name="entityID" value="https://idp.one.edu.au/idp/shibboleth" />
							<g:hiddenField name="homeOrganization" value="one.edu.au" />
							<g:hiddenField name="homeOrganizationType" value="university:australia" />
							<g:submitButton name="Login" />
						</g:form>
					</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp; <strong>or</strong> &nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>
						<h3>Joe Schmoe</h3>
						<p>UniqueID: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-2</p>
						<p>Administrator of University Two SP</p>
					  	<g:form action="devauth" method="post">
							<g:hiddenField name="uniqueID" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-2" />
							<g:hiddenField name="givenName" value="Joe" />
							<g:hiddenField name="surname" value="Schmoe" />
							<g:hiddenField name="email" value="joeschmoe@one.edu.au" />
							<g:hiddenField name="entityID" value="https://idp.one.edu.au/idp/shibboleth" />
							<g:hiddenField name="homeOrganization" value="one.edu.au" />
							<g:hiddenField name="homeOrganizationType" value="university:australia" />
							<g:submitButton name="Login" />
						</g:form>
					</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp; <strong>or</strong> &nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>
						<h3>Max Mustermann</h3>
						<p>UniqueID: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-3</p>
						<p>Generic User</p>
					  	<g:form action="devauth" method="post">
							<g:hiddenField name="uniqueID" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-4" />
							<g:hiddenField name="displayName" value="Max Mustermann" />
							<g:hiddenField name="email" value="maxmustermann@one.edu.au" />
							<g:hiddenField name="entityID" value="https://idp.one.edu.au/idp/shibboleth" />
							<g:hiddenField name="homeOrganization" value="one.edu.au" />
							<g:hiddenField name="homeOrganizationType" value="university:australia" />
							<g:submitButton name="Login" />
						</g:form>
					</td>
				</tr>
			</tbody>
		</table>

	</body>
</html>