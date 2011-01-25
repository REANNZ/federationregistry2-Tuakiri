
<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.entity.create.title" /></title>
		
		<r:script>
			$(function() {	

				$('form').validate({
						success: function(label) {
							if($(label).next())
								$(label).next().remove()
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
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.members.entity.create.heading" /></h2>
			
			<n:errors bean="${entity}" />
				
			<g:form action="save">
				<g:hiddenField name="contact.id" value="${fr.contactID()}"/>
				<g:hiddenField name="active" value="true"/>
				
				<div class="step" id="basic">
					<h3><g:message code="fedreg.view.members.entity.create.basicinformation.heading" /></h3>
					<p>
						<g:message code="fedreg.view.members.entity.create.basicinformation.details" />
					</p>
					<table>
						<tbody>
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
									<label for="entity.identifier"><g:message code="label.entitydescriptor" /></label>
								</td>
								<td>
									<g:textField name="entity.identifier" value="${entity.entityID}" size="50" class="required url"/>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<div class="step submit_step" id="creationsummary">
					<h3><g:message code="fedreg.view.members.entity.create.summary.heading" /></h3>
					<p>
						<g:message code="fedreg.view.members.entity.create.summary.details" />
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
