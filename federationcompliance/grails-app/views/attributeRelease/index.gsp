<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
		
		<h2><g:message code="fedreg.view.compliance.attributerelease.heading" /></h2>
		
		<div id="attributereleasecompliance">
			<table id="comparisondata" class="cleantable">
				<tbody>
					<tr>
						<td>
							<h3><g:message code="fedreg.label.serviceprovider"/></h3>
							<g:select id="sp" from="${activeSP}" optionKey="id" optionValue="${{it.displayName ? it.displayName.encodeAsHTML():it.entityDescriptor.entityID.encodeAsHTML()}}"/>
						</td>
						<td>
							<a href="#" id="analyzecompatibility" class="button icon icon_arrow_join"><g:message code="fedreg.link.compare"/></a>
						</td>
						<td>
							<h3><g:message code="fedreg.label.identityprovider"/></h3>
							<g:select id="idp" from="${activeIDP}" optionKey="id" optionValue="${{it.displayName ? it.displayName.encodeAsHTML():it.entityDescriptor.entityID.encodeAsHTML()}}"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
	</body>
</html>