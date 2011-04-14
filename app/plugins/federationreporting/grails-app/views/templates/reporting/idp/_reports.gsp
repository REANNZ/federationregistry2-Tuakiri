
<form id="reportrequirements">
	<label><g:message code="label.type" />
		<select class="reporttype">
		  <option value="connections"><g:message code="label.connections" /></option>
		  <option value="totals"><g:message code="label.totals" /></option>
		</select>
	</label>
	
	<label><g:message code="label.day" /><input name="day" size="2" class="number"/><fr:tooltip code='fedreg.help.report.day' /></label>
	<label><g:message code="label.month" /><input name="month" size="2" class="number"/><fr:tooltip code='fedreg.help.report.month' /></label>
	<label><g:message code="label.year" /><input name="year" size="4" class="required number"/><fr:tooltip code='fedreg.help.report.year' /></label>

	<a href="#" onClick="if($('#reportrequirements').valid()) fedreg.renderIdPConnectivityReport(); return false;" class="search-button"><g:message code="label.generate" /></a>
</form>

<div id="idpconnectivityreport" class="loadhide idpreport">
	<g:render template="/templates/reporting/idp/connections" plugin="federationreporting" />
</div>
<div id="idptotalsreport" class="loadhide idpreport">
	
</div>