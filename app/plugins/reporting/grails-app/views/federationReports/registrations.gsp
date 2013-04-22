<html>
  <head>
    <meta name="layout" content="reporting" />
  </head>
  <body>
    <div class="centered">
      <form id="detailed-registration-report-parameters" class="form-inline report-parameters well validating">
        <label for="startDate"><g:message encodeAs="HTML" code="label.startdate"/></label>
        <input name="startDate" placeholder="Start Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

        <label for="endDate"><g:message encodeAs="HTML" code="label.enddate"/></label>
        <input name="endDate" placeholder="End Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

        <a class="request-detailed-registration-report btn"><g:message encodeAs="HTML" code="label.generate"/></a>
        <a class="export-detailed-registration-report export-button btn btn-info hidden" rel="tooltip" title="${g.message(encodeAs:"HTML", code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
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
        <h3><g:message encodeAs="HTML" code="label.organizations"/></h3>
        <table class="table borderless fixed">
          <thead>
            <tr>
              <th><g:message encodeAs="HTML" code="label.name" /></th>
              <th><g:message encodeAs="HTML" code="label.datecreated" /></th>          
              <th></th>
            </tr>
          </thead>
          <tbody id="organizationregistrations">
          </tbody>
        </table>

        <h3><g:message encodeAs="HTML" code="label.identityproviders"/></h3>
        <table class="table borderless fixed">
          <thead>
            <tr>
              <th><g:message encodeAs="HTML" code="label.name" /></th>
              <th><g:message encodeAs="HTML" code="label.datecreated" /></th>          
              <th></th>
            </tr>
          </thead>
          <tbody id="idpregistrations">
          </tbody>
        </table>

        <h3><g:message encodeAs="HTML" code="label.serviceproviders"/></h3>
        <table class="table borderless fixed">
          <thead>
            <tr>
              <th><g:message encodeAs="HTML" code="label.name" /></th>
              <th><g:message encodeAs="HTML" code="label.datecreated" /></th>          
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
        fr.setup_date_range();
        $('.request-detailed-registration-report').click();
      });
    </r:script>
  </body>
</html>
