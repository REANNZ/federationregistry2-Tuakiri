<table>
	<thead>
		<tr>
			<th>Header Name</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>				
		<g:each in="${attr}" var="e">
		<tr>
			<td>
	    		${e.key}
	 		</td>
			<td>
				${e.value}
			</td>
		</tr>
		</g:each>
	</tbody>
</table>