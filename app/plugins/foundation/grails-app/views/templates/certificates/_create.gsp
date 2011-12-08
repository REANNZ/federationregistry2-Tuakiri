
<%@page import="aaf.fr.foundation.SPSSODescriptor" %>

<n:hasPermission target="descriptor:${descriptor.id}:crypto:create">
	<r:script>
		var newCertificateValid = false;
		$(function() {
			$("#newcertificate").hide();
			$("#addnewcertificatelink").hide();
			$("#cert").bind('paste', function() { setTimeout(function() {fedreg.keyDescriptor_verify('${descriptor.entityDescriptor.entityID}'); 		if(newCertificateValid) $("#addnewcertificatelink").fadeIn(); else 	$("#addnewcertificatelink").fadeOut(); }, 100); });
		});
	</r:script>
	<hr>
	<div id="addcertificate">
		<n:button onclick="\$('#addcertificate').fadeOut(); \$('#newcertificate').fadeIn();" label="${message(code:'label.addcertificate')}" class="add-button"/>
	</div>
	
	<div id="newcertificate">
		<h3><g:message code="fedreg.templates.certificates.certificatemanagement.addnew.heading"/></h3>
		<p>
			<g:message code="fedreg.templates.certificates.certificatemanagement.addnew.requestformat" />
		</p>
		<form id="newcryptoform">
			<div id="newcertificatedetails">
			</div>
			<table>
				<tbody>
					<tr>
						<th><g:message code="label.name"/></th>
						<td>
							<g:textField name="certname" size="50"/>
							<fr:tooltip code='fedreg.help.certificate.name' />
						</td>
					</tr>
					<tr>
						<th><g:message code="label.certificate"/></th>
						<td>
							<g:textArea name="cert" rows="25" cols="80"/>
							<fr:tooltip code='fedreg.help.certificate' />
						</td>
					</tr>
					<tr>
						<th><g:message code="label.signing" /></th>
						<td>
							<g:checkBox name="signing" value="${true}" />
							<fr:tooltip code='fedreg.help.certificate.sign' />
						</td>
					</tr>
					<tr>
						<th><g:message code="label.encryption" /></th>
						<td>
							<g:checkBox name="encryption" value="${descriptor.instanceOf(SPSSODescriptor)}"/>
							<fr:tooltip code='fedreg.help.certificate.enc' />
						</td>
					</tr>
					<tr>
						<td/>
						<td>
							<n:button id="addnewcertificatelink" onclick="fedreg.keyDescriptor_create(); \$('#validcertificate').fadeOut();" label="${message(code:'label.add')}" class="add-button"/>
							<n:button onclick="\$('#newcertificate').fadeOut(); \$('#addcertificate').fadeIn();" label="${message(code:'label.close')}" class="close-button"/>
						</td>
					</tr>
				</tbody>
			</table>
			
		</form>
	</div>
</n:hasPermission>