<html>
	<head>
		
		<meta name="layout" content="reporting" />
		<g:set var="entityName" value="${message(code: 'identityProviderAttributeCompliance.label')}" />
		<title><g:message code="fedreg.view.compliance.attribute.title"/></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.compliance.attribute.heading" args="${[attribute?.name]}" /> (${supportingIdentityProviderList.size().encodeAsHTML()}<span class="total"> / ${identityProviderList.size().encodeAsHTML()})</h2>

			<table>
				<thead>
					<tr>
						<th><g:message code="label.identityprovider" /></th>
						<th><g:message code="label.status" /></th>
						<th/>
					</tr>
				</thead>
				<tbody>
				<g:each in="${identityProviderList}" status="i" var="idp">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td style="width:300px;">${idp?.displayName?.encodeAsHTML()}</td>
						<td style="width:200px;">
						<g:if test="${supportingIdentityProviderList.contains(idp)}">
							<span class="success"><g:message code="label.supported"/></span>
						</g:if>
						<g:else>
							<span><g:message code="label.notsupported"/></span>
						</g:else>
						</td>
						<td style="width:200px;">
							<n:button href="${createLink(controller: 'IDPSSODescriptorAttributeCompliance', action:'comprehensive', id: idp.id)}" label="${message(code:'label.idpattributesupport')}" class="view-button"/>
						</td>
					</tr>
				</g:each>
			   </tbody>
		   </table>

		</section>
	</body>
</html>
