
<g:if test="${corrupt}">
	<div class="critical">
		<p class="icon icon_exclamation"><g:message code="fedreg.template.certificates.validation.corrupt" /></p>
	</div>
	<p>
		<br/><a href="#" onClick="$('#newcertificate').hide('slide'); $('#addcertificate').show('slide');" class="modal_close button icon icon_cancel"><g:message code="fedreg.link.cancel"/></a>
	</p>
</g:if>
<g:else>
	<table>
		<tbody>
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
			<tr>
				<th><g:message code="fedreg.label.optionalname"/></th>
				<td><input type="text" id="newcertificatename" /></td>
			</tr>
		</tbody>
	</table>
	<a href="#" onClick="createNewCertificate();" class="button icon icon_add"><g:message code="fedreg.link.add"/></a>
	</g:if>
	<g:else>
			</tbody>
		</table>
		<div class="warning">
			<span class="icon icon_error"><strong><g:message code="fedreg.template.certificates.validation.errors"/></strong></span>
			<p>
			<g:each in="${certerrors}" status="i" var="e">
				${i+1}). <g:message code="${e}" />
			</g:each>
			<p>
		</div>
	</g:else>
	<a href="#" onClick="$('#newcertificate').hide('slide'); $('#addcertificate').show('slide');" class="modal_close button icon icon_cancel"><g:message code="fedreg.link.cancel"/></a>
</g:else>