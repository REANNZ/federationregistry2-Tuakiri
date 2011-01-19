<%@ page import="grails.plugins.nimble.core.UserBase" %>
<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.user.list.title" /></title>
		
		<r:script>
			<njs:datatable tableID="userlist" sortColumn="1" />
		</r:script>
	</head>

	<body>

		<h2><g:message code="nimble.view.user.list.heading" /></h2>

		<table id="userlist">
			<thead>
				<tr>
					<th><g:message code="label.username" /></th>
					<th><g:message code="label.fullname" /></th>
					<th><g:message code="label.state" /></th>
					<th/>
				</tr>
			</thead>
			<tbody>
				<g:each in="${users}" status="i" var="user">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<g:if test="${user.username.length() > 50}">
							<td>${user.username?.substring(0,50).encodeAsHTML()}...</td>
						</g:if>
						<g:else>
							<td>${user.username?.encodeAsHTML()}</td>
						</g:else>
						<g:if test="${user.profile?.fullName}">
							<td valign="top" class="value">${user.profile?.fullName?.encodeAsHTML()}</td>
						</g:if>
						<g:else>
							<td>&nbsp;</td>
						</g:else>
						<td>
							<g:if test="${user.enabled}">
								<g:message code="label.enabled" />
							</g:if>
							<g:else>
								<g:message code="label.disabled" />
							</g:else>
						</td>
						<td>
								<n:button href="${createLink(action:'show', id: user.id)}" label="label.view" class="view-button"/>
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>

	</body>
</html>