<div id="reporting">
  <div class="reportingsupported">
    <form id="reportrequirements" class="validating form-stacked centered">
      <div class="row">
        <label for="day"><g:message code="label.day" /></label>
        <input name="day" size="2" class="number span1"/> <fr:tooltip code='fedreg.help.report.day'/>

        <label for="month"><g:message code="label.month" /></label>
        <input name="month" size="2" class="number span1"/> <fr:tooltip code='fedreg.help.report.month'/>

        <label for="year"><g:message code="label.year" /></label>
        <input name="year" size="4"  class="required number span2"/> <fr:tooltip code='fedreg.help.report.year'/>
      </div>
      <div class="row" style="padding-top: 6px;">
        <select name="reporttype" class="reporttype">
            <option value="sessions"><g:message code="label.sessions" /></option>
            <option value="connections"><g:message code="label.connections" /></option>
            <option value="totals"><g:message code="label.sessiontotals" /></option>
            <option value="logins"><g:message code="label.loginbreakdown" /></option>
        </select>
        <a class="create-idp-report btn"><g:message code="label.generate" /></a>
      </div>
    </form>

    <div id="reports" class="centered">
      <g:render template="/templates/reporting/idp/connections" plugin="federationreporting" />
      <g:render template="/templates/reporting/idp/totals" plugin="federationreporting" />
      <g:render template="/templates/reporting/idp/logins" plugin="federationreporting" />
      <g:render template="/templates/reporting/idp/sessions" plugin="federationreporting" />
    </div>
  </div>
  <div class="reportingunsupported">
    <p><g:render template="/templates/reporting/unsupported" plugin="federationreporting"/></p>
  </div>
</div>
