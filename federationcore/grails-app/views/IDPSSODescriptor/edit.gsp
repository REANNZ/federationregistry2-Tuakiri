
<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.identityprovider.edit.title" /></title>
		
		<script type="text/javascript">
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
			});
		</script>
	</head>
	<body>

		<section>
			<h2><g:message code="fedreg.view.members.identityprovider.edit.heading" args="[identityProvider.displayName]"/></h2>
			
			<g:form action="update" id="${identityProvider.id}" method="PUT">
				<div class="step" id="basic">
					<h3><g:message code="fedreg.view.members.identityprovider.edit.basicinformation.heading" /></h3>
					<p>
						<g:message code="fedreg.view.members.identityprovider.edit.basicinformation.details" />
					</p>
					<table>
						<tr>
							<th>
								<label for="idp.displayName"><g:message code="label.displayname" /></label>
							</th>
							<td>
								<g:textField name="idp.displayName"  value="${identityProvider.displayName}" size="50" class="required" minlength="4" maxlength="255" />
							</td>
						</tr>
						<tr>
							<th>
								<label for="idp.description"><g:message code="label.description" /></label>
							</th>
							<td>
								<g:textArea name="idp.description"  value="${identityProvider.description}" class="required" minlength="4" rows="8" cols="36" maxlength="2000"/>
							</td>
						</tr>
						<tr>
							<th>
								<label for="idp.scope"><g:message code="label.scope" /></label>
							</th>
							<td>
								<g:textField name="idp.scope"  value="${identityProvider.scope}" class="required" size="50" class="required" minlength="4" maxlength="255"/>
							</td>
						</tr>
						<tr>
							<th>
								<label for="idp.status"><g:message code="label.status" /></label>
							</th>
							<td>
								<g:radioGroup name="idp.status" values="['true', 'false']" labels="['label.active', 'label.inactive']" value="${identityProvider.active}" >
									 ${it.radio} <g:message code="${it.label}" /> <br>
								</g:radioGroup>
							</td>
						</tr>
						<tr>
							<th>
								<label for="idp.wantauthnrequestssigned"><g:message code="label.requiresignedauthn" /></label>
							</th>
							<td>
								<g:radioGroup name="idp.wantauthnrequestssigned" values="['true', 'false']" labels="['label.true', 'label.false']" value="${identityProvider.wantAuthnRequestsSigned}" >
									 ${it.radio} <g:message code="${it.label}" /> <br>
								</g:radioGroup>
							</td>
						</tr>
						<tr>
							<th>
								<label for="idp.autoacceptservices"><g:message code="label.autoacceptservices" /></label>
							</th>
							<td>
								<g:radioGroup name="idp.autoacceptservices" values="['true', 'false']" labels="['label.true', 'label.false']" value="${identityProvider.autoAcceptServices}" >
									 ${it.radio} <g:message code="${it.label}" /> <br>
								</g:radioGroup>
							</td>
						</tr>
					</table>
				</div>
				
				<div class="step submit_step" id="creationsummary">
					<h3><g:message code="fedreg.view.members.identityprovider.edit.summary.heading" /></h3>
					<p>
						<g:message code="fedreg.view.members.identityprovider.edit.summary.details" />
					</p>
				</div>

				<nav> 							
					<input class="navigation_button" id="back" value="Back" type="reset" />
					<input class="navigation_button" id="next" value="Next" type="submit" />
				</nav>
			</g:form>
		</section>

	</body>
</html>