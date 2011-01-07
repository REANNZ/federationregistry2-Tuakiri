
<g:if test="${nameIDFormats}">
<table>
	<thead>
		<tr>
			<th><g:message code="label.supportedformat" /></th>
			<th><g:message code="label.description" /></th>
			<th/>
		</tr>
	</thead>
	<tbody>
	<g:each in="${ nameIDFormats.sort{it.id} }" status="i" var="nidf">
		<tr>
			<td>${nidf.uri.encodeAsHTML()}</td>
			<td>${nidf.description?.encodeAsHTML()}</td>
			<td>
				<n:hasPermission target="descriptor:${descriptor.id}:nameidformat:remove">
					<n:confirmaction action="fedreg.nameIDFormat_remove(${nidf.id}, '${containerID}' );" title="${message(code: 'fedreg.templates.nameidformats.remove.confirm.title')}" msg="${message(code: 'fedreg.templates.nameidformats.remove.confirm.descriptive', args:[nidf.uri.encodeAsHTML()])}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" class="delete-button" label="${message(code: 'label.remove')}" />
				</n:hasPermission>
			</td>
		</tr>
	</g:each>
	</tbody>
</table>
</g:if>
<g:else>
	<div>
		<p class="error"><g:message code="fedreg.templates.nameidformats.noresults"/></p>
	</div>
</g:else>