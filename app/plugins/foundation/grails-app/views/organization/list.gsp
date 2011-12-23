
<html>
	<head>
		
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.organization.list.title" /></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.members.organization.list.heading" /></h2>

			<table class="sortable-table">
				<thead>
					<tr>
					
						<th>${message(code: 'label.organization')}</th>
						<th>${message(code: 'label.url')}</th>
						<th>${message(code: 'label.primarytype')}</th>
						<th />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${organizationList.sort{it.name}}" status="i" var="organization">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${fieldValue(bean: organization, field: "displayName")}</td>
						<td>${fieldValue(bean: organization, field: "url")}</td>
						<td>${fieldValue(bean: organization, field: "primary.displayName")}</td>
						<td><a href="${createLink(controller:'organization', action:'show', id:organization.id)}" class="btn" /><g:message code="label.view"/></a></td>
					</tr>
				</g:each>
				</tbody>
			</table>

		</section>
	</body>
</html>
