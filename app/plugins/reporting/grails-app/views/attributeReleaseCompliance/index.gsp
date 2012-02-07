<html>
	<head>
		
		<meta name="layout" content="reporting" />
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
							<g:select id="sp" from="${activeServiceProviderList.sort{it.displayName}}" optionKey="id" optionValue="displayName"/>
						</td>
						<td>

							<n:button id="analyzecompatibility" label="${message(code:'label.compare')}" class="search-button"/>
						</td>
						<td>
							<h3><g:message code="label.identityprovider"/></h3>
							<g:select id="idp" from="${activeIdentityProviderList.sort{it.displayName}}" optionKey="id" optionValue="displayName"/>
						</td>
					</tr>
				</tbody>
			</table>
		</section>
		
	</body>
</html>
