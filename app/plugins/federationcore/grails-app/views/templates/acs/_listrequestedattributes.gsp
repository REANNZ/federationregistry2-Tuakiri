<g:if test="${requestedAttributes}">
	<script type="text/javascript">
		$(function() {
			$(".editablerow").hide();
			$('.tip').tipTip({maxWidth: "auto", edgeOffset: 10, maxWidth:'200px', defaultPosition:"top"});
		});
	</script>
	<table>
		<thead>
			<tr>
				<th><g:message code="label.attribute" /></th>
				<th><g:message code="label.category" /></th>
				<th><g:message code="label.approved" /></th>
				<th><g:message code="label.required" /></th>
				<th><g:message code="label.reason" /></th>
				<th/>
			</tr>
		</thead>
		<tbody>
			<g:each in="${requestedAttributes}" status="j" var="ra">
				<tr id="ra-${ra.id}">
					<td>
						${ra.base.friendlyName.encodeAsHTML()}
						<pre>OID: ${ra.base.oid?.encodeAsHTML()}</pre>
					</td>
					<td>${ra.base.category.name.encodeAsHTML()}</td>
					<td>
						<g:if test="${ra.approved}">
							<g:message code="label.yes" />
						</g:if>
						<g:else>
							<span class="warning"><g:message code="fedreg.templates.acs.reqattributes.workflow" /></span>
						</g:else>
					</td>
					<td>
						<g:if test="${ra.isRequired}">
							<g:message code="label.yes" />
						</g:if>
						<g:else>
							<g:message code="label.no" />
						</g:else>
					</td>
					<td> 
						<div id="ra-reason-${ra.id}">
							${ra.reasoning?.encodeAsHTML()}
						</div>
					</td>
					<td>
						<n:hasPermission target="descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:add">
							<n:button onclick="\$('#ra-${ra.id}').hide(); \$('#ra-edit-${ra.id}').fadeIn(); return false;" label="label.edit" icon="gear"/>
						</n:hasPermission>
						<n:hasPermission target="descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:remove">
								<n:confirmaction action="fedreg.acs_reqattribute_remove(${ra.id}, ${ra.attributeConsumingService.id}, '${containerID}' );" title="${message(code: 'fedreg.templates.acs.reqattributes.remove.confirm.title')}" msg="${message(code: 'fedreg.templates.acs.reqattributes.remove.confirm.descriptive', args:[ra.base.friendlyName.encodeAsHTML()])}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" icon="trash" label="label.remove" />
						</n:hasPermission>
					</td>
				</tr>
				<tr id="ra-edit-${ra.id}" class="editablerow">
					<td>
						${ra.base.friendlyName.encodeAsHTML()}
						<pre>OID: ${ra.base.oid?.encodeAsHTML()}</pre>
					</td>
					<td>${ra.base.category.name.encodeAsHTML()}</td>
					<td>
						<g:if test="${ra.approved}">
							<g:message code="label.yes" />
						</g:if>
						<g:else>
							<span class="warning"><g:message code="fedreg.templates.acs.reqattributes.workflow" /></span>
						</g:else>
					</td>
					<td>
						<g:checkBox name="ra-edit-${ra.id}-required" id="ra-edit-${ra.id}-required" checked="${ra?.isRequired}"/>
						<fr:tooltip code='fedreg.help.serviceprovider.attribute.isrequired' />
					</td>
					<td> 
						<form class="needsvalidation">
							<input name="ra-edit-${ra.id}-reason" id="ra-edit-${ra.id}-reason" type="text" class="required" size="40" value="${ra.reasoning?.encodeAsHTML()}"/>
							<fr:tooltip code='fedreg.help.acs.reason' /><br>
						</form>
					</td>
					<td>
						<n:button onclick="if( \$('#ra-edit-${ra.id}-reason').parent().valid() ) fedreg.acs_reqattribute_update( ${ra.attributeConsumingService.id}, ${ra.id}, \$('#ra-edit-${ra.id}-reason').val(), \$('#ra-edit-${ra.id}-required').is(':checked'), '${containerID}');" label="${message(code:'label.update')}" icon="check"/>
						<n:button onclick="\$('#ra-edit-${ra.id}').hide(); \$('#ra-${ra.id}').fadeIn(); return false;" label="${message(code:'label.cancel')}" class="close-button"/>
					</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<div class="warning">
		<g:message code="fedreg.templates.acs.reqattributes.not.requested" />
	</div>
</g:else>