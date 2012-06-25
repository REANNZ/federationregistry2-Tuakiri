<html>
  <head>
    <meta name="layout" content="reporting" />
  </head>
  <body>
    <div class="centered">
      <form id="detailed-growth-report-parameters" class="form-inline report-parameters well validating">
        <label for="startDate"><g:message code="label.startdate"/></label>
        <input name="startDate" placeholder="Start Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

        <label for="endDate"><g:message code="label.enddate"/></label>
        <input name="endDate" placeholder="End Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

        <a class="request-detailed-growth-report btn"><g:message code="label.generate"/></a>
        <a class="export-detailed-growth-report export-button btn btn-info hidden" rel="tooltip" title="${g.message(code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
      </form>
    </div>

    <div class="row">
      <div class="span11">
        <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        <div id="detailedgrowthchart"></div>
      </div>
    </div>

    <r:script>
      var detailedgrowthEndpoint = "${createLink(controller:'federationReports', action:'reportsubscribergrowth')}"

      $(document).ready(function() {
        fr.setup_date_range();
        $('.request-detailed-growth-report').click();
      });
    </r:script>
  </body>
</html>
