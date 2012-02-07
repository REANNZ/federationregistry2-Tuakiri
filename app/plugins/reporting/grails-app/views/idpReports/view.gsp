<html>
	<head>
		<meta name="layout" content="reporting" />
		<title><g:message code="fedreg.view.reporting.identityprovider.show.title" /></title>
		
		<r:script>			
			var idpReportsConnectivityEndpoint = "${createLink(controller:'idPReports', action:'connectivityjson')}"
			var idpReportsSessionsEndpoint = "${createLink(controller:'idPReports', action:'sessionsjson')}"
			var idpReportsTotalsEndpoint = "${createLink(controller:'idPReports', action:'totalsjson')}"
			var idpReportsLoginsEndpoint = "${createLink(controller:'idPReports', action:'loginsjson')}"
		</r:script>
	</head>
	<body>
		<section>
			<div class="reportingsupported">
				<h2><g:message code="fedreg.view.reporting.identityprovider.show.heading" /></h2>
				<div id="reporting">
					<form id="reportrequirements">
						<label><g:message code="label.identityprovider" />
						<g:select name="id"
								  from="${idpList}"
								  optionValue="displayName"
								  optionKey="id" />
						</label>
						<br><br>
						<label><g:message code="label.type" />
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

						<a href="#" onClick="if($('#reportrequirements').valid()){ fedreg.renderIdPReport(); } return false;" class="search-button"><g:message code="label.generate" /></a>
					</form>

					<div id="reports">
						<g:render template="/templates/reporting/idp/connections" plugin="federationreporting" />
						<g:render template="/templates/reporting/idp/totals" plugin="federationreporting" />
						<g:render template="/templates/reporting/idp/logins" plugin="federationreporting" />
						<g:render template="/templates/reporting/idp/sessions" plugin="federationreporting" />
					</div>
				</div>
			</div>
			<div class="reportingunsupported">
				<p><g:render template="/templates/reporting/unsupported" plugin="federationreporting"/></p>
			</div>
		</section>
	</body>
</html>
