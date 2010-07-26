
<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.contacts.list.title" /></title>
	</head>
	<body>

		<section>
			<h2><g:message code="fedreg.view.members.contacts.list.heading" /></h2>

			<table>
				<thead>
					<tr>
						<g:sortableColumn property="givenName" title="${message(code: 'label.givenname')}" />
						<g:sortableColumn property="surname" title="${message(code: 'label.surname')}" />
						<th><g:message code='label.organization' /></th>
						<th/>
					</tr>
				</thead>
				<tbody>
				<g:each in="${contactList}" status="i" var="contact">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${fieldValue(bean: contact, field: "givenName")}</td>
						<td>${fieldValue(bean: contact, field: "surname")}</td>
						<td>${fieldValue(bean: contact, field: "organization.displayName")}</td>
						<td><n:button href="${createLink(action:'show', id:contact.id)}" label="label.view"  icon="arrowthick-1-ne"/></td>
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="paginatebuttons">
				<g:paginate total="${contactTotal}" />
			</div>
			
		</section>
	</body>
</html>
