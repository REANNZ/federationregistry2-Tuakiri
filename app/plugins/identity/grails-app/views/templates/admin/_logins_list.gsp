<g:if test="${logins?.size() > 0}">
	<p>
		<g:message code="nimble.template.logins.list.heading" args="[logins.size()]"/>
	</p>

	<table>
		<thead>
			<tr>
				<th class="first"><g:message code="label.remoteaddress" /></th>
				<th class=""><g:message code="label.remotehost" /></th>
				<th class=""><g:message code="label.subjectagent" /></th>
				<th class="last"><g:message code="label.time" /></th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${logins}" status="i" var="login">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td>${login.remoteAddr?.encodeAsHTML()}</td>
					<td>${login.remoteHost?.encodeAsHTML()}</td>
					<td>${login.subjectAgent?.encodeAsHTML()}</td>
					<td>${login.dateCreated?.encodeAsHTML()}</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<p>
		<g:message code="nimble.template.logins.list.noresults" />
	</p>
</g:else>