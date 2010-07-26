
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="workflow" />
		<title><g:message code="fedreg.view.workflow.script.show.title" /></title>
	</head>
	
	<body>
		
		<h2><g:message code="fedreg.view.workflow.script.show.heading" args="[script.name]"/></h2>
		<div class="details">
			<table class="datatable buttons">
				<tbody>		
					<tr>
						<th><g:message code="label.description" /></th>
						<td>${fieldValue(bean: script, field: "description")}</td>
					</tr>
					<tr>
						<th><g:message code="label.created" /></th>
						<td>${fieldValue(bean: script, field: "dateCreated")}</td>
					</tr>
					<tr>
						<th><g:message code="label.lastupdated" /></th>
						<td>${fieldValue(bean: script, field: "lastUpdated")}</td>
					</tr>
					<tr>
						<th><g:message code="label.creator" /></th>
						<td><g:link controller="user" action="show" id="${script.creator.id}">${script.creator.profile?.fullName ?: script.creator.username}</g:link></td>
					</tr>
					<tr>
						<th><g:message code="label.definition" /></th>
						<td></td>
					</tr>
				</tbody>
			</table>
			<pre style="padding: 24px;">${fieldValue(bean: script, field: "definition")}</pre>
		</div>
		
	</body>
	
</html>