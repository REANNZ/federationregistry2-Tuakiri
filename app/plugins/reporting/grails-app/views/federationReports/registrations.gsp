<html>
	<head>
		<meta name="layout" content="reporting" />
		<title><g:message code="fedreg.view.reporting.federation.registrations.title" /></title>
		
		<r:script>
			var federationReportsRegistrationsEndpoint = "${createLink(controller:'federationReports', action:'registrationsjson')}"
		</r:script>
	</head>
	<body>
		<section>
			<div class="reportingsupported">
				<h2><g:message code="fedreg.view.reporting.federation.registrations.heading" /></h2>
			
				<div id="reporting">
					<div id="reports">
				
						<form id="reportrequirements">
							<label><g:message code="label.type" />: 
								<select id="registrationstype">
										<option value="organization"><g:message code="label.organizations" /></option>
										<option value="identityprovider"><g:message code="label.identityproviders" /></option>
										<option value="serviceprovider"><g:message code="label.serviceproviders" /></option>
								</select>
							</label>
							<label><g:message code="label.day" />: <input name="day" size="2" class="number"/><fr:tooltip code='fedreg.help.report.day' /></label>
							<label><g:message code="label.month" />: <input name="month" size="2" class="number"/><fr:tooltip code='fedreg.help.report.month' /></label>
							<label><g:message code="label.year" />: <input name="year" size="4"  class="required number"/><fr:tooltip code='fedreg.help.report.year' /></label>

							<a href="#" onClick="if($('#reportrequirements').valid()){ fedreg.renderFederationReport('registrations'); } return false;" class="search-button"><g:message code="label.generate" /></a>
						</form>
					
						<div id="registrationsreport" class="revealable reportdata">
							<div class="description">
								<h4 id="registrationstitle"></h4>
							</div>

							<div id="registrationsdata">
							</div>
							
							<br><br>
							<table align="center">
								<thead>
									<tr>
										<th><g:message code="label.datecreated" /></th>
										<th><g:message code="label.id" /></th>
										<th><g:message code="label.name" /></th>
										<th></th>
									</tr>
								</thead>
								<tbody id="registrationslist">
								</tbody>
							</table>
						</div>

						<div id="registrationsreportnodata" class="revealable reportdata">
							<p><em><g:message code="fedreg.templates.reports.nodata.description"/></em></p>
						</div>

						<script type="text/javascript+protovis">
							fedreg.renderFederationRegistrations = function(data, refine) {

									$('.reportdata').hide();
									$('#registrationsdata').empty();
									$("#registrationslist").empty();
									$('#registrationstitle').html(data.title);
									
									$.each( data.registrations, function(index, r) {
											$("#registrationslist").append("<tr><td>"+r.date+"</td><td>"+r.id+"</td><td>"+r.name+"</td><td><a href='"+r.manage+"' class='view-button'>${g.message(code:"label.view")}</td></tr>");
									});
									fedreg.stylebuttons($("#registrationslist"));
		
									var canvas = document.createElement("div");
									$('#registrationsdata').append(canvas);
			
									/* Sizing and scales. */
									var w = 800,
										h = 400,
										x = pv.Scale.linear(data.totals, function(d) d.t).range(0, w),
										y = pv.Scale.linear(0, data.max + 5).range(0, h),
										i = -1;

									/* The root panel. */
									var vis = new pv.Panel()
									.canvas(canvas)
										.width(w)
										.height(h)
										.bottom(60)
										.left(80)
										.right(5)
										.top(5);

									/* Y-axis and ticks. */
									vis.add(pv.Rule)
										.data(y.ticks(4))
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
										.data(x.ticks(data.totals.length))
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
										.fillStyle("rgb(121,173,210)")
					
									 var line = area.anchor("top").add(pv.Line)
										.lineWidth(3)
			
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
										.text(function(d) "Registrations: " + d.c); 
				
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
									$('#registrationsreport').fadeIn();
								
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
