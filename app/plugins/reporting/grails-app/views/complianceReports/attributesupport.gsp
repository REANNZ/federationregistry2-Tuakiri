<html>
  <head>
    <meta name="layout" content="reporting" />  
  </head>
  <body>

    <div class="row">
      <div class="span11">
        <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        <div id="attributesupportchart"></div>
      </div>
    </div>

    <div id="rar">
    </div>

    <div class="row">
      <div id="attributesupporttables" class="span9 offset2 hidden">
        <h3><g:message encodeAs="HTML" code="label.idpattributesupportbreakdown"/></h3>
        <select id="choose-indepth-idp"></select>
      </div>
    </div>

    <r:script>
      var attributesupportEndpoint = "${createLink(controller:'complianceReports', action:'reportattributeavailability')}"

      $(document).ready(function() {
        fr.attributesupport_compliance_report();
      });
    </r:script>
  </body>
</html>
