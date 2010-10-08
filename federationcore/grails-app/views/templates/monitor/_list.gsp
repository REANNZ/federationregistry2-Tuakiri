<g:if test="${roleDescriptor.monitors.size() > 0}">
	<table>
		<thead>
			<tr>
				<th><g:message code="label.type"/></th>
				<th><g:message code="label.url"/></th>
				<th><g:message code="label.interval"/></th>
			</tr>	
		</thead>
		<tbody>
			<g:each in="${roleDescriptor.monitors}" var="m">
			<tr>
				<td>${m.type.name}</td>
				<td>${m.url}</td>
				<td>${m.interval}</td>
			</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<p><g:message code="fedreg.template.monitor.none"/></p>
</g:else>