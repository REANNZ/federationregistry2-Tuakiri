
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.contacts.list.title" /></title>
	</head>
	<body>

		<div class="container">
			<h2><g:message code="fedreg.view.members.contacts.list.heading" /></h2>
			<div>
				<table class="enhancedtabledata buttons">
					<thead>
						<tr>
							<g:sortableColumn property="givenName" title="${message(code: 'fedreg.label.givenname')}" />
							<g:sortableColumn property="surname" title="${message(code: 'fedreg.label.surname')}" />
							<th><g:message code='fedreg.label.email' /></th>
							<th><g:message code='fedreg.label.organization' /></th>
							<th/>
						</tr>
					</thead>
					<tbody>
					<g:each in="${contactList}" status="i" var="contact">
						<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
							<td>${fieldValue(bean: contact, field: "givenName")}</td>
							<td>${fieldValue(bean: contact, field: "surname")}</td>
							<td>${fieldValue(bean: contact, field: "email.uri")}</td>
							<td>${fieldValue(bean: contact, field: "organization.displayName")}</td>
							<td><g:link action="show" id="${contact.id}" class="button icon icon_magnifier"><g:message code="fedreg.link.view" /></g:link></td>
						</tr>
					</g:each>
					</tbody>
				</table>
			</div>
			<div class="paginateButtons">
				<g:paginate total="${contactTotal}" />
			</div>
		</div>
	</body>
</html>
