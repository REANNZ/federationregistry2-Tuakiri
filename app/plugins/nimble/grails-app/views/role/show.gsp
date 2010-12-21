<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.role.show.title" args="[role.name]" /></title>
		<script type="text/javascript">
			<njs:permission parent="${role}"/>
			<njs:member parent="${role}"/>
			nimble.createTabs('tabs');
		</script>
	</head>
	
	<body>

		<h2><g:message code="nimble.view.role.show.heading" args="[role.name.encodeAsHTML()]" /></h2>

		<h3><g:message code="nimble.view.role.show.details.heading" /></h3>
		<table>
			<tbody>
				<tr>
					<td><g:message code="label.name" /></td>
					<td>${fieldValue(bean: role, field: 'name')}</td>
				</tr>
				<tr>
					<td><g:message code="label.description" /></td>
					<td>${fieldValue(bean: role, field: 'description')}</td>
				</tr>
				<tr>
					<td><g:message code="label.protected" /></td>
					<td>
						<g:if test="${role.protect}">
							<g:message code="label.yes" />
						</g:if>
						<g:else>
							<g:message code="label.no" />
						</g:else>
					</td>
				</tr>
			</tbody>
		</table>

		<section id="tabs">
			<ul>
				<li><a href="#tab-permissions" class="icon icon_lock"><g:message code="label.permissions" /></a></li>
				<li><a href="#tab-members" class="icon icon_cog"><g:message code="label.members" /></a></li>
			</ul>

			<div id="tab-permissions">
				<g:render template="/templates/admin/permissions" contextPath="${pluginContextPath}" model="[parent:role]"/>
			</div>
			<div id="tab-members">
				<g:render template="/templates/admin/members" contextPath="${pluginContextPath}" model="[parent:role, protect:role.protect, groupmembers:true]"/>
			</div>
		</section>

		<g:form action="delete" name="deleterole">
			<g:hiddenField name="id" value="${role.id}"/>
		</g:form>

	</body>
</html>