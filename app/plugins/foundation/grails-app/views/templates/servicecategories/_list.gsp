
<p>
	<g:message code="fedreg.templates.servicecategories.descriptive" />
</p>
<g:if test="${categories}">
<table>
	<thead>
		<tr>
			<th><g:message code="label.name" /></th>
			<th><g:message code="label.description" /></th>
			<th/>
		</tr>
	</thead>
	<tbody>
	<g:each in="${ categories.sort{it.id} }" status="i" var="cat">
		<tr>
			<td>${cat.name.encodeAsHTML()}</td>
			<td>${cat.description?.encodeAsHTML()}</td>
			<td>
				<n:hasPermission target="descriptor:${descriptor.id}:category:remove">
					<n:confirmaction action="fedreg.serviceCategory_remove(${cat.id}, '${containerID}' );" title="${message(code: 'fedreg.templates.servicecategories.remove.confirm.title')}" msg="${message(code: 'fedreg.templates.servicecategories.remove.confirm.descriptive')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" class="delete-button" label="${message(code: 'label.remove')}" />
				</n:hasPermission>
			</td>
		</tr>
	</g:each>
	</tbody>
</table>
</g:if>
<g:else>
	<div>
		<p class="error"><g:message code="fedreg.templates.servicecategories.noresults"/></p>
	</div>
</g:else>