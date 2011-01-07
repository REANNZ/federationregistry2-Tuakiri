<div id="members">
	<h3><g:message code="nimble.template.members.heading" /></h3>
	<div id="currentmembers">
	</div>

	<g:if test="${!protect}">
		<div id="showaddmembers">
			<n:button label="label.addmembers" class="add-button" onclick="\$('#showaddmembers').hide(); \$('#addmembers').fadeIn();"/>
		</div>

		<div id="addmembers">
			<h4><g:message code="nimble.template.members.add.heading" /></h4>

			<g:if test="${groupmembers}">
				<g:radio name="memberselect" id="searchmembergroups" onClick="\$('#memberaddusers').hide(); \$('#memberaddgroups').fadeIn();"/><g:message code="label.groups" />
				<g:radio name="memberselect" id="searchmemberusers" checked="true" onClick="\$('#memberaddgroups').hide(); \$('#memberaddusers').fadeIn();"/><g:message code="label.users" />
			</g:if>

			<div id="memberaddusers">
				<p>
					<g:message code="nimble.template.members.add.user.descriptive" />
				</p>

				<div class="searchbox">
					<g:textField name="qmembers" class="enhancedinput"/>
					<n:button onclick="nimble.searchMembers(${parent.id.encodeAsHTML()});" class="button icon icon_magnifier" label="label.searchusers" class="search-button"/>
					<n:button label="label.close" class="close-button" onclick="\$('#addmembers').hide(); \$('#showaddmembers').fadeIn();"/>
				</div>
				<div id="membersearchresponse" class="clear">
				</div>
			</div>

			<g:if test="${groupmembers}">
				<div id="memberaddgroups">
					<p>
						<g:message code="nimble.template.members.add.group.descriptive" />
					</p>

					<div class="searchbox">
						<g:textField name="qmembersgroup" class="enhancedinput"/>
						<n:button onclick="nimble.searchGroupMembers(${parent.id.encodeAsHTML()});" class="button icon icon_magnifier" label="label.searchgroups" class="search-button"/>
						<n:button label="label.close" class="close-button" onclick="\$('#addmembers').hide(); \$('#showaddmembers').fadeIn();"/>
					</div>
					<div id="membergroupsearchresponse">
					</div>
				</div>
			</g:if>
		</div>
	</g:if>
</div>