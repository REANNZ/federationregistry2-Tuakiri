<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="compliance" />
		<title><g:message code="fedreg.view.compliance.attributerelease.title"/></title>
	</head>
	<body>
		
		<table>
			<tbody>
				<tr>
					<td>
						<h3><g:message code="fedreg.label.serviceprovider"/></h3>
						<g:select id="sp" from="${activeSP}" optionKey="id" optionValue="${{it.entityDescriptor.entityID}}"/>
					</td>
					<td>
						<a href="#" id="analyzecompatibility" class="button icon icon_link_break"><g:message code="fedreg.link.compare"/></a>
					</td>
					<td>
						<h3><g:message code="fedreg.label.identityprovider"/></h3>
						<g:select id="idp" from="${activeIDP}" optionKey="id" optionValue="${{it.entityDescriptor.entityID}}"/>
					</td>
				</tr>
			</tbody>
		</table>
		
	</body>
</html>