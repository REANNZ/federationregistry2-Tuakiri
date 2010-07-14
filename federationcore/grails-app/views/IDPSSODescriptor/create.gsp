
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="members" />
        <title><g:message code="fedreg.view.members.identityprovider.list.title" /></title>
		
		<script type="text/javascript">
			var certificateValidationEndpoint = "${createLink(controller:'descriptorKeyDescriptor', action:'validateCertificate')}";
			var newCertificateValid = false;
			
			$(function() {	

				$('form').validate({
						success: function(label) {
							if($(label).next())
								$(label).next().remove()	// fix annoying bug where success labels are left laying about if duplicate validations
							label.removeClass("error").addClass("icon icon_accept").html("&nbsp;");
						},
						keyup: false
				});
				
				// We want IDP creation to be simple for users but we're actually creating both
				// an IDPSSODescriptor and an AttributeDescriptor. Backend expects data for both so we do a little bit of
				// cunning background copying ;).
				$('#idp\\.displayName').change( function() {
				    $('#aa\\.displayName').val($(this).val());
				} );
				$('#idp\\.description').change( function() {
				    $('#aa\\.description').val($(this).val());
				} );
				$('#newcertificatedata').change( function() {
					$('#idp\\.crypto\\.sigdata').val($(this).val());
					$('#idp\\.crypto\\.encdata').val($(this).val());
					$('#aa\\.crypto\\.sigdata').val($(this).val());
					$('#aa\\.crypto\\.encdata').val($(this).val());
				} );
						
				$('#tgt').click( function () {
					validateCertificate();
					if(newCertificateValid) {
						$('form').submit();
					}
					else
						$('form').validate().form();
				});
				
				$("#newcertificatedata").bind('blur', function() { setTimeout(function() { validateCertificate(); }, 100); });
				//$("#newcertificatedata").change( function() { setTimeout(function() {fedreg.keyDescriptor_verify();}, 100); });
			});
			
			function attrchange(id) {
				if($('#idp\\.attributes\\.' + id + ':checked').val() != null)
					$('#aa\\.attributes\\.' + id).val('on')
				else
					$('#aa\\.attributes\\.' + id).val('off')
			}
			
			function validateCertificate() {
				$('#newcertificatedata').removeClass('error');
				fedreg.keyDescriptor_verify();
				if(!newCertificateValid) {
					$('#newcertificatedata').addClass('error');
				}
			}
			
		</script>
		
    </head>
	
    <body>
		
        <div class="container">
            <h2><g:message code="fedreg.view.members.identityprovider.create.heading" /></h2>

			<div id="tgt">CLICK</div>
			<div id="submitdata">d</div>
			
			<g:form action="save">
				<g:hiddenField name="aa.create" value="true"/>
				
				<table class="easyinput datatable">
					<tr>
						<td>
							<label for="organization.id"><g:message code="fedreg.label.organization" /></label>
						</td>
						<td>
							<g:select name="organization.id" from="${organizationList}" optionKey="id" optionValue="displayName" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.displayName"><g:message code="fedreg.label.displayname" /></label>
						</td>
						<td>
							<g:hiddenField name="aa.displayName" value=""/>
							<g:textField name="idp.displayName" class="required" minlength="4"/>
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.description"><g:message code="fedreg.label.description" /></label>
						</td>
						<td>
							<g:hiddenField name="aa.description" />
							<g:textField name="idp.description" class="required" minlength="4"/>
						</td>
					</tr>
					<tr>
						<td>
							<label for="entity.identifier"><g:message code="fedreg.label.entitydescriptor" /></label>
						</td>
						<td>
							<g:textField name="entity.identifier" class="required url"/>
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.post.uri"><g:message code="fedreg.label.httppostendpoint" /></label>
						</td>
						<td>
							<g:textField name="idp.post.uri" class="required url"/>
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.redirect.uri"><g:message code="fedreg.label.httpredirectendpoint" /></label>
						</td>
						<td>
							<g:textField name="idp.redirect.uri" class="required url"/>
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.artifact.uri"><g:message code="fedreg.label.soapartifactendpoint" /></label>
						</td>
						<td>
							<g:textField name="idp.artifact.uri" class="required url"/>
						</td>
					</tr>
					<tr>
						<td>
							<label for="soapatrributequery"><g:message code="fedreg.label.soapatrributequeryendpoint" /></label>
						</td>
						<td>
							<g:textField name="aa.attributeservice.uri" class="required url"/>
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.crypto.sigdata"><g:message code="fedreg.label.certificate" /></label>
						</td>
						<td>
							<div id="newcertificatedetails">
							</div>
							<g:textArea name="newcertificatedata" />
							<g:hiddenField name="idp.crypto.sigdata" value="${true}" />
							<g:hiddenField name="idp.crypto.sig" value="${true}" />
							<g:hiddenField name="idp.crypto.encdata" />
							<g:hiddenField name="idp.crypto.enc" value="${true}" />
							
							<g:hiddenField name="aa.crypto.sigdata" />
							<g:hiddenField name="aa.crypto.sig" value="${true}" />
							<g:hiddenField name="aa.crypto.encdata" />
							<g:hiddenField name="aa.crypto.enc" value="${true}" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="active"><g:message code="fedreg.label.active" /></label>
						</td>
						<td>
							<g:checkBox name="active" value="${true}"/>
						</td>
					</tr>
				</table>
				
				<g:message code="fedreg.label.supportedattributes" />
				<table class="enhancedtabledata">
					<tr>
						<th><g:message code="fedreg.label.name" /></th>
						<th><g:message code="fedreg.label.category" /></th>
						<th><g:message code="fedreg.label.description" /></th>
						<th><g:message code="fedreg.label.supported" /></th>
					</tr>
					<g:each in="${attributeList.sort{it.category.name}}" var="attr" status="i">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>
							${fieldValue(bean: attr, field: "friendlyName")}
						</td>
						<td>
							${fieldValue(bean: attr, field: "category.name")}
						</td>
						<td>
							${fieldValue(bean: attr, field: "description")}
						</td>
						<td>
							<g:hiddenField name="aa.attributes.${attr.id}" />
							<g:checkBox name="idp.attributes.${attr.id}" onchange="attrchange(${attr.id})"/>
						</td>
					</tr>
					</g:each>
				</table>
				
				<g:message code="fedreg.label.supportednameidformats" />
				<table class="enhancedtabledata">
					<tr>
						<th><g:message code="fedreg.label.name" /></th>
						<th><g:message code="fedreg.label.description" /></th>
						<th><g:message code="fedreg.label.supported" /></th>
					</tr>
					<g:each in="${nameIDFormatList.sort{it.uri}}" var="nameidformat" status="i">
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>
							${fieldValue(bean: nameidformat, field: "uri")}
						</td>
						<td>
							${fieldValue(bean: nameidformat, field: "description")}
						</td>
						<td>
							<g:checkBox name="idp.nameidformats.${nameidformat.id}"/>
						</td>
					</tr>
					</g:each>
				</table>

			</g:form>

        </div>
    </body>
</html>
