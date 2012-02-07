<%@ page import="grails.plugins.nimble.core.Subject" %>
<html>
	<head>
		<meta name="layout" content="${grailsApplication.config.nimble.layout.administration}"/>
		<title><g:message code="nimble.view.subject.list.title" /></title>
	</head>

	<body>

		<h2><g:message code="nimble.view.subject.list.heading" /></h2>

		<table class="sortable-table">
			<thead>
				<tr>
					<th><g:message code="label.subjectname" /></th>
					<th><g:message code="label.fullname" /></th>
					<th><g:message code="label.state" /></th>
					<th/>
				</tr>
			</thead>
			<tbody>
				<g:each in="${subjects}" status="i" var="subject">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<g:if test="${subject.subjectname.length() > 50}">
							<td>${subject.subjectname?.substring(0,50).encodeAsHTML()}...</td>
						</g:if>
						<g:else>
							<td>${subject.subjectname?.encodeAsHTML()}</td>
						</g:else>
						<g:if test="${subject.profile?.displayName}">
							<td valign="top" class="value">${subject.profile?.displayName?.encodeAsHTML()}</td>
						</g:if>
						<g:else>
							<td>&nbsp;</td>
						</g:else>
						<td>
							<g:if test="${subject.enabled}">
								<g:message code="label.enabled" />
							</g:if>
							<g:else>
								<g:message code="label.disabled" />
							</g:else>
						</td>
						<td>
								<n:button href="${createLink(action:'show', id: subject.id)}" label="label.view" class="view-button"/>
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>

	</body>
</html>