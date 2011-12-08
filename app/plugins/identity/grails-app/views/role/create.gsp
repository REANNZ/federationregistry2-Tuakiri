<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.role.create.title" /></title>
	</head>
	<body>

		<h2><g:message code="nimble.view.role.create.heading" /></h2>

		<p>
			<g:message code="nimble.view.role.create.descriptive" />
		</p>

		<n:errors bean="${role}"/>

		<g:form action="save" method="post">
			<table>
				<tbody>
					<tr>
						<th><g:message code="label.name" /></th>
						<td><g:textField name="name" size-"30" value="${fieldValue(bean: role, field: 'name')}"/></td>
					</tr>
					<tr>
						<th><g:message code="label.description" /></th>
						<td><g:textField name="description" size-"30" value="${fieldValue(bean: role, field: 'description')}"/></td>
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
