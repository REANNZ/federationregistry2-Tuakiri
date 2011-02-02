<table>
	<tbody>
		<tr>
			<th>
				<g:message code="label.description" />
			</th>
			<td>
				${fieldValue(bean: serviceProvider, field: "description")}
			</td>
		</tr>
		<tr>
			<th>
				<g:message code="label.serviceurl" />
			</th>
			<td>
				${fieldValue(bean: serviceProvider, field: "serviceDescription.connectURL")}
			</td>
		</tr>
		<tr>
			<th>
				<g:message code="label.servicelogourl" />
			</th>
			<td>
				${fieldValue(bean: serviceProvider, field: "serviceDescription.logoURL")}
			</td>
		</tr>
	</tbody>
</table>