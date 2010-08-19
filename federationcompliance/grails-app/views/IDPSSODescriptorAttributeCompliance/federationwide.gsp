<html>
	<head>
		
		<meta name="layout" content="compliance" />
		<g:set var="entityName" value="${message(code: 'identityProviderAttributeCompliance.label')}" />
		<title><g:message code="fedreg.view.compliance.attribute.title"/></title>
	</head>
	<body>
		<section>
		<h2><g:message code="fedreg.view.compliance.attribute.heading" args="${[attribute?.friendlyName]}" /></h2>

				<h3><g:message code="fedreg.view.compliance.attribute.graph.heading" /></h3>
				<div class="numeric">
					<strong>${supportingIdentityProviderList.size().encodeAsHTML()}<span class="total"> / ${identityProviderList.size().encodeAsHTML()}</span></strong>
				</div>
				<div id="graphic${i}" style="width:200px;"></div>
				<script type="text/javascript">
					line${i} = [['supported',${supportingIdentityProviderList.size()}], ['unsupported',${(identityProviderList.size() - supportingIdentityProviderList.size())}] ];
					plot${i} = $.jqplot('graphic${i}', [line${i}], {
						title: '',
						seriesColors: [ "#30A800", "#D44226" ],
						grid: { background: '#fff', borderColor: '#fff', shadow: false },
						seriesDefaults:{renderer:$.jqplot.PieRenderer, rendererOptions:{sliceMargin:0, diameter: 80}}
					});
				</script>

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
						<td style="width:200px;">${idp?.displayName?.encodeAsHTML()}</td>
						<td style="width:200px;">
						<g:if test="${supportingIdentityProviderList.contains(idp)}">
							<span class="icon icon_tick"><g:message code="label.supported"/></span>
						</g:if>
						<g:else>
							<span class="icon icon_cross"><g:message code="label.notsupported"/></span>
						</g:else>
						</td>
						<td style="width:200px;">
							<n:button href="${createLink(controller: 'IDPSSODescriptorAttributeCompliance', action:'comprehensive', id: idp.id)}" label="${message(code:'label.idpattributesupport')}" icon="arrowthick-1-ne"/>
						</td>
					</tr>
				</g:each>
			   </tbody>
		   </table>

		</section>
	</body>
</html>