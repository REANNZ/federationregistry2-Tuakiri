<%@ page import="grails.plugins.nimble.core.Group" %>

<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.group.list.title" /></title>
	</head>
	
	<body>
		
		<h2><g:message code="nimble.view.group.list.heading" /></h2>
		
		<p>
			<g:message code="nimble.view.group.edit.descriptive" />
		</p>

		<table>
			<thead>
				<tr>
					<g:sortableColumn property="name" titleKey="label.name" class="first icon icon_arrow_refresh"/>
					<th class=""><g:message code="label.description" /></th>
					<th class="last"/>
				</tr>
			</thead>
			<tbody>
				<g:each in="${groups}" status="i" var="group">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${fieldValue(bean: group, field: 'name')}</td>
						<td>${fieldValue(bean: group, field: 'description')}</td>
						<td>
							<n:button href="${createLink(action:'show', id: group.id)}" label="label.view" icon="arrowthick-1-ne"/>
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>

		<div class="paginatebuttons">
			<g:paginate total="${Group.count().encodeAsHTML()}"/>
		</div>
		
	</body>
</html>
