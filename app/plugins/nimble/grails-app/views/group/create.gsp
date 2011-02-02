<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.group.create.title" /></title>
	</head>
	<body>

		<h2><g:message code="nimble.view.group.create.heading" /></h2>

		<p>
			<g:message code="nimble.view.group.create.descriptive" />
		</p>

		<n:errors bean="${group}"/>

		<g:form action="save">
			<table>
				<tbody>
					<tr>
						<th>
							<g:message code="label.name" />
						</th>
						<td>
							<g:textField name="name" value="${fieldValue(bean: group, field: 'name')}" size="30"/>
						</td>
					</tr>
					<tr>
						<th>
							<g:message code="label.description" />
						</th>
						<td>
							<g:textField name="description" value="${fieldValue(bean: group, field: 'description')}" size="30"/>
						</td>
					</tr>
				</tbody>
			</table>
			
			<div class="buttons">
				<n:button onclick="\$('form').submit();" label="label.add" class="add-button"/>
				<n:button href="${createLink(action:'list')}" label="label.cancel" class="close-button"/>
			</div>
		</g:form>

	</body>
</html>
