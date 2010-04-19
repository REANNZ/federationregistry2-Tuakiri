<html>
<head>
<title>Process Manager for ${authenticatedUser.profile.fullName}</title>
<meta name="layout" content="workflow" />
</head>

<body>
	
	<h2>Process Manager for ${authenticatedUser.profile.fullName}</h2>
	
	<g:include controller="workflowManager" action="myTasks" />
	
	<g:include controller="workflowManager" action="myProcesses" />

</body>
</html>
