
<g:if test="${endpoints}">
<table>
	<tbody>
	<g:each in="${endpoints.sort{it.id} }" status="i" var="ep">
		<tr>
			<td colspan="3"><h4><g:message code="fedreg.label.endpoint"/> ${i+1}</h4></td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.status" /></th>
			<td>
			<g:if test="${ep.active}">
				<div class="icon icon_tick"><g:message code="fedreg.label.active" /></div>
			</g:if>
			<g:else>
				<div class="icon icon_cross"><g:message code="fedreg.label.inactive" /></div>
			</g:else>
			</td>
			<td>
				<fr:confirmaction action="fedreg.endpoint_toggle(${ep.id}, '${endpointType}', '${containerID}' );" title="${message(code: 'fedreg.template.endpoints.toggle.confirm.title')}" msg="${message(code: 'fedreg.template.endpoints.toggle.confirm.descriptive')}" accept="${message(code: 'fedreg.label.accept')}" cancel="${message(code: 'fedreg.label.cancel')}" label="${message(code: 'fedreg.link.togglestate')}" icon="refresh" />
			</td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.location" /></th>
			<td>${ep.location.uri.encodeAsHTML()}</td>
			<td>
				<g:if test="${allowremove}">
						<fr:confirmaction action="fedreg.endpoint_delete(${ep.id}, '${endpointType}', '${containerID}' );" title="${message(code: 'fedreg.template.endpoints.remove.confirm.title')}" msg="${message(code: 'fedreg.template.endpoints.remove.confirm.descriptive')}" accept="${message(code: 'fedreg.label.accept')}" cancel="${message(code: 'fedreg.label.cancel')}" label="${message(code: 'fedreg.link.delete')}" icon="trash" />
				</g:if>
			</td>
		</tr>
		<g:if test="${resloc}">
		<tr>
			<th><g:message code="fedreg.label.responselocation" /></th>
			<td colspan="2">${(ep.responseLocation?.uri ?:ep.location.uri).encodeAsHTML()}</td>
		</tr>
		</g:if>
		<tr>
			<th><g:message code="fedreg.label.binding" /></th>
			<td colspan="2">${ep.binding.uri.encodeAsHTML()}</td>
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