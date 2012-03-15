<html>
  <head>
    <meta name="layout" content="reporting" />    
  </head>
  <body>
    <div class="centered">
      <form id="detailed-detailedidptoserviceutilization-report-parameters" class="form-inline report-parameters well validating">
        <label for="idpID"><g:message code="label.identityprovider"/></label>
        <g:select name="idpID" from="${idpList}" optionKey="id" optionValue="displayName" class="span3"/>

        <label for="startDate"><g:message code="label.startdate"/></label>
        <input name="startDate" placeholder="start date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

        <label for="startDate"><g:message code="label.enddate"/></label>
        <input name="endDate" placeholder="end date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

        <a class="request-detailed-detailedidptoserviceutilization-reports btn"><g:message code="label.generate"/></a>
        <a class="export-detailed-detailedidptoserviceutilization-reports export-button btn btn-info hidden" rel="tooltip" title="${g.message(code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
      </form>
    </div>

    <div class="row">
      <div class="span11">
        <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        <div id="detailedidptoserviceutilization"></div>
      </div>
    </div>
    <div class="row row-spacer">
      <div class="span11">
        <div id="detailedidptoserviceutilizationtotals"></div>
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

    <r:script>
      var detailedidptoserviceutilizationEndpoint = "${createLink(controller:'identityProviderReports', action:'detailedserviceutilization')}"

      $(document).ready(function() {
        var currentTime = new Date()
        var month = currentTime.getMonth() + 1
        var day = currentTime.getDate()
        var year = currentTime.getFullYear()
        $('input[name="startDate"]').val(year - 1 + "-" + month + "-" + day);   
        $('input[name="endDate"]').val(year + "-" + month + "-" + day); 
        $('.request-detailed-detailedidptoserviceutilization-reports').click();
      });
    </r:script>
  </body>
</html>
