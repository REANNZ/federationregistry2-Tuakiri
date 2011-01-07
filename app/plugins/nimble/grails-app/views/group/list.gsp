<%@ page import="grails.plugins.nimble.core.Group" %>

<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.group.list.title" /></title>
		
		<script type="text/javascript">
			<njs:datatable tableID="grouplist" sortColumn="0" />
		</script>
	</head>
	
	<body>
		
		<h2><g:message code="nimble.view.group.list.heading" /></h2>
		
		<p>
			<g:message code="nimble.view.group.edit.descriptive" />
		</p>

		<table id="grouplist">
			<thead>
				<tr>
					<th><g:message code="label.name" /></th>
					<th><g:message code="label.description" /></th>
					<th class="last">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${groups}" status="i" var="group">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${fieldValue(bean: group, field: 'name')}</td>
						<td>${fieldValue(bean: group, field: 'description')}</td>
						<td>
							<n:button href="${createLink(action:'show', id: group.id)}" label="label.view" class="view-button"/>
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>
		
	</body>
</html>
