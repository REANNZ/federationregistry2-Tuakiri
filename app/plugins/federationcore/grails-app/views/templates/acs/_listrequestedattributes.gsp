<g:if test="${requestedAttributes}">
	<table>
		<thead>
			<tr>
				<th><g:message code="label.attribute" /></th>
				<th><g:message code="label.category" /></th>
				<th><g:message code="label.required" /></th>
				<th><g:message code="label.reason" /></th>
				<th><g:message code="label.approved" /></th>
				<th/>
			</tr>
		</thead>
		<tbody>
			<g:each in="${requestedAttributes}" status="j" var="ra">
				<tr class="${(j % 2) == 0 ? 'odd' : 'even'}">
					<td>
						${ra.base.friendlyName.encodeAsHTML()}
						<pre>OID: ${ra.base.oid?.encodeAsHTML()}</pre>
					</td>
					<td>${ra.base.category.name.encodeAsHTML()}</td>
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
						<g:if test="${ra.approved}">
							<g:message code="label.yes" />
						</g:if>
						<g:else>
							<span class="warning"><g:message code="fedreg.templates.acs.reqattributes.workflow" /></span>
						</g:else>
					</td>
					<n:hasPermission target="descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:remove">
						<td>
							<n:confirmaction action="fedreg.acs_reqattribute_remove(${ra.id}, ${ra.attributeConsumingService.id}, '${containerID}' );" title="${message(code: 'fedreg.templates.acs.reqattributes.remove.confirm.title')}" msg="${message(code: 'fedreg.templates.acs.reqattributes.remove.confirm.descriptive', args:[ra.base.friendlyName.encodeAsHTML()])}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" icon="trash" label="label.remove" />
						</td>
					</n:hasPermission>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<div class="warning">
		<g:message code="fedreg.templates.acs.reqattributes.not.requested" />
	</div>
</g:else>