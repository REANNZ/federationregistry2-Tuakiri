
<div id="connectivityreport" class="revealable reportdata">
	<div class="description">
		<h4 id="connectivitytitle"></h4>
		<p><g:message code="fedreg.templates.reports.identityprovider.connectivity.description"/></p>

		<div class="reportrefinement">
			<form id='connectivityrefinement' class="reportrefinementinput loadhide">
				<h5><g:message code="fedreg.templates.reports.identityprovider.connectivity.refinement.title"/> ( <a href="#" onClick="$('#connectivitycomponents :unchecked').attr('checked', true); return false;"><g:message code="label.addallchecks" /></a> | <a href="#" onClick="$('#connectivitycomponents :checked').attr('checked', false); return false;"><g:message code="label.removeallchecks" /></a> )</h5>
				<input type="hidden" name='activesp' value='0'/>
			
				<span id="connectivitycomponents" class="reportrefinementcomponents">
				</span>
			
				<div  class="buttons">
					<a href="#" onClick="fedreg.refineIdPReport($('#connectivityrefinement')); return false;" class="update-button"><g:message code="label.update" /></a>
					<a href="#" onClick="$('.reportrefinementinput').slideUp(); $('.reportrefinementopen').show(); return false;" class="close-button"><g:message code="label.close" /></a>
				</div>
			</form>
			<div class="reportrefinementopen">
				<a href="#" onClick="$('.reportrefinementopen').hide(); $('.reportrefinementinput').slideDown(); return false;" class="download-button"><g:message code="label.refine" /></a>
			</div>
		</div>
	</div>
	<div id="connectivitydata">
	</div>

</div>

<div id="connectivityreportnodata" class="revealable reportdata">
	<p><em><g:message code="fedreg.templates.reports.nodata.description"/></em></p>
</div>	

<script type="text/javascript+protovis">
	fedreg.renderIdPConnectivity = function(data, refine) {
		if(refine || data.populated) {
			$('#connectivitydata').empty();
			$('#connectivitytitle').html(data.title);
		
			if(refine) {
				if(!data.populated) {
					$('#connectivityreportnodata').fadeIn();
					return;
				} else
					$('#connectivityreportnodata').hide();
			} else {
				$('.reportdata').hide();
				
				$('#connectivitycomponents').empty();
				$.each( data.services, function(index, sp) {
					if(sp.rendered)
						$("#connectivitycomponents").append("<label class='choice'><input type='checkbox' checked='checked' name='activesp' value='"+sp.id+"'></input>"+sp.name+"</label>");
					else
						$("#connectivitycomponents").append("<label class='choice'><input type='checkbox' name='activesp' value='"+sp.id+"'></input>"+sp.name+"</label>");
				});
			}
		
			var canvas = document.createElement("div");
			$('#connectivitydata').append(canvas);
			var vis = new pv.Panel()
				.canvas(canvas)
				.width(900)
				.height(500)
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