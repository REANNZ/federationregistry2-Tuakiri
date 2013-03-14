<html>
  <head>
    <meta name="layout" content="reporting" />  
  </head>
  <body>

    <div class="row">
      <div class="span11">
        <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        <div class="span11 detailed-idpattributesupport-report-parameters hidden">
          <form class="form form-inline well centered">
            <g:message encodeAs="HTML" code="label.identityprovider"/>
            <select id="choose-indepth-idp"></select>

            <a class="detailed-idpattributesupport-report btn"><g:message encodeAs="HTML" code="label.generate"/></a>
            <a class="detailed-idpattributesupport-reports btn"><g:message encodeAs="HTML" code="label.all"/></a>
            <a class="export-detailed-idpattributesupport-report export-button btn btn-info" rel="tooltip" title="${g.message(encodeAs:"HTML", code:'label.exportexcel')}"><i class="icon-edit icon-white"></i></a>
          </form>
      </div>
      </div>
    </div>

    <div class="row">
      <div id="attributesupporttables" class="span9 offset2 hidden">
      </div>
    </div>

    <r:script>
      var attributesupportEndpoint = "${createLink(controller:'complianceReports', action:'reportattributeavailability')}"

      $(document).ready(function() {
        fr.detailed_attributesupport_compliance_report();
      });
    </r:script>
  </body>
</html>
