<html>
	<head>
		<meta name="layout" content="reporting" />
		<title><g:message code="fedreg.view.reporting.federation.subscribers.title" /></title>
		
		<r:script>
			var federationReportsSubscribersEndpoint = "${createLink(controller:'federationReports', action:'subscribersjson')}"
		</r:script>
	</head>
	<body>
		<section>
			<div class="reportingsupported">
				<h2><g:message code="fedreg.view.reporting.federation.subscribers.heading" /></h2>
			
				<div id="reporting">
					<div id="reports">
				
						<form id="reportrequirements">
							<label><g:message code="label.type" />: 
								<select id="subscriberstype">
										<option value="organization"><g:message code="label.organizations" /></option>
										<option value="identityprovider"><g:message code="label.identityproviders" /></option>
										<option value="serviceprovider"><g:message code="label.serviceproviders" /></option>
								</select>
							</label>
							<label><g:message code="label.month" />: <input name="month" size="2" class="number"/><fr:tooltip code='fedreg.help.report.month' /></label>
							<label><g:message code="label.year" />: <input name="year" size="4" class="number"/><fr:tooltip code='fedreg.help.report.yearnr' /></label>

							<a href="#" onClick="if($('#reportrequirements').valid()){ fedreg.renderFederationReport('subscribers'); } return false;" class="search-button"><g:message code="label.generate" /></a>
						</form>
					
						<div id="subscribersreport" class="revealable reportdata">
							<div class="description">
								<h4 id="subscriberstitle"></h4>
							</div>

							<div id="subscribersdata">
							</div>
						</div>

						<div id="subscribersreportnodata" class="revealable reportdata">
							<p><em><g:message code="fedreg.templates.reports.nodata.description"/></em></p>
						</div>

						<script type="text/javascript+protovis">
							fedreg.renderFederationSubscribers = function(data, refine) {

									$('.reportdata').hide();
									$('#subscribersdata').empty();
									$("#subscriberslist").empty();
									$('#subscriberstitle').html(data.title);
		
									var canvas = document.createElement("div");
									$('#subscribersdata').append(canvas);
			
									/* Sizing and scales. */
									var w = 800,
										h = 400,
										x = pv.Scale.linear(data.totals, function(d) d.t).range(0, w),
										y = pv.Scale.linear(0, data.max + 10).range(0, h),
										i = -1;

									/* The root panel. */
									var vis = new pv.Panel()
									.canvas(canvas)
										.width(w)
										.height(h)
										.bottom(60)
										.left(80)
										.right(40)
										.top(5);

									/* Y-axis and ticks. */
									vis.add(pv.Rule)
										.data(y.ticks(6))
										.bottom(y)
										.strokeStyle(function(d) d ? "#eee" : "#000")
									  .anchor("left").add(pv.Label)
										.text(y.tickFormat);
										
									/* Y-axis label */
									vis.add(pv.Label)
									    .data([data.yaxis])
									    .left(-45)
									    .bottom(h/2)
									    .textAlign("center")
										.textAngle(-Math.PI/2);

									/* X-axis and ticks. */
									vis.add(pv.Rule)
										.data(x.ticks(data.totals.length - 1))
										.visible(function(d) d)
										.left(x)
										.bottom(-5)
										.height(5)
									  .anchor("bottom").add(pv.Label)
										.text(x.tickFormat);
										
									/* X-axis label */
									vis.add(pv.Label)
									    .data([data.xaxis])
									    .left(w/2)
									    .bottom(-45)
									    .textAlign("center");

									/* The area with top line. */
									var area = vis.add(pv.Area)
										.data(data.totals)
										.left(function(d) x(d.t))
										.height(function(d) y(d.c))
										.bottom(1)
										.fillStyle("rgb(243,134,48)")
					
									 var line = area.anchor("top").add(pv.Line)
										.lineWidth(3)
										.strokeStyle("rgb(250,105,0)");
			
									 var dot = line.add(pv.Dot)
										.visible(function() i >= 0)
										.data(function() [data.totals[i]])
										.fillStyle(function() line.strokeStyle())
										.strokeStyle("#000")
										.size(0)
										.lineWidth(0);
										//TODO: Figure out why the line dot won't follow the actual line disabled size 0 for now

									 dot.add(pv.Dot)
										.left(10)
										.bottom(10)
										.size(20)
									  .anchor("right").add(pv.Label)
									  	.textStyle("#000")
										.text(function(d) "Subscribers: " + d.c); 
				
										vis.add(pv.Bar)
											.fillStyle("rgba(0,0,0,.001)")
											.event("mouseout", function() {
												i = -1;
												return vis;
											  })
											.event("mousemove", function() {
												var mx = x.invert(vis.mouse().x);
												i = pv.search(data.totals.map(function(d) d.t), mx);
												i = i < 0 ? (-i - 2) : i;
												return vis;
											  });

									vis.render();
									$('#subscribersreport').fadeIn();
								
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
