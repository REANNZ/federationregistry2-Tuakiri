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
		$('form').formwizard({ 
		 	formPluginEnabled: false,
		 	validationEnabled: true,
		 	focusFirstInput : true,
		});
	});
</script>

<g:form action="${saveAction}">
	<g:hiddenField name="active" value="true"/>
	<g:if test="${!requiresContactDetails}">
		<g:hiddenField name="contact.id" value="${fr.contactID()}"/>
	</g:if>
	<g:hiddenField name="contact.type" value="administrative" />
	<g:hiddenField name="organization.lang" value="en" />
	
	<g:if test="${requiresContactDetails}">
		<div class="step" id="contact">
			<h3><g:message code="fedreg.templates.organization.create.contact.heading" /></h3>
			<p>
				<g:message code="fedreg.templates.organization.create.contact.details" />
			</p>
			<table>
				<tr>
					<td>
						<label for="contact.givenName"><g:message code="label.givenname" /></label>
					</td>
					<td>
						<g:textField name="contact.fullName"  size="50" class="required" minlength="4"/>
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
	
	<div class="step" id="basicinformation">
		<h3><g:message code="fedreg.templates.organization.create.basicinformation.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.organization.create.basicinformation.details" />
		</p>
		<table>
			<tr>
				<td>
					<label for="organization.name"><g:message code="label.name" /></label>
				</td>
				<td>
					<g:textField name="organization.name"  size="50" class="required" minlength="4"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="organization.displayName"><g:message code="label.displayname" /></label>
				</td>
				<td>
					<g:textField name="organization.displayName"  size="50" class="required" minlength="4"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="organization.url"><g:message code="label.organizationurl" /></label>
				</td>
				<td>
					<g:textField name="organization.url"  size="50" class="required url" minlength="4"/>
				</td>
			</tr>
			<tr>
				<td>
					<label for="organization.primary"><g:message code="label.organizationtype" /></label>
				</td>
				<td>
					<g:select name="organization.primary" from="${organizationTypes}" optionKey="id" optionValue="displayName" />
				</td>
			</tr>
		</table>
	</div>
	
	<div class="step submit_step" id="creationsummary">
		<h3><g:message code="fedreg.templates.organization.create.summary.heading" /></h3>
		<p>
			<g:message code="fedreg.templates.organization.create.summary.details" />
		</p>
	</div>

	<nav> 							
		<input class="navigation_button" id="back" value="Back" type="reset" />
		<input class="navigation_button" id="next" value="Next" type="submit" />
	</nav>

</g:form>