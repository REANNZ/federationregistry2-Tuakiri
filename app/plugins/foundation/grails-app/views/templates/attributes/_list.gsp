<g:if test="${attrs}">
<table>
	<thead>
		<tr>
			<th><g:message code="label.attribute" /></th>
			<th><g:message code="label.category" /></th>
			<th><g:message code="label.description" /></th>
			<th/>
		</tr>
	</thead>
	<tbody>
	<g:each in="${attrs}" status="i" var="attr">
		<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
			<td>
				${attr.base.name.encodeAsHTML()}
				<pre>OID: ${attr.base.oid?.encodeAsHTML()}</pre>
			</td>
			<td>${attr.base.category.name.encodeAsHTML()}</td>
			<td> ${attr.base.description.encodeAsHTML()}</td>
			<td>
				<fr:hasPermission target="descriptor:${descriptor.id}:attribute:remove">
					<n:confirmaction action="fedreg.attribute_remove(${attr.id}, '${containerID}' );" title="${message(code: 'fedreg.templates.attributes.remove.confirm.title')}" msg="${message(code: 'fedreg.templates.attributes.remove.confirm.descriptive', args:[attr.base.name.encodeAsHTML()])}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" class="delete-button" label="label.remove" />
				</fr:hasPermission>
			</td>
		</tr>
	</g:each>
	</tbody>
</table>
</g:if>
<g:else>
	<div>
		<p class="icon icon_information"><g:message code="fedreg.templates.attributes.noresults"/></p>
	</div>
</g:else>