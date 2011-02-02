<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.group.edit.title" /></title>
	</head>

	<body>

		<h2><g:message code="nimble.view.group.edit.heading" args="[group.name.encodeAsHTML()]"/></h2>

		<p>
			<g:message code="nimble.view.group.edit.descriptive" />
		</p>

		<n:errors bean="${group}"/>

		<g:form method="post" action="update">
			<input type="hidden" name="id" value="${group?.id.encodeAsHTML()}"/>

			<table class="datatable">
				<tbody>
					<tr>
						<th>
							<label for="name"><g:message code="label.name" /></label>
						</th>
						<td>
							<input type="text" id="name" name="name" value="${fieldValue(bean: group, field: 'name')}" class="easyinput"/>
						</td>
					</tr>
					<tr>
						<th>
							<label for="description"><g:message code="label.description" /></label>
						</th>
						<td>
							<g:textField name="description" value="${fieldValue(bean: group, field: 'description')}"/> 
						</td>
					</tr>
				</tbody>
			</table>
			
			<div class="buttons">
				<n:button onclick="\$('form').submit();" label="label.update" class="update-button"/>
				<n:button href="${createLink(action:'show', id: group.id)}" label="label.cancel" class="close-button"/>
			</div>
		</g:form>

	</body>
</html>
