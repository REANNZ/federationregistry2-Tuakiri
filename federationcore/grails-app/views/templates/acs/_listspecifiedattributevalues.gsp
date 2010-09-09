<g:if test="${requestedAttribute?.values?.size() > 0}">
	<table>
		<tbody>
			<g:each in="${requestedAttribute.values}" status="i" var="val">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td> ${val.value?.encodeAsHTML()}</td>
					<n:hasPermission target="descriptor:${acs.descriptor.id}:attribute:value:remove">
						<td>
							<n:confirmaction action="fedreg.acs_specattribute_remove(${requestedAttribute.id}, ${val.id}, '${containerID}');" title="${message(code: 'fedreg.templates.acs.reqattributes.remove.value.confirm.title')}" msg="${message(code: 'fedreg.templates.acs.reqattributes.remove.value.confirm.descriptive', args:[requestedAttribute.base.friendlyName.encodeAsHTML()])}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" icon="trash" label="label.remove" />
						</td>
					</n:hasPermission>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<div class="error">
		<g:message code="fedreg.templates.acs.specattributes.no.values.currently.requested" args="[requestedAttribute.base.friendlyName, requestedAttribute.base.oid]"/>
	</div>
</g:else>