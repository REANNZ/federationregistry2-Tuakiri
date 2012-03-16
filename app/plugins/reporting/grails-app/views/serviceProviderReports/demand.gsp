<html>
  <head>
    <meta name="layout" content="reporting" />
  </head>
  <body>
    <div class="centered">
      <form id="detailed-spdemand-report-parameters" class="form-inline report-parameters well validating">
        <label for="spID"><g:message code="label.serviceprovider"/></label>
        <g:select name="spID" from="${spList}" optionKey="id" optionValue="displayName" class="span3"/>
        
        <label for="startDate"><g:message code="label.startdate"/></label>
        <input name="startDate" placeholder="start date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

        <label for="startDate"><g:message code="label.enddate"/></label>
        <input name="endDate" placeholder="end date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

        <a class="request-detailed-spdemand-report btn"><g:message code="label.generate"/></a>
        <a class="export-detailed-spdemand-report export-button btn btn-info hidden" rel="tooltip" title="${g.message(code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
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
        var currentTime = new Date()
        var month = currentTime.getMonth() + 1
        var day = currentTime.getDate()
        var year = currentTime.getFullYear()
        $('input[name="startDate"]').val(year - 1 + "-" + month + "-" + day);   
        $('input[name="endDate"]').val(year + "-" + month + "-" + day); 
        $('.request-detailed-spdemand-report').click();
      });
    </r:script>
  </body>
</html>
