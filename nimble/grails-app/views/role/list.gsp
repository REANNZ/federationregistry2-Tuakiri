<%@ page import="grails.plugins.nimble.core.Role" %>

<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.role.list.title" /></title>
	</head>

	<body>

		<h2><g:message code="nimble.view.role.list.heading" /></h2>

		<table>
			<thead>
				<tr>
					<g:sortableColumn property="name" titleKey="label.name" class="first icon icon_arrow_refresh"/>
					<th><g:message code="label.name" /></th>
					<th class="last">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${roles}" status="i" var="role">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${role.name?.encodeAsHTML()}</td>
						<td>${role.description?.encodeAsHTML()}</td>
						<td>
							<n:button href="${createLink(action:'show', id: role.id)}" label="label.view" icon="arrowthick-1-ne"/>
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>

		<div class="paginatebuttons">
			<g:paginate total="${Role.count()}"/>
		</div>

	</body>
</html>