
<html>
	<head>
		
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.organization.list.title" /></title>
		
		<script type="text/javascript">
			<njs:datatable tableID="organizationlist" sortColumn="0" />
		</script>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.members.organization.list.heading" /></h2>

			<table id="organizationlist">
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
						<td>${fieldValue(bean: organization, field: "url.uri")}</td>
						<td>${fieldValue(bean: organization, field: "primary.displayName")}</td>
						<td><n:button href="${createLink(controller:'organization', action:'show', id:organization.id)}" label="label.view" class="view-button" /></td>
					</tr>
				</g:each>
				</tbody>
			</table>

		</section>
	</body>
</html>
