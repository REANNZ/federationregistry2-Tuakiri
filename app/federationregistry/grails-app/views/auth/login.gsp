<html>
	<head>
	  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	  <meta name="layout" content="public" />
	</head>
	
	<body>
	  	<div class="success" style="margin-bottom: 36px; text-align: center; padding: 12px; font-size: 1.1em; font-weight: bold;">This page is for development and demonstration purposes only and not provided in production</div>
		<div style="text-align: center;"><h2>Select login account</h2></div>
		<table >
			<tbody>
				<tr>
					<td style="text-align: center;">
						<h3>Fred Bloggs</h3>
						<p>UniqueID: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-1</p>
						<p><strong><em>Federation Registry wide administrator</em></strong></p>
					  	<g:form action="devauth" method="post">
							<g:hiddenField name="uniqueID" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-1" />
							<g:hiddenField name="givenName" value="Fred" />
							<g:hiddenField name="surname" value="Bloggs" />
							<g:hiddenField name="email" value="fredbloggs@one.edu.au" />
							<g:hiddenField name="entityID" value="https://idp.one.edu.au/idp/shibboleth" />
							<g:hiddenField name="homeOrganization" value="one.edu.au" />
							<g:hiddenField name="homeOrganizationType" value="university:australia" />
							<g:submitButton name="Login" class="save-button"/>
						</g:form>
					</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp; <strong>OR</strong> &nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td style="text-align: center;">
						<h3>Joe Schmoe</h3>
						<p>UniqueID: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-2</p>
						<p><strong><em>Administrator of University Two SP</em></strong></p>
					  	<g:form action="devauth" method="post">
							<g:hiddenField name="uniqueID" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-2" />
							<g:hiddenField name="givenName" value="Joe" />
							<g:hiddenField name="surname" value="Schmoe" />
							<g:hiddenField name="email" value="joeschmoe@one.edu.au" />
							<g:hiddenField name="entityID" value="https://idp.one.edu.au/idp/shibboleth" />
							<g:hiddenField name="homeOrganization" value="one.edu.au" />
							<g:hiddenField name="homeOrganizationType" value="university:australia" />
							<g:submitButton name="Login" class="save-button"/>
						</g:form>
					</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp; <strong>OR</strong> &nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td style="text-align: center;">
						<h3>Max Mustermann</h3>
						<p>UniqueID: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-3</em></strong></p>
						<p><strong><em>Generic User</p>
					  	<g:form action="devauth" method="post">
							<g:hiddenField name="uniqueID" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-4" />
							<g:hiddenField name="displayName" value="Max Mustermann" />
							<g:hiddenField name="email" value="maxmustermann@one.edu.au" />
							<g:hiddenField name="entityID" value="https://idp.one.edu.au/idp/shibboleth" />
							<g:hiddenField name="homeOrganization" value="one.edu.au" />
							<g:hiddenField name="homeOrganizationType" value="university:australia" />
							<g:submitButton name="Login" class="save-button"/>
						</g:form>
					</td>
				</tr>
			</tbody>
		</table>

	</body>
</html>