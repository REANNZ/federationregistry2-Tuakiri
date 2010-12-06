
<n:hasPermission target="descriptor:${descriptor.id}:crypto:create">
	<script type="text/javascript">
		var newCertificateValid = false;
		$(function() {
			$("#newcertificate").hide();
			$("#addnewcertificatelink").hide();
			$("#cert").bind('paste', function() { setTimeout(function() {fedreg.keyDescriptor_verify('${descriptor.entityDescriptor.entityID}'); 		if(newCertificateValid) $("#addnewcertificatelink").fadeIn(); else 	$("#addnewcertificatelink").fadeOut(); }, 100); });
		});
	</script>
	<hr>
	<div id="addcertificate">
		<n:button onclick="\$('#addcertificate').fadeOut(); \$('#newcertificate').fadeIn();" label="${message(code:'label.addcertificate')}" icon="plus"/>
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
						<td><g:textField name="certname" size="50"/></td>
					</tr>
					<tr>
						<th><g:message code="label.certificate"/></th>
						<td><g:textArea name="cert" rows="25" cols="80"/></td>
					</tr>
					<tr>
						<th><g:message code="label.signing" /></th>
						<td><g:checkBox name="signing" value="${true}" /></td>
					</tr>
					<tr>
						<th><g:message code="label.encryption" /></th>
						<td><g:checkBox name="encryption" /></td>
					</tr>
					<tr>
						<td/>
						<td>
							<n:button id="addnewcertificatelink" onclick="fedreg.keyDescriptor_create(); \$('#validcertificate').fadeOut();" label="${message(code:'label.add')}" icon="plus"/>
							<n:button onclick="\$('#newcertificate').fadeOut(); \$('#addcertificate').fadeIn();" label="${message(code:'label.close')}" icon="close"/>
						</td>
					</tr>
				</tbody>
			</table>
			
		</form>
	</div>
</n:hasPermission>