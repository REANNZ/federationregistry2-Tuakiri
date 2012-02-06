<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.subject.edit.title" /></title>
		<r:script>
			<njs:subject subject="${subject}"/>
		</r:script>
	</head>

	<body>

		<h2><g:message code="nimble.view.subject.edit.heading" args="[subject.subjectname]" /></h2>

		<p>
			<g:message code="nimble.view.subject.edit.descriptive" />
		</p>

		<n:errors bean="${subject}"/>

		<g:form action="update" class="editaccount">
			<input type="hidden"
 name="id" value="${subject.id}"/>
			<input type="hidden"
 name="version" value="${subject.version}"/>

			<table>
				<tbody>
					<tr>
						<th><label for="subjectname"><g:message code="label.subjectname" /></label></th>
						<td>
							<input type="text" id="subjectname" name="subjectname" value="${subject.subjectname?.encodeAsHTML()}" size="40"/>  <span class="icon icon_bullet_green">&nbsp;</span>
						</td>
					</tr>
					<tr>
						<th><g:message code="label.externalaccount" /></th>
						<td>
							<g:if test="${subject.external}">
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
							<g:if test="${subject.federated}">
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
				<n:button href="${createLink(action:'show', id: subject.id)}" label="label.cancel" class="close-button"/>
			</div>
			
		</g:form>

	</body>
</html>