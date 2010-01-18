<html>
	<head>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	    <meta name="layout" content="login" />
	    <title><g:message code="fedreg.view.index.title"/></title>
	</head>

	<body>
		
		<h2>Purpose</h2>

		<p>The Resource Registry collects information about Resources and Home Organizations which participate in the AAF Federation. This information is then used to generate the metadata and other files required by all participating Identity Providers and Service Providers.</p>

		<h2>Intended Audience</h2>
		<ul>
			<li>Resource Administrators</li>
			<li>Home Organization Administrators</li>
			<li>Resource Registration Authority Administrators</li>
		</ul>
	
		<h2>Required Attributes</h2>

		<p>In order to get access to the Resource Registry, you need an account in one of the above listed federations. The Identity Provider must be able to release the following attributes for a user</p>

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
	
	</body>
</html>