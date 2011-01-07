<%! import fedreg.core.AttributeCategory %>
<html>
	<head>	
		<meta name="layout" content="compliance" />
		<title><g:message code="fedreg.view.compliance.summary.title"/></title>		
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.compliance.summary.heading"/></h2>		
			<table class="sortable-table">
				<thead>
					<tr>
						<th><g:message code="label.identityprovider" /></th>
						<g:each in="${AttributeCategory.listOrderByName()}">
							<th><g:message code="label.${it.name.toLowerCase()}" /></th>
						</g:each>
						<th/>
					</tr>
				</thead>
				<tbody>
				<g:each in="${identityProviderList}" status="i" var="idp">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td class="organizationname" style="width:300px;">${idp?.displayName?.encodeAsHTML()}</td>
						<g:findAll in="${categorySupportSummaries}" expr="it.idp.id == idp.id">
							<td style="width:200px;">
								<div class="numeric">
									<strong>${it.supportedCount.encodeAsHTML()}<span class="total"> / ${it.totalCount.encodeAsHTML()}</span></strong>
								</div>
							</td>
						</g:findAll>
						<td style="width:300px;">
							<n:button href="${createLink(controller: 'IDPSSODescriptorAttributeCompliance', action:'comprehensive', id: idp.id)}" label="${message(code:'label.view')}" icon="arrowthick-1-ne"/>
						</td>
					</tr>
				</g:each>
				</tbody>
			</table>
		</section>
	</body>
</html>
