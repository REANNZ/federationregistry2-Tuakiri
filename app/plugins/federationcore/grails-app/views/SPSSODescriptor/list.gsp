
<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.serviceprovider.list.title" /></title>
		<script type="text/javascript">
			<njs:datatable tableID="spssodescriptorlist" sortColumn="0" />
		</script>
	</head>
	<body>

		<section>
			<h2><g:message code="fedreg.view.members.serviceprovider.list.heading" /></h2>
			<table id="spssodescriptorlist">
				<thead>
					<tr>
						<th><g:message code="label.serviceprovider" /></th>
						<th><g:message code="label.organization" /></th>
						<th><g:message code="label.entitydescriptor" /></th>
						<th><g:message code="label.active" /></th>
						<th><g:message code="label.approved" /></th>
						<th/>
					</tr>
				</thead>
				<tbody>
				<g:each in="${serviceProviderList}" status="i" var="serviceProvider">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${fieldValue(bean: serviceProvider, field: "displayName")}</td>
						<td>${fieldValue(bean: serviceProvider, field: "organization.name")}</td>
						<td>${fieldValue(bean: serviceProvider, field: "entityDescriptor.entityID")}</td>
						<td>${fieldValue(bean: serviceProvider, field: "active")}</td>
						<td>${fieldValue(bean: serviceProvider, field: "approved")}</td>
						<td>
							<n:button href="${createLink(controller:'SPSSODescriptor', action:'show', id: serviceProvider.id)}" label="${message(code:'label.view')}" class="view-button"/>
						</td>
					</tr>
				</g:each>
				</tbody>
			</table>

		</section>

	</body>
</html>
