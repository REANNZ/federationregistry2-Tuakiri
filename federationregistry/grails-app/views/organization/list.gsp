
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.organization.list.title" /></title>
	</head>
	<body>
		<div class="container">
			<h2><g:message code="fedreg.view.members.organization.list.heading" /></h2>
			<div>
				<table class="enhancedtabledata">
					<thead>
						<tr>
						
							<g:sortableColumn property="name" title="${message(code: 'fedreg.label.organization')}" />
							<g:sortableColumn property="name" title="${message(code: 'fedreg.label.primarytype')}" />
							<th />
						
						</tr>
					</thead>
					<tbody>
					<g:each in="${organizationList}" status="i" var="organization">
						<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
							<td>${fieldValue(bean: organization, field: "displayName")}</td>
							<td>${fieldValue(bean: organization, field: "primary")}</td>
							<td><g:link action="show" id="${organization.id}" class="button icon icon_magnifier"><g:message code="fedreg.link.view" /></g:link></td>
						</tr>
					</g:each>
					</tbody>
				</table>
			</div>
			<div class="paginateButtons">
				<g:paginate total="${organizationTotal}" />
			</div>
		</div>
	</body>
</html>
