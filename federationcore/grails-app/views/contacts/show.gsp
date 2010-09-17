<html>
	<head>
		
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.contacts.show.title" /></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.members.contacts.show.heading" args="[contact.givenName, contact.surname]"/></h2>
		
			<table>
				<tbody>	
					<g:if test="${contact.organization}">
						<tr>
							<th><g:message code="label.organization" /></th>
							<td><g:link controller="organization" action="show" id="${contact.organization.id}">${fieldValue(bean: contact, field: "organization.displayName")}</g:link></td>
						</tr>	
					</g:if>
					<tr>
						<th><g:message code="label.givenname" /></th>
						<td>${fieldValue(bean: contact, field: "givenName")}</td>
					</tr>
					<tr>
						<tr>
							<th><g:message code="label.surname" /></th>
							<td>${fieldValue(bean: contact, field: "surname")}</td>
						</tr>
					</tr>
					<tr>
						<tr>
							<th><g:message code="label.email" /></th>
							<td><a href="mailto:${fieldValue(bean: contact, field: "email.uri")}">${fieldValue(bean: contact, field: "email.uri")}</a></td>
						</tr>
					</tr>
					<tr>
						<tr>
							<th><g:message code="label.linkedaccount" /></th>
							<g:if test="${contact.userLink}">
								<td><n:button href="${createLink(controller:'user', action:'show', id:contact.userID)}" label="label.yes" plain="true"/><td>
							</g:if>
							<g:else>
								<td><g:message code="label.no" /></td>
							</g:else>
						</tr>
					</tr>
					<g:if test="${contact.secondaryEmail}">
						<tr>
							<th><g:message code="label.secondaryemail" /></th>
							<td><a href="mailto:${fieldValue(bean: contact, field: "secondaryEmail.uri")}">${fieldValue(bean: contact, field: "secondaryEmail.uri")}</a></td>
						</tr>
					</g:if>
					<tr>
						<th><g:message code="label.workphone" /></th>
						<td>${fieldValue(bean: contact, field: "workPhone.uri")}</td>
					</tr>
					<tr>
						<th><g:message code="label.mobilephone" /></th>
						<td>${fieldValue(bean: contact, field: "mobilePhone.uri")}</td>
					</tr>
					<tr>
						<th><g:message code="label.homephone" /></th>
						<td>${fieldValue(bean: contact, field: "homePhone.uri")}</td>
					</tr>
					<tr>
						<th><g:message code="label.description" /></th>
						<td>${fieldValue(bean: contact, field: "description")}</td>
					</tr>
				</tbody>
			</table>

		</section>
		
	</body>
</html>