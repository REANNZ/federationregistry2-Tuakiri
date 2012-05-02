<html>
  <head>
    <meta name="layout" content="reporting" />
    <r:require modules="protovis"/>
  </head>
  <body>
    <div class="centered">
      <form id="idp-connections-report-parameters" class="form-inline report-parameters well validating">
        <label for="idpID"><g:message code="label.identityprovider"/></label>
        <g:select name="idpID" from="${idpList}" optionKey="id" optionValue="displayName" class="span3"/>
        
        <label for="startDate"><g:message code="label.startdate"/></label>
        <input name="startDate" placeholder="start date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

        <label for="startDate"><g:message code="label.enddate"/></label>
        <input name="endDate" placeholder="end date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

        <a class="request-idp-connections-report btn"><g:message code="label.generate"/></a>
      </form>
    </div>

    <div class="row">
      <div class="span11">
        <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        <div id="idpconnectivity" class="span11 centered"></div>
        <div id="noconnectivity" class="hidden alert alert-error">No report data available.</div>
      </div>
    </div>

    <script type="text/javascript+protovis">
      var idpconnectionsEndpoint = "${createLink(controller:'identityProviderReports', action:'reportconnectivity')}"

      $(".request-idp-connections-report").click(function () {
        $('#idpconnectivity').hide();
        $('#noconnectivity').addClass('hidden');
        fr.showspinner();

        var form = $('#idp-connections-report-parameters');
        if(form.valid()) { 
          var params = form.serialize();
          
          $.getJSON(idpconnectionsEndpoint, params, function(data) {
            renderIdPConnectivity(data)
          });
        }
      });

      function renderIdPConnectivity(data) {
        if(!data.populated) {
          fr.hidespinner();
          $('#noconnectivity').removeClass('hidden');
          return;
        }

        $('#noconnectivity').addClass('hidden');
        $('#idpconnectivity').empty();
        var canvas = document.createElement("div");
        $('#idpconnectivity').append(canvas);
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
        $('#idpconnectivity').show();
      };

      $(document).ready(function() {
        var currentTime = new Date()
        var month = currentTime.getMonth() + 1
        var day = currentTime.getDate()
        var year = currentTime.getFullYear()
        $('input[name="startDate"]').val(year - 1 + "-" + month + "-" + day);   
        $('input[name="endDate"]').val(year + "-" + month + "-" + day); 
        $('.request-idp-connections-report').click();
      });
    </script>
  </body>
</html>