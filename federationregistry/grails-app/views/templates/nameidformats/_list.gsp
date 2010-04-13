
<g:if test="${nameIDFormats}">
<table>
	<thead>
		<tr>
			<th><g:message code="fedreg.label.supportedformat" /></th>
			<th><g:message code="fedreg.label.description" /></th>
			<th/>
		</tr>
	</thead>
	<tbody>
	<g:each in="${nameIDFormats}" status="i" var="nidf">
		<tr>
			<td>${nidf.uri.encodeAsHTML()}</td>
			<td>${nidf.description?.encodeAsHTML()}</td>
			<td>
				<n:confirmaction action="fedreg.nameIDFormat_remove(${nidf.id}, '${containerID}' );" title="${message(code: 'fedreg.template.nameidformats.remove.confirm.title')}" msg="${message(code: 'fedreg.template.nameidformats.remove.confirm.descriptive', args:[nidf.uri.encodeAsHTML()])}" accept="${message(code: 'nimble.link.accept')}" cancel="${message(code: 'nimble.link.cancel')}" class="button icon icon_bin"><g:message code="fedreg.link.remove"/></n:confirmaction>
			</td>
		</tr>
	</g:each>
	</tbody>
</table>
</g:if>
<g:else>
	<div>
		<p class="icon icon_information"><g:message code="fedreg.template.nameidformats.noresults"/></p>
	</div>
</g:else>