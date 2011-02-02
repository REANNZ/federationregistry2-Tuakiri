<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.role.edit.title" /></title>
	</head>
	
	<body>

		<h2><g:message code="nimble.view.role.edit.heading" args="[role.name.encodeAsHTML()]" /></h2>

		<p>
			<g:message code="nimble.view.role.edit.descriptive" />
		</p>

		<n:errors bean="${role}"/>

		<g:form method="post" name="editRole" action="update">
			<input type="hidden" name="id" value="${role?.id.encodeAsHTML()}"/>

			<table>
				<tbody>
					<tr>
						<td>
							<label for="name"><g:message code="label.name" /></label>
						</td>
						<td>
							<g:textField name="name" value="${fieldValue(bean: role, field: 'name')}" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="description"><g:message code="label.description" /></label>
						</td>
						<td>
							<g:textField name="description" value="${fieldValue(bean: role, field: 'description')}"/>
						</td>
					</tr>
				</tbody>
			</table>
			
			<div class="buttons">
				<n:button onclick="\$('form').submit();" label="label.update" class="update-button"/>
				<n:button href="${createLink(action:'show', id: role.id)}" label="label.cancel" class="close-button"/>
			</div>
		</g:form>
		
	</body>
</html>
