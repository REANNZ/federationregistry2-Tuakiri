<h4 id="arcreporttitle"></h4>
<script type="text/javascript+protovis">
	$.ajax({url:"/federationregistry/reporting/internal/arcjson/${id}?year=${year}&month=${month}", 
		dataType: 'json',
		async:false, 
		success: function(d){
    		data = d;
			$('#arcreporttitle').append(d.title);
				
		  }
		});

	var vis = new pv.Panel()
	    .width(800)
	    .height(400)
	    .bottom(200);

	var arc = vis.add(pv.Layout.Arc)
	    .nodes(data.nodes)
	    .links(data.links)
		.sort(function(a, b) a.group == b.group
		        ? a.linkDegree - b.linkDegree
		        : a.group - b.group);
	
	var line = arc.link.add(pv.Line)
		

	var dot = arc.node.add(pv.Dot)
	    .size(function(d) d.linkDegree * 5);

	arc.label.add(pv.Label)
	vis.render();
</script>