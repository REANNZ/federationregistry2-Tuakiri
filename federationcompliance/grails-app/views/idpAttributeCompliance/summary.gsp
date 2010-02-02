
<%! import fedreg.core.AttributeCategory %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="compliance" />
        <g:set var="entityName" value="${message(code: 'identityProviderAttributeCompliance.label')}" />
        <title><g:message code="fedreg.view.compliance.summary.title"/></title>
    </head>
    <body>
	
			<h2><g:message code="fedreg.view.compliance.summary.heading"/></h2>
			
				<div class="attributecompliancesummary">
					
                	<table class="cleantable buttons">
	                    <thead>
	                        <tr>                        
	                            <th><g:message code="fedreg.label.organization" /></th>
								<g:each in="${AttributeCategory.listOrderByName()}">
									<th><g:message code="fedreg.label.${it.name.toLowerCase()}" /></th>
								</g:each>
								<th/>                        
	                        </tr>
	                    </thead>
	                    <tbody>
						<g:each in="${idpInstanceList}" status="i" var="idp">
							<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
								<td class="organizationname">${idp?.organization?.name.encodeAsHTML()}</td>
	                    		<g:findAll in="${categorySupportSummaries}" expr="it.idp == idp">
			                        <td>
										<div class="numeric">
											<strong>${it.supportedCount.encodeAsHTML()}<span class="total"> / ${it.totalCount.encodeAsHTML()}</span></strong>
										</div>
									</td>	                        
			                    </g:findAll>
								<td><g:link action="comprehensive" id="${idp.id}" class="button icon icon_view icon_view_attributeCompliance"><g:message code="fedreg.link.view" /></g:link></td>
							</tr>
						</g:each>
	                    </tbody>
	                </table>

				</div>
				
            </div>

    </body>
</html>
