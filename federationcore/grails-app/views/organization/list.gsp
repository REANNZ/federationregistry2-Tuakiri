
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.organization.list.title" /></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.members.organization.list.heading" /></h2>

			<table class="enhancedtabledata">
				<thead>
					<tr>
					
						<th>${message(code: 'label.organization')}</th>
						<th>${message(code: 'label.primarytype')}</th>
						<th />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${organizationList.sort{it.name}}" status="i" var="organization">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${fieldValue(bean: organization, field: "displayName")}</td>
						<td>${fieldValue(bean: organization, field: "primary.displayName")}</td>
						<td><n:button href="${createLink(controller:'organization', action:'show', id:organization.id)}" label="label.view" icon="arrowthick-1-ne" /></td>
					</tr>
				</g:each>
				</tbody>
			</table>

			<div class="paginatebuttons">
				<g:paginate total="${organizationTotal}" />
			</div>
		</section>
	</body>
</html>
