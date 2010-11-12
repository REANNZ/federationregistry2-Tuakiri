
<html>
	<head>
		
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.identityprovider.list.title" /></title>
		<script type="text/javascript">
			<njs:datatable tableID="idpssodescriptorlist" sortColumn="0" />
		</script>
	</head>
	<body>

		<section>
			<h2><g:message code="fedreg.view.members.identityprovider.list.heading" /></h2>
			<table id="idpssodescriptorlist">
				<thead>
					<tr>
						<th><g:message code="label.identityprovider" /></th>
						<th><g:message code="label.organization" /></th>
						<th><g:message code="label.active" /></th>
						<th><g:message code="label.approved" /></th>
						<th/>
					</tr>
				</thead>
				<tbody>
				<g:each in="${identityProviderList}" status="i" var="identityProvider">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${fieldValue(bean: identityProvider, field: "displayName")}</td>
						<td>${fieldValue(bean: identityProvider, field: "organization.name")}</td>
						<td>${fieldValue(bean: identityProvider, field: "active")}</td>
						<td>${fieldValue(bean: identityProvider, field: "approved")}</td>
						<td>
							<n:button href="${createLink(controller:'IDPSSODescriptor', action:'show', id: identityProvider.id)}" label="${message(code:'label.view')}" icon="arrowthick-1-ne"/>
						</td>
					</tr>
				</g:each>
				</tbody>
			</table>

		</section>

	</body>
</html>
