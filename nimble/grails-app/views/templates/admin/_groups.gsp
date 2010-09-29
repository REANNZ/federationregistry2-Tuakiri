<div id="groups">
	<h3><g:message code="nimble.template.groups.heading" /></h3>

	<div id="assignedgroups">
	</div>

	<div id="showaddgroups">
		<n:button onclick="\$('#showaddgroups').hide(); \$('#addgroups').fadeIn();" label="label.addgroups" icon="plus"/>
	</div>

	<div id="addgroups">
		<h4><g:message code="nimble.template.groups.add.heading" /></h4>
		<p>
			<g:message code="nimble.template.groups.add.descriptive" />
		</p>

		<div class="searchbox">
			<g:textField name="qgroups" class="enhancedinput"/>
			<n:button onclick="nimble.searchGroups('${parent.id.encodeAsHTML()}');" label="label.search" icon="search"/>
			<n:button onclick="\$('#addgroups').hide(); \$('#showaddgroups').fadeIn();" label="label.close" icon="close"/>
		</div>

		<div id="groupsearchresponse"></div>
	</div>
</div>