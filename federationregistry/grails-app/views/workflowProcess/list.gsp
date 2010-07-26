
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="workflow" />
		<title><g:message code="fedreg.view.workflow.process.list.title" /></title>
	</head>
	<body>
		<h2><g:message code="fedreg.view.workflow.process.list.heading" /></h2>
		
		<table class="enhancedtabledata">
			<thead>
				<tr>
					<g:sortableColumn property="name" title="${message(code: 'label.name')}" />
					<g:sortableColumn property="description" title="${message(code: 'label.description')}" />
					<th/>
					<th/>
				</tr>
			</thead>
			<tbody>
			<g:each in="${processList}" var="p" status="i">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td>${fieldValue(bean: p, field: "name")}</td>
					<td>${fieldValue(bean: p, field: "description")}</td>
					<td><g:link action="show" id="${p.id}" class="button icon icon_magnifier"><g:message code="label.view" /></g:link></td>
				</tr>
			</g:each>
			</tbody>
		</table>
	</body>
</html>