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
			<div class="reportingsupported">
				<h2><g:message code="fedreg.view.reporting.federation.summary.heading" /></h2>
				<p><g:message code="fedreg.view.reporting.federation.summary.description" /></p>
				<div id="reporting">
					<div id="reports">
					
						<div class="reportsummaryrow">
							<div class="reportsummarycell">
								<h3 class="description"><g:message code="fedreg.view.reporting.federation.summary.heading.sessions" /></h3>
								<div id="sessioncreationgraph"></div>
							</div>
					
							<div class="reportsummarycell">
								<h3 class="description"><g:message code="fedreg.view.reporting.federation.summary.heading.orgs" /></h3>
								<div id="orgcreationgraph"></div>
							</div>
						</div>
					
						<div class="reportsummaryrow">
							<div class="reportsummarycell">
								<h3 class="description"><g:message code="fedreg.view.reporting.federation.summary.heading.idp" /></h3>
								<div id="idpcreationgraph"></div>
							</div>
							<div class="reportsummarycell">
								<h3 class="description"><g:message code="fedreg.view.reporting.federation.summary.heading.sp" /></h3>
								<div id="spcreationgraph"></div>
							</div>
						</div>
						
						<div class="reportsummaryrow">
							<div class="reportsummarycell">
								<h3 class="description"><g:message code="fedreg.view.reporting.federation.summary.heading.subgrowth" /></h3>
								<center><div id="subscriberstackgraph"></div></center>
							</div>
						</div>
					
						<script type="text/javascript+protovis">
							fedreg.renderCreationSummary = function(data) {	
								fedreg.renderSummaryGraph($('#sessioncreationgraph'), data.sessionvalues, data.sessionmax, "rgba(225,187,120,.7)", "rgb(255,127,14)", "Established");							
								fedreg.renderSummaryGraph($('#orgcreationgraph'), data.orgvalues, data.orgmax, "rgba(152,223,138,.7)", "rgb(44,160,44)", "Created");
								fedreg.renderSummaryGraph($('#idpcreationgraph'), data.idpvalues, data.idpmax, "rgba(121,173,210,.7)", "rgb(31,119,180)", "Created");
								fedreg.renderSummaryGraph($('#spcreationgraph'), data.spvalues, data.spmax, "rgba(197,176,213,.7)", "rgb(148,103,189)", "Created");
								fedreg.renderStackedGraph($('#subscriberstackgraph'), data.subscribers, data.subscribersMax, data.subscriberlabels, "rgb(197,176,213)", "rgb(148,103,189)", "Created");
							};
							
							fedreg.renderStackedGraph = function(element, data, maxVal, labels, fillColor, strokeColor) {
								var canvas = document.createElement("div");
								element.append(canvas);
								
								/* Sizing and scales. */
								var w = 800,
									h = 300,
									x = pv.Scale.linear(2010, 2011).range(0, w),
									y = pv.Scale.linear(0, maxVal + 20).range(0, h),
									c = pv.Colors.category20(),
									i = -1;
				
								/* The root panel. */
								var vis = new pv.Panel()
									.canvas(canvas)
									.width(w)
									.height(h)
									.bottom(20)
									.left(80)
									.right(200)
									.top(5);
									
								/* X-axis and ticks. */
								vis.add(pv.Rule)
								    .data(x.ticks(data[0].length - 1))
								    .visible(function(d) d)
								    .left(x)
								    .bottom(-5)
								    .height(5)
								  .anchor("bottom").add(pv.Label)
								    .text(function(d) d);

								/* The stack layout. */
								vis.add(pv.Layout.Stack)
								    .layers(data)
								    .x(function(d) x(d.t))
								    .y(function(d) y(d.c))
								  .layer.add(pv.Area)
									 .fillStyle(c.by(pv.parent));
										
								/* Y-axis and ticks. */
								vis.add(pv.Rule)
								    .data(y.ticks(3))
								    .bottom(y)
								    .strokeStyle(function(d) d ? "rgba(128,128,128,.2)" : "#000")
								  .anchor("left").add(pv.Label)
								    .text(y.tickFormat);	
								
								/* Y-axis label */
								vis.add(pv.Label)
								    .text('Subscriptions')
								    .left(-45)
								    .bottom(h/2)
								    .textAlign("center")
									.textAngle(-Math.PI/2);
									
									vis.add(pv.Dot)
										.data([0,1])
										.right(-40)
										.top(function() 20 + this.index * 16)
										.fillStyle(function(d) c(d))
										.strokeStyle(null)
										.size(30)
									  .anchor("right").add(pv.Label)
										.textMargin(6)
										.textAlign("left")
										.text(function(d) labels[d]);
												
								vis.render();
							}
							
							fedreg.renderSummaryGraph = function(element, values, maxVal, fillColor, strokeColor, type) {
						
								var canvas = document.createElement("div");
								element.append(canvas);
							
								/* Sizing and scales. */
								var w = 375,
									h = 250,
									x = pv.Scale.linear(values, function(d) d.x).range(0, w),
									y = pv.Scale.linear(0, maxVal).range(0, h),
									i = -1;
				
								/* The root panel. */
								var vis = new pv.Panel()
									.canvas(canvas)
									.width(w)
									.height(h)
									.bottom(20)
									.left(40)
									.right(40)
									.top(5);

								/* Y-axis and ticks. */
								vis.add(pv.Rule)
									.data(y.ticks(10))
									.bottom(y)
									.strokeStyle(function(d) d ? "#eee" : "#000")
								  .anchor("left").add(pv.Label)
									.text(y.tickFormat);

								/* X-axis and ticks. */
								vis.add(pv.Rule)
									.data(x.ticks(values.length - 1))
									.visible(function(d) d)
									.left(x)
									.bottom(-5)
									.height(5)
								  .anchor("bottom").add(pv.Label)
									.text(function(d) d);

								/* The area with top line. */
								var area = vis.add(pv.Area)
									.data(values)
									.left(function(d) x(d.x))
									.height(function(d) y(d.y))
									.bottom(1)
									.fillStyle(fillColor);

								 var line = area.anchor("top").add(pv.Line)
									.lineWidth(3)
									.strokeStyle(strokeColor);
							
								var dot = line.add(pv.Dot)
									.visible(function() i >= 0)
									.data(function() [values[i]])
									.fillStyle(function() line.strokeStyle())
									.strokeStyle("#000")
									.size(0)
									.lineWidth(0);
									//TODO: Figure out why the line dot won't follow the actual line disabled size 0 for now

								 dot.add(pv.Dot)
									.left(10)
									.bottom(10)
									.size(30)
								  .anchor("right").add(pv.Label)
								  	.textStyle("#444")
								  	.font("13px arial")
									.text(function(d) type + ": " + d.y); 

									vis.add(pv.Bar)
										.fillStyle("rgba(0,0,0,.001)")
										.event("mouseout", function() {
											i = -1;
											return vis;
										  })
										.event("mousemove", function() {
											var mx = x.invert(vis.mouse().x);
											i = pv.search(values.map(function(d) d.x), mx);
											i = i < 0 ? (-i - 2) : i;
											return vis;
										  });

								vis.render();
							}
							
							fedreg.renderFederationSummaryReport();
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
