
<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.serviceprovider.edit.title" /></title>
		
		<script type="text/javascript">
			$(function() {			
				$('form').validate({
						ignore: ":disabled",
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
			<h2><g:message code="fedreg.view.members.serviceprovider.edit.heading" args="[serviceProvider.displayName]"/></h2>
			
			<g:form action="update" id="${serviceProvider.id}" method="PUT">
				<div class="step" id="basic">
					<h3><g:message code="fedreg.view.members.serviceprovider.edit.basicinformation.heading" /></h3>
					<p>
						<g:message code="fedreg.view.members.serviceprovider.edit.basicinformation.details" />
					</p>
					<table>
						<tr>
							<th>
								<label for="sp.status"><g:message code="label.status" /></label>
							</th>
							<td>
								<g:radioGroup name="sp.status" values="['true', 'false']" labels="['label.active', 'label.inactive']" value="${serviceProvider.active}" >
									 ${it.radio} <g:message code="${it.label}" />
								</g:radioGroup>
								<fr:tooltip code='fedreg.help.serviceprovider.status' />
							</td>
						</tr>
						<tr>
							<th>
								<label for="sp.displayName"><g:message code="label.displayname" /></label>
							</th>
							<td>
								<g:textField name="sp.displayName"  value="${serviceProvider.displayName}" size="50" class="required" minlength="4" maxlength="255" />
								<fr:tooltip code='fedreg.help.serviceprovider.displayname' />
							</td>
						</tr>
						<tr>
							<th>
								<label for="sp.description"><g:message code="label.description" /></label>
							</th>
							<td>
								<g:textArea name="sp.description"  value="${serviceProvider.description}" class="required" minlength="4" rows="8" cols="36" maxlength="2000"/>
								<fr:tooltip code='fedreg.help.serviceprovider.description' />
							</td>
						</tr>
						<tr>
							<th>
								<label for="sp.servicedescription.connecturl"><g:message code="label.serviceurl" /></label>
							</th>
							<td>
								<g:textField name="sp.servicedescription.connecturl" value="${serviceProvider.serviceDescription.connectURL}" size="50" class="required url" maxlength="255"/>
								<fr:tooltip code='fedreg.help.serviceprovider.connecturl' />
							</td>
						</tr>
						<tr>
							<th>
								<label for="sp.servicedescription.logourl"><g:message code="label.servicelogourl" /></label>
							</th>
							<td>
								<g:textField name="sp.servicedescription.logourl" value="${serviceProvider.serviceDescription.logoURL}" size="50" class="url" maxlength="255"/>
								<fr:tooltip code='fedreg.help.serviceprovider.logourl' />
							</td>
						</tr>
					</table>
				</div>
				
				<div class="step submit_step" id="creationsummary">
					<h3><g:message code="fedreg.view.members.serviceprovider.edit.summary.heading" /></h3>
					<p>
						<g:message code="fedreg.view.members.serviceprovider.edit.summary.details" />
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