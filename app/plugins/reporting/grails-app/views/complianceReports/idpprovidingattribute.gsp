<html>
  <head>
    <meta name="layout" content="reporting" />
  </head>
  <body>
    <div class="centered">
      <form id="idpprovidingattribute-report-parameters" class="form-inline report-parameters well validating">
        <label for="attribute"><g:message code="label.attribute"/></label>
        <fr:attributeSelection/>
        <a class="request-idpprovidingattribute-report btn"><g:message code="label.generate"/></a>
      </form>
    </div>

    <div class="row">
      <div class="span11">
        <div class="spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        <div id="idpprovidingattribute" class="centered">
          <div class="span9 offset1 alert alert-info" style="text-align:left;">
            <p>For the attribute <strong><span class="attributename"></span></strong>, a total of <strong><span class="supportedcount"></span></strong> Identity Providers are capable of providing values to requesting services while <strong><span class="unsupportedcount"></span></strong> are not.</p>
            <p><strong>Please note that capability does not mean any one specific Identity Provider will provide your service values.</strong> This is particuarly true if they are noted as not automatically releasing attribute values below. If your service is not recieving an attribute from an Identity Provider capable of providing it please contact the administrators of that Identity Provider directly.
          </div>
          <div class="span5">
            <h4><g:message code="label.supportedby"/></h4>
            <table class="table borderless">
              <tbody class="supported">
              </tbody>
            </table>
          </div>
          
          <div class="span5">
            <h4><g:message code="label.unsupportedby"/></h4>
            <table class="table borderless">
              <tbody class="unsupported">
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <r:script>
      var idpprovidingattributeEndpoint = "${createLink(controller:'complianceReports', action:'reportprovidingattribute')}"

      $(document).ready(function() {
        $('.request-idpprovidingattribute-report').click();
      });
    </r:script>
  </body>
</html>
