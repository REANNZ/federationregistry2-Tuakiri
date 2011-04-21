
<form id="reportrequirements">
	<label><g:message code="label.type" />
		<select class="reporttype">
				<option value="sessions"><g:message code="label.sessions" /></option>
				<option value="connections"><g:message code="label.connections" /></option>
				<option value="totals"><g:message code="label.usagetotals" /></option>
				<option value="logins"><g:message code="label.loginbreakdown" /></option>
		</select>
	</label>
	
	<label><g:message code="label.day" />: <input name="day" size="2" class="number"/><fr:tooltip code='fedreg.help.report.day' /></label>
	<label><g:message code="label.month" />: <input name="month" size="2" class="number"/><fr:tooltip code='fedreg.help.report.month' /></label>
	<label><g:message code="label.year" />: <input name="year" size="4" value="2010" class="required number"/><fr:tooltip code='fedreg.help.report.year' /></label>

	<a href="#" onClick="if($('#reportrequirements').valid()){ fedreg.renderIdPReport(); } return false;" class="search-button"><g:message code="label.generate" /></a>
</form>

<div id="reports">
	<g:render template="/templates/reporting/idp/connections" plugin="federationreporting" />
	<g:render template="/templates/reporting/idp/totals" plugin="federationreporting" />
	<g:render template="/templates/reporting/idp/logins" plugin="federationreporting" />
	<g:render template="/templates/reporting/idp/sessions" plugin="federationreporting" />
</div>