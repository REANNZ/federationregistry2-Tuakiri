
<html>
	<head>
		<meta name="layout" content="dashboard" />
		<title><g:message code="fedreg.view.dashboard.title" /></title>
	</head>
	<body>
		<div id="dashboardcontent">
			<h2><g:message code="fedreg.view.dashboard.welcome" args="[authenticatedUser.profile.fullName]" /></h2>
			<p><g:message code="fedreg.view.dashboard.welcome.descriptive"/></p>
			
			<div class="dashrow dashrowinternal">
				<div class="dashcol3">
					<h3><g:message code="fedreg.view.dashboard.myorganizations"/></h3>
					<g:if test="${organizations}">
					<ul  class="dashlist">
						<g:each in="${organizations}" var="org">
							<li><a href="${createLink(controller:'organization', action:'show', id:org?.id)}">${fieldValue(bean: org, field: 'displayName')} </a></li>
						</g:each>
					</ul>
					</g:if>
					<g:else>
						<p><g:message code="fedreg.view.dashboard.myorganizations.none"/></p>
					</g:else>
				</div>
				<div class="dashcol3">
					<h3><g:message code="fedreg.view.dashboard.myidentityproviders"/></h3>
					<g:if test="${identityProviders}">
					<ul class="dashlist">
						<g:each in="${identityProviders}" var="idp">
							<li><a href="${createLink(controller:'IDPSSODescriptor', action:'show', id:idp.id)}">${fieldValue(bean: idp, field: 'displayName')}</a></li>
						</g:each>
					</ul>
					</g:if>
					<g:else>
						<p><g:message code="fedreg.view.dashboard.myidentityproviders.none"/></p> 
					</g:else>
				</div>
				<div class="dashcol3">
					<h3><g:message code="fedreg.view.dashboard.myserviceproviders"/></h3>
					<g:if test="${serviceProviders}">
					<ul class="dashlist">
						<g:each in="${serviceProviders}" var="sp">
							<li><a href="${createLink(controller:'SPSSODescriptor', action:'show', id:sp.id)}">${fieldValue(bean: sp, field: 'displayName')}</a></li>
						</g:each>
					</ul>
					</g:if>
					<g:else>
						<p><g:message code="fedreg.view.dashboard.myserviceproviders.none"/></p> 
					</g:else>
				</div>
			</div>
			
			<div class="dashrow">
				<div class="dashcol3 dashstats">
					<h3><g:message code="fedreg.view.dashboard.federation.statistics"/></h3>
					<p><strong>${orgCount}</strong> <g:message code="label.organizations"/> - <g:link controller="organization" action="list" class=""><g:message code="label.viewall"/></g:link></p>
					<p><strong>${idpCount}</strong> <g:message code="label.identityproviders"/> - <g:link controller="IDPSSODescriptor" action="list" class=""><g:message code="label.viewall"/></g:link></p>
					<p><strong>${spCount}</strong> <g:message code="label.serviceproviders"/> - <g:link controller="SPSSODescriptor" action="list" class=""><g:message code="label.viewall"/>l</g:link></p>
					<p><strong>${endpointCount}</strong> <g:message code="label.activeendpoints"/></p>
					<p><strong>${certCounts}</strong> <g:message code="label.activecertificates"/></p>
				</div>
				<div class="dashcol3"></div>
				<div class="dashcol3">
					<div class="user">
						<div class="userlogo">
							<a href="http://gravatar.com"><avatar:gravatar email="${authenticatedUser.profile.email}" size="50" /></a>
							<br>
							<a href="http://gravatar.com"><g:message code="label.change"/></a>
						</div>
						<div class="userdetails">
							<h3><n:principalName /></h3>
							<h4><g:message code="fedreg.view.dashboard.outstanding.tasks"/></h4>
							<g:if test="${tasks}">
								<g:link controller="workflowApproval" action="list"><g:message code="fedreg.view.dashboard.outstanding.tasks.required" args="[tasks.size()]"/></g:link>
							</g:if>
							<g:else>
								<g:message code="fedreg.view.dashboard.outstanding.tasks.none"/>
							</g:else>
							<h4><g:message code="fedreg.view.dashboard.targetedid"/></h4>
							${fieldValue(bean: authenticatedUser, field: 'username')}
							<h4><g:message code="fedreg.view.dashboard.email"/></h4>
							${fieldValue(bean: authenticatedUser, field: 'profile.email')}
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
