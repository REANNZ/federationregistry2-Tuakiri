<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.contacts.edit.title" /></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.members.contacts.edit.heading" args="[contact.givenName, contact.surname]"/></h2>
			
			<n:errors bean="${contact}"/>
			
			<g:form action="update" id="${contact.id}">
				<table>
					<tbody>		
						<tr>
							<th><g:message code="label.givenname" /></th>
							<td><input type="text" name="givenname" value="${fieldValue(bean: contact, field: 'givenName')}" /></td>
						</tr>
						<tr>
							<tr>
								<th><g:message code="label.surname" /></th>
								<td><input type="text" name="surname" value="${fieldValue(bean: contact, field: 'surname')}" /></td>
							</tr>
						</tr>
						<tr>
							<tr>
								<th><g:message code="label.email" /></th>
								<td><input type="text" name="email" value="${fieldValue(bean: contact, field: 'email.uri')}" /></td>
							</tr>
						</tr>
						<tr>
							<th><g:message code="label.secondaryemail" /></th>
							<td>
								<input type="text" name="secondaryEmail" value="${fieldValue(bean: contact, field: 'secondaryEmail.uri')}" />
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
				
				<div class="buttons">
					<n:button onclick="\$('form').submit();" label="label.update" icon="plus"/>
					<n:button href="${createLink(action:'show', id:contact.id)}"  label="label.cancel" icon="cancel"/>
				</div>
					
			</g:form>
		</section>
	</body>
</html>