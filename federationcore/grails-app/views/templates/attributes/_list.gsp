<g:if test="${attrs}">
<table class="cleantable">
	<thead>
		<tr>
			<th><g:message code="fedreg.label.attribute" /></th>
			<th><g:message code="fedreg.label.oid" /></th>
			<th><g:message code="fedreg.label.description" /></th>
			<th/>
		</tr>
	</thead>
	<tbody>
	<g:each in="${attrs.sort{it.friendlyName}}" status="i" var="attr">
		<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
			<td>${attr.friendlyName.encodeAsHTML()}</td>
			<td>${attr.oid.encodeAsHTML()}</td>
			<td> ${attr.description.encodeAsHTML()}</td>
			<td>
				<n:confirmaction action="fedreg.attribute_remove(${attr.id}, '${containerID}' );" title="${message(code: 'fedreg.template.attributes.remove.confirm.title')}" msg="${message(code: 'fedreg.template.attributes.remove.confirm.descriptive', args:[attr.friendlyName.encodeAsHTML()])}" accept="${message(code: 'nimble.link.accept')}" cancel="${message(code: 'nimble.link.cancel')}" class="button icon icon_bin"><g:message code="fedreg.link.remove"/></n:confirmaction>
			</td>
		</tr>
	</g:each>
	</tbody>
</table>
</g:if>
<g:else>
	<div>
		<p class="icon icon_information"><g:message code="fedreg.template.attributes.noresults"/></p>
	</div>
</g:else>