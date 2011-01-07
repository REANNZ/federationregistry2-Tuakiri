
<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.identityprovider.edit.title" /></title>
	</head>
	<body>
		<r:script>
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
				 	focusFirstInput : true,
					disableUIStyles: true
				});
			});
		</r:script>
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
								<label for="idp.status"><g:message code="label.status" /></label>
							</th>
							<td>
								<g:radioGroup name="idp.status" values="['true', 'false']" labels="['label.active', 'label.inactive']" value="${identityProvider.active}" >
									 ${it.radio} <g:message code="${it.label}" />
								</g:radioGroup>
								<fr:tooltip code='fedreg.help.identityprovider.status' />
							</td>
						</tr>
						<tr>
							<th>
								<label for="idp.displayName"><g:message code="label.displayname" /></label>
							</th>
							<td>
								<g:textField name="idp.displayName"  value="${identityProvider.displayName}" size="50" class="required" minlength="4" maxlength="255" />
								<fr:tooltip code='fedreg.help.identityprovider.displayname' />
							</td>
						</tr>
						<tr>
							<th>
								<label for="idp.description"><g:message code="label.description" /></label>
							</th>
							<td>
								<g:textArea name="idp.description"  value="${identityProvider.description}" class="required" minlength="4" rows="8" cols="36" maxlength="2000"/>
								<fr:tooltip code='fedreg.help.identityprovider.description' />
							</td>
						</tr>
						<tr>
							<th>
								<label for="idp.scope"><g:message code="label.scope" /></label>
							</th>
							<td>
								<g:textField name="idp.scope"  value="${identityProvider.scope}" class="required" size="50" class="required" minlength="4" maxlength="255"/>
								<fr:tooltip code='fedreg.help.identityprovider.scope' />
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