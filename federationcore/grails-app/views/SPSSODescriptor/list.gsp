
<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.serviceprovider.list.title" /></title>
		<script type="text/javascript">
			$(function() {
				$('#spssodescriptorlist').dataTable( {
						"sPaginationType": "full_numbers",
						"bLengthChange": false,
						"iDisplayLength": 10,
						"aaSorting": [[0, "asc"]],
						"oLanguage": {
							"sSearch": "${g.message(code:'label.filter')}"
						}
					} );
			});
		</script>
	</head>
	<body>

		<section>
			<h2><g:message code="fedreg.view.members.serviceprovider.list.heading" /></h2>
			<table id="spssodescriptorlist">
				<thead>
					<tr>
						<g:sortableColumn property="displayName" title="${message(code: 'label.serviceprovider')}" />
						<g:sortableColumn property="organization" title="${message(code: 'label.organization')}" />
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
						<td>${fieldValue(bean: serviceProvider, field: "active")}</td>
						<td>${fieldValue(bean: serviceProvider, field: "approved")}</td>
						<td>
							<n:button href="${createLink(controller:'SPSSODescriptor', action:'show', id: serviceProvider.id)}" label="${message(code:'label.view')}" icon="arrowthick-1-ne"/>
						</td>
					</tr>
				</g:each>
				</tbody>
			</table>

		</section>

	</body>
</html>
