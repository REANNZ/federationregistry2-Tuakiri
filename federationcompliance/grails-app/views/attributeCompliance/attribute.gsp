<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="compliance" />
        <g:set var="entityName" value="${message(code: 'identityProviderAttributeCompliance.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
	
		<h2><g:message code="compliance.attributes.crossfederation.label" args="${[attribute?.friendlyName]}" /></h2>

		<div class="attributesummary">
			<div class="attribute">
				<h3><g:message code="compliance.attributes.crossfederation.graph.label"/></h3>
				<div class="numeric">
					<strong>${supportingIdpInstanceList.size().encodeAsHTML()}<span class="total"> / ${idpInstanceList.size().encodeAsHTML()}</span></strong>
				</div>
				<div class="graphic">	
						<img src="http://chart.apis.google.com/chart?
						chs=225x50
						&chco=2dac3f,ea2f31
						&chd=t:${supportingIdpInstanceList.size().encodeAsHTML()},${(idpInstanceList.size() - supportingIdpInstanceList.size()).encodeAsHTML()}
						&cht=p3
						&chl=<g:message code="compliance.attributes.supported"/>|<g:message code="compliance.attributes.notsupported"/>"
						alt="${attribute?.friendlyName.encodeAsHTML()} support chart" />
				</div>
			</div>
		</div>
		<div class="attributedetail">
				<div class="attribute">

					<table class="cleantable buttons">
	                    <thead>
	                        <tr>

	                            <th><g:message code="organization.name.label" /></th>
								<th/>
								<th/>

	                        </tr>
	                    </thead>
	                    <tbody>
	                    <g:each in="${idpInstanceList}" status="i" var="idp">
	                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

	                            <td>${idp?.organization?.name?.encodeAsHTML()}</td>

	                            <td>
									<g:if test="${supportingIdpInstanceList.contains(idp)}">
										<span class="icon icon_tick"><g:message code="compliance.attributes.supported"/></span>
									</g:if>
									<g:else>
										<span class="icon icon_cross"><g:message code="compliance.attributes.notsupported"/></span>
									</g:else>
								</td>

								<td><g:link action="identityprovider" id="${idp.id}" class="button icon icon_view icon_view_attributeCompliance"><g:message code="attributeCompliance.view.label" /></g:link></td>
	                        </tr>
	                    </g:each>
	                    </tbody>
	                </table>
				</div>

		</div>
	
	</body>
</html>