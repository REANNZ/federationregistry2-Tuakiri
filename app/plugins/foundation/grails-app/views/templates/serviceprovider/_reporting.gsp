<div class="tabbable">
  <ul class="nav nav-tabs">
    <li class="active"><a href="#tab-sessions" data-toggle="tab"><g:message code="label.sessions" /></a></li>
    <li><a href="#tab-utilization" data-toggle="tab"><g:message code="label.utilisation" /></a></li>
    <li><a href="#tab-demand" data-toggle="tab"><g:message code="label.demand" /></a></li>
    <li><a href="#tab-connections" data-toggle="tab"><g:message code="label.connections" /></a></li>
  </ul>

  <div class="tab-content">

    <div id="tab-sessions" class="tab-pane active">
      <div class="centered">
        <form id="detailed-spsessions-report-parameters" class="form-inline report-parameters well validating">
          <g:hiddenField name="spID" value="$spID" />

          <label for="startDate"><g:message code="label.startdate"/></label>
          <input name="startDate" placeholder="start date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2" type="text"/>

          <label for="startDate"><g:message code="label.enddate"/></label>
          <input name="endDate" placeholder="end date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2" type="text"/>

          <a class="request-detailed-spsessions-report generate-btn btn"><g:message code="label.generate"/></a>
          <a class="export-detailed-spsessions-report export-button btn btn-info hidden" rel="tooltip" title="${g.message(code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
        </form>
      </div>

      <div class="row">
        <div class="span11">
          <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
          <div id="detailedspsessionschart">
            <div class="span9 generate-notice offset1 alert alert-info"><g:message code="label.generate"/></div>
          </div>
        </div>
      </div>
    </div>

    <div id="tab-utilization" class="tab-pane">
      <div class="centered">
        <form id="detailed-detailedsptoidputilization-report-parameters" class="form-inline report-parameters well validating">
          <g:hiddenField name="spID" value="$spID" />

          <label for="startDate"><g:message code="label.startdate"/></label>
          <input name="startDate" placeholder="start date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

          <label for="startDate"><g:message code="label.enddate"/></label>
          <input name="endDate" placeholder="end date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

          <a class="request-detailed-detailedsptoidputilization-reports generate-btn btn"><g:message code="label.generate"/></a>
          <a class="export-detailed-detailedsptoidputilization-reports export-button btn btn-info hidden" rel="tooltip" title="${g.message(code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
        </form>
      </div>

      <div class="row">
        <div class="span11">
          <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
          <div id="detailedsptoidputilization">
            <div class="span9 generate-notice offset1 alert alert-info"><g:message code="label.generate"/></div>
          </div>
        </div>
      </div>
      <div class="row row-spacer">
        <div class="span11">
          <div id="detailedsptoidputilizationtotals"></div>
        </div>
      </div>

      <div id="refine-detailedsptoidputilization-content" class="row row-spacer hidden">
        <h3 class="span9">Refine Graph</h3>
        <form id="refine-detailedsptoidputilization-report-parameters" class="form-inline">
          <div class="span11 topten hidden">
            <h4 class="span9">Top Ten Services</h4>
            <a class="select-all-topten-services"><g:message code="label.addallchecks" /></a> | <a class="unselect-all-topten-services"><g:message code="label.removeallchecks" /></a>
            <div id="topten-utilized-services" class="span11 services"></div>

            <div class="span2 offset9">
              <a class="btn btn-success request-refine-detailedsptoidputilization-content"><g:message code="label.refine" /></a>
            </div>
          </div>
          <div class="span11 remainder hidden">
            <hr>
            <h4 class="span9">Remaining Services</h4>
            <a class="select-all-remaining-services"><g:message code="label.addallchecks" /></a> | <a class="unselect-all-remaining-services"><g:message code="label.removeallchecks" /></a>
            <div id="remaning-utilized-services" class="span11 services"></div>
            <div class="span2 offset9">
              <a class="btn btn-success request-refine-detailedsptoidputilization-content"><g:message code="label.refine" /></a>
            </div>
          </div>
        </form>
      </div>
    </div>

    <div id="tab-demand" class="tab-pane">
      <div class="centered">
        <form id="detailed-spdemand-report-parameters" class="form-inline report-parameters well validating">
          <g:hiddenField name="spID" value="$spID" />
          
          <label for="startDate"><g:message code="label.startdate"/></label>
          <input name="startDate" placeholder="start date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

          <label for="startDate"><g:message code="label.enddate"/></label>
          <input name="endDate" placeholder="end date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

          <a class="request-detailed-spdemand-report generate-btn btn"><g:message code="label.generate"/></a>
          <a class="export-detailed-spdemand-report export-button btn btn-info hidden" rel="tooltip" title="${g.message(code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
        </form>
      </div>

      <div class="row">
        <div class="span11">
          <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
          <div id="spdemanddetailed">
            <div class="span9 generate-notice offset1 alert alert-info"><g:message code="label.generate"/></div>
          </div>
        </div>
      </div>
    </div>

    <div id="tab-connections" class="tab-pane">
      <div class="centered">
        <form id="sp-connections-report-parameters" class="form-inline report-parameters well validating">
          <g:hiddenField name="spID" value="$spID" />
          
          <label for="startDate"><g:message code="label.startdate"/></label>
          <input name="startDate" placeholder="start date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

          <label for="startDate"><g:message code="label.enddate"/></label>
          <input name="endDate" placeholder="end date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

          <a class="request-sp-connections-report generate-btn btn"><g:message code="label.generate"/></a>
        </form>
      </div>

      <div class="row">
        <div class="span11">
          <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
          <div class="span9 generate-notice offset1 alert alert-info"><g:message code="label.generate"/></div>
          <div id="spconnectivity" class="span11 centered"></div>
          <div id="noconnectivity" class="hidden alert alert-error">No report data available.</div>
        </div>
      </div>

      <script type="text/javascript+protovis">
        var spconnectionsEndpoint = "${createLink(controller:'serviceProviderReports', action:'reportconnectivity')}"

        $(".request-sp-connections-report").click(function () {
          $('#spconnectivity').hide();
          $('#noconnectivity').addClass('hidden');
          fedreg.showspinner();

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
            fedreg.hidespinner();
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
          fedreg.hidespinner();
          $('#spconnectivity').show();
        };
      </script>
    </div>
  </div>

  <r:script>
    var detailedspsessionsEndpoint = "${createLink(controller:'serviceProviderReports', action:'reportsessions')}"
    var detailedsptoidputilizationEndpoint = "${createLink(controller:'serviceProviderReports', action:'reportidputilization')}"
    var detailedspdemandEndpoint = "${createLink(controller:'serviceProviderReports', action:'reportdemand')}"

    $(".generate-btn").click(function() {
      $('.generate-notice').hide();
    });

    $(document).ready(function() {
      fedreg.hidespinner();

      var currentTime = new Date()
      var month = currentTime.getMonth() + 1
      var day = currentTime.getDate()
      var year = currentTime.getFullYear()
      $('input[name="startDate"]').val(year - 1 + "-" + month + "-" + day);   
      $('input[name="endDate"]').val(year + "-" + month + "-" + day); 
    });
  </r:script>
</div>