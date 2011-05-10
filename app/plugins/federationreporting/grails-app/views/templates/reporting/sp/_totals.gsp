
<div id="totalsreport" class="revealable reportdata">
	<div class="description">
		<h4 id="totalstitle"></h4>
		<p><g:message code="fedreg.templates.reports.serviceprovider.totals.description"/></p>

		<div class="reportrefinement">
			<div class="reportrefinementinput revealable">
				<form id='totalsrefinement'>
					<h5><g:message code="fedreg.templates.reports.serviceprovider.totals.refinement.title"/> ( <a href="#" onClick="$('#totalscomponents :unchecked').attr('checked', true); return false;"><g:message code="label.addallchecks" /></a> | <a href="#" onClick="$('#totalscomponents :checked').attr('checked', false); return false;"><g:message code="label.removeallchecks" /></a> )</h5>
					<input type="hidden" name='activeidp' value='0'/>
			
					<span id="totalscomponents" class="reportrefinementcomponents">
					</span>
			
					<h5><g:message code="fedreg.templates.reports.serviceprovider.totals.refinement.maxmin.title"/> ( <a href="#" onClick="$('#.reportrefinementval :input').val(''); return false;"><g:message code="label.clear" /></a>  )</h5>
					<div class="reportrefinementval">
						<label><g:message code="label.min" /><input name="min" size="4" value="" class="number"/><fr:tooltip code='fedreg.help.report.min' /></label>
						<label><g:message code="label.max" /><input name="max" size="4" value="" class="number"/><fr:tooltip code='fedreg.help.report.max' /></label>
					</div>
					<div class="buttons">
						<a href="#" onClick="fedreg.refineSPReport($('#totalsrefinement')); return false;" class="update-button"><g:message code="label.update" /></a>
						<a href="#" onClick="fedreg.closeRefinement();" class="close-button"><g:message code="label.close" /></a>
					</div>
				</form>
			</div>
			<div class="reportrefinementopen">
				<a href="#" onClick="fedreg.openRefinement();" class="download-button"><g:message code="label.refine" /></a>
			</div>
		</div>
	</div>

	<div id="totalsdata">
	</div>
</div>

<div id="totalsreportnodata" class="revealable reportdata">
	<p><em><g:message code="fedreg.templates.reports.nodata.description"/></em></p>
</div>

<script type="text/javascript+protovis">
	fedreg.renderSPTotals = function(data, refine) {
		if(refine || data.populated) {
			$('#totalsdata').empty();
			$('#totalstitle').html(data.title);
		
			if(refine) {
				if(!data.populated) {
					$('#totalsreportnodata').fadeIn();
					return;
				} else {
					$('#totalsreportnodata').hide();
				}
			} else {
				$('.reportdata').hide();
				$('#totalscomponents').empty();
				$.each( data.providers, function(index, idp) {
					if(idp.rendered)
						$("#totalscomponents").append("<label class='choice'><input type='checkbox' checked='checked' name='activeidp' value='"+idp.id+"'></input>"+idp.name+"</label>");
					else
						$("#totalscomponents").append("<label class='choice'><input type='checkbox' name='activeidp' value='"+idp.id+"'></input>"+idp.name+"</label>");
				});
			}
		
			var canvas = document.createElement("div");
			$('#totalsdata').append(canvas);

			var w = 900,
			    h = data.bars.length * 25,
			    x = pv.Scale.linear(0, data.maxlogins).range(0, w),
			    y = pv.Scale.ordinal(pv.range(data.providercount + 1)).splitBanded(0, h, 4/5);

			var vis = new pv.Panel()
				.canvas(canvas)
			    .width(w)
			    .height(h)
			    .bottom(20)
			    .left(200)
			    .right(10)
			    .top(5);

			var bar = vis.add(pv.Bar)
			    .data(data.bars)
			    .top(function() y(this.index))
			    .height(y.range().band)
			    .left(0)
			    .width(x)
				.fillStyle("rgb(148,103,189)")
			    .text(function(d) d)
				.event("mouseover", pv.Behavior.tipsy({gravity:'w', fade:true}));

			bar.anchor("left").add(pv.Label)
			    .textMargin(10)
			    .textAlign("right")
			    .text(function(d) data.barlabels[this.index]);

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
			$('#totalsreport').fadeIn();
		} else {
			$('.reportdata').hide();
			$('#totalsreportnodata').fadeIn();
		}
	};
</script>
