
<g:if test="${corrupt}">
	<div class="alert alert-message alert-danger">
		<p><g:message code="templates.fr.certificates.validation.corrupt" /></p>
	</div>
</g:if>
<g:else>
	<g:if test="${valid}">
		<div class="alert alert-message alert-success">
	</g:if>
	<g:else>
		<div class="alert alert-message alert-danger">
	</g:else>
	
	<table class="table borderless">
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
		</div>
		<div class="alert alert-message alert-danger">
			<span><strong><g:message code="templates.fr.certificates.validation.errors"/></strong></span>
				<ol>
				<g:each in="${certerrors}" var="e">
					<li><g:message code="${e}" /></li>
				</g:each>
				</ol>
		</div>
	</g:else>
</g:else>