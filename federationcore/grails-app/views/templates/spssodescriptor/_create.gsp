<script type="text/javascript">
	var certificateValidationEndpoint = "${createLink(controller:'coreUtilities', action:'validateCertificate')}";
	var newCertificateValid = false;
	
	$(function() {			
		$('form').validate({
				ignore: ":disabled",
				success: function(label) {
					if($(label).next())
						$(label).next().remove()	// fix annoying bug where success labels are left laying about if duplicate validations
					label.removeClass("error").addClass("icon icon_accept").html("&nbsp;");
				},
				keyup: false
		});
		$('form').formwizard({ 
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
		
		$('#cert').change( function() {
			$('#sp\\.crypto\\.encdata').val($(this).val());
			$('#sp\\.crypto\\.sigdata').val($(this).val());
		});
		$('#hostname').bind('blur',  function() {
			$('#entity\\.identifier').val($(this).val() + '/shibboleth');
			$('#sp\\.acs\\.post\\.uri').val($(this).val() + '/Shibboleth.sso/SAML2/POST');
			$('#sp\\.acs\\.artifact\\.uri').val($(this).val() + '/Shibboleth.sso/SAML2/Artifact');
			
			$('#sp\\.slo\\.artifact\\.uri').val($(this).val() + '/Shibboleth.sso/SLO/Artifact');
			$('#sp\\.slo\\.redirect\\.uri').val($(this).val() + '/Shibboleth.sso/SLO/Redirect');
			$('#sp\\.slo\\.soap\\.uri').val($(this).val() + '/Shibboleth.sso/SLO/SOAP');
			$('#sp\\.slo\\.post\\.uri').val($(this).val() + '/Shibboleth.sso/SLO/POST');
			$('#sp\\.drs\\.uri').val($(this).val() + '/Shibboleth.sso/DS');
			$('#sp\\.mnid\\.artifact\\.uri').val($(this).val() + '/Shibboleth.sso/NIM/Artifact');
			$('#sp\\.mnid\\.redirect\\.uri').val($(this).val() + '/Shibboleth.sso/NIM/Redirect');
			$('#sp\\.mnid\\.soap\\.uri').val($(this).val() + '/Shibboleth.sso/NIM/SOAP');
			$('#sp\\.mnid\\.post\\.uri').val($(this).val() + '/Shibboleth.sso/NIM/POST');
		});
		
		$("#cert").bind('paste', function() { setTimeout(function() { validateCertificate(); }, 100); });
	});
	
	function validateCertificate() {
		$('#newcertificatedata').removeClass('error');
		fedreg.keyDescriptor_verify();
		if(!newCertificateValid) {
			$('#newcertificatedata').addClass('error');
		}
	}
	
</script>

<g:form action="${saveAction}">
	<g:hiddenField name="active" value="true"/>
	<g:hiddenField name="aa.create" value="true"/>
	<g:if test="${!requiresContactDetails}">
		<g:hiddenField name="contact.id" value="${fr.contactID()}"/>
	</g:if>
	<g:hiddenField name="contact.type" value="administrative" />
	
	<g:if test="${requiresContactDetails}">
		<div class="step" id="contact">
			<h3><g:message code="fedreg.templates.serviceprovider.create.contact.heading" /></h3>
			<p>
				<g:message code="fedreg.templates.serviceprovider.create.contact.details" />
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
		<h3><g:message code="fedreg.templates.serviceprovider.create.basicinformation.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.serviceprovider.create.basicinformation.details" />
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
					<label for="sp.displayName"><g:message code="label.displayname" /></label>
				</td>
				<td>
					<g:textField name="sp.displayName"  size="50" class="required" minlength="4"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.description"><g:message code="label.description" /></label>
				</td>
				<td>
					<g:textArea name="sp.description"  class="required" minlength="4" rows="8" cols="36"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.servicedescription.connecturl"><g:message code="label.serviceurl" /></label>
				</td>
				<td>
					<g:textField name="sp.servicedescription.connecturl" size="50" class="required url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.servicedescription.logourl"><g:message code="label.servicelogourl" /></label>
				</td>
				<td>
					<g:textField name="sp.servicedescription.logourl" size="50" class="url"/> (100x100px should be publicly accessible)
				</td>
			</tr>
		</table>
	</div>
	
	<div class="step" id="servicedescription">
		<h3><g:message code="fedreg.templates.serviceprovider.create.servicedescription.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.serviceprovider.create.servicedescription.details" />
		</p>
		<table>
			<tr>
				<td>
					<label for="sp.servicedescription.furtherInfo"><g:message code="label.furtherinfo" /></label>
				</td>
				<td>
					<g:textArea name="sp.servicedescription.furtherinfo" rows="8" cols="48"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.servicedescription.provides"><g:message code="label.provides" /></label>
				</td>
				<td>
					<g:textArea name="sp.servicedescription.provides" rows="8" cols="48"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.servicedescription.benefits"><g:message code="label.benefits" /></label>
				</td>
				<td>
					<g:textArea name="sp.servicedescription.benefits" rows="8" cols="48"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.servicedescription.audience"><g:message code="label.audience" /></label>
				</td>
				<td>
					<g:textArea name="sp.servicedescription.audience" rows="8" cols="48"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.servicedescription.restrictions"><g:message code="label.restrictions" /></label>
				</td>
				<td>
					<g:textArea name="sp.servicedescription.restrictions" rows="8" cols="48"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.servicedescription.accessing"><g:message code="label.accessing" /></label>
				</td>
				<td>
					<g:textArea name="sp.servicedescription.accessing" rows="8" cols="48"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.servicedescription.support"><g:message code="label.support" /></label>
				</td>
				<td>
					<g:textArea name="sp.servicedescription.support" rows="8" cols="48"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.servicedescription.maintenance"><g:message code="label.maintenance" /></label>
				</td>
				<td>
					<g:textArea name="sp.servicedescription.maintenance" rows="8" cols="48"/>
				</td>
			</tr>
		</table>
	</div>
	
	<div class="step" id="saml">
		<h3><g:message code="fedreg.templates.serviceprovider.create.saml.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.serviceprovider.create.saml.details" />
		</p>
		<a href="#" onClick="$('#samladvancedmode').hide(); $('#samlbasicmode').show();">Shibboleth SP</a> | <a href="#" onClick="$('#samlbasicmode').hide(); $('#samladvancedmode').show();">Other SAML 2.x</a>
		<table id="samlbasicmode">
			<tr>
				<td>
					<label for="hostname"><g:message code="label.url" /></label>
				</td>
				<td>
					<g:textField name="hostname" size="50" class="required url"/> <em> e.g https://sp.example.org
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
					<label for="sp.acs.post.uri"><g:message code="label.acspostendpoint" /></label>
				</td>
				<td>
					<g:hiddenField name="sp.acs.post.isdefault" value="${true}" />
					<g:textField name="sp.acs.post.uri" size="75" class="required url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.acs.artifact.uri"><g:message code="label.acsartifactendpoint" /></label>
				</td>
				<td>
					<g:hiddenField name="sp.acs.artifact.isdefault" value="${false}" />
					<g:textField name="sp.acs.artifact.uri" size="75" class="required url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.slo.artifact.uri"><g:message code="label.sloartifactendpoint" /></label>
				</td>
				<td>
					<g:textField name="sp.slo.artifact.uri" size="75" class="url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.slo.redirect.uri"><g:message code="label.sloredirectendpoint" /></label>
				</td>
				<td>
					<g:textField name="sp.slo.redirect.uri" size="75" class="url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.slo.soap.uri"><g:message code="label.slosoapendpoint" /></label>
				</td>
				<td>
					<g:textField name="sp.slo.soap.uri" size="75" class="url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.slo.post.uri"><g:message code="label.slopostendpoint" /></label>
				</td>
				<td>
					<g:textField name="sp.slo.post.uri" size="75" class="url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.drs.uri"><g:message code="label.drsendpoint" /></label>
				</td>
				<td>
					<g:textField name="sp.drs.uri" size="75" class="url"/>
					<g:hiddenField name="sp.drs.isdefault" value="${true}" />
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.mnid.artifact.uri"><g:message code="label.mnidartifactendpoint" /></label>
				</td>
				<td>
					<g:textField name="sp.mnid.artifact.uri" size="75" class="url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.mnid.redirect.uri"><g:message code="label.mnidredirectendpoint" /></label>
				</td>
				<td>
					<g:textField name="sp.mnid.redirect.uri" size="75" class="url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.mnid.soap.uri"><g:message code="label.mnidsoapendpoint" /></label>
				</td>
				<td>
					<g:textField name="sp.mnid.soap.uri" size="75" class="url"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="sp.mnid.post.uri"><g:message code="label.mnidpostendpoint" /></label>
				</td>
				<td>
					<g:textField name="sp.mnid.post.uri" size="75" class="url"/>
				</td>
			</tr>
		</table>
	</div>
	
	<div class="step" id="crypto">
		<h3><g:message code="fedreg.templates.serviceprovider.create.crypto.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.serviceprovider.create.crypto.details" />
		</p>
		<table>
			<tr>
				<td>
					<label for="newcertificatedata"><g:message code="label.certificate" /></label>
				</td>
				<td>
					<div id="newcertificatedetails">
					</div>
					<g:hiddenField name="sp.crypto.sigdata" />
					<g:hiddenField name="sp.crypto.sig" value="${true}" />
					<g:hiddenField name="sp.crypto.encdata" />
					<g:hiddenField name="sp.crypto.enc" value="${true}" />
					<g:textArea name="cert" id="cert" rows="25" cols="60" />
				</td>
			</tr>
		</table>
	</div>
	
	<div class="step" id="attributesupport">
		<h3><g:message code="fedreg.templates.serviceprovider.create.attributesupport.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.serviceprovider.create.attributesupport.details" />
		</p>
		<table>
			<tbody>
				<tr>
					<th><g:message code="label.name" /></th>
					<th><g:message code="label.description" /></th>
					<th><g:message code="label.requested" /></th>
					<th><g:message code="label.required" /></th>
					<th><g:message code="label.reasonrequested" /></th>
				</tr>
				<g:each in="${attributeList.sort{it.category.name}}" var="attr" status="i">
					<g:if test="${!attr.specificationRequired}">
						<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
							<td>
								${fieldValue(bean: attr, field: "friendlyName")}
							</td>
							<td>
								${fieldValue(bean: attr, field: "description")}
							</td>
							<td>
								<g:checkBox name="sp.attributes.${attr.id}.requested" id="sp.attributes.${attr.id}.requested" onClick="\$('#spattributes${attr.id}reasoning').toggleClass('required');"/>
							</td>
							<td>
								<g:checkBox name="sp.attributes.${attr.id}.required"/>
							</td>
							<td>
								<input name="sp.attributes.${attr.id}.reasoning" id="spattributes${attr.id}reasoning" size="40" />
							</td>
						</tr>
					</g:if>
				</g:each>
			</tbody>
		</table>
	</div>
	
	<div class="step" id="specifiedattributesupport">
		<h3><g:message code="fedreg.templates.serviceprovider.create.specifiedattributesupport.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.serviceprovider.create.specifiedattributesupport.details" />
		</p>
			<table>
				<tbody>
					<tr>
						<th><g:message code="label.name" /></th>
						<th><g:message code="label.description" /></th>
						<th><g:message code="label.requested" /></th>
						<th><g:message code="label.required" /></th>
						<th><g:message code="label.reasonrequested" /></th>
					</tr>
					<g:each in="${attributeList.sort{it.category.name}}" var="attr" status="i">
						<g:if test="${attr.specificationRequired}">
							<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
								<td>
									${fieldValue(bean: attr, field: "friendlyName")}
								</td>
								<td>
									${fieldValue(bean: attr, field: "description")}
								</td>
								<td>
									<g:checkBox name="sp.attributes.${attr.id}.requested" onClick="\$('#spattributes${attr.id}reasoning').toggleClass('required'); \$('#spattributes${attr.id}requestedvalues1').toggleClass('required');"/>
								</td>
								<td>
									<g:checkBox name="sp.attributes.${attr.id}.required"/>
								</td>
								<td>
									<g:textField name="sp.attributes.${attr.id}.reasoning" id="spattributes${attr.id}reasoning" size="40"/>
								</td>
							</tr>
							<tr>
								<td>
								<td colspan="4">
									<table>
										<tbody>
											<tr>
												<th>
													<g:message code="label.requestedvalues" />
												</th>
												<td colspan="4">
													<g:each in="${1..10}" var="v">
														<g:textField name="sp.attributes.${attr.id}.requestedvalues.${v}" id="spattributes${attr.id}requestedvalues${v}" size="40"/><br>
													</g:each>
												</td>
											</tr>
										</tbody>
									</table>
							</tr>
						</g:if>
					</g:each>
				</tbody>
			</table>
	</div>
	
	<div class="step" id="nameidformatsupport">
		<h3><g:message code="fedreg.templates.serviceprovider.create.nameidformatsupport.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.serviceprovider.create.nameidformatsupport.details" />
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
					<g:checkBox name="sp.nameidformats.${nameidformat.id}"/>
				</td>
			</tr>
			</g:each>
		</table>
	</div>
	
	<div class="step submit_step" id="creationsummary">
		<h3><g:message code="fedreg.templates.serviceprovider.create.summary.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.serviceprovider.create.summary.details" />
		</p>
	</div>

	<nav> 							
		<input class="navigation_button" id="back" value="Back" type="reset" />
		<input class="navigation_button" id="next" value="Next" type="submit" />
	</nav>

</g:form>