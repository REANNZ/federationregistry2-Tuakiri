
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="workflow" />
		<title><g:message code="fedreg.view.workflow.process.show.title" /></title>
	</head>
	
	<body>
		
		<h2><g:message code="fedreg.view.workflow.process.show.heading" /></h2>
		<div class="details">
			<table class="datatable buttons">
				<tbody>		
					<tr>
						<th><g:message code="fedreg.label.name" /></th>
						<td>${fieldValue(bean: process, field: "name")}</td>
					</tr>
					<tr>
						<th><g:message code="fedreg.label.description" /></th>
						<td>${fieldValue(bean: process, field: "description")}</td>
					</tr>
					<tr>
						<th><g:message code="fedreg.label.active" /></th>
						<td>${fieldValue(bean: process, field: "active")}</td>
					</tr>
					<tr>
						<th><g:message code="fedreg.label.version" /></th>
						<td>${fieldValue(bean: process, field: "processVersion")}</td>
					</tr>
					<tr>
						<th><g:message code="fedreg.label.created" /></th>
						<td>${fieldValue(bean: process, field: "dateCreated")}</td>
					</tr>
					<tr>
						<th><g:message code="fedreg.label.lastupdated" /></th>
						<td>${fieldValue(bean: process, field: "lastUpdated")}</td>
					</tr>
					<tr>
						<th><g:message code="fedreg.label.creator" /></th>
						<td><g:link controller="user" action="show" id="${process.creator.id}">${process.creator.profile?.fullName ?: process.creator.username}</g:link></td>
					</tr>
					<tr>
						<th><g:message code="fedreg.label.definition" /></th>
						<td></td>
					</tr>
				</tbody>
			</table>
			<pre>${fieldValue(bean: process, field: "definition")}</pre>
		</div>
		
	</body>
	
</html>