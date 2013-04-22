<html>
  <head>
    <meta name="layout" content="reporting" />
    <r:require modules="protovis"/>
  </head>
  <body>
    <div class="centered">
      <form id="sp-connections-report-parameters" class="form-inline report-parameters well validating">
        <label for="spID"><g:message encodeAs="HTML" code="label.serviceprovider"/></label>
        <g:select name="spID" from="${spList}" optionKey="id" optionValue="displayName" class="span3"/>
        
        <label for="startDate"><g:message encodeAs="HTML" code="label.startdate"/></label>
        <input name="startDate" placeholder="Start Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

        <label for="endDate"><g:message encodeAs="HTML" code="label.enddate"/></label>
        <input name="endDate" placeholder="End Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

        <a class="request-sp-connections-report btn"><g:message encodeAs="HTML" code="label.generate"/></a>
      </form>
    </div>

    <div class="row">
      <div class="span11">
        <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        <div id="spconnectivity" class="span11 centered"></div>
        <div id="noconnectivity" class="hidden alert alert-error">No report data available.</div>
      </div>
    </div>

    <script type="text/javascript+protovis">
      var spconnectionsEndpoint = "${createLink(controller:'serviceProviderReports', action:'reportconnectivity')}"

      $(".request-sp-connections-report").click(function () {
        $('#spconnectivity').hide();
        $('#noconnectivity').addClass('hidden');
        fr.showspinner();

        var form = $('#sp-connections-report-parameters');
        if(form.valid()) { 
          var params = form.serialize();
          
          $.getJSON(spconnectionsEndpoint, params, function(data) {
            renderSpConnectivity(data)
          });
        }
      });

      function renderSpConnectivity(data) {
        if(!data.populated) {
          fr.hidespinner();
          $('#noconnectivity').removeClass('hidden');
          return;
        }

        $('#noconnectivity').addClass('hidden');
        $('#spconnectivity').empty();
        var canvas = document.createElement("div");
        $('#spconnectivity').append(canvas);
        var vis = new pv.Panel()
          .canvas(canvas)
          .height(500)
          .width(900)
          .bottom(220);

        var arc = vis.add(pv.Layout.Arc).reset().nodes(data.nodes).links(data.links);

        var line = arc.link.add(pv.Line)

        var dot = arc.node.add(pv.Dot).size(function(d) d.linkDegree);

        arc.label.add(pv.Label)
        vis.render();
        fr.hidespinner();
        $('#spconnectivity').show();
      };

      $(document).ready(function() {
        fr.setup_date_range();
        $('.request-sp-connections-report').click();
      });
    </script>
  </body>
</html>
