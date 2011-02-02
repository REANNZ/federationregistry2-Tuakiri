
<html>
	<head>
		
		<meta name="layout" content="workflow" />
		<title><g:message code="fedreg.view.workflow.process.show.title" /></title>
	</head>
	
	<body>
		<section>
			<h2><g:message code="fedreg.view.workflow.process.show.heading" args="[process.name]"/></h2>

			<table>
				<tbody>		
					<tr>
						<th><g:message code="label.description" /></th>
						<td>${fieldValue(bean: process, field: "description")}</td>
					</tr>
					<tr>
						<th><g:message code="label.active" /></th>
						<td>${fieldValue(bean: process, field: "active")}</td>
					</tr>
					<tr>
						<th><g:message code="label.version" /></th>
						<td>${fieldValue(bean: process, field: "processVersion")}</td>
					</tr>
					<tr>
						<th><g:message code="label.created" /></th>
						<td>${fieldValue(bean: process, field: "dateCreated")}</td>
					</tr>
					<tr>
						<th><g:message code="label.creator" /></th>
						<td><g:link controller="user" action="show" id="${process.creator.id}">${process.creator.profile?.fullName ?: process.creator.username}</g:link></td>
					</tr>
					<tr>
						<th><g:message code="label.definition" />:</th>
						<td></td>
					</tr>
				</tbody>
			</table>
			<pre style="padding: 24px;">${fieldValue(bean: process, field: "definition")}</pre>
		</section>
		
	</body>
</html>