<html>
  <head>
    <meta name="layout" content="reporting" />    
  </head>
  <body>
    <div class="centered">
      <form id="detailed-detailedidptoserviceutilization-report-parameters" class="form-inline report-parameters well validating">
        <label for="idpID"><g:message encodeAs="HTML" code="label.identityprovider"/></label>
        <g:select name="idpID" from="${idpList}" optionKey="id" optionValue="displayName" class="span3"/>

        <label for="startDate"><g:message encodeAs="HTML" code="label.startdate"/></label>
        <input name="startDate" placeholder="Start Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

        <label for="endDate"><g:message encodeAs="HTML" code="label.enddate"/></label>
        <input name="endDate" placeholder="End Date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

        <a class="request-detailed-detailedidptoserviceutilization-reports btn"><g:message encodeAs="HTML" code="label.generate"/></a>
        <a class="export-detailed-detailedidptoserviceutilization-reports export-button btn btn-info hidden" rel="tooltip" title="${g.message(encodeAs:"HTML", code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
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
          <a class="select-all-topten-services"><g:message encodeAs="HTML" code="label.addallchecks" /></a> | <a class="unselect-all-topten-services"><g:message encodeAs="HTML" code="label.removeallchecks" /></a>
          <div id="topten-utilized-services" class="span11 services"></div>

          <div class="span2 offset9">
            <a class="btn btn-success request-refine-detailedidptoserviceutilization-content"><g:message encodeAs="HTML" code="label.refine" /></a>
          </div>
        </div>
        <div class="span11 remainder hidden">
          <hr>
          <h4 class="span9">Remaining Services</h4>
          <a class="select-all-remaining-services"><g:message encodeAs="HTML" code="label.addallchecks" /></a> | <a class="unselect-all-remaining-services"><g:message encodeAs="HTML" code="label.removeallchecks" /></a>
          <div id="remaning-utilized-services" class="span11 services"></div>
          <div class="span2 offset9">
            <a class="btn btn-success request-refine-detailedidptoserviceutilization-content"><g:message encodeAs="HTML" code="label.refine" /></a>
          </div>
        </div>
      </form>
    </div>

    <r:script>
      var detailedidptoserviceutilizationEndpoint = "${createLink(controller:'identityProviderReports', action:'reportserviceutilization')}"

      $(document).ready(function() {
        fr.setup_date_range();
        $('.request-detailed-detailedidptoserviceutilization-reports').click();
      });
    </r:script>
  </body>
</html>
