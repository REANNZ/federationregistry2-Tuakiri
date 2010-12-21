<html>
	<head>
		
		<meta name="layout" content="compliance" />
		<title><g:message code="fedreg.view.compliance.attributerelease.title"/></title>
		<script type="text/javascript">
			$(function() {
				$("#analyzecompatibility").click(function(){
					var comparisonEndpoint = "${createLink(controller:'attributeRelease', action:'compare')}";
					var dataString = "idp=" + $('#idp').val() + "&sp=" + $('#sp').val()
					$.ajax({
					    type: "POST",
					    url: comparisonEndpoint,
						data: dataString,
					    success: function(res) {
					      $("#comparisondata > tbody > .dynamicrow").remove();
					      $("#comparisondata > tbody").append(res);
					    },
					    error: function (xhr, ajaxOptions, thrownError) {
					      growl('error', xhr.responseText);
					    }
					  });
				});
			});
		</script>
	</head>
	<body>
		
		<section>
			<h2><g:message code="fedreg.view.compliance.attributerelease.heading" /></h2>
		
			<table id="comparisondata">
				<tbody>
					<tr>
						<td>
							<h3><g:message code="label.serviceprovider"/></h3>
							<g:select id="sp" from="${activeServiceProviderList}" optionKey="id" optionValue="${{it.displayName ? it.displayName.encodeAsHTML():it.entityDescriptor.entityID.encodeAsHTML()}}"/>
						</td>
						<td>

							<n:button id="analyzecompatibility" label="${message(code:'label.compare')}" icon="search"/>
						</td>
						<td>
							<h3><g:message code="label.identityprovider"/></h3>
							<g:select id="idp" from="${activeIdentityProviderList}" optionKey="id" optionValue="${{it.displayName ? it.displayName.encodeAsHTML():it.entityDescriptor.entityID.encodeAsHTML()}}"/>
						</td>
					</tr>
				</tbody>
			</table>
		</section>
		
	</body>
</html>