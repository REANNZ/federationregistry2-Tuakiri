
<html>
    <head>
        <meta name="layout" content="compliance" />
        <g:set var="entityName" value="${message(code: 'identityProviderAttributeCompliance.label')}" />
        <title><g:message code="fedreg.view.compliance.identityprovider.title"/></title>
    </head>
    <body>
		<section>
			<h2><g:message code="fedreg.view.compliance.identityprovider.heading" args="${[identityProvider?.displayName]}" /></h2>
			
			<g:each in="${categorySupport}" status="i" var="currentStatus">
				<h3>${currentStatus.name.encodeAsHTML()} - ${currentStatus.supportedCount}<span class="total"> / ${currentStatus.totalCount.encodeAsHTML()}</span></h3>
				<table>
                    <thead>
                        <tr>
                            <th><g:message code="label.attribute" /></th>
							<th><g:message code="label.status" /></th>
							<th/>                 
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${currentStatus.available}" status="j" var="attr">
                        <tr class="${(j % 2) == 0 ? 'odd' : 'even'}">
                            <td style="width:300px;">${attr.friendlyName.encodeAsHTML()}</td>
                            <td style="width:200px;"> 
								<g:if test="${currentStatus.supported.find{it.base == attr}}">
									<span class="success"><g:message code="label.supported"/></span>
								</g:if>
								<g:else>
									<span><g:message code="label.notsupported"/></span>
								</g:else>
							</td>
							<td style="width:200px;">
								<n:button href="${createLink(controller: 'IDPSSODescriptorAttributeCompliance', action:'federationwide', id: attr.id)}" label="${message(code:'label.federationwide')}" icon="arrowthick-1-ne"/>
							</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
			</g:each>
		</section>
	</body>
</html>

