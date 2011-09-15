<r:script>
	var certificateValidationEndpoint = "${createLink(controller:'coreUtilities', action:'validateCertificate')}";
	var knownIDPImplEndpoint = "${createLink(controller:'coreUtilities', action:'knownIDPImpl')}";
	var newCertificateValid = false;
	
	var knownIDPImpl;
	var currentImpl;
	
	$(function() {
		$.ajax({
			type: "GET",
			cache: false,
			dataType: 'json',
			url: knownIDPImplEndpoint,
			success: function(res) {
				knownIDPImpl = res;
				$.each(knownIDPImpl, function(key, value) {
					if(knownIDPImpl[key].selected) {
						currentImpl = key
						$('<input type="radio" class="currentimpl" name="knownimpls" checked value='+key+'> <strong>' + knownIDPImpl[key].displayName + '</strong><br>').appendTo($("#knownimpl"));
					}
					else
						$('<input type="radio" class="currentimpl" name="knownimpls" value='+key+'> <strong>' + knownIDPImpl[key].displayName + '</strong><br>').appendTo($("#knownimpl"));
				});
				
				$('input.currentimpl').change(function() {
					currentImpl = $(this).val();
					fedreg.configureIdentityProviderSAML($('#hostname').val());
				});
		    },
		    error: function (xhr, ajaxOptions, thrownError) {
				nimble.growl('error', xhr.responseText);
		    }
		});
		
		$('#hostname').alphanumeric({nocaps:true, ichars:';'});
		$('#idp\\.scope').alphanumeric({nocaps:true, allow:'.'});
		
		$('form').validate({
				rules: {
					'hostname': {
						required: function() {
							return ($("#entity\\.identifier").val() == "");
						}
					}
				},
				keyup: false
		});
		$('#idpssodescriptorcreateform').formwizard({ 
		 	formPluginEnabled: false,
		 	validationEnabled: true,
		 	focusFirstInput : true,
			disableUIStyles: true
		});
		jQuery.validator.addMethod("validcert", function(value, element, params) { 
			fedreg.validateCertificate();
			return newCertificateValid == true; 
		}, jQuery.format("PEM data invalid"));
		
		
		$('#cert').rules("add", {
		     required: true,
		     validcert: true
		});
		
		$('#samladvancedmode').hide();
		
		$('#hostname').bind('blur',  function() {
			if( $(this).val().indexOf('/', $(this).val().length - 1) !== -1 && $(this).val().length > 9)
				$(this).val($(this).val().substring(0, $(this).val().length - 1));

			fedreg.configureIdentityProviderSAML($(this).val());
		});
	});
	
	function attrchange(id) {
		if($('#idp\\.attributes\\.' + id + ':checked').val() != null)
			$('#aa\\.attributes\\.' + id).val('on');
		else
			$('#aa\\.attributes\\.' + id).val('off');
	}
	
</r:script>

<g:hasErrors>
    <div class="warning">
       <g:message code="fedreg.templates.identityprovider.create.errors" />
    </div>

	<n:errors bean="${identityProvider}" />
</g:hasErrors>

<g:form action="${saveAction}" name="idpssodescriptorcreateform">
	<g:hiddenField name="active" value="true"/>
	<g:hiddenField name="idp.autoacceptservices" value="true"/>
	<g:hiddenField name="aa.create" value="true"/>
	<g:if test="${!requiresContactDetails}">
		<g:hiddenField name="contact.id" value="${fr.contactID()}"/>
	</g:if>
	<g:hiddenField name="contact.type" value="administrative" />
	
	<div class="step" id="overview">
		<h3><g:message code="fedreg.templates.identityprovider.create.overview.heading" /></h3>
		<g:message code="fedreg.templates.identityprovider.create.overview.details" />
	</div>
	
	<g:if test="${requiresContactDetails}">
		<div class="step" id="contact">
			<h3><g:message code="fedreg.templates.identityprovider.create.contact.heading" /></h3>
			<p>
				<g:message code="fedreg.templates.identityprovider.create.contact.details" />
			</p>
			<g:hasErrors bean="${contact}">
				<div class="error"><g:renderErrors bean="${contact}"as="list"/></div>
			</g:hasErrors>
			<table>
				<tr>
					<td>
						<label for="contact.givenName"><g:message code="label.givenname" /></label>
					</td>
					<td>
						<g:textField name="contact.givenName"  size="50" class="required" value="${contact?.givenName}"/>
					</td>
				</tr>
				<tr>
					<td>
						<label for="contact.surname"><g:message code="label.surname" /></label>
					</td>
					<td>
						<g:textField name="contact.surname"  size="50" class="required" value="${contact?.surname}"/>
					</td>
				</tr>
				<tr>
					<td>
						<label for="contact.email"><g:message code="label.email" /></label>
					</td>
					<td>
						<g:textField name="contact.email"  size="50" class="required email" value="${contact?.email?.uri}"/>
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
					<g:select name="organization.id" from="${organizationList.sort{it.displayName}}" optionKey="id" optionValue="displayName" value="${organization?.id}"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="idp.displayName"><g:message code="label.displayname" /></label>
				</td>
				<td>
					<g:hiddenField name="aa.displayName" value=""/>
					<g:textField name="idp.displayName"  size="50" class="required" value="${identityProvider?.displayName}"/>
					<fr:tooltip code='fedreg.help.identityprovider.displayname' />
				</td>
			</tr>
			<tr>
				<td>
					<label for="idp.description"><g:message code="label.description" /></label>
				</td>
				<td>
					<g:hiddenField name="aa.description" />
					<g:textArea name="idp.description"  class="required" rows="8" cols="36" value="${identityProvider?.description}"/>
					<fr:tooltip code='fedreg.help.identityprovider.description' />
				</td>
			</tr>
		</table>
	</div>
	
	<div class="step" id="saml">
		<h3><g:message code="fedreg.templates.identityprovider.create.saml.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.identityprovider.create.saml.details" />
		</p>

		<span id="samlbasicmode">
			<h4><g:message code="fedreg.templates.identityprovider.create.saml.known.heading" /></h4>
			<p><g:message code="fedreg.templates.identityprovider.create.saml.known.descriptive" /></p>
			<p><span style="float:right;"><a href="#" class="view-button" onClick="$('#samlbasicmode').hide(); $('#samladvancedmode').fadeIn(); return false;"><g:message code="fedreg.templates.identityprovider.create.saml.known.switch" /></a></span></p>
			<table>
				<tr>
					<td>
						<label for="knownimpl"><g:message code="label.implementation" /></label>
					</td>
					<td>
						<span id="knownimpl"></span>
					</td>
				</tr>
				<tr>
					<td>
						<label for="hostname"><g:message code="label.host" /></label>
					</td>
					<td>
						<g:hasErrors bean="${entityDescriptor}">
							<div class="error"><g:renderErrors bean="${entityDescriptor}"as="list"/></div>
						</g:hasErrors>
						<g:textField name="hostname" size="50" class="url" value="${hostname}"/>
						<fr:tooltip code='fedreg.help.identityprovider.hostname' />
					</td>
				</tr>
			</table>
			
		</span>
		
		<span id="samladvancedmode">
			<h4><g:message code="fedreg.templates.identityprovider.create.saml.advanced.heading" /></h4>
			<p><g:message code="fedreg.templates.identityprovider.create.saml.advanced.descriptive" /></p>
			<p><span style="float: right;"><a href="#" class="view-button" onClick="$('#samladvancedmode').hide(); $('#samlbasicmode').fadeIn(); return false;"><g:message code="fedreg.templates.identityprovider.create.saml.advanced.switch" /></a></span></p>
			<table>
				<thead>
					<th/>
					<th><g:message code="label.url"/></th>
					<th><g:message code="label.index"/></th>
				</thead>
				<tbody>
					<tr>
						<td>
							<label for="entity.identifier"><g:message code="label.entitydescriptor" /></label>
						</td>
						<td>
							<g:hasErrors bean="${entityDescriptor}">
								<div class="error"><g:renderErrors bean="${entityDescriptor}"as="list"/></div>
							</g:hasErrors>
							<g:textField name="entity.identifier" size="64" class="required url" value="${entityDescriptor?.entityID}"/>
							<fr:tooltip code='fedreg.help.identityprovider.entitydescriptor' />
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.post.uri"><g:message code="label.httppostendpoint" /></label>
							<pre><g:message code="label.binding" />: SAML:2.0:bindings:HTTP-POST</pre>
						</td>
						<td>
							<g:hasErrors bean="${httpPost}">
								<div class="error"><g:renderErrors bean="${httpPost}"as="list"/></div>
							</g:hasErrors>
							<g:textField name="idp.post.uri" size="64" class="required url" value="${httpPost?.location?.uri}"/>
							<fr:tooltip code='fedreg.help.identityprovider.authpost' />
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.redirect.uri"><g:message code="label.httpredirectendpoint" /></label>
							<pre><g:message code="label.binding" />: SAML:2.0:bindings:HTTP-Redirect</pre>
						</td>
						<td>
							<g:hasErrors bean="${httpRedirect}">
								<div class="error"><g:renderErrors bean="${httpRedirect}"as="list"/></div>
							</g:hasErrors>
							<g:textField name="idp.redirect.uri" size="64" class="required url" value="${httpRedirect?.location?.uri}"/>
							<fr:tooltip code='fedreg.help.identityprovider.authredirect' />
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.artifact.uri"><g:message code="label.soapartifactendpoint" /></label>
							<pre><g:message code="label.binding" />: SAML:2.0:bindings:HTTP-Artifact</pre>
						</td>
						<td>
							<g:hasErrors bean="${soapArtifact}">
								<div class="error"><g:renderErrors bean="${soapArtifact}"as="list"/></div>
							</g:hasErrors>
							<g:textField name="idp.artifact.uri" size="64" class="required url" value="${soapArtifact?.location?.uri}"/>
							<fr:tooltip code='fedreg.help.identityprovider.authartifact' />
						</td>
						<td>
							<g:textField name="idp.artifact.index" size="2" class="required number" value="${soapArtifact?.index}"/>
							<fr:tooltip code='fedreg.help.endpoint.index' />
						</td>
					</tr>
					<tr>
						<td>
							<label for="aa.attributeservice.uri"><g:message code="label.soapatrributequeryendpoint" /></label>
							<pre><g:message code="label.binding" />: SAML:2.0:bindings:SOAP</pre>
						</td>
						<td>
							<g:hasErrors bean="${attributeAuthority}">
								<div class="error"><g:renderErrors bean="${attributeAuthority}"as="list"/></div>
							</g:hasErrors>
							<g:textField name="aa.attributeservice.uri" size="64" class="required url" value="${soapAttributeService?.location?.uri}"}/>
							<fr:tooltip code='fedreg.help.identityprovider.aasoap' />
						</td>
					</tr>
				</tbody>
			</table>
		</span>
	</div>
	
	<div class="step" id="scope">
		<h3><g:message code="fedreg.templates.identityprovider.create.scope.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.identityprovider.create.scope.details" />
		</p>
		<p>
			<g:message code="fedreg.templates.identityprovider.create.scope.example" />
		</p>
		<table id="samlbasicmode">
			<tr>
				<td>
					<label for="scope"><g:message code="label.scope" /></label>
				</td>
				<td>
					<g:textField name="idp.scope" size="50" class="required" value="${scope}"/>
					<fr:tooltip code='fedreg.help.identityprovider.scope' />
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
					<g:hiddenField name="idp.crypto.sig" value="${true}" />
					<g:textArea name="cert" id="cert" rows="25" cols="60" value="${certificate}"/>
					<fr:tooltip code='fedreg.help.serviceprovider.certificate' />
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
					${fieldValue(bean: attr, field: "name")}<br>
					<pre>OID: ${fieldValue(bean: attr, field: "oid")}</pre>
				</td>
				<td>
					${fieldValue(bean: attr, field: "category.name")}
				</td>
				<td style="width:400px;">
					${fieldValue(bean: attr, field: "description")}
				</td>
				<td>
					<g:hiddenField name="aa.attributes.${attr.id}" />
					<g:checkBox name="idp.attributes.${attr.id}" onchange="attrchange(${attr.id})" checked="${supportedAttributes?.contains(attr)}"/>
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
		<p>
			<strong><g:message code="fedreg.templates.identityprovider.create.nameidformatsupport.details.highlight" /></strong>
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
					<g:checkBox name="idp.nameidformats.${nameidformat.id}" checked="${nameidformat.uri == 'urn:oasis:names:tc:SAML:2.0:nameid-format:transient' || supportedNameIDFormats?.contains(nameidformat)}"/>
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
		<input id="back" value="${g.message(code:'label.back')}" type="reset" /> 
		<input id="next" value="${g.message(code:'label.next')}" type="submit" />
	</nav>

</g:form>