<html>
  <head>
    <meta name="layout" content="admin" />
  </head>
  <body>

    <div class="row">
      <div class="span4 centered">
        <div class="well">
          <strong class="dashboard-wow">${subjectCount}</strong><hr><h4><g:message encodeAs="HTML" code="label.subjects" default="Subjects"/></h4>
        </div>
      </div>
      <div class="span4 centered">
        <div class="well">
          <strong class="dashboard-wow">${roleCount}</strong><hr><h4><g:message encodeAs="HTML" code="label.roles" default="Roles"/></h4>
        </div>
      </div>
      <div class="span4 centered">
        <div class="well">
          <strong class="dashboard-wow">${permCount}</strong><hr><h4><g:message encodeAs="HTML" code="label.permissions" default="Permissions"/></h4>
        </div>
      </div>
    </div>

    <hr>

    <div class="row">
      <div class="span4 centered">
        <div class="well">
          <strong class="dashboard-wow">${lastHourSessions}</strong><hr><h4><g:message encodeAs="HTML" code="label.sessionspasthour" default="Sessions in past hour"/></h4>
        </div>
      </div>
      <div class="span4 centered">
        <div class="well">
          <strong class="dashboard-wow">${lastDaySessions}</strong><hr><h4><g:message encodeAs="HTML" code="label.sessionspastday" default="Sessions in past day"/></h4>
        </div>
      </div>
      <div class="span4 centered">
        <div class="well">
          <strong class="dashboard-wow">${lastWeekSessions}</strong><hr><h4><g:message encodeAs="HTML" code="label.sessionspastweek" default="Sessions in past week"/></h4>
        </div>
      </div>
    </div>

    <hr>

    <form id="detailed-frsessions-report-parameters" class="form-inline report-parameters centered validating">
      <label for="startDate"><g:message encodeAs="HTML" code="label.startdate"/></label>
      <input name="startDate" placeholder="start date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

      <label for="endDate"><g:message encodeAs="HTML" code="label.enddate"/></label>
      <input name="endDate" placeholder="end date (YYYY-MM-DD)" class="datepicker required span2" type="text"/>

      <a class="request-detailed-frsessions-report btn"><g:message encodeAs="HTML" code="label.generate"/></a>
    </form>

     
      <div id="detailedfrsessionschart" class="row-spacer centered">
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
