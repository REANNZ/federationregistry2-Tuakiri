<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.user.show.title" args="[user.profile?.fullName ?: user.username]" /></title>
		<script type="text/javascript">
			<njs:user user="${user}"/>
			<njs:permission parent="${user}"/>
			<njs:role parent="${user}"/>
			<njs:group parent="${user}"/>
			nimble.createTabs('tabs');
		</script>
	</head>

	<body>

		<h2><g:message code="nimble.view.user.show.heading" args="[user.username?.encodeAsHTML()]" /></h2>

		<h3><g:message code="nimble.view.user.show.details.heading" /></h3>
		<table>
			<tbody>
				<tr>
					<th><g:message code="label.username" /></th>
					<td>${user.username?.encodeAsHTML()}</td>
				</tr>
				<tr>
					<th><g:message code="label.created" /></th>
					<td><g:formatDate format="E dd/MM/yyyy HH:mm:s:S" date="${user.dateCreated}"/></td>
				</tr>
				<tr>
					<th><g:message code="label.lastupdated" /></th>
					<td><g:formatDate format="E dd/MM/yyyy HH:mm:s:S" date="${user.lastUpdated}"/></td>
				</tr>
				<tr>
					<th><g:message code="label.type" /></th>
					<g:if test="${user.external}">
						<td><g:message code="label.external.managment" /></td>
					</g:if>
					<g:else>
						<td><g:message code="label.local.managment" /></td>
					</g:else>
				</tr>
				<tr>
					<th><g:message code="label.state" /></th>
					<td>
						<div id="disableduser">
							<g:message code="label.enabled" />
						</div>
						<div id="enableduser">
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
				<li><a href="#tab-extendedinfo" class="icon icon_user"><g:message code="label.extendedinformation" /></a></li>
				<g:if test="${user.federated}">
					<li><a href="#tab-federation" class="icon icon_world"><g:message code="label.federatedaccount" /></a></li>
				</g:if>
				<li><a href="#tab-permissions" class="icon icon_lock"><g:message code="label.permissions" /></a></li>
				<li><a href="#tab-roles" class="icon icon_cog"><g:message code="label.roles" /></a></li>
				<li><a href="#tab-groups" class="icon icon_group"><g:message code="label.groups" /></a></li>
				<li><a href="#tab-logins" class="icon icon_key"><g:message code="label.logins" /></a></li>
			</ul>

			<div id="tab-extendedinfo">
				<g:render template="/templates/nimble/user/extendedinformation" contextPath="/" model="[user:user]"/>	
			</div>

			<g:if test="${user.federated}">
				<div id="tab-federation">
					<g:render template="/templates/nimble/user/federationinformation" contextPath="/" model="[user:user]"/>
				</div>
			</g:if>

			<div id="tab-permissions">
				<g:render template="/templates/admin/permissions" contextPath="${pluginContextPath}" model="[parent:user]"/>
			</div>

			<div id="tab-roles">
				<g:render template="/templates/admin/roles" contextPath="${pluginContextPath}" model="[parent:user]"/>
			</div>

			<div id="tab-groups">
				<g:render template="/templates/admin/groups" contextPath="${pluginContextPath}" model="[parent:user]"/>
			</div>

			<div id="tab-logins">
				<g:render template="/templates/admin/logins" contextPath="${pluginContextPath}" model="[parent:user]"/>		 
			</div>
		</section>

	</body>
</html>