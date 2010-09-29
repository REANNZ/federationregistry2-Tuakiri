
<g:if test="${corrupt}">
	<div class="error">
		<p><g:message code="fedreg.template.certificates.validation.corrupt" /></p>
	</div>
</g:if>
<g:else>
	<g:if test="${valid}">
		<div class="success">
	</g:if>
	
	<table>
		<tbody>
			
			<tr>
				<th><g:message code="label.status" /></th>
				<g:if test="${valid}">
					<td><g:message code="label.valid" /> <span class="icon icon_accept">&nbsp;</span></td>
				</g:if>
				<g:else>
					<td><g:message code="label.invalid" /> <span class="icon icon_error">&nbsp;</span></td>
				</g:else>
			</tr>
			<tr>
				<th><g:message code="label.subject" /></th>
				<td>${subject}</td>
			</tr>
			<tr>
				<th><g:message code="label.issuer"/></th>
				<td>${issuer}</td>
			</tr>
			<tr>
				<th><g:message code="label.expirydate"/></th>
				<td>${expires}</td>
			</tr>
	<g:if test="${valid}">
		</tbody>
	</table>
	</div>
	</g:if>
	<g:else>
			</tbody>
		</table>
		<div class="error">
			<span><strong><g:message code="fedreg.template.certificates.validation.errors"/></strong></span>
			<p>
			<g:each in="${certerrors}" status="i" var="e">
				${i+1}). <g:message code="${e}" />
			</g:each>
			<p>
		</div>
	</g:else>
</g:else>