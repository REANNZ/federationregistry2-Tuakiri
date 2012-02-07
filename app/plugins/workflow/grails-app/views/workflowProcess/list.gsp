
<html>
	<head>
		
		<meta name="layout" content="workflow" />
		<title><g:message code="fedreg.view.workflow.process.list.title" /></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.workflow.process.list.heading" /></h2>
		
			<table>
				<thead>
					<tr>
						<g:sortableColumn property="name" title="${message(code: 'label.name')}" />
						<g:sortableColumn property="description" title="${message(code: 'label.description')}" />
						<th/>
						<th/>
					</tr>
				</thead>
				<tbody>
				<g:each in="${processList.sort{it.name}}" var="p" status="i">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${fieldValue(bean: p, field: "name")}</td>
						<td>${fieldValue(bean: p, field: "description")}</td>
						<td>
							<n:button href="${createLink(controller:'workflowProcess', action:'show', id: p.id)}" label="${message(code:'label.view')}" class="view-button"/>
						</td>
					</tr>
				</g:each>
				</tbody>
			</table>
		</section>
	</body>
</html>