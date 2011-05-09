
<div id="loginsreport" class="revealable reportdata">
	<div class="description">
		<h4 id="loginstitle"></h4>
		<p><g:message code="fedreg.templates.reports.serviceprovider.logins.description"/></p>
	</div>

	<div id="loginsdata">
	</div>
</div>

<div id="loginsreportnodata" class="revealable reportdata">
	<p><em><g:message code="fedreg.templates.reports.nodata.description"/></em></p>
</div>

<script type="text/javascript+protovis">
	fedreg.renderSPLogins = function(data) {
		$('.reportdata').hide();
		if(data.populated) {
			$('#loginsdata').empty();
			$('#loginstitle').html(data.title);
				
			var canvas = document.createElement("div");
			$('#loginsdata').append(canvas);

			var w = 800,
				h = 400,
				x = pv.Scale.linear(data.logins, function(d) d.hour).range(0, w),
				y = pv.Scale.linear(0, data.maxlogins + 5).range(0, h),
				i = -1;
				
			/* The root panel. */
			var vis = new pv.Panel()
			.canvas(canvas)
				.width(w)
				.height(h)
				.bottom(20)
				.left(80)
				.right(5)
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
				.data(x.ticks(data.logins.length))
				.visible(function(d) d)
				.left(x)
				.bottom(-5)
				.height(5)
			  .anchor("bottom").add(pv.Label)
				.text(function(d) d + ":00");

			/* The area with top line. */
			var area = vis.add(pv.Area)
				.data(data.logins)
				.left(function(d) x(d.hour))
				.height(function(d) y(d.count))
				.bottom(1)
				.fillStyle("rgb(152,223,138)")

			 var line = area.anchor("top").add(pv.Line)
				.lineWidth(3)
				.strokeStyle("rgb(44,160,44)");

			 var dot = line.add(pv.Dot)
				.visible(function() i >= 0)
				.data(function() [data.logins[i]])
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
			  	.textStyle("#111")
				.text(function(d) "Sessions: " + d.count); 

				vis.add(pv.Bar)
					.fillStyle("rgba(0,0,0,.001)")
					.event("mouseout", function() {
						i = -1;
						return vis;
					  })
					.event("mousemove", function() {
						var mx = x.invert(vis.mouse().x);
						i = pv.search(data.logins.map(function(d) d.hour), mx);
						i = i < 0 ? (-i - 2) : i;
						return vis;
					  });

			vis.render();
			$('#loginsreport').fadeIn();
		}
		else {
			$('#loginsreportnodata').fadeIn();
		}
	};
</script>
