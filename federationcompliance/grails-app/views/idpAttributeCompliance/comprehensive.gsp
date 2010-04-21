
<html>
    <head>
        <meta name="layout" content="compliance" />
        <g:set var="entityName" value="${message(code: 'identityProviderAttributeCompliance.label')}" />
        <title><g:message code="fedreg.view.compliance.identityprovider.title"/></title>
		
    </head>
    <body>
	
		<h2><g:message code="fedreg.view.compliance.identityprovider.heading" args="${[idp?.organization?.displayName]}" /></h2>
		
		<div class="graphcontent">
		<g:each in="${categorySupport}" status="i" var="currentStatus">
			<div class="horizontalelem" >
				<h3><g:message code="fedreg.label.${currentStatus.name.toLowerCase()}"/></h3>
				<div class="numeric">
					<strong>${currentStatus.supportedCount.encodeAsHTML()}<span class="total"> / ${currentStatus.totalCount.encodeAsHTML()}</span></strong>
				</div>
				<div id="graphic${i}" ></div>
				<script type="text/javascript">
					line${i} = [['supported',${currentStatus.supportedCount}], ['unsupported',${(currentStatus.totalCount - currentStatus.supportedCount)}] ];
					plot${i} = $.jqplot('graphic${i}', [line${i}], {
					    title: '',
					    seriesColors: [ "#30A800", "#D44226" ],
						grid: { background: '#fff', borderColor: '#fff', shadow: false },
					    seriesDefaults:{renderer:$.jqplot.PieRenderer, rendererOptions:{sliceMargin:0, diameter: 80}}
					});
				</script>	
			</div>
		</g:each>
		</div>
			<g:each in="${categorySupport}" status="i" var="currentStatus">
				<div class="contentblock">
					<div class="numeric">
						<strong>${currentStatus.name.encodeAsHTML()} - ${currentStatus.supportedCount}<span class="total"> / ${currentStatus.totalCount.encodeAsHTML()}</span></strong>
					</div>
				
					<table class="enhancedtabledata">
	                    <thead>
	                        <tr>
                        
	                            <th><g:message code="fedreg.label.attribute" /></th>
								<th><g:message code="fedreg.label.status" /></th>
								<th/>
							                        
	                        </tr>
	                    </thead>
	                    <tbody>
	                    <g:each in="${currentStatus.available}" status="j" var="attr">
	                        <tr class="${(j % 2) == 0 ? 'odd' : 'even'}">
                        
	                            <td>${attr.friendlyName.encodeAsHTML()}</td>
                        
	                            <td> 
									<g:if test="${currentStatus.supported.contains(attr)}">
										<span class="icon icon_tick"><g:message code="fedreg.label.supported"/></span>
									</g:if>
									<g:else>
										<span class="icon icon_cross"><g:message code="fedreg.label.notsupported"/></span>
									</g:else>
								</td>
								
								<td><g:link action="federationwide" id="${attr.id}" class="button icon icon_magnifier"><g:message code="fedreg.link.federationavailability" /></g:link></td>
	                        </tr>
	                    </g:each>
	                    </tbody>
	                </table>
				</div>

			</g:each>
		</div>

	</body>
</html>

