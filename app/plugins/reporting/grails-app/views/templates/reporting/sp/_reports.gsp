<div id="reporting">
  <div class="reportingsupported">
    <form id="reportrequirements" class="validating form-stacked centered">
      <div class="row">
        <div class="clearfix">
          <label for="day"><g:message code="label.day" /></label>
          <div class="input">
            <input name="day" size="2" class="number span1"/> <fr:tooltip code='fedreg.help.report.day'/>
          </div>
        </div>

        <div class="clearfix">
          <label for="month"><g:message code="label.month" /></label>
          <div class="input">
            <input name="month" size="2" class="number span1"/> <fr:tooltip code='fedreg.help.report.month'/>
          </div>
        </div>

        <div class="clearfix">
          <label for="year"><g:message code="label.year" /></label>
          <div class="input">
            <input name="year" size="4"  class="required number span2"/> <fr:tooltip code='fedreg.help.report.year'/>
          </div>
        </div>
      </div>
      <div class="row">
        <select name="reporttype" class="reporttype">
            <option value="sessions"><g:message code="label.sessions" /></option>
            <option value="connections"><g:message code="label.connections" /></option>
            <option value="totals"><g:message code="label.sessiontotals" /></option>
            <option value="logins"><g:message code="label.loginbreakdown" /></option>
        </select>

        <a class="create-sp-report btn"><g:message code="label.generate" /></a>
      </div>
    </form>

    <div id="reports" class="centered">
      <g:render template="/templates/reporting/sp/connections" plugin="federationreporting" />
      <g:render template="/templates/reporting/sp/totals" plugin="federationreporting" />
      <g:render template="/templates/reporting/sp/logins" plugin="federationreporting" />
      <g:render template="/templates/reporting/sp/sessions" plugin="federationreporting" />
    </div>
  </div>
  <div class="reportingunsupported">
    <p><g:render template="/templates/reporting/unsupported" plugin="federationreporting"/></p>
  </div>
</div>
