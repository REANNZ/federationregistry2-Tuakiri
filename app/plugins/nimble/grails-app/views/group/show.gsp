<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.group.show.title"  args="[group.name.encodeAsHTML()]" /></title>
		<script type="text/javascript">
			<njs:permission parent="${group}"/>
			<njs:role parent="${group}"/>
			<njs:member parent="${group}"/>
			nimble.createTabs('tabs');
		</script>
	</head>
	<body>

		<h2><g:message code="nimble.view.group.show.heading" args="[group.name.encodeAsHTML()]" /></h2>

		<h3><g:message code="nimble.view.group.show.details.heading" /></h3>
		<table>
			<tbody>
			<tr>
				<th><g:message code="label.name" /></th>
				<td>${fieldValue(bean: group, field: 'name')}</td>
			</tr>
			<tr>
				<th><g:message code="label.description" /></th>
				<td valign="top" class="value">${fieldValue(bean: group, field: 'description')}</td>
			</tr>
			<tr>
				<th><g:message code="label.protected" /></th>
				<td valign="top" class="value">
					<g:if test="${group.protect}">
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
				<li><a href="#tab-roles" class="icon icon_cog"><g:message code="label.roles" /></a></li>
				<li><a href="#tab-members" class="icon icon_group"><g:message code="label.members" /></a></li>
			</ul>

			<div id="tab-permissions">
				<g:render template="/templates/admin/permissions" contextPath="${pluginContextPath}" model="[parent:group]"/>
			</div>
			<div id="tab-roles">
				<g:render template="/templates/admin/roles" contextPath="${pluginContextPath}" model="[parent:group]"/>
			</div>
			<div id="tab-members">
				<g:render template="/templates/admin/members" contextPath="${pluginContextPath}" model="[parent:group, protect:group.protect, groupmembers:false]"/>
			</div>
		</section>

		<g:form action="delete" name="deletegroup">
			<g:hiddenField name="id" value="${group.id.encodeAsHTML()}"/>
		</g:form>

	</body>
</html>
