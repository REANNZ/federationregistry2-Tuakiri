
<html>
	<head>
		
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.identityprovider.list.title" /></title>
	</head>
	<body>

		<section>
			<h2><g:message code="fedreg.view.members.identityprovider.list.heading" /></h2>
			<table>
				<thead>
					<tr>
						<g:sortableColumn property="displayName" title="${message(code: 'label.identityprovider')}" />
						<g:sortableColumn property="organization" title="${message(code: 'label.organization')}" />
						<th><g:message code="label.active" /></th>
						<th/>
					</tr>
				</thead>
				<tbody>
				<g:each in="${identityProviderList}" status="i" var="identityProvider">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${fieldValue(bean: identityProvider, field: "displayName")}</td>
						<td>${fieldValue(bean: identityProvider, field: "organization.name")}</td>
						<td>${fieldValue(bean: identityProvider, field: "active")}</td>
						<td>
							<n:button href="${createLink(controller:'IDPSSODescriptor', action:'show', id: identityProvider.id)}" label="${message(code:'label.view')}" icon="arrowthick-1-ne"/>
						</td>
					</tr>
				</g:each>
				</tbody>
			</table>

			<div class="paginatebuttons">
				<g:paginate total="${identityProviderTotal}" />
			</div>
		</section>

	</body>
</html>
