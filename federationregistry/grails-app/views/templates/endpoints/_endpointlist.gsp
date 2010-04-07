
<g:if test="${endpoints}">
<table>
	<tbody>
	<g:each in="${endpoints}" status="i" var="ep">
		<tr>
			<td colspan="3"><h4><g:message code="fedreg.label.endpoint"/> ${i+1}</h4></td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.binding" /></th>
			<td>${ep.binding.uri.encodeAsHTML()}</td>
			<td>
			<g:if test="${allowremove}">
					<n:confirmaction action="removeEndpoint(${ep.id}, '${endpointType}', '${containerID}' );" title="${message(code: 'fedreg.template.endpoints.remove.confirm.title')}" msg="${message(code: 'fedreg.template.endpoints.remove.confirm.descriptive')}" accept="${message(code: 'nimble.link.accept')}" cancel="${message(code: 'nimble.link.cancel')}" class="button icon icon_delete"><g:message code="fedreg.link.remove" /></n:confirmaction>
			</g:if>
			</td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.location" /></th>
			<td colspan="2">${ep.location.uri.encodeAsHTML()}</td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.responselocation" /></th>
			<td colspan="2">${(ep.responseLocation?.uri ?:ep.location.uri).encodeAsHTML()}</td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.status" /></th>
			<td colspan="2">
			<g:if test="${ep.active}">
				<div class="icon icon_tick"><g:message code="fedreg.label.active" /></div>
			</g:if>
			<g:else>
				<div class="icon icon_cross"><g:message code="fedreg.label.inactive" /></div>
			</g:else>
			<td>
		</tr>
	</g:each>
	</tbody>
</table>
</g:if>
<g:else>
	<div>
		<p class="icon icon_information"><g:message code="fedreg.template.endpoints.noresults"/></p>
	</div>
</g:else>