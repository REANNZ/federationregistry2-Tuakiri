
<h3><g:message code="nimble.template.roles.heading" /></h3>

<div id="assignedroles">
</div>

<div id="showaddroles">
	<n:button onclick="\$('#showaddroles').hide(); \$('#addroles').fadeIn();" label="label.addroles" icon="plus"/>
</div>

<div id="addroles">
	<h4><g:message code="nimble.template.roles.add.heading" /></h4>
	<p>
		<g:message code="nimble.template.roles.add.descriptive" />
	</p>

	<div class="searchbox">
		<g:textField name="qroles" class=""/>
		<n:button onclick="nimble.searchRoles(${parent.id.encodeAsHTML()});" label="label.search" icon="search"/>
		<n:button onclick="\$('#addroles').hide(); \$('#showaddroles').fadeIn();" label="label.close" icon="close"/>
	</div>

	<div id="rolesearchresponse" class="clear">
	</div>
</div>
