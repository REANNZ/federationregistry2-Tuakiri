<script type="text/javascript">
	var newCertificateValid = false;
	$(function() {
		$("#newcertificate").hide();
		$("#validcertificate").hide();
		$("#newcertificatedata").bind('paste', function() { setTimeout(function() {fedreg.keyDescriptor_verify(); if(newCertificateValid) $("#validcertificate").show('highlight'); else $("#validcertificate").fadeOut(); }, 100); });
	});
</script>

	<div id="addcertificate">
		<n:button onclick="\$('#addcertificate').fadeOut(); \$('#newcertificate').fadeIn();" label="${message(code:'label.addcertificate')}" icon="plus"/>
	</div>
	
	<div id="newcertificate">
		<h3><g:message code="fedreg.template.certificates.certificatemanagement.addnew.heading"/></h3>
		<p>
			<g:message code="fedreg.template.certificates.certificatemanagement.addnew.requestformat" />
		</p>
		<div id="newcertificatedetails">
		</div>
		<section id="validcertificate">
			<n:button id="addnewcertificatelink" onclick="fedreg.keyDescriptor_create(); \$('#validcertificate').fadeOut();" label="${message(code:'label.add')}" icon="plus"/>
		</section>
		<g:textArea name="newcertificatedata" rows="25" cols="80"/><br>
		<n:button onclick="\$('#newcertificate').fadeOut(); \$('#addcertificate').fadeIn();" label="${message(code:'label.close')}" icon="close"/>
	</div>