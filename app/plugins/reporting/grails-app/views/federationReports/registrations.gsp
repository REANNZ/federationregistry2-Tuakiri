<html>
  <head>
    <meta name="layout" content="reporting" />
  </head>
  <body>
    <div class="centered">
      <form id="detailed-registration-report-parameters" class="form-inline report-parameters well validating">
        <label for="startDate"><g:message code="label.startdate"/></label>
        <input name="startDate" placeholder="start date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2" type="text"/>

        <label for="startDate"><g:message code="label.enddate"/></label>
        <input name="endDate" placeholder="end date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2" type="text"/>

        <a class="request-detailed-registration-report btn"><g:message code="label.generate"/></a>
        <a class="export-detailed-registration-report export-button btn btn-info hidden" rel="tooltip" title="${g.message(code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
      </form>
    </div>

    <div class="row">
      <div class="span11">
        <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        <div id="detailedregistrationschart"></div>
      </div>
    </div>

    <div id="registrationdetails" class="row hidden">
      <div class="span9 offset2">
        <h3><g:message code="label.organizations"/></h3>
        <table class="table borderless fixed">
          <thead>
            <tr>
              <th><g:message code="label.name" /></th>
              <th><g:message code="label.datecreated" /></th>          
              <th></th>
            </tr>
          </thead>
          <tbody id="organizationregistrations">
          </tbody>
        </table>

        <h3><g:message code="label.identityproviders"/></h3>
        <table class="table borderless fixed">
          <thead>
            <tr>
              <th><g:message code="label.name" /></th>
              <th><g:message code="label.datecreated" /></th>          
              <th></th>
            </tr>
          </thead>
          <tbody id="idpregistrations">
          </tbody>
        </table>

        <h3><g:message code="label.serviceproviders"/></h3>
        <table class="table borderless fixed">
          <thead>
            <tr>
              <th><g:message code="label.name" /></th>
              <th><g:message code="label.datecreated" /></th>          
              <th></th>
            </tr>
          </thead>
          <tbody id="spregistrations">
          </tbody>
        </table>
      </div>
    </div>

    <r:script>
      var detailedregistrationsEndpoint = "${createLink(controller:'federationReports', action:'reportregistrations')}"

      $(document).ready(function() {
        var currentTime = new Date()
        var month = currentTime.getMonth() + 1
        var day = currentTime.getDate()
        var year = currentTime.getFullYear()
        $('input[name="startDate"]').val(year - 1 + "-" + month + "-" + day);   
        $('input[name="endDate"]').val(year + "-" + month + "-" + day); 
        $('.request-detailed-registration-report').click();
      });
    </r:script>
  </body>
</html>
