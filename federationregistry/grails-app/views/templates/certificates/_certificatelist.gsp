<table>
	<tbody>
	<g:each in="${descriptor.keyDescriptors}" status="i" var="kd">
		<tr><td colspan="2"><h4>Certificate ${i+1}</h4></td></tr>
		<tr>
			<th><g:message code="fedreg.label.keytype" /></th>
			<td>${kd.keyType.encodeAsHTML()}</td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.created" /></th>
			<td>${kd.dateCreated.encodeAsHTML()}</td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.lastupdated" /></th>
			<td>${kd.lastUpdated.encodeAsHTML()}</td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.name" /></th>
			<td>${(kd.keyInfo.keyName?:"N/A").encodeAsHTML()}</td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.issuer" /></th>
			<td>${kd.keyInfo.certificate.issuer.encodeAsHTML()}</td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.subject" /></th>
			<td>${kd.keyInfo.certificate.subject.encodeAsHTML()}</td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.expirydate" /></th>
			<td>
				${kd.keyInfo.certificate.expiryDate.encodeAsHTML()}
				<g:if test="${kd.keyInfo.certificate.criticalAlert()}">
					<div class="critical">
						<p class="icon icon_exclamation"><g:message code="fedreg.label.certificatecritical"/></p>
					</div>
				</g:if>
				<g:else>
					<g:if test="${kd.keyInfo.certificate.warnAlert()}">
						<div class="warning">
							<p class="icon icon_error"><g:message code="fedreg.label.certificatewarning"/></p>
						</div>
					</g:if>
					<g:else>
						<g:if test="${kd.keyInfo.certificate.infoAlert()}">
							<div class="information">
								<p class="icon icon_information"><g:message code="fedreg.label.certificateinfo"/></p>
							</div>
						</g:if>
					</g:else>
				</g:else>
			</td>
		</tr>
		<tr>
			<th><g:message code="fedreg.label.selfsigned" /></th>
			<td>
				<g:if test="${kd.keyInfo.certificate.subject.equals(kd.keyInfo.certificate.issuer)}">
					<div class="icon icon_tick"><g:message code="fedreg.label.yes" /></div>
				</g:if>
				<g:else>
					<div class="icon icon_cross"><g:message code="fedreg.label.no" /></div>
				</g:else>
			</td>
		</tr>
		<tr>
			<td />
			<td>
				<g:if test="${allowremove}">
				<a href="#" onClick="removeKeyDescriptor(${kd.id});" class="button icon icon_delete"><g:message code="fedreg.link.remove"/></td>
				</g:if>
			</td>
		</tr>
	</g:each>
	</tbody>
</table>