
<%! import aaf.fedreg.core.AttributeCategory %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="compliance" />
        <g:set var="entityName" value="${message(code: 'identityProviderAttributeCompliance.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
	
			<h2><g:message code="compliance.attributes.summary.label"/></h2>
			
				<div class="attributecompliancesummary">
					
                	<table class="cleantable buttons">
	                    <thead>
	                        <tr>
                        
	                            <th><g:message code="organization.displayName.label" /></th>
								<g:each in="${AttributeCategory.listOrderByName()}">
									<th>${it.name.encodeAsHTML()}</th>
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
								<td><g:link action="identityprovider" id="${idp.id}" class="button icon icon_view icon_view_attributeCompliance"><g:message code="attributeCompliance.view.label" /></g:link></td>
							</tr>
						</g:each>
	                    </tbody>
	                </table>

				</div>
				
            </div>

    </body>
</html>
