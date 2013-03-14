<html>
  <head>
    <meta name="layout" content="reporting" />
  </head>
  <body>
    <div class="centered">
      <form id="detailed-spdemand-report-parameters" class="form-inline report-parameters well validating">
        <label for="spID"><g:message encodeAs="HTML" code="label.serviceprovider"/></label>
        <g:select name="spID" from="${spList}" optionKey="id" optionValue="displayName" class="span3"/>
        
        <label for="startDate"><g:message encodeAs="HTML" code="label.startdate"/></label>
        <input name="startDate" placeholder="Start Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

        <label for="endDate"><g:message encodeAs="HTML" code="label.enddate"/></label>
        <input name="endDate" placeholder="End Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

        <a class="request-detailed-spdemand-report btn"><g:message encodeAs="HTML" code="label.generate"/></a>
        <a class="export-detailed-spdemand-report export-button btn btn-info hidden" rel="tooltip" title="${g.message(encodeAs:"HTML", code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
      </form>
    </div>

    <div class="row">
      <div class="span11">
        <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        <div id="spdemanddetailed"></div>
      </div>
    </div>
    
    <r:script>
      var detailedspdemandEndpoint = "${createLink(controller:'serviceProviderReports', action:'reportdemand')}"

      $(document).ready(function() {
        fr.setup_date_range();
        $('.request-detailed-spdemand-report').click();
      });
    </r:script>
  </body>
</html>
