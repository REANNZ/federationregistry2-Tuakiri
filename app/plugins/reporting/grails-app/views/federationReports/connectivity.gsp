
<html>
	<head>
		<meta name="layout" content="reportingclean" />
		<title><g:message code="views.fr.reporting.federation.connectivity.title" /></title>
		
		<r:script>
			var federationConnectivtyEndpoint = "${createLink(controller:'federationReports', action:'reportconnectivity')}"
		</r:script>
	</head>
	<body>
		<section>
			<div class="reportingsupported">
				<h2><g:message code="views.fr.reporting.federation.connectivity.heading" /></h2>
			
				<div id="reporting">
					<div id="reports">
						<form id="reportrequirements">	
							<label><g:message code="label.day" />: <input name="day" size="2" class="number"/><fr:tooltip code='help.fr.report.day' /></label>
							<label><g:message code="label.month" />: <input name="month" size="2" class="number"/><fr:tooltip code='help.fr.report.month' /></label>
							<label><g:message code="label.year" />: <input name="year" size="4"  class="required number"/><fr:tooltip code='help.fr.report.year' /></label>

							<a href="#" onClick="if($('#reportrequirements').valid()){ fedreg.renderFederationReport('connectivity'); } return false;" class="search-button"><g:message code="label.generate" /></a>
						</form>

						<div id="connectivityreport" class="revealable reportdata">
							<div id="connectivitydata">
							</div>
						</div>

						<div id="connectivityreportnodata" class="revealable reportdata">
							<p><em><g:message code="templates.fr.reports.nodata.description"/></em></p>
						</div>	

						<script type="text/javascript+protovis">
							fedreg.renderFederationConnectivity = function(data, refine) {
								if(refine || data.populated) {
									$('#connectivitydata').empty();
									$('#connectivitytitle').html(data.title);

									var canvas = document.createElement("div");
									$('#connectivitydata').append(canvas);
									var vis = new pv.Panel()
										.canvas(canvas)
										.width(data.nodes.length * 62)
										.height(data.nodes.length * 31)
										.bottom(220);

									var arc = vis.add(pv.Layout.Arc)
										.reset()
									    .nodes(data.nodes)
									    .links(data.links);
	
									var line = arc.link.add(pv.Line)
		
									var dot = arc.node.add(pv.Dot)
									    .size(function(d) d.linkDegree);

									arc.label.add(pv.Label)
									vis.render();
									$('#connectivityreport').fadeIn();
								}
								else {
									$('.reportdata').hide();
									$('#connectivityreportnodata').fadeIn();
								}
							};
						</script>
						</div>
					</div>
				</div>
				<div class="reportingunsupported">
					<g:render template="/templates/reporting/unsupported" plugin="federationreporting"/>
				</div>
			</section>
		</body>
	</html>