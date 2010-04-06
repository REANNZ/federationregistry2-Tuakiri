<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.contacts.create.title" /></title>
	</head>
	<body>
		<h2><g:message code="fedreg.view.members.contacts.create.heading" /></h2>
		
		<div id="contact">
			
			<n:errors bean="${contact}"/>
			
			<div>
				<g:form action="save">
				<table class="datatable buttons">
					<tbody>		
						<tr>
							<th><g:message code="fedreg.label.givenname" /></th>
							<td><input type="text" name="givenname" value="${fieldValue(bean: contact, field: 'givenName')}" /></td>
						</tr>
						<tr>
							<tr>
								<th><g:message code="fedreg.label.surname" /></th>
								<td><input type="text" name="surname" value="${fieldValue(bean: contact, field: 'surname')}" /></td>
							</tr>
						</tr>
						<tr>
							<tr>
								<th><g:message code="fedreg.label.email" /></th>
								<td><input type="text" name="email" value="${fieldValue(bean: contact, field: 'email.uri')}" /></td>
							</tr>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.secondaryemail" /></th>
							<td>
								<input type="text" name="secondaryEmail" value="${fieldValue(bean: contact, field: 'secondaryEmail.uri')}" />
							</td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.workphone" /></th>
							<td>
								<input type="text" name="workPhone" value="${fieldValue(bean: contact, field: 'workPhone.uri')}" />
							</td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.mobilephone" /></th>
							<td>
								<input type="text" name="mobilePhone" value="${fieldValue(bean: contact, field: 'mobilePhone.uri')}" />
							</td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.homephone" /></th>
							<td>
								<input type="text" name="homePhone" value="${fieldValue(bean: contact, field: 'homePhone.uri')}" />
							</td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.description" /></th>
							<td>
								<textarea rows="2" cols="20" name="description">${fieldValue(bean: contact, field: 'description')}</textarea>
							</td>
						</tr>
						<tr>
							<td colspan="2"><button class="button icon icon_user_add" type="submit"><g:message code="fedreg.link.create" /></button> <g:link action="list"  class="button icon icon_cancel"><g:message code="fedreg.link.cancel" /></g:link></td>
					</tbody>
				</table>
					
				</g:form>
			</div>
		</div>
		
	</body>
</html>