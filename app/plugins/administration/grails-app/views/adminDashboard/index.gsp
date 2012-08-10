<html>
  <head>
    <meta name="layout" content="admin" />
  </head>
  <body>

    <div class="row span12 row-spacer">
      <div class="span3 well centered">
        <strong class="dashboard-wow">${subjectCount}</strong><hr><h4><g:message code="label.subjects" default="Subjects"/></h4>
      </div>
      <div class="span3 well centered">
        <strong class="dashboard-wow">${roleCount}</strong><hr><h4><g:message code="label.roles" default="Roles"/></h4>
      </div>
      <div class="span3 well centered">
        <strong class="dashboard-wow">${permCount}</strong><hr><h4><g:message code="label.permissions" default="Permissions"/></h4>
      </div>


      <hr class="span9 offset1"/>


        <div class="span3 well centered">
          <strong class="dashboard-wow">${lastHourSessions}</strong><hr><h4><g:message code="label.sessionspasthour" default="Sessions in past hour"/></h4>
        </div>
        <div class="span3 well centered">
          <strong class="dashboard-wow">${lastDaySessions}</strong><hr><h4><g:message code="label.sessionspastday" default="Sessions in past day"/></h4>
        </div>
        <div class="span3 well centered">
          <strong class="dashboard-wow">${lastWeekSessions}</strong><hr><h4><g:message code="label.sessionspastweek" default="Sessions in past week"/></h4>
        </div>


      <hr class="span9 offset1"/>

      <form id="detailed-frsessions-report-parameters" class="form-inline report-parameters centered span11 validating">
        <label for="startDate"><g:message code="label.startdate"/></label>
        <input name="startDate" placeholder="start date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

        <label for="endDate"><g:message code="label.enddate"/></label>
        <input name="endDate" placeholder="end date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

        <a class="request-detailed-frsessions-report btn"><g:message code="label.generate"/></a>
      </form>

     
      <div id="detailedfrsessionschart" class="row-spacer span10 centered">
        <div class="spinner"><r:img dir="images" file="spinner.gif"/></div>
      </div>    
    </div>

    <span style="color:#fff">.</span>

    <r:script>
      var detailedfrsessionsEndpoint = "${createLink(controller:'federationReports', action:'reportinternalsessions')}"

      $(document).ready(function() {
        fr.setup_date_range();
        $('.request-detailed-frsessions-report').click();
      });
    </r:script>

.  </body>
</html>