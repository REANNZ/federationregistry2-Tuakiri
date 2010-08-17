
<html>
	<head>
		<meta name="layout" content="members" />
		<title><g:message code="fedreg.view.members.organization.edit.title" /></title>
		
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
				 	focusFirstInput : true,
				});
			});
		</script>
	</head>
	<body>

		<section>
			<h2><g:message code="fedreg.view.members.organization.edit.heading" args="[organization.displayName]"/></h2>
			
			<g:form action="update" id="${organization.id}">
				<g:hiddenField name="organization.lang" value="en" />
				
				<div class="step" id="basic">
					<h3><g:message code="fedreg.view.members.organization.edit.basicinformation.heading" /></h3>
					<p>
						<g:message code="fedreg.view.members.organization.edit.basicinformation.details" />
					</p>
					<table>
						<tr>
							<th>
								<label for="organization.name"><g:message code="label.name" /></label>
							</th>
							<td>
								<g:textField name="organization.name"  value="${organization.name}" class="required" minlength="4" maxlength="255"/>
							</td>
						</tr>
						<tr>
							<th>
								<label for="organization.displayName"><g:message code="label.displayname" /></label>
							</th>
							<td>
								<g:textField name="organization.displayName"  value="${organization.displayName}" size="50" class="required" minlength="4" maxlength="255" />
							</td>
						</tr>
						<tr>
							<th>
								<label for="organization.url"><g:message code="label.organizationurl" /></label>
							</th>
							<td>
								<g:textField name="organization.url"  size="50" class="required url" minlength="4" maxlength="255" value="${organization.url.uri}"/>
							</td>
						</tr>
						<tr>
							<th>
								<label for="organization.status"><g:message code="label.status" /></label>
							</th>
							<td>
								<g:radioGroup name="organization.active" values="['true', 'false']" labels="['label.active', 'label.inactive']" value="${organization.active}" >
									 ${it.radio} <g:message code="${it.label}" /> <br>
								</g:radioGroup>
							</td>
						</tr>
						<tr>
							<th>
								<label for="organization.primary"><g:message code="label.organizationtype" /></label>
							</th>
							<td>
								<g:select name="organization.primary" from="${organizationTypes}" optionKey="id" optionValue="displayName" value="${organization.primary.id}"/>
							</td>
						</tr>
						<tr>
							<th>
								<label for="organization.types"><g:message code="label.organizationsecondarytypes" /></label>
							</th>
							<td>
								<ul class="clean">
									<g:each in="${organizationTypes}" var="t">
										<g:if test="${t.name != organization.primary.name}">
											<li><g:checkBox name="organization.types.${t.id}" value="on" checked="${organization.types.contains(t)}"/> ${fieldValue(bean: t, field: "displayName")}</li>
										</g:if>
									</g:each>
								</ul>
							</td>
						</tr>
					</table>
				</div>
				
				<div class="step submit_step" id="creationsummary">
					<h3><g:message code="fedreg.view.members.organization.edit.summary.heading" /></h3>
					<p>
						<g:message code="fedreg.view.members.organization.edit.summary.details" />
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