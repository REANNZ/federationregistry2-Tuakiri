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
										<g:message code="fedreg.label.yes" class="icon icon_tick"/>
									</g:if>
									<g:else>
										<g:message code="fedreg.label.no" class="icon icon_cross"/>
									</g:else>
								</td>
							</tr>
						</tr>
						<g:if test="${contact.secondaryEmailAddresses}">
						<tr>
							<th><g:message code="fedreg.label.secondaryemail" /></th>
							<td>
							<g:each in="${contact.secondaryEmailAddresses}" status="i" var="email">
								<a href="mailto:${fieldValue(bean: email, field: "uri")}" class="icon icon_email">${fieldValue(bean: email, field: "uri")}</a><br/>
							</g:each>
							</td>
						</tr>
						</g:if>
						<g:if test="${contact.telephoneNumbers}">
						<tr>
							<th><g:message code="fedreg.label.phonenumbers" /></th>
							<td>
							<g:each in="${contact.telephoneNumbers}" status="i" var="phone">
								${fieldValue(bean: phone, field: "uri")} ( ${fieldValue(bean: phone, field: "description") })<br/>
							</g:each>
							</td>
						</tr>
						<tr>
						</tr>
						</g:if>
					</tbody>
				</table>
			</div>
		</div>
		
	</body>
</html>