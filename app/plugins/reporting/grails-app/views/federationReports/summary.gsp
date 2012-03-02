<html>
  <head>
    <meta name="layout" content="reporting" />
  </head>
  <body>


    <div class="row">
      <div class="span9 offset1">
        <div id="registrations">
          <div class="span9 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        </div>
      </div>
    </div>

    <div class="row row-spacer">
      <div class="span9 offset1">
        <div id="growth">
          <div class="span9 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        </div>
      </div>
    </div>

    <div class="row row-spacer">
      <div class="span9 offset1">
        <div id="sessions">
          <div class="span9 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        </div>
      </div>
    </div>

    <r:script>
      var summaryregistrationsEndpoint = "${createLink(controller:'federationReports', action:'summaryregistrations')}"

      var summarysessionsEndpoint = "${createLink(controller:'federationReports', action:'summarysessions')}"

      var summarysubscribergrowthEndpoint = "${createLink(controller:'federationReports', action:'summarysubscribergrowth')}"

      $(document).ready(function() {
        fr.summary_registrations_report('registrations');
        fr.summary_subscriber_growth_report('growth');
        fr.summary_sessions_report('sessions');
      });
    </r:script>
  </body>
</html>
