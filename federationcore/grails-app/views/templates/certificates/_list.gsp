<div class="componentlist">
	<g:if test="${descriptor.keyDescriptors && descriptor.keyDescriptors.findAll{it.disabled == false}.size() > 0}">
		<table>
			<tbody>
			<g:each in="${descriptor.keyDescriptors.sort{it.id}}" status="i" var="kd">
				<g:if test="${!kd.disabled}">
					<tr><td colspan="2"><h4><g:message code="label.certificate"/> ${i+1}</h4></td></tr>
					<tr>
						<th><g:message code="label.keytype" /></th>
						<td>${kd.keyType.encodeAsHTML()}</td>
						<td>
							<n:hasPermission target="descriptor:${descriptor.id}:crypto:delete">
								<n:confirmaction action="fedreg.keyDescriptor_delete(${kd.id});" title="${message(code: 'fedreg.templates.certificates.remove.confirm.title')}" msg="${message(code: 'fedreg.templates.certificates.remove.confirm.descriptive')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" label="${message(code: 'label.delete')}" icon="trash" />
								<fr:tooltip code='fedreg.help.certificate.delete' />
							</n:hasPermission>
						</td>
					</tr>
					<tr>
						<th><g:message code="label.name" /></th>
						<td colspan="2">${(kd.keyInfo.keyName?:"N/A").encodeAsHTML()}</td>
					</tr>
					<tr>
						<th><g:message code="label.issuer" /></th>
						<td colspan="2">${kd.keyInfo.certificate.issuer.encodeAsHTML()}</td>
					</tr>
					<tr>
						<th><g:message code="label.subject" /></th>
						<td>${kd.keyInfo.certificate.subject.encodeAsHTML()}</td>
					</tr>
					<tr>
						<th><g:message code="label.expirydate" /></th>
						<td colspan="2">
							${kd.keyInfo.certificate.expiryDate.encodeAsHTML()}
							<g:if test="${kd.keyInfo.certificate.criticalAlert()}">
								<div class="critical">
									<p class="icon icon_exclamation"><g:message code="label.certificatecritical"/></p>
								</div>
							</g:if>
							<g:else>
								<g:if test="${kd.keyInfo.certificate.warnAlert()}">
									<div class="warning">
										<p><g:message code="label.certificatewarning"/></p>
									</div>
								</g:if>
								<g:else>
									<g:if test="${kd.keyInfo.certificate.infoAlert()}">
										<div class="information">
											<p><g:message code="label.certificateinfo"/></p>
										</div>
									</g:if>
								</g:else>
							</g:else>
						</td>
					</tr>
					<tr>
						<th><g:message code="label.selfsigned" /></th>
						<td colspan="2">
							<g:if test="${kd.keyInfo.certificate.subject.equals(kd.keyInfo.certificate.issuer)}">
								<g:message code="label.yes" />
							</g:if>
							<g:else>
								<g:message code="label.no" />
							</g:else>
						</td>
					</tr>
				</g:if>
			</g:each>
			</tbody>
		</table>
	</g:if>
	<g:else>
		<p class="error"><g:message code="fedreg.templates.certificates.none" /></p>
	</g:else>
</div>