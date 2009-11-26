
<%@ page import="aaf.fedreg.saml2.metadata.orm.SingleSignOnService" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'singleSignOnService.label')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>

			<div class="actions">
				<ul class="horizmenu">
					<li>
						<g:link action="edit" id="${singleSignOnServiceInstance?.id}" class="icon icon_edit icon_edit_singleSignOnService"><g:message code="singleSignOnService.edit.label"/></g:link>
					</li>
					<n:hasPermission target="singleSignOnService:delete:${singleSignOnServiceInstance.id}">
					<li>
						<g:form action="delete" name="deletesingleSignOnService">
							<g:hiddenField name="id" value="${singleSignOnServiceInstance?.id}"/>
							<n:confirmaction action="document.deletesingleSignOnService.submit()" title="${message(code: 'delete.confirm.title')}" msg="${message(code: 'singleSignOnService.delete.confirm.msg')}" accept="${message(code: 'default.button.accept.label')}" cancel="${message(code: 'default.button.cancel.label')}" class="icon icon_delete icon_delete_singleSignOnService"><g:message code="singleSignOnService.delete.label" /></n:confirmaction>
						</g:form>
					</li>
					</n:hasPermission>
				</ul>
			</div>

            <div class="details">
                <table>
                    <tbody>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="singleSignOnService.id.label" default="Id" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: singleSignOnServiceInstance, field: "id")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="singleSignOnService.binding.label" default="Binding" /></td>
					                            
					                            <td valign="top" class="value"><g:link controller="samlURI" action="show" id="${singleSignOnServiceInstance?.binding?.id}">${singleSignOnServiceInstance?.binding?.encodeAsHTML()}</g:link></td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="singleSignOnService.location.label" default="Location" /></td>
					                            
					                            <td valign="top" class="value"><g:link controller="urlURI" action="show" id="${singleSignOnServiceInstance?.location?.id}">${singleSignOnServiceInstance?.location?.encodeAsHTML()}</g:link></td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="singleSignOnService.responseLocation.label" default="Response Location" /></td>
					                            
					                            <td valign="top" class="value"><g:link controller="urlURI" action="show" id="${singleSignOnServiceInstance?.responseLocation?.id}">${singleSignOnServiceInstance?.responseLocation?.encodeAsHTML()}</g:link></td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="singleSignOnService.owner.label" default="Owner" /></td>
					                            
					                            <td valign="top" class="value"><g:link controller="IDPSSODescriptor" action="show" id="${singleSignOnServiceInstance?.owner?.id}">${singleSignOnServiceInstance?.owner?.encodeAsHTML()}</g:link></td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="singleSignOnService.version.label" default="Version" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: singleSignOnServiceInstance, field: "version")}</td>
					                            
					                        </tr>
					                    
					                    </tbody>
                </table>
            </div>

        </div>
		<n:confirm/>
    </body>
</html>
