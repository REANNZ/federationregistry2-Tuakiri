
<h3><g:message code="nimble.template.permissions.heading" /></h3>
<div id="currentpermission">
</div>

<div id="showaddpermissions">
	<n:button onclick="\$('#showaddpermissions').hide(); \$('#addpermissions').fadeIn();" label="label.addpermission" class="add-button"/>
</div>

<div id="addpermissions">
	<h4><g:message code="nimble.template.permission.add.heading" /></h4>
	<p>
		<g:message code="nimble.template.permission.add.descriptive" />
	</p>

	<div id="addpermissionserror"></div>

	<table>
		<tbody>
			<tr>
				<td>
					<g:textField size="15" name="first_p"/> <strong>:</strong>
					<g:textField size="15" name="second_p"/> <strong>:</strong>
					<g:textField size="15" name="third_p"/> <strong>:</strong>
					<g:textField size="15" name="fourth_p"/>
				</td>
			</tr>
		</tbody>
	</table>
	
	<div class="buttons">
		<n:button onclick="nimble.createPermission(${parent.id.encodeAsHTML()});" label="label.add" class="add-button"/>
		<n:button onclick="\$('#addpermissions').hide(); \$('#showaddpermissions').fadeIn();" label="label.close" class="close-button"/>
	</div>
</div>