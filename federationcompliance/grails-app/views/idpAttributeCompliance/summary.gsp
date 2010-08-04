<%! import fedreg.core.AttributeCategory %>
<html>
	<head>
		
		<meta name="layout" content="compliance" />
		<g:set var="entityName" value="${message(code: 'identityProviderAttributeCompliance.label')}" />
		<title><g:message code="fedreg.view.compliance.summary.title"/></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.compliance.summary.heading"/></h2>		
			<table class="enhancedtabledata">
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
				<g:each in="${idpInstanceList}" status="i" var="idp">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td class="organizationname">${idp?.displayName?.encodeAsHTML()}</td>
							<g:findAll in="${categorySupportSummaries}" expr="it.idp == idp">
							<td>
								<div class="numeric">
									<strong>${it.supportedCount.encodeAsHTML()}<span class="total"> / ${it.totalCount.encodeAsHTML()}</span></strong>
								</div>
							</td>							
						</g:findAll>
						<td>
							<n:button href="${createLink(controller: 'idpAttributeCompliance', action:'comprehensive', id: idp.id)}" label="${message(code:'label.view')}" icon="arrowthick-1-ne"/>
						</td>
					</tr>
				</g:each>
				</tbody>
			</table>
		</section>
	</body>
</html>
