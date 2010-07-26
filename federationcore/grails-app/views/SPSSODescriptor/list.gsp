
<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.serviceprovider.list.title" /></title>
	</head>
	<body>

		<section>
			<h2><g:message code="fedreg.view.members.serviceprovider.list.heading" /></h2>
			<table>
				<thead>
					<tr>
						<g:sortableColumn property="displayName" title="${message(code: 'label.serviceprovider')}" />
						<g:sortableColumn property="organization" title="${message(code: 'label.organization')}" />
						<th/>
					</tr>
				</thead>
				<tbody>
				<g:each in="${serviceProviderList}" status="i" var="serviceProvider">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${fieldValue(bean: serviceProvider, field: "displayName")}</td>
						<td>${fieldValue(bean: serviceProvider, field: "organization.name")}</td>					
						<td>
							<n:button href="${createLink(controller:'SPSSODescriptor', action:'show', id: serviceProvider.id)}" label="${message(code:'label.view')}" icon="arrowthick-1-ne"/>
						</td>
					</tr>
				</g:each>
				</tbody>
			</table>

			<div class="paginatebuttons">
				<g:paginate total="${serviceProviderTotal}" />
			</div>
		</section>

	</body>
</html>
