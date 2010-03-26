<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.contacts.show.title" /></title>
	</head>
	<body>
		<h2><g:message code="fedreg.view.members.contacts.show.heading" args="[contact.givenName, contact.surname]"/></h2>
		
		<div id="contact">
			<div class="details">
				<table class="datatable buttons">
					<tbody>		
						<tr>
							<th><g:message code="fedreg.label.givenname" /></th>
							<td>${fieldValue(bean: contact, field: "givenName")}</td>
						</tr>
						<tr>
							<tr>
								<th><g:message code="fedreg.label.surname" /></th>
								<td>${fieldValue(bean: contact, field: "surname")}</td>
							</tr>
						</tr>
						<tr>
							<tr>
								<th><g:message code="fedreg.label.email" /></th>
								<td><a href="mailto:${fieldValue(bean: contact, field: "email.uri")}" class="icon icon_email">${fieldValue(bean: contact, field: "email.uri")}</a></td>
							</tr>
						</tr>
						<tr>
							<tr>
								<th><g:message code="fedreg.label.linkedaccount" /></th>
								<td>
									<g:if test="${contact.userLink}">
										<span class="icon icon_tick"><g:message code="fedreg.label.yes"/></span>
									</g:if>
									<g:else>
										<span class="icon icon_cross"><g:message code="fedreg.label.no" /></span>
									</g:else>
								</td>
							</tr>
						</tr>
						<g:if test="${contact.secondaryEmail}">
						<tr>
							<th><g:message code="fedreg.label.secondaryemail" /></th>
							<td><a href="mailto:${fieldValue(bean: contact, field: "secondaryEmail.uri")}" class="icon icon_email">${fieldValue(bean: contact, field: "secondaryEmail.uri")}</a></td>
						</tr>
						</g:if>
						<tr>
							<th><g:message code="fedreg.label.workphone" /></th>
							<td>${fieldValue(bean: contact, field: "workPhone.uri")}</td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.mobilephone" /></th>
							<td>${fieldValue(bean: contact, field: "mobilePhone.uri")}</td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.homephone" /></th>
							<td>${fieldValue(bean: contact, field: "homePhone.uri")}</td>
						</tr>
						<tr>
							<th><g:message code="fedreg.label.description" /></th>
							<td>${fieldValue(bean: contact, field: "description")}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		
	</body>
</html>