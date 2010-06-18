<%@ page contentType="text/html" %

<html>

<head>

</head>

<body>
	THIS SI THE BODY~
	<p>
		<g:message code="workflow.requestapproval.descriptive" />
	</p>

	<p>
  		<a href="${createLink(absolute:true, controller: 'workflow', action: 'approvetask', id: taskInstance.id}"><g:message code="workflow.requestapproval.link" /></a>
	</p>

	<p>
		<g:message code="workflow.requestapproval.copypaste.descriptive" />
	</p>
	<p>
		${createLink(absolute:true, controller: 'workflow', action: 'approvetask', id: taskInstance.id}
	</p>

</body>

</html>