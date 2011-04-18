
<div id="loginsreport" class="revealable reportdata">
	<div class="description">
		<h4 id="loginstitle"></h4>
		<p><g:message code="fedreg.templates.reports.identityprovider.logins.description"/></p>
	</div>

	<div id="loginsdata">
	</div>
</div>

<div id="loginsreportnodata" class="revealable reportdata">
	<p><em><g:message code="fedreg.templates.reports.nodata.description"/></em></p>
</div>

<script type="text/javascript+protovis">
	fedreg.renderIdPLogins = function(data) {
		$('.reportdata').hide();
		if(data.populated) {
			$('#loginsdata').empty();
			$('#loginstitle').html(data.title);
				
			var canvas = document.createElement("div");
			$('#loginsdata').append(canvas);

			var w = 900,
			    h = 400,
			    r = 200,
				c = pv.Colors.category20(),
			    a = pv.Scale.linear(0, pv.sum(data.values)).range(0, 2 * Math.PI);

			var vis = new pv.Panel()
				.canvas(canvas)
				.top(0)
				.left(100)
			    .width(w)
			    .height(h);

			vis.add(pv.Wedge)
			    .data(data.values)
			    .bottom(200)
			    .left(400)
			    .innerRadius(r - 60)
			    .outerRadius(r)
			    .angle(a)
				.fillStyle(function(d) c(d))
			    .event("mouseover", function() this.innerRadius(0))
			    .event("mouseout", function() this.innerRadius(r - 40))
			
			vis.add(pv.Dot)
			    .data(data.values)
			    .left(10)
			    .top(function() 20 + this.index * 16)
			    .fillStyle(function(d) c(d))
			    .strokeStyle(null)
			    .size(30)
			  .anchor("right").add(pv.Label)
			    .textMargin(6)
			    .textAlign("left")
				.text(function(d) data.labels[this.index] + ":00 - " + (data.labels[this.index] + 1) + ":00 (" + d + " logins)");

			vis.render();
			$('#loginsreport').fadeIn();
		}
		else {
			$('#loginsreportnodata').fadeIn();
		}
	};
</script>