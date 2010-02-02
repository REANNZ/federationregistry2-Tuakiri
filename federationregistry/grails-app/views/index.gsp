<html>
	<head>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	    <meta name="layout" content="login" />
	    <title><g:message code="fedreg.view.index.title"/></title>
	</head>

	<body>
		<div class="logincontent">
			<h2>Purpose</h2>

			<p>The Federation Registry collects information about organisations, partners, administrators, Identity Providers and Services Providers who participate in the federation which it manages. This information is then used to securely generate Metadata and other reports required by administrators, Identity Providers and Service Providers to ensure a viable and active federation is provided for end users.</p>
	
			<h2>Login</h2>
			
			<p><g:link controller="idpAttributeCompliance" action="summary">Login to the Federation Registry</g:link></p>
			
			<h3>Required Attributes</h3>

			<p>In order to get access to the Federation Registry, you need an account provided by an Identity Provider that is active within the federation. The Identity Provider must be able to release the following attributes:</p>

			<ul>
				<li>Given Name</li>
				<li>Surname</li>
				<li>EMail Address - to be able to send you notifications.</li>
				<li>Unique Identifier - to recognise you as known user the next time you connect</li>
				<li>Home Organisation Name</li>
				<li>Home Organisation Type</li>
				<li>Phone number (optional) - to let a Resource Registration Authority administrator call you in order to verify requests</li>
				<li>Affiliation (optional) - to grant access to certain functions only for staff members</li>
			</ul>
		</div>
	</body>
</html>