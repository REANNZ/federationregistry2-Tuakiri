<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.contacts.show.title" /></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.members.contacts.show.heading" args="[contact.givenName, contact.surname]"/></h2>
		
			<table>
				<tbody>		
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
							<td><a href="mailto:${fieldValue(bean: contact, field: "email.uri")}" class="icon icon_email">${fieldValue(bean: contact, field: "email.uri")}</a></td>
						</tr>
					</tr>
					<tr>
						<tr>
							<th><g:message code="label.linkedaccount" /></th>
							<td>
								<g:if test="${contact.userLink}">
									<span class="icon icon_tick"><g:message code="label.yes"/></span>
								</g:if>
								<g:else>
									<span class="icon icon_cross"><g:message code="label.no" /></span>
								</g:else>
							</td>
						</tr>
					</tr>
					<g:if test="${contact.secondaryEmail}">
						<tr>
							<th><g:message code="label.secondaryemail" /></th>
							<td><a href="mailto:${fieldValue(bean: contact, field: "secondaryEmail.uri")}" class="icon icon_email">${fieldValue(bean: contact, field: "secondaryEmail.uri")}</a></td>
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