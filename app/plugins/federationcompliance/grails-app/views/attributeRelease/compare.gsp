
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
	<g:each in="${requiredAttributes.sort{it.name}}" status="i" var="requiredAttribute">
		<tr class="dynamicrow">
			<td>
				${fieldValue(bean: requiredAttribute, field: "name")}
			</td>
			<td />
			<td>
				<g:if test="${suppliedRequiredAttributes.contains(requiredAttribute)}">
					<g:message code="label.supported"/>
				</g:if>
				<g:else>
					<g:message code="label.notsupported"/>
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
	<g:each in="${optionalAttributes.sort{it.name}}" status="i" var="optionalAttribute">
		<tr class="dynamicrow">
			<td>
				${fieldValue(bean: optionalAttribute, field: "name")}
			</td>
			<td />
			<td>
				<g:if test="${suppliedOptionalAttributes.contains(optionalAttribute)}">
					<g:message code="label.supported"/>
				</g:if>
				<g:else>
					<g:message code="label.notsupported"/>
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