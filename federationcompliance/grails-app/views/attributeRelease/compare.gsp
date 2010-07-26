
<g:if test="${!minimumRequirements}">
<tr class="dynamicrow">
	<td colspan="3" class="error">
		<g:message code="fedreg.view.compliance.attributerelease.compare.required.warning" />
	</td>
</tr>	
</g:if>
<g:else>
<tr class="dynamicrow">
	<td colspan="3" class="success">
		<g:message code="fedreg.view.compliance.attributerelease.compare.valid" />
	</td>
</tr>
</g:else>

<tr class="dynamicrow">
	<td colspan="3">
		<h4><g:message code="fedreg.view.compliance.attributerelease.compare.required" /></h4>
	</td>
</tr>

<g:if test="${requiredAttributes.size() > 0}">
	<g:each in="${requiredAttributes.sort{it.attribute.friendlyName}}" status="i" var="requestedAttribute">
		<tr class="dynamicrow">
			<td>
				${requestedAttribute.attribute.friendlyName}
			</td>
			<td />
			<td>
				<g:if test="${idp.attributes.contains(requestedAttribute.attribute)}">
					<span class="icon icon_tick"><g:message code="label.supported"/></span>
				</g:if>
				<g:else>
					<span class="icon icon_cross"><g:message code="label.notsupported"/></span>
				</g:else>
			</td>
		</tr>
	</g:each>
</g:if>
<g:else>
	<tr class="dynamicrow">
		<td colspan="3">
			<g:message code="fedreg.view.compliance.attributerelease.compare.norequiredattributes" />
		</td>
	</tr>
</g:else>
	
<tr class="dynamicrow">
	<td colspan="3">
		<h4><g:message code="fedreg.view.compliance.attributerelease.compare.optional" /></h4>
	</td>
</tr>

<g:if test="${optionalAttributes.size() > 0}">
	<g:each in="${optionalAttributes}" status="i" var="requestedAttribute">
		<tr class="dynamicrow">
			<td>
				${requestedAttribute.attribute.friendlyName}
			</td>
			<td />
			<td>
				<g:if test="${idp.attributes.contains(requestedAttribute.attribute)}">
					<span class="icon icon_tick"><g:message code="label.supported"/></span>
				</g:if>
				<g:else>
					<span class="icon icon_cross"><g:message code="label.notsupported"/></span>
				</g:else>
			</td>
		</tr>
	</g:each>
</g:if>
<g:else>
	<tr class="dynamicrow">
		<td colspan="3">
			<g:message code="fedreg.view.compliance.attributerelease.compare.nooptionalattributes" />
		</td>
	</tr>
</g:else>