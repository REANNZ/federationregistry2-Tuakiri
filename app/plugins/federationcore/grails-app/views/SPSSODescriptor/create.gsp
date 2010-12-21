
<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.serviceprovider.create.title" /></title>
	</head>
	<body>

		<section>
			<h2><g:message code="fedreg.view.members.serviceprovider.create.heading" /></h2>
			
			<g:render template="/templates/spssodescriptor/create" plugin="federationcore" model="[saveAction:'save', requiresContactDetails:false]"/>
			
		</section>

	</body>
</html>
