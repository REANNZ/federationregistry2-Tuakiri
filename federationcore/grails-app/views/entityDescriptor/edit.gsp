<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.entity.edit.title" /></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.members.entity.edit.heading" args="[entity.entityID]"/></h2>
			
			<n:errors bean="${entity}" />

			<g:form action="update" id="${entity.id}">
				<table>
					
						<tr>
							<td>
								<label for="entity.identifier"><g:message code="label.entitydescriptor" /></label>
							</td>
							<td>
								<g:textField name="entity.identifier" value="${entity.entityID}" size="50" class="required url"/>
							</td>
						</tr>
					</tbody>
				</table>
				
				<nav>
					<n:button onclick="\$('form').submit();" label="label.update" icon="plus"/>
					<n:button href="${createLink(action:'show', id:entity.id)}"  label="label.cancel" icon="cancel"/>
				</nav>
				
			</g:form>

		</section>
	</body>
</html>