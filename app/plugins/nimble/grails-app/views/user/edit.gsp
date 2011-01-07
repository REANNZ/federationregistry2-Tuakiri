<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.user.edit.title" /></title>
		<script type="text/javascript">
			<njs:user user="${user}"/>
		</script>
	</head>

	<body>

		<h2><g:message code="nimble.view.user.edit.heading" args="[user.username]" /></h2>

		<p>
			<g:message code="nimble.view.user.edit.descriptive" />
		</p>

		<n:errors bean="${user}"/>

		<g:form action="update" class="editaccount">
			<input type="hidden" name="id" value="${user.id}"/>
			<input type="hidden" name="version" value="${user.version}"/>

			<table>
				<tbody>
					<tr>
						<th><label for="username"><g:message code="label.username" /></label></th>
						<td>
							<input type="text" id="username" name="username" value="${user.username?.encodeAsHTML()}" size="40"/>  <span class="icon icon_bullet_green">&nbsp;</span>
						</td>
					</tr>
					<tr>
						<th><g:message code="label.externalaccount" /></th>
						<td>
							<g:if test="${user.external}">
								<input type="radio" name="external" value="true" checked="true"/><g:message code="label.true" />
								<input type="radio" name="external" value="false"/><g:message code="label.false" />
							</g:if>
							<g:else>
								<input type="radio" name="external" value="true"/><g:message code="label.true" />
								<input type="radio" name="external" value="false" checked="true"/><g:message code="label.false" />
							</g:else>
						</td>
					</tr>
					<tr>
						<th><g:message code="label.federatedaccount" /></th>
						<td>
							<g:if test="${user.federated}">
								<input type="radio" name="federated" value="true" checked="true"/><g:message code="label.true" />
								<input type="radio" name="federated" value="false"/><g:message code="label.false" />
							</g:if>
							<g:else>
								<input type="radio" name="federated" value="true"/><g:message code="label.true" />
								<input type="radio" name="federated" value="false" checked="true"/><g:message code="label.false" />
							</g:else>
						</td>
					</tr>
				</tbody>
			</table>
			
			<div class="buttons">
				<n:button onclick="\$('form').submit();" label="label.update" class="add-button"/>
				<n:button href="${createLink(action:'show', id: user.id)}" label="label.cancel" class="close-button"/>
			</div>
			
		</g:form>

	</body>
</html>