
<g:if test="${endpoints}">

<g:if test="${!minEndpoints}">
	<g:set var="minEndpoints" value="${0}" />
</g:if>


	<g:each in="${endpoints.sort { it.id }}" status="i" var="ep">
		<div id="endpoint-${ep.id}">
			<h4><g:message code="label.endpoint"/> ${i+1}</h4>
			<table>
				<tbody>
					<tr>
						<th><g:message code="label.status" /></th>
						<td>
						<g:if test="${ep.active}">
							<div class="icon icon_tick"><g:message code="label.active" /></div>
						</g:if>
						<g:else>
							<div class="icon icon_cross"><g:message code="label.inactive" /></div>
						</g:else>
						</td>
					</tr>
					<tr>
						<th><g:message code="label.approved" /></th>
						<td>
							<g:if test="${ep.approved}">
								<g:message code="label.yes" />
							</g:if>
							<g:else>
								<g:message code="label.no" />
							</g:else>
						</td>
					</tr>
					<tr>
						<th><g:message code="label.location" /></th>
						<td>${ep.location.uri.encodeAsHTML()}</td>
					</tr>
					<g:if test="${resloc}">
						<tr>
							<th><g:message code="label.responselocation" /></th>
							<td colspan="2">${(ep.responseLocation?.uri ?:ep.location.uri).encodeAsHTML()}</td>
						</tr>
					</g:if>
					<g:if test="${ep.instanceOf(fedreg.core.IndexedEndpoint)}">
						<tr>
							<th><g:message code="label.isdefault" /></th>
							<td>
								<g:if test="${ep.isDefault}">
									<g:message code="label.yes" />
								</g:if>
								<g:else>
									<g:message code="label.no" />
								</g:else>
							</td>
						</tr>
					</g:if>
					<tr>
						<th><g:message code="label.binding" /></th>
						<td>${ep.binding.uri.encodeAsHTML()}</td>
						<td/>
					</tr>
					<tr>
						<td colspan="2">
							<n:hasPermission target="descriptor:${ep.descriptor.id}:endpoint:edit">
								<n:button onclick="fedreg.endpoint_edit(${ep.id}, '${endpointType}', '${containerID}');" label="label.edit" class="view-button" />
							</n:hasPermission>
							<n:hasPermission target="descriptor:${ep.descriptor.id}:endpoint:toggle">
								<n:confirmaction action="fedreg.endpoint_toggle(${ep.id}, '${endpointType}', '${containerID}' );" title="${message(code: 'fedreg.templates.endpoints.toggle.confirm.title')}" msg="${message(code: 'fedreg.templates.endpoints.toggle.confirm.descriptive')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" label="${message(code: 'label.togglestate')}" class="toggle-button" />
							</n:hasPermission>
							<g:if test="${ep.instanceOf(fedreg.core.IndexedEndpoint) && !ep.isDefault}">
								<n:hasPermission target="descriptor:${ep.descriptor.id}:endpoint:toggle">
									<n:confirmaction action="fedreg.endpoint_makedefault(${ep.id}, '${endpointType}', '${containerID}' );" title="${message(code: 'fedreg.templates.endpoints.makedefault.confirm.title')}" msg="${message(code: 'fedreg.templates.endpoints.makedefault.confirm.descriptive')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" label="${message(code: 'label.makedefault')}" class="add-button" />
								</n:hasPermission>
							</g:if>
							<n:hasPermission target="descriptor:${ep.descriptor.id}:endpoint:remove">
								<g:if test="${endpoints.size() > minEndpoints}">
									<n:confirmaction action="fedreg.endpoint_delete(${ep.id}, '${endpointType}', '${containerID}' );" title="${message(code: 'fedreg.templates.endpoints.remove.confirm.title')}" msg="${message(code: 'fedreg.templates.endpoints.remove.confirm.descriptive')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" class="delete-button" label="${message(code: 'label.delete')}"/>
								</g:if>
							</n:hasPermission>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</g:each>


</g:if>
<g:else>
	<p class="icon icon_information"><g:message code="fedreg.templates.endpoints.noresults"/></p>
</g:else>