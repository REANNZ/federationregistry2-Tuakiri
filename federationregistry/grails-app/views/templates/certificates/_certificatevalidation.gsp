
<g:if test="${corrupt}">
	<div class="critical">
		<p class="icon icon_exclamation"><g:message code="fedreg.template.certificate.validation.corrupt" /></p>
	</div>
</g:if>
<g:else>
	<g:if test="${invalidca}">
		<div class="warning">
			<p class="icon icon_error"><g:message code="fedreg.template.certificate.validation.invalidca" /></p>
		</div>
	</g:if>
	${subject} <br/>
	${issuer} <br/>
	${expires} <br/>
	${valid}<br/>
	<a href="#" onClick="createNewCertificate();" class="button icon icon_add"><g:message code="fedreg.link.add"/></a>
</g:else>