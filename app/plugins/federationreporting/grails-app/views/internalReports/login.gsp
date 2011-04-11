<html>
	<head>
		<title>XX</title>
		<r:use modules="html5, jquery-ui, nimble, nimble-ui, tiptip, jgrowl, protvis, datejs"/>
		<r:layoutResources/>
	</head>
	
	<body>

		<script type="text/javascript+protovis">
			var showUserLink = "${createLink(controller:'user', action:'show' )}";
			
			var data;
			var i = -1;
			
			$.ajax({url:'/federationregistry/reporting/internal/dailylogins?year=2011&month=3', 
					dataType: 'json',
					async:false, 
					success: function(d){
			    		data = d;
					  }
					});
					
			var w = 400,
			    h = 200,
				x = pv.Scale.linear(data, function(d) new Date(d.date)).range(4, w),
			    y = pv.Scale.linear(0, 16).range(0, h);
		
			var vis = new pv.Panel()
			    .width(w)
			    .height(h)
			    .bottom(20)
			    .left(20)
			    .right(20)
			    .top(5);

			vis.add(pv.Rule)
			    .data(x.ticks())
			    .left(x)
			    .strokeStyle("#ccc")
			  .anchor("bottom").add(pv.Label)
			    .text(x.tickFormat);

			vis.add(pv.Rule)
			    .data(y.ticks(10))
			    .bottom(y)
			    .strokeStyle(function(d) d ? "#eee" : "#000")
			  .anchor("left").add(pv.Label)
			    .text(y.tickFormat);

			var line = vis.add(pv.Line)
			    .data(data)
			    .left(function(d) x(d.date))
			    .bottom(function(d) y(d.count))
			    .lineWidth(4);
			
			var dot = line.add(pv.Dot)
			    .visible(function() i >= 0)
			    .data(function() [data[i]])
			    .fillStyle(function() line.strokeStyle())
			    .strokeStyle("#000")
			    .size(20)
			    .lineWidth(1);
			dot.anchor("top").add(pv.Label)
				.text(function(d) d.count);
			
			vis.add(pv.Bar)
			    .fillStyle("rgba(0,0,0,.001)")
			    .event("mouseout", function() {
			        i = -1;
			        return vis;
			      })
			    .event("mousemove", function() {
			        var mx = x.invert(vis.mouse().x);
			        i = pv.search(data.map(function(d) d.date), mx);
			        i = i < 0 ? (-i - 2) : i;
			        return vis;
			      });
				
			vis.render();
			
			$.each(data, function(i,rec) {
				$("#logins").append("<tr><td>"+new Date(rec.date).toString('dd/MM/yyyy')+" (" + rec.count + ")</td><td/></tr>")
				$.each(rec.sessions, function(j, session){
					$("#logins").append("<tr><td><td>"+new Date(session.time).toString('hh:mm:ss')+"</td><td><a href=" + showUserLink + "/" + session.userID + ">"+session.fullName+"</a></td></tr>")
				});
			});
		</script>
		
		<table >
			<thead>
				<tr>
					<th>Date</th>
					<th>Time</th>
					<th>User</th>
				</tr>
			</thead>
			<tbody id="logins">
			</tbody>
		</table>
		
		<r:layoutResources/>
	</body>
</html>