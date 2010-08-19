
<html>
    <head>
        <meta name="layout" content="compliance" />
        <g:set var="entityName" value="${message(code: 'identityProviderAttributeCompliance.label')}" />
        <title><g:message code="fedreg.view.compliance.identityprovider.title"/></title>
    </head>
    <body>
		<section>
			<h2><g:message code="fedreg.view.compliance.identityprovider.heading" args="${[idp?.organization?.displayName]}" /></h2>
			
			<table>
				<tbody>
					<tr>
					<g:each in="${categorySupport}" status="i" var="currentStatus">
						<td style="width: 200px;">
							<h3><g:message code="label.${currentStatus.name.toLowerCase()}"/></h3>
							<strong>${currentStatus.supportedCount.encodeAsHTML()}<span class="total"> / ${currentStatus.totalCount.encodeAsHTML()}</span></strong>
							<div id="graphic${i}" > </div>
							<script type="text/javascript">
								line${i} = [['supported',${currentStatus.supportedCount}], ['unsupported',${(currentStatus.totalCount - currentStatus.supportedCount)}] ];
								plot${i} = $.jqplot('graphic${i}', [line${i}], {
								    title: '',
								    seriesColors: [ "#30A800", "#D44226" ],
									grid: { background: '#fff', borderColor: '#fff', shadow: false },
								    seriesDefaults:{renderer:$.jqplot.PieRenderer, rendererOptions:{sliceMargin:0, diameter: 80}}
								});
							</script>	
						</td>
					</g:each>
					</tr>
				</tbody>
			</table>
			
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
                            <td style="width:200px;">${attr.friendlyName.encodeAsHTML()}</td>
                            <td style="width:200px;"> 
								<g:if test="${currentStatus.supported.contains(attr)}">
									<span class="icon icon_tick"><g:message code="label.supported"/></span>
								</g:if>
								<g:else>
									<span class="icon icon_cross"><g:message code="label.notsupported"/></span>
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

