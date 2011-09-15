<html>
	<head>
		<meta name="layout" content="reporting" />
		<title><g:message code="fedreg.view.reporting.serviceprovider.show.title" /></title>
		
		<r:script>			
			var spReportsConnectivityEndpoint = "${createLink(controller:'spReports', action:'connectivityjson')}"
			var spReportsSessionsEndpoint = "${createLink(controller:'spReports', action:'sessionsjson')}"
			var spReportsTotalsEndpoint = "${createLink(controller:'spReports', action:'totalsjson')}"
			var spReportsLoginsEndpoint = "${createLink(controller:'spReports', action:'loginsjson')}"
		</r:script>
	</head>
	<body>
		<section>
			<div class="reportingsupported">
				<h2><g:message code="fedreg.view.reporting.serviceprovider.show.heading" /></h2>
				<div id="reporting">
					<form id="reportrequirements">
						<label><g:message code="label.serviceprovider" />:
						<g:select name="id"
								  from="${spList}"
								  optionValue="displayName"
								  optionKey="id" />
						</label>
						<br><br>
						<label><g:message code="label.type" />:
							<select class="reporttype">
									<option value="sessions"><g:message code="label.sessions" /></option>
									<option value="connections"><g:message code="label.connections" /></option>
									<option value="totals"><g:message code="label.sessiontotals" /></option>
									<option value="logins"><g:message code="label.loginbreakdown" /></option>
							</select>
						</label>
	
						<label><g:message code="label.day" />: <input name="day" size="2" class="number"/><fr:tooltip code='fedreg.help.report.day' /></label>
						<label><g:message code="label.month" />: <input name="month" size="2" class="number"/><fr:tooltip code='fedreg.help.report.month' /></label>
						<label><g:message code="label.year" />: <input name="year" size="4" class="required number"/><fr:tooltip code='fedreg.help.report.year' /></label>

						<a href="#" onClick="if($('#reportrequirements').valid()){ fedreg.renderSPReport(); } return false;" class="search-button"><g:message code="label.generate" /></a>
					</form>

					<div id="reports">
						<g:render template="/templates/reporting/sp/connections" plugin="federationreporting" />
						<g:render template="/templates/reporting/sp/totals" plugin="federationreporting" />
						<g:render template="/templates/reporting/sp/logins" plugin="federationreporting" />
						<g:render template="/templates/reporting/sp/sessions" plugin="federationreporting" />
					</div>
				</div>
			</div>
			<div class="reportingunsupported">
				<p><g:render template="/templates/reporting/unsupported" plugin="federationreporting"/></p>
			</div>
		</section>
	</body>
</html>
