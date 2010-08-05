<table>
	<thead>
		<tr>
			<th><g:message code="label.attribute" /></th>
			<th><g:message code="label.required" /></th>
			<th><g:message code="label.reason" /></th>
			<th/>
		</tr>
	</thead>
	<tbody>
		<g:each in="${requestedAttributes?.sort{it.base.friendlyName}}" status="j" var="ra">
			<tr class="${(j % 2) == 0 ? 'odd' : 'even'}">
				<td>${ra.base.friendlyName.encodeAsHTML()}</td>
				<td>
					<g:if test="${ra.isRequired}">
						<g:message code="label.yes" />
					</g:if>
					<g:else>
						<g:message code="label.no" />
					</g:else>
				</td>
				<td> ${ra.reasoning?.encodeAsHTML()}</td>
				<td>
					<n:confirmaction action="fedreg.acs_reqattribute_remove(${ra.id}, ${ra.attributeConsumingService.id}, '${containerID}' );" title="${message(code: 'fedreg.template.acs.reqattributes.remove.confirm.title')}" msg="${message(code: 'fedreg.template.acs.reqattributes.remove.confirm.descriptive', args:[ra.base.friendlyName.encodeAsHTML()])}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" icon="trash" label="label.remove" />
				</td>
			</tr>
		</g:each>
	</tbody>
</table>