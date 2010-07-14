
<g:if test="${corrupt}">
	<div class="error">
		<p><g:message code="fedreg.template.certificates.validation.corrupt" /></p>
	</div>
</g:if>
<g:else>
	<table>
		<tbody>
			
			<tr>
				<th><g:message code="fedreg.label.status" /></th>
				<g:if test="${valid}">
					<td><g:message code="fedreg.label.valid" /> <span class="icon icon_accept">&nbsp;</span></td>
				</g:if>
				<g:else>
					<td><g:message code="fedreg.label.invalid" /> <span class="icon icon_error">&nbsp;</span></td>
				</g:else>
			</tr>
			<tr>
				<th><g:message code="fedreg.label.subject" /></th>
				<td>${subject}</td>
			</tr>
			<tr>
				<th><g:message code="fedreg.label.issuer"/></th>
				<td>${issuer}</td>
			</tr>
			<tr>
				<th><g:message code="fedreg.label.expirydate"/></th>
				<td>${expires}</td>
			</tr>
	<g:if test="${valid}">
		</tbody>
	</table>

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