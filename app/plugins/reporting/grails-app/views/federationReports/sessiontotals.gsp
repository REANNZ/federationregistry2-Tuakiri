<html>
	<head>
		<meta name="layout" content="reporting" />
		<title><g:message code="fedreg.view.reporting.federation.sessiontotals.title" /></title>
		
		<r:script>
			var federationReportsSessionTotalsEndpoint = "${createLink(controller:'federationReports', action:'totalsjson')}"
		</r:script>
	</head>
	<body>
		<section>
			<div class="reportingsupported">
				<h2><g:message code="fedreg.view.reporting.federation.sessiontotals.heading" /></h2>
			
				<div id="reporting">
					<div id="reports">
				
						<form id="reportrequirements">	
							<label><g:message code="label.day" />: <input name="day" size="2" class="number"/><fr:tooltip code='fedreg.help.report.day' /></label>
							<label><g:message code="label.month" />: <input name="month" size="2" class="number"/><fr:tooltip code='fedreg.help.report.month' /></label>
							<label><g:message code="label.year" />: <input name="year" size="4"  class="required number"/><fr:tooltip code='fedreg.help.report.year' /></label>

							<a href="#" onClick="if($('#reportrequirements').valid()){ fedreg.renderFederationReport('sessiontotals'); } return false;" class="search-button"><g:message code="label.generate" /></a>
						</form>
					
						<div id="servicesreport" class="revealable reportdata">
							<div class="description">
								<h3 id="servicestitle"></h3>
								<p><g:message code="fedreg.view.reporting.federation.sessiontotals.period.description"/></p>

								<div class="reportrefinement">
									<div class="reportrefinementinput revealable">
										<form id='servicesrefinement'>
											<input type="hidden"
 name='activesp' value='0'/>
			
											<span id="servicescomponents" class="reportrefinementcomponents">
												<h5><g:message code="fedreg.view.reporting.federation.sessiontotals.topten.refinement.title"/> ( <a href="#" onClick="$('#servicescomponentstopten :unchecked').attr('checked', true); return false;"><g:message code="label.addallchecks" /></a> | <a href="#" onClick="$('#servicescomponentstopten :checked').attr('checked', false); return false;"><g:message code="label.removeallchecks" /></a> )</h5>
												<span id="servicescomponentstopten" class="reportrefinementcomponents">
												</span>
												<hr>
												<h5><g:message code="fedreg.view.reporting.federation.sessiontotals.remaining.refinement.title"/> ( <a href="#" onClick="$('#servicescomponentsremaining :unchecked').attr('checked', true); return false;"><g:message code="label.addallchecks" /></a> | <a href="#" onClick="$('#servicescomponentsremaining :checked').attr('checked', false); return false;"><g:message code="label.removeallchecks" /></a> )</h5>
												<span id="servicescomponentsremaining" class="reportrefinementcomponents">
												</span>
											</span>
			
											<h5><g:message code="fedreg.view.reporting.federation.sessiontotals.maxmin.refinement.title"/> ( <a href="#" onClick="$('#.reportrefinementval :input').val(''); return false;"><g:message code="label.clear" /></a>  )</h5>
											<div class="reportrefinementval">
												<label><g:message code="label.min" /><input name="min" size="4" value="" class="number"/><fr:tooltip code='fedreg.help.report.min' /></label>
												<label><g:message code="label.max" /><input name="max" size="4" value="" class="number"/><fr:tooltip code='fedreg.help.report.max' /></label>
											</div>
											<div class="buttons">
												<a href="#" onClick="fedreg.refineFederationReport('sessiontotals', $('#servicesrefinement')); return false;" class="update-button"><g:message code="label.update" /></a>
												<a href="#" onClick="fedreg.closeRefinement();" class="close-button"><g:message code="label.close" /></a>
											</div>
										</form>
									</div>
									<div class="reportrefinementopen">
										<a href="#" onClick="fedreg.openRefinement();" class="download-button"><g:message code="label.refine" /></a>
									</div>
								</div>
							</div>

							<div id="servicesdata">
								<div class="description">
									<h4><g:message code="fedreg.view.reporting.federation.sessiontotals.totals" /></h4>
								</div>
								<div id="servicesdatatotals" class="dynamic">
								</div>
								<br><br>
								<div class="description">
									<h4><g:message code="fedreg.view.reporting.federation.sessiontotals.percentage" /></h4>
								</div>
								<div id="servicesdatapercent" class="dynamic">
								</div>
							</div>
						</div>

						<div id="servicesreportnodata" class="revealable reportdata">
							<p><em><g:message code="fedreg.templates.reports.nodata.description"/></em></p>
						</div>

						<script type="text/javascript+protovis">
							fedreg.renderFederationServices = function(data, refine) {
								if(refine || data.populated) {
									$('.dynamic').empty();
									$('#servicestitle').html(data.title);
		
									if(refine) {
										if(!data.populated) {
											$('#servicesreportnodata').fadeIn();
											return;
										} else {
											$('#servicesreportnodata').hide();
										}
									} else {
										$('.reportdata').hide();
										$('#servicescomponentstopten').empty();
										$('#servicescomponentsremaining').empty();
										$.each( data.toptenservices, function(index, sp) {
											if(sp.rendered)
												$("#servicescomponentstopten").append("<label class='choice'><input type='checkbox' checked='checked' name='activesp' value='"+sp.id+"'></input>"+sp.name+"</label>");
											else
												$("#servicescomponentstopten").append("<label class='choice'><input type='checkbox' name='activesp' value='"+sp.id+"'></input>"+sp.name+"</label>");
										});
										if(data.remainingservices) {
											$.each( data.remainingservices, function(index, sp) {
												if(sp.rendered)
													$("#servicescomponentsremaining").append("<label class='choice'><input type='checkbox' checked='checked' name='activesp' value='"+sp.id+"'></input>"+sp.name+"</label>");
												else
													$("#servicescomponentsremaining").append("<label class='choice'><input type='checkbox' name='activesp' value='"+sp.id+"'></input>"+sp.name+"</label>");
											});
										}
									}
		
									var canvas = document.createElement("div");
									$('#servicesdatatotals').append(canvas);

									var w = 840,
										h = data.values.length * 25,
										x = pv.Scale.linear(0, data.maxsessions).range(0, w),
										y = pv.Scale.ordinal(pv.range(data.values.length)).splitBanded(0, h, 4/5);

									var vis = new pv.Panel()
										.canvas(canvas)
										.width(w)
										.height(h)
										.bottom(20)
										.left(240)
										.right(10)
										.top(5);

									var bar = vis.add(pv.Bar)
										.data(data.values)
										.top(function() y(this.index))
										.height(y.range().band)
										.left(0)
										.width(x)
										.fillStyle("rgb(148,103,189)")
										.text(function(d) d + " ( " + ((d / data.totalsessions) * 100).toFixed(3) + "%" + " of sessions for period )")
										.event("mouseover", pv.Behavior.tipsy({gravity:'w', fade:true}));

									bar.anchor("left").add(pv.Label)
										.textMargin(10)
										.textAlign("right")
										.text(function(d) data.valuelabels[this.index]);

									vis.add(pv.Rule)
										.data(x.ticks(10))
										.left(x)
										.strokeStyle(function(d) d ? "rgba(255,255,255,.3)" : "#000")
									  .add(pv.Rule)
										.bottom(0)
										.height(5)
										.strokeStyle("#000")
									  .anchor("bottom").add(pv.Label)
										.text(x.tickFormat);
									vis.render();
									
									var canvas2 = document.createElement("div");
									$('#servicesdatapercent').append(canvas2);

									var w2 = 900,
										h2 = data.values.length * 18 < 450 ? 450 : data.values.length * 18,
										r = 200,
										c = pv.Colors.category20(),
										a = pv.Scale.linear(0, pv.sum(data.values)).range(0, 2 * Math.PI);

									var vis2 = new pv.Panel()
										.canvas(canvas2)
										.top(0)
										.left(100)
										.width(w2)
										.height(h2);

									vis2.add(pv.Wedge)
										.data(data.values)
										.top(225)
										.left(400)
										.outerRadius(r)
										.angle(a)
										.fillStyle(function(d) c(d))
			
									vis2.add(pv.Dot)
										.data(data.values)
										.right(250)
										.top(function() 20 + this.index * 16)
										.fillStyle(function(d) c(d))
										.strokeStyle(null)
										.size(30)
									  .anchor("right").add(pv.Label)
										.textMargin(6)
										.textAlign("left")
										.text(function(d) data.valuelabels[this.index] + " - " + ((d / data.totalsessions) * 100).toFixed(3) + "%");

									vis2.render();
								
									$('#servicesreport').fadeIn();
								} else {
									$('.reportdata').hide();
									$('#servicesreportnodata').fadeIn();
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
