<html>
  <head>
    <meta name="layout" content="reporting" />
  </head>
  <body>
    <div class="centered">
      <form id="detailed-dsutilization-report-parameters" class="form-inline report-parameters well validating">
        <label for="startDate"><g:message code="label.startdate"/></label>
        <input name="startDate" placeholder="start date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

        <label for="startDate"><g:message code="label.enddate"/></label>
        <input name="endDate" placeholder="end date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

        <a class="request-detailed-dsutilization-reports btn"><g:message code="label.generate"/></a>
        <a class="export-detailed-dsutilization-report export-button btn btn-info hidden" rel="tooltip" title="${g.message(code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
      </form>
    </div>

    <div class="row">
      <div class="span11">
        <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        <div id="detailedwayfnodesessions"></div>
      </div>
    </div>

    <div class="row">
      <div class="span11">
        <div id="detailedwayfnodesessionstotals"></div>
      </div>
    </div>

    <r:script>
      var detaileddsutilizationEndpoint = "${createLink(controller:'federationReports', action:'reportdsutilization')}"

      $(document).ready(function() {
        var currentTime = new Date()
        var month = currentTime.getMonth() + 1
        var day = currentTime.getDate()
        var year = currentTime.getFullYear()
        $('input[name="startDate"]').val(year - 1 + "-" + month + "-" + day);   
        $('input[name="endDate"]').val(year + "-" + month + "-" + day); 
        $('.request-detailed-dsutilization-reports').click();
      });
    </r:script>
  </body>
</html>
