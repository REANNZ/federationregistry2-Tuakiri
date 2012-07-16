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
        <form id="detailed-idpsessions-report-parameters" class="form-inline report-parameters well validating">
          <g:hiddenField name="idpID" value="$idpID" />

          <label for="startDate"><g:message code="label.startdate"/></label>
          <input name="startDate" placeholder="Start Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

          <label for="endDate"><g:message code="label.enddate"/></label>
          <input name="endDate" placeholder="End Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

          <a class="generate-btn request-detailed-idpsessions-report btn"><g:message code="label.generate"/></a>
          <a class="export-detailed-idpsessions-report export-button btn btn-info hidden" rel="tooltip" title="${g.message(code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
        </form>
      </div>

      <div class="row">
        <div class="span11">
          <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
          <div id="detailedidpsessionschart">
            <div class="span9 generate-notice offset1 alert alert-info"><g:message code="templates.fr.identityprovider.reporting.generate"/></div>
          </div>
        </div>
      </div>
    </div>

    <div id="tab-utilization" class="tab-pane">
      <div class="centered">
        <form id="detailed-detailedidptoserviceutilization-report-parameters" class="form-inline report-parameters well validating">
          <g:hiddenField name="idpID" value="$idpID" />

          <label for="startDate"><g:message code="label.startdate"/></label>
          <input name="startDate" placeholder="Start Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

          <label for="endDate"><g:message code="label.enddate"/></label>
          <input name="endDate" placeholder="End Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

          <a class="generate-btn request-detailed-detailedidptoserviceutilization-reports btn"><g:message code="label.generate"/></a>
          <a class="export-detailed-detailedidptoserviceutilization-reports export-button btn btn-info hidden" rel="tooltip" title="${g.message(code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
        </form>
      </div>

      <div class="row">
        <div class="span11">
          <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
          <div id="detailedidptoserviceutilization"></div>
        </div>
      </div>
      <div class="row">
        <div class="span11">
          <div id="detailedidptoserviceutilizationtotals">
            <div class="span9 offset1 alert alert-info generate-notice"><g:message code="templates.fr.identityprovider.reporting.generate"/></div>
          </div>
        </div>
      </div>

      <div id="refine-detailedidptoserviceutilization-content" class="row row-spacer hidden">
        <h3 class="span9">Refine Graph</h3>
        <form id="refine-detailedidptoserviceutilization-report-parameters" class="form-inline">
          <div class="span11 topten hidden">
            <h4 class="span9">Top Ten Services</h4>
            <a class="select-all-topten-services"><g:message code="label.addallchecks" /></a> | <a class="unselect-all-topten-services"><g:message code="label.removeallchecks" /></a>
            <div id="topten-utilized-services" class="span11 services"></div>

            <div class="span2 offset9">
              <a class="btn btn-success request-refine-detailedidptoserviceutilization-content"><g:message code="label.refine" /></a>
            </div>
          </div>
          <div class="span11 remainder hidden">
            <hr>
            <h4 class="span9">Remaining Services</h4>
            <a class="select-all-remaining-services"><g:message code="label.addallchecks" /></a> | <a class="unselect-all-remaining-services"><g:message code="label.removeallchecks" /></a>
            <div id="remaning-utilized-services" class="span11 services"></div>
            <div class="span2 offset9">
              <a class="btn btn-success request-refine-detailedidptoserviceutilization-content"><g:message code="label.refine" /></a>
            </div>
          </div>
        </form>
      </div>
    </div>

    <div id="tab-demand" class="tab-pane">
      <div class="centered">
        <form id="detailed-idpdemand-report-parameters" class="form-inline report-parameters well validating">
          <g:hiddenField name="idpID" value="$idpID" />
          
          <label for="startDate"><g:message code="label.startdate"/></label>
          <input name="startDate" placeholder="Start Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

          <label for="endDate"><g:message code="label.enddate"/></label>
          <input name="endDate" placeholder="End Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

          <a class="generate-btn request-detailed-idpdemand-report btn"><g:message code="label.generate"/></a>
          <a class="export-detailed-idpdemand-report export-button btn btn-info hidden" rel="tooltip" title="${g.message(code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
        </form>
      </div>

      <div class="row">
        <div class="span11">
          <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
          <div id="idpdemanddetailed">
            <div class=" span9 offset1 alert alert-info generate-notice"><g:message code="templates.fr.identityprovider.reporting.generate"/></div>
          </div>
        </div>
      </div>
    </div>

    <div id="tab-connections" class="tab-pane">
      <div class="centered">
        <form id="idp-connections-report-parameters" class="form-inline report-parameters well validating">
          <g:hiddenField name="idpID" value="$idpID" />
          
          <label for="startDate"><g:message code="label.startdate"/></label>
          <input name="startDate" placeholder="Start Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

          <label for="endDate"><g:message code="label.enddate"/></label>
          <input name="endDate" placeholder="End Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

          <a class="generate-btn request-idp-connections-report btn"><g:message code="label.generate"/></a>
        </form>
      </div>

      <div class="row">
        <div class="span11">
          <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
          <div class="span9 offset1 alert alert-info generate-notice"><g:message code="templates.fr.identityprovider.reporting.generate"/></div>
          <div id="idpconnectivity" class="centered">
            
          </div>
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
      </script>
    </div>

  </div>

  <r:script>
    var detailedidpsessionsEndpoint = "${createLink(controller:'identityProviderReports', action:'reportsessions')}";
    var detailedidptoserviceutilizationEndpoint = "${createLink(controller:'identityProviderReports', action:'reportserviceutilization')}";
    var detailedidpdemandEndpoint = "${createLink(controller:'identityProviderReports', action:'reportdemand')}";

    $(".generate-btn").click(function() {
      $('.generate-notice').hide();
    });
        
    $(document).ready(function() {
      fr.hidespinner();
      fr.setup_date_range();
    });
  </r:script>
</div>