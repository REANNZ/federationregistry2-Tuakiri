<html>
<head>
<title>Process Manager for ${authenticatedUser.profile.fullName}</title>
<meta name="layout" content="workflow" />
</head>

<body>
	<h2><g:message code="fedreg.workflow.views.manager.heading" args="[authenticatedUser.profile.fullName]"/></h2>
	
	<g:include controller="workflowManager" action="tasks" />
	
	<g:include controller="workflowManager" action="processes" />

</body>
</html>
