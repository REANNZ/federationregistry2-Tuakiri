<g:if test="${roleDescriptor.monitors.size() > 0}">
	<table>
		<thead>
			<tr>
				<th><g:message code="label.type"/></th>
				<th><g:message code="label.url"/></th>
				<th><g:message code="label.interval"/></th>
				<th/>
			</tr>	
		</thead>
		<tbody>
			<g:each in="${roleDescriptor.monitors}" var="m">
			<tr>
				<td>${m.type.name}</td>
				<td>${m.url}</td>
				<td>
					<g:if test="${m.checkPeriod == 0}">
						<g:message code="label.externallydefined"/>
					</g:if>
					<g:else>
						${m.checkPeriod} <g:message code="label.seconds"/>
					</g:else>
				</td>
				<td>
					<n:hasPermission target="descriptor:${roleDescriptor.id}:monitor:delete">
						<n:confirmaction action="fedreg.monitor_delete(${m.id});" title="${message(code: 'fedreg.templates.monitor.delete.confirm.title')}" msg="${message(code: 'fedreg.templates.monitor.delete.confirm.descriptive')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" class="delete-button" label="${message(code: 'label.delete')}" />
					</n:hasPermission>
				</td>
			</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<p><g:message code="fedreg.templates.monitor.none"/></p>
</g:else>