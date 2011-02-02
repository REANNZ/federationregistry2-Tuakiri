<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.user.create.title" /></title>
	</head>

	<body>

		<h2><g:message code="nimble.view.user.create.heading" /></h2>

		<p>
			<g:message code="nimble.view.user.create.descriptive" />
		</p>

		<n:errors bean="${user}"/>

		<g:form action="save">
			<table>
				<tbody>
					<tr>
						<td><label for="username"><g:message code="label.username" /></label></td>
						<td>
							<g:textField name="username" size="30"/>
						</td>
					</tr>
					<tr>
						<td><label for="pass"><g:message code="label.password" /></label></td>
						<td>
							<input type="password" size="30" id="pass" name="pass" value="${user.pass?.encodeAsHTML()}" class="password"/>
						</td>
					</tr>
					<tr>
						<td><label for="passConfirm"><g:message code="label.password.confirmation" /></label></td>
						<td>
							<input type="password" size="30" id="passConfirm" name="passConfirm" value="${user.passConfirm?.encodeAsHTML()}"/>
						</td>
					</tr>
					<tr>
						<td><label for="fullName"><g:message code="label.fullname" /></label></td>
						<td>
							<input type="text" size="30" id="fullName" name="fullName" value="${user.profile?.fullName?.encodeAsHTML()}" class="easyinput"/>
						</td>
					</tr>
					<tr>
						<td><label for="email"><g:message code="label.email" /></label></td>
						<td>
							<input type="text" size="30" id="email" name="email" value="${user.profile?.email?.encodeAsHTML()}" class="easyinput"/> <span class="icon icon_bullet_green">&nbsp;</span>
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