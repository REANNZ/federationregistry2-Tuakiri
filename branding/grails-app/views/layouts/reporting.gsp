<!DOCTYPE html>

<html>
	<head>
		<title><g:message code="fedreg.title.${grailsApplication.config.fedreg.deployment.environment}" /> | <g:layoutTitle /></title>
		<r:use modules="html5, jquery-ui, blockui, tiptip, jgrowl, datatables, validate, alphanumeric, formwizard, zenbox, app, protvis, tipsy"/>
		<r:layoutResources/>
		<g:layoutHead />
	</head>

	<body>

	    <header>
			<g:render template='/templates/frheader' />
	    </header>
	
		<nav>
		<n:isLoggedIn>
			<g:render template='/templates/frtopnavigation'/>
			
			<ul class="level2">
				<n:hasPermission target="federation:reporting">
					<li class="${controllerName == 'federationReports' ? 'active':''}"><g:link controller="federationReports" action="summary"><g:message code="fedreg.navigation.federationreports"/></g:link></li>
					<li class="${controllerName == 'idPReports' ? 'directactive':''}"><g:link controller="idPReports" action="view"><g:message code="fedreg.navigation.idpreports"/></g:link></li>
					<li class="${controllerName == 'spReports' ? 'directactive':''}"><g:link controller="spReports" action="view"><g:message code="fedreg.navigation.spreports"/></g:link></li>
				</n:hasPermission>
				<li class="${controllerName in ['IDPSSODescriptorAttributeCompliance', 'attributeRelease', 'certifyingAuthorityUsage'] ? 'active':''}"><g:link controller="IDPSSODescriptorAttributeCompliance" action="summary"><g:message code="fedreg.navigation.compliance"/></g:link></li>
			</ul>

			<g:if test="${controllerName in ['IDPSSODescriptorAttributeCompliance', 'attributeRelease', 'certifyingAuthorityUsage']}" >
				<ul class="level3a">
					<li class="${controllerName == 'IDPSSODescriptorAttributeCompliance' ? 'active':''}">
						<g:link controller="IDPSSODescriptorAttributeCompliance" action="summary"><g:message code="label.attributesummary" /></g:link>
				 	</li>
					<li class="${controllerName == 'attributeRelease' ? 'active':''}">
						<g:link controller="attributeRelease" action="index"><g:message code="label.attributerelease" /></g:link>
				 	</li>
					<li class="${controllerName == 'certifyingAuthorityUsage' ? 'active':''}">
						<g:link controller="certifyingAuthorityUsage" action="index"><g:message code="label.cautilization" /></g:link>
					</li>
				</ul>	
			</g:if>
			
			<g:if test="${controllerName in ['federationReports']}" >
				<ul class="level3a">
					<li class="${controllerName == 'federationReports' && actionName == 'summary' ? 'active':''}">
						<g:link controller="federationReports" action="summary"><g:message code="fedreg.navigation.reporting.summary" /></g:link>
				 	</li>
					<li class="${controllerName == 'federationReports' && actionName == 'subscribers' ? 'active':''}">
						<g:link controller="federationReports" action="subscribers"><g:message code="fedreg.navigation.reporting.subscribers" /></g:link>
				 	</li>
					<li class="${controllerName == 'federationReports' && actionName == 'registrations' ? 'active':''}">
						<g:link controller="federationReports" action="registrations"><g:message code="fedreg.navigation.reporting.registrations" /></g:link>
				 	</li>
				 	<li class="${controllerName == 'federationReports' && actionName == 'sessions' ? 'active':''}">
						<g:link controller="federationReports" action="sessions"><g:message code="fedreg.navigation.reporting.sessions" /></g:link>
				 	</li>
				 	<li class="${controllerName == 'federationReports' && actionName == 'sessiontotals' ? 'active':''}">
						<g:link controller="federationReports" action="sessiontotals"><g:message code="fedreg.navigation.reporting.sessiontotals" /></g:link>
				 	</li>
				 	<li class="${controllerName == 'federationReports' && actionName == 'logins' ? 'active':''}">
						<g:link controller="federationReports" action="logins"><g:message code="fedreg.navigation.reporting.logins" /></g:link>
				 	</li>
				</ul>	
			</g:if>
		</n:isLoggedIn>
		</nav>

		<section>
			<g:layoutBody/>
	    </section>

		<footer>
			<g:render template='/templates/frfooter' />
		</footer>
		
		<r:layoutResources/>
	</body>
</html>
