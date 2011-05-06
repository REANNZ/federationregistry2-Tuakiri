<html>
	<head>
		<meta name="layout" content="reporting" />
		<title><g:message code="fedreg.view.reporting.federation.summary.title" /></title>
		
		<r:script>
			var federationReportsSummaryEndpoint = "${createLink(controller:'federationReports', action:'summaryjson')}"
		</r:script>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.reporting.federation.summary.heading" /></title>
			
			<div id="reporting">
				<div id="reports">
					
				</div>
			</div>
		</section>
	</body>
</html>
