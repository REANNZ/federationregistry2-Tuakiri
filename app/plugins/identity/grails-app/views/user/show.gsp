<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.subject.show.title" args="[subject.profile?.fullName ?: subject.subjectname]" /></title>
		<r:script>
			<njs:subject subject="${subject}"/>
			<njs:permission parent="${subject}"/>
			<njs:role parent="${subject}"/>
			<njs:group parent="${subject}"/>
			nimble.createTabs('tabs');
		</r:script>
	</head>

	<body>

		<h2><g:message code="nimble.view.subject.show.heading" args="[subject.subjectname?.encodeAsHTML()]" /></h2>

		<h3><g:message code="nimble.view.subject.show.details.heading" /></h3>
		<table>
			<tbody>
				<tr>
					<th><g:message code="label.subjectname" /></th>
					<td>${subject.subjectname?.encodeAsHTML()}</td>
				</tr>
				<tr>
					<th><g:message code="label.created" /></th>
					<td><g:formatDate format="E dd/MM/yyyy HH:mm:s:S" date="${subject.dateCreated}"/></td>
				</tr>
				<tr>
					<th><g:message code="label.lastupdated" /></th>
					<td><g:formatDate format="E dd/MM/yyyy HH:mm:s:S" date="${subject.lastUpdated}"/></td>
				</tr>
				<tr>
					<th><g:message code="label.type" /></th>
					<g:if test="${subject.external}">
						<td><g:message code="label.external.managment" /></td>
					</g:if>
					<g:else>
						<td><g:message code="label.local.managment" /></td>
					</g:else>
				</tr>
				<tr>
					<th><g:message code="label.state" /></th>
					<td>
						<div id="disabledsubject">
							<g:message code="label.enabled" />
						</div>
						<div id="enabledsubject">
							<g:message code="label.disabled" />
						</div>
					</td>
				</tr>
				<tr>
					<th><g:message code="label.remoteapi" /></th>
					<td>
						<div id="enabledapi">
								<g:message code="label.enabled" />
							</div>
						<div id="disabledapi">
							<g:message code="label.disabled" />
						</div>
					</td>
				</tr>
			</tbody>
		</table>

		<section id="tabs">
			<ul>
				<li><a href="#tab-extendedinfo" class="icon icon_subject"><g:message code="label.extendedinformation" /></a></li>
				<g:if test="${subject.federated}">
					<li><a href="#tab-federation" class="icon icon_world"><g:message code="label.federatedaccount" /></a></li>
				</g:if>
				<li><a href="#tab-permissions" class="icon icon_lock"><g:message code="label.permissions" /></a></li>
				<li><a href="#tab-roles" class="icon icon_cog"><g:message code="label.roles" /></a></li>
				<li><a href="#tab-groups" class="icon icon_group"><g:message code="label.groups" /></a></li>
				<li><a href="#tab-logins" class="icon icon_key"><g:message code="label.logins" /></a></li>
			</ul>

			<div id="tab-extendedinfo">
				<g:render template="/templates/nimble/subject/extendedinformation" contextPath="/" model="[subject:subject]"/>	
			</div>

			<g:if test="${subject.federated}">
				<div id="tab-federation">
					<g:render template="/templates/nimble/subject/federationinformation" contextPath="/" model="[subject:subject]"/>
				</div>
			</g:if>

			<div id="tab-permissions">
				<g:render template="/templates/admin/permissions" contextPath="${pluginContextPath}" model="[parent:subject]"/>
			</div>

			<div id="tab-roles">
				<g:render template="/templates/admin/roles" contextPath="${pluginContextPath}" model="[parent:subject]"/>
			</div>

			<div id="tab-groups">
				<g:render template="/templates/admin/groups" contextPath="${pluginContextPath}" model="[parent:subject]"/>
			</div>

			<div id="tab-logins">
				<g:render template="/templates/admin/logins" contextPath="${pluginContextPath}" model="[parent:subject]"/>		 
			</div>
		</section>

	</body>
</html>