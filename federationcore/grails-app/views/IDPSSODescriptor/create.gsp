
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="members" />
        <title><g:message code="fedreg.view.members.identityprovider.list.title" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="fedreg.view.members.identityprovider.create.heading" /></h2>

			<g:form action="save">
				
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
							<g:textField name="idp.displayName" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.description"><g:message code="fedreg.label.description" /></label>
						</td>
						<td>
							<g:textField name="idp.description" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="entity.identifier"><g:message code="fedreg.label.entitydescriptor" /></label>
						</td>
						<td>
							<g:textField name="entity.identifier" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.post.uri"><g:message code="fedreg.label.httppostendpoint" /></label>
						</td>
						<td>
							<g:textField name="idp.post.uri" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.redirect.uri"><g:message code="fedreg.label.httpredirectendpoint" /></label>
						</td>
						<td>
							<g:textField name="idp.redirect.uri" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.artifact.uri"><g:message code="fedreg.label.soapartifactendpoint" /></label>
						</td>
						<td>
							<g:textField name="idp.artifact.uri" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="soapatrributequery"><g:message code="fedreg.label.soapatrributequeryendpoint" /></label>
						</td>
						<td>
							<g:textField name="soapatrributequery" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="idp.crypto.sigdata"><g:message code="fedreg.label.certificate" /></label>
						</td>
						<td>
							<g:textArea name="idp.crypto.sigdata" />
							<g:hiddenField name="idp.crypto.sig" value="${true}" />
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
				
				<g:submitButton name="save" value="Save" />
				
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
										<g:checkBox name="idp.attributes.${attr.id}" />
									</td>
								</tr>
								</g:each>
							</table>
						</td>
			</g:form>

        </div>
    </body>
</html>
