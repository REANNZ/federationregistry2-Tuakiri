<script type="text/javascript">
	var certificateValidationEndpoint = "${createLink(controller:'coreUtilities', action:'validateCertificate')}";
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
		$('#idpssodescriptorcreateform').formwizard({ 
		 	formPluginEnabled: false,
		 	validationEnabled: true,
		 	focusFirstInput : true
		});
		jQuery.validator.addMethod("validcert", function(value, element, params) { 
			validateCertificate();
			return newCertificateValid == true; 
		}, jQuery.format("PEM data invalid"));
		
		$('#cert').rules("add", {
		     required: true,
		     validcert: true
		});
		
		$('#samladvancedmode').hide();
		
		// We want IDP creation to be simple for users but we're actually creating both
		// an IDPSSODescriptor and an AttributeDescriptor. Backend expects data for both so we do a little bit of
		// cunning background copying ;).
		$('#idp\\.displayName').bind('blur', function() {
		    $('#aa\\.displayName').val($(this).val());
		});
		$('#idp\\.description').bind('blur', function() {
		    $('#aa\\.description').val($(this).val());
		});
		$('#cert').change( function() {
			$('#idp\\.crypto\\.encdata').val($(this).val());
			$('#idp\\.crypto\\.sigdata').val($(this).val());
		});
		$('#hostname').bind('blur',  function() {
			$('#entity\\.identifier').val($(this).val() + '/idp/shibboleth');
			$('#idp\\.post\\.uri').val($(this).val() + '/idp/profile/SAML2/POST/SSO');
			$('#idp\\.redirect\\.uri').val($(this).val() + '/idp/profile/SAML2/Redirect/SSO');
			$('#idp\\.artifact\\.uri').val($(this).val() + '/idp/profile/SAML2/SOAP/ArtifactResolution');
			$('#aa\\.attributeservice\\.uri').val($(this).val() + '/idp/profile/SAML2/SOAP/AttributeQuery');
		});
		
		$("#cert").bind('paste', function() { setTimeout(function() { validateCertificate(); }, 100); });
	});
	
	function attrchange(id) {
		if($('#idp\\.attributes\\.' + id + ':checked').val() != null)
			$('#aa\\.attributes\\.' + id).val('on');
		else
			$('#aa\\.attributes\\.' + id).val('off');
	}
	
	function validateCertificate() {
		$('#newcertificatedata').removeClass('error');
		fedreg.keyDescriptor_verify();
		if(!newCertificateValid) {
			$('#newcertificatedata').addClass('error');
		}
	}
	
</script>

<g:form action="${saveAction}" name="idpssodescriptorcreateform">
	<g:hiddenField name="active" value="true"/>
	<g:hiddenField name="aa.create" value="true"/>
	<g:if test="${!requiresContactDetails}">
		<g:hiddenField name="contact.id" value="${fr.contactID()}"/>
	</g:if>
	<g:hiddenField name="contact.type" value="administrative" />
	
	<g:if test="${requiresContactDetails}">
		<div class="step" id="contact">
			<h3><g:message code="fedreg.templates.identityprovider.create.contact.heading" /></h3>
			<p>
				<g:message code="fedreg.templates.identityprovider.create.contact.details" />
			</p>
			<table>
				<tr>
					<td>
						<label for="contact.givenName"><g:message code="label.givenname" /></label>
					</td>
					<td>
						<g:textField name="contact.givenName"  size="50" class="required" minlength="4"/>
					</td>
				</tr>
				<tr>
					<td>
						<label for="contact.surname"><g:message code="label.surname" /></label>
					</td>
					<td>
						<g:textField name="contact.surname"  size="50" class="required" minlength="4"/>
					</td>
				</tr>
				<tr>
					<td>
						<label for="contact.email"><g:message code="label.email" /></label>
					</td>
					<td>
						<g:textField name="contact.email"  size="50" class="required email" minlength="4"/>
					</td>
				</tr>
			</table>
		</div>
	</g:if>
	
	<div class="step" id="basic">
		<h3><g:message code="fedreg.templates.identityprovider.create.basicinformation.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.identityprovider.create.basicinformation.details" />
		</p>
		<table>
			<tr>
				<td>
					<label for="organization.id"><g:message code="label.organization" /></label>
				</td>
				<td>
					<g:select name="organization.id" from="${organizationList}" optionKey="id" optionValue="displayName" />
				</td>
			</tr>
			<tr>
				<td>
					<label for="idp.displayName"><g:message code="label.displayname" /></label>
				</td>
				<td>
					<g:hiddenField name="aa.displayName" value=""/>
					<g:textField name="idp.displayName"  size="50" class="required" minlength="4"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="idp.description"><g:message code="label.description" /></label>
				</td>
				<td>
					<g:hiddenField name="aa.description" />
					<g:textArea name="idp.description"  class="required" minlength="4" rows="8" cols="36"/>
				</td>
			</tr>
		</table>
	</div>
	
	<div class="step" id="saml">
		<h3><g:message code="fedreg.templates.identityprovider.create.saml.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.identityprovider.create.saml.details" />
		</p>
		<a href="#" onClick="$('#samladvancedmode').hide(); $('#samlbasicmode').show();">Shibboleth IDP</a> | <a href="#" onClick="$('#samlbasicmode').hide(); $('#samladvancedmode').show();">Other SAML 2.x</a>
		<table id="samlbasicmode">
			<tr>
				<td>
					<label for="hostname"><g:message code="label.url" /></label>
				</td>
				<td>
					<g:textField name="hostname" size="50" class="required url"/> <em> e.g https://idp.example.org
				</td>
			</tr>
		</table>
		<table id="samladvancedmode">
			<tr>
				<td>
					<label for="entity.identifier"><g:message code="label.entitydescriptor" /></label>
				</td>
				<td>
					<g:textField name="entity.identifier" size="75" class="required url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="idp.post.uri"><g:message code="label.httppostendpoint" /></label>
				</td>
				<td>
					<g:textField name="idp.post.uri" size="75" class="required url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="idp.redirect.uri"><g:message code="label.httpredirectendpoint" /></label>
				</td>
				<td>
					<g:textField name="idp.redirect.uri" size="75" class="required url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="idp.artifact.uri"><g:message code="label.soapartifactendpoint" /></label>
				</td>
				<td>
					<g:textField name="idp.artifact.uri" size="75" class="required url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="soapatrributequery"><g:message code="label.soapatrributequeryendpoint" /></label>
				</td>
				<td>
					<g:textField name="aa.attributeservice.uri" size="75" class="required url"/>
				</td>
			</tr>
		</table>
	</div>
	
	<div class="step" id="scope">
		<h3><g:message code="fedreg.templates.identityprovider.create.scope.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.identityprovider.create.scope.details" />
		</p>
		<table id="samlbasicmode">
			<tr>
				<td>
					<label for="scope"><g:message code="label.scope" /></label>
				</td>
				<td>
					<g:textField name="idp.scope" size="50" class="required"/> <em> e.g example.org
				</td>
			</tr>
		</table>
	</div>
	
	<div class="step" id="crypto">
		<h3><g:message code="fedreg.templates.identityprovider.create.crypto.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.identityprovider.create.crypto.details" />
		</p>
		<table>
			<tr>
				<td>
					<label for="newcertificatedata"><g:message code="label.certificate" /></label>
				</td>
				<td>
					<div id="newcertificatedetails">
					</div>
					<g:hiddenField name="idp.crypto.sigdata" />
					<g:hiddenField name="idp.crypto.sig" value="${true}" />
					<g:hiddenField name="idp.crypto.encdata" />
					<g:hiddenField name="idp.crypto.enc" value="${true}" />
					<g:textArea name="cert" id="cert" rows="25" cols="60" />
				</td>
			</tr>
		</table>
	</div>
	
	<div class="step" id="attributesupport">
		<h3><g:message code="fedreg.templates.identityprovider.create.attributesupport.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.identityprovider.create.attributesupport.details" />
		</p>
		<table>
			<tr>
				<th><g:message code="label.name" /></th>
				<th><g:message code="label.category" /></th>
				<th><g:message code="label.description" /></th>
				<th><g:message code="label.supported" /></th>
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
	</div>
	
	<div class="step" id="nameidformatsupport">
		<h3><g:message code="fedreg.templates.identityprovider.create.nameidformatsupport.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.identityprovider.create.nameidformatsupport.details" />
		</p>
		<table>
			<tr>
				<th><g:message code="label.name" /></th>
				<th><g:message code="label.description" /></th>
				<th><g:message code="label.supported" /></th>
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
	</div>
	
	<div class="step submit_step" id="creationsummary">
		<h3><g:message code="fedreg.templates.identityprovider.create.summary.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.identityprovider.create.summary.details" />
		</p>
	</div>

	<nav> 							
		<input class="navigation_button" id="back" value="Back" type="reset" />
		<input class="navigation_button" id="next" value="Next" type="submit" />
	</nav>

</g:form>