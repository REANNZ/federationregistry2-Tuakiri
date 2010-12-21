<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.user.changelocalpassword.title" /></title>
		<script type="text/javascript">
			<njs:user user="${user}"/>
		</script>
	</head>

	<body>

		<h2><g:message code="nimble.view.user.changelocalpassword.heading" args="[user.username]" /></h2>

		<p>
			<g:message code="nimble.view.user.changelocalpassword.descriptive" />
		</p>

		<n:errors bean="${user}"/>

		<g:form action="savepassword" class="passwordchange">
			<g:hiddenField name="id" value="${user.id.encodeAsHTML()}"/>
			<table>
			 <tbody>
					<tr>
						<th><g:message code="label.password" /></th>
						<td>
							<input type="password" id="pass" name="pass" class="password easyinput"/>
						</td>
					</tr>
					<tr>
						<th><g:message code="label.password.confirmation" /></th>
						<td>
							<input type="password" id="passConfirm" name="passConfirm" class="easyinput"/>
						</td>
					</tr>
				</tbody>
			</table>
			
			<div class="buttons">
				<n:button onclick="\$('form').submit();" label="label.confirm" icon="check"/>
				<n:button href="${createLink(action:'show', id: user.id)}" label="label.cancel" icon="cancel"/>
			</div>
			
		</g:form>

	</body>
</html>