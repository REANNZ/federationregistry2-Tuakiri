<html>
  <head>
    <meta name="layout" content="reporting" />
  </head>
  <body>
    <div class="centered">
      <form id="detailed-idpdemand-report-parameters" class="form-inline report-parameters well validating">
        <label for="idpID"><g:message code="label.identityprovider"/></label>
        <g:select name="idpID" from="${idpList}" optionKey="id" optionValue="displayName" class="span3"/>
        
        <label for="startDate"><g:message code="label.startdate"/></label>
        <input name="startDate" placeholder="start date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

        <label for="startDate"><g:message code="label.enddate"/></label>
        <input name="endDate" placeholder="end date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2 date-value" type="text"/>

        <a class="request-detailed-idpdemand-report btn"><g:message code="label.generate"/></a>
        <a class="export-detailed-idpdemand-report export-button btn btn-info hidden" rel="tooltip" title="${g.message(code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
      </form>
    </div>

    <div class="row">
      <div class="span11">
        <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        <div id="idpdemanddetailed"></div>
      </div>
    </div>
    
    <r:script>
      var detailedidpdemandEndpoint = "${createLink(controller:'identityProviderReports', action:'reportdemand')}"

      $(document).ready(function() {
        var currentTime = new Date()
        var month = currentTime.getMonth() + 1
        var day = currentTime.getDate()
        var year = currentTime.getFullYear()
        $('input[name="startDate"]').val(year - 1 + "-" + month + "-" + day);   
        $('input[name="endDate"]').val(year + "-" + month + "-" + day); 
        $('.request-detailed-idpdemand-report').click();
      });
    </r:script>
  </body>
</html>