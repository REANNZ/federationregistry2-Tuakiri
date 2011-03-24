<html>
	<head>
		<r:script>
			$(function() {
				$('form').validate();
			});
		</r:script>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.contacts.create.title" /></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.members.contacts.create.heading" /></h2>
			
			<n:errors bean="${contact}"/>
			
			<g:form action="save">
				<table>
					<tbody>	
						<tr>
							<th><g:message code="label.organization" /></th>
							<td>
								<g:select name="organization"
								          from="${organizations}"
										  noSelection="${['null':'No Organization']}"
								          optionKey="id" optionValue="name"/>
							</td>
						</tr>	
						<tr>
							<th><g:message code="label.givenname" /></th>
							<td><input type="text" name="givenname" value="${fieldValue(bean: contact, field: 'givenName')}" class="required"/></td>
						</tr>
						<tr>
							<tr>
								<th><g:message code="label.surname" /></th>
								<td><input type="text" name="surname" value="${fieldValue(bean: contact, field: 'surname')}" class="required"/></td>
							</tr>
						</tr>
						<tr>
							<tr>
								<th><g:message code="label.email" /></th>
								<td><input type="text" name="email" value="${fieldValue(bean: contact, field: 'email.uri')}" class="required email"/></td>
							</tr>
						</tr>
						<tr>
							<th><g:message code="label.secondaryemail" /></th>
							<td>
								<input type="text" name="secondaryEmail" value="${fieldValue(bean: contact, field: 'secondaryEmail.uri')}" class="email"/>
							</td>
						</tr>
						<tr>
							<th><g:message code="label.workphone" /></th>
							<td>
								<input type="text" name="workPhone" value="${fieldValue(bean: contact, field: 'workPhone.uri')}" />
							</td>
						</tr>
						<tr>
							<th><g:message code="label.mobilephone" /></th>
							<td>
								<input type="text" name="mobilePhone" value="${fieldValue(bean: contact, field: 'mobilePhone.uri')}" />
							</td>
						</tr>
						<tr>
							<th><g:message code="label.homephone" /></th>
							<td>
								<input type="text" name="homePhone" value="${fieldValue(bean: contact, field: 'homePhone.uri')}" />
							</td>
						</tr>
						<tr>
							<th><g:message code="label.description" /></th>
							<td>
								<textarea rows="10" cols="40" name="description">${fieldValue(bean: contact, field: 'description')}</textarea>
							</td>
						</tr>
					</tbody>
				</table>
				
				<nav>
					<n:button onclick="\$('form').submit();" label="label.create" class="add-button"/>
					<n:button href="${createLink(action:'list')}"  label="label.cancel" class="close-button"/>
				</nav>
				
			</g:form>
		</section>	
	</body>
</html>