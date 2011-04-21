
<div id="sessionsreport" class="revealable reportdata">
	<div class="description">
		<h4 id="sessionstitle"></h4>
		<p><g:message code="fedreg.templates.reports.identityprovider.sessions.description"/></p>
	</div>

	<div id="sessionsdata">
	</div>
</div>

<div id="sessionsreportnodata" class="revealable reportdata">
	<p><em><g:message code="fedreg.templates.reports.nodata.description"/></em></p>
</div>

<script type="text/javascript+protovis">
	fedreg.renderIdPSessions = function(data, refine) {
		
		if(data.populated) {
			$('.reportdata').hide();
			$('#sessionsdata').empty();
			$('#sessionstitle').html(data.title);
		
			var canvas = document.createElement("div");
			$('#sessionsdata').append(canvas);
			
			/* Sizing and scales. */
			var w = 700,
			    h = 300,
			    x = pv.Scale.linear(data.sessions, function(d) d.date).range(0, w),
			    y = pv.Scale.linear(0, data.maxlogins + 5).range(0, h),
				i = -1;

			/* The root panel. */
			var vis = new pv.Panel()
			.canvas(canvas)
			    .width(w)
			    .height(h)
			    .bottom(20)
			    .left(20)
			    .right(10)
			    .top(5);

			/* Y-axis and ticks. */
			vis.add(pv.Rule)
			    .data(y.ticks(5))
			    .bottom(y)
			    .strokeStyle(function(d) d ? "#eee" : "#000")
			  .anchor("left").add(pv.Label)
			    .text(y.tickFormat);

			/* X-axis and ticks. */
			vis.add(pv.Rule)
			    .data(x.ticks())
			    .visible(function(d) d)
			    .left(x)
			    .bottom(-5)
			    .height(5)
			  .anchor("bottom").add(pv.Label)
			    .text(x.tickFormat);

			/* The area with top line. */
			vis.add(pv.Area)
			    .data(data.sessions)
			    .left(function(d) x(d.date))
			    .height(function(d) y(d.count))
			    .bottom(1)
			    .fillStyle("rgb(121,173,210)")
				.event("mouseout", function() {
				        i = -1;
				        return vis;
				      })
				    .event("mousemove", function() {
				        var mx = x.invert(vis.mouse().date);
				        i = pv.search(data.sessions.map(function(d) d.date), mx);
				        i = i < 0 ? (-i - 2) : i;
				        return vis;
				      })
			  .anchor("top").add(pv.Line)
			    .lineWidth(3);

			vis.render();
			$('#sessionsreport').fadeIn();
		} else {
			$('.reportdata').hide();
			$('#sessionsreportnodata').fadeIn();
		}
	};
</script>