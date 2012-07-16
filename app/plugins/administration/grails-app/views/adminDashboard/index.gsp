<html>
  <head>
    <meta name="layout" content="admin" />
  </head>
  <body>
    <div class="row row-spacer">
      <div class="span3 offset1 well centered">
        <strong class="dashboard-wow">${subjectCount}</strong><hr><h4><g:message code="label.subjects" default="Subjects"/></h4>
      </div>
      <div class="span3 well centered">
        <strong class="dashboard-wow">${roleCount}</strong><hr><h4><g:message code="label.roles" default="Roles"/></h4>
      </div>
      <div class="span3 well centered">
        <strong class="dashboard-wow">${permCount}</strong><hr><h4><g:message code="label.permissions" default="Permissions"/></h4>
      </div>
    </div>

    <div class="row row-spacer">
      <div class="span3 offset1 well centered">
        <strong class="dashboard-wow">${lastHourSessions}</strong><hr><h4><g:message code="label.sessionspasthour" default="Sessions in past hour"/></h4>
      </div>
      <div class="span3 well centered">
        <strong class="dashboard-wow">${lastDaySessions}</strong><hr><h4><g:message code="label.sessionspastday" default="Sessions in past day"/></h4>
      </div>
      <div class="span3 well centered">
        <strong class="dashboard-wow">${lastWeekSessions}</strong><hr><h4><g:message code="label.sessionspastweek" default="Sessions in past week"/></h4>
      </div>
    </div>

    <div class="centered row-spacer">
      <form id="detailed-frsessions-report-parameters" class="form-inline report-parameters span11 well validating">
        <label for="startDate"><g:message code="label.startdate"/></label>
        <input name="startDate" placeholder="start date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2" type="text"/>

        <label for="startDate"><g:message code="label.enddate"/></label>
        <input name="endDate" placeholder="end date (YYYY-MM-DD)" data-datepicker="datepicker" class="required span2" type="text"/>

        <a class="request-detailed-frsessions-report btn"><g:message code="label.generate"/></a>
      </form>
    </div>

    <div class="row row-spacer">
      <div class="span11">
        <div class="span11 spinner centered"><r:img dir="images" file="spinner.gif"/></div>
        <div id="detailedfrsessionschart"></div>
      </div>
    </div>

    <r:script>
      var detailedfrsessionsEndpoint = "${createLink(controller:'federationReports', action:'reportinternalsessions')}"

      $(document).ready(function() {
        var currentTime = new Date()
        var month = currentTime.getMonth() + 1
        var day = currentTime.getDate()
        var year = currentTime.getFullYear()
        $('input[name="startDate"]').val(year - 1 + "-" + month + "-" + day);   
        $('input[name="endDate"]').val(year + "-" + month + "-" + day); 
        $('.request-detailed-frsessions-report').click();
      });
    </r:script>

  </body>
</html>