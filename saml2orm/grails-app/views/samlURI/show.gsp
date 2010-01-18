
<%@ page import="fedreg.saml2.metadata.orm.SamlURI" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'samlURI.label')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>

			<div class="actions">
				<ul class="horizmenu">
					<li>
						<g:link action="edit" id="${samlURIInstance?.id}" class="icon icon_edit icon_edit_samlURI"><g:message code="samlURI.edit.label"/></g:link>
					</li>
					<n:hasPermission target="samlURI:delete:${samlURIInstance.id}">
					<li>
						<g:form action="delete" name="deletesamlURI">
							<g:hiddenField name="id" value="${samlURIInstance?.id}"/>
							<n:confirmaction action="document.deletesamlURI.submit()" title="${message(code: 'delete.confirm.title')}" msg="${message(code: 'samlURI.delete.confirm.msg')}" accept="${message(code: 'default.button.accept.label')}" cancel="${message(code: 'default.button.cancel.label')}" class="icon icon_delete icon_delete_samlURI"><g:message code="samlURI.delete.label" /></n:confirmaction>
						</g:form>
					</li>
					</n:hasPermission>
				</ul>
			</div>

            <div class="details">
                <table>
                    <tbody>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="samlURI.id.label" default="Id" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: samlURIInstance, field: "id")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="samlURI.description.label" default="Description" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: samlURIInstance, field: "description")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="samlURI.type.label" default="Type" /></td>
					                            
					                            <td valign="top" class="value">${samlURIInstance?.type?.encodeAsHTML()}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="samlURI.uri.label" default="Uri" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: samlURIInstance, field: "uri")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="samlURI.version.label" default="Version" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: samlURIInstance, field: "version")}</td>
					                            
					                        </tr>
					                    
					                    </tbody>
                </table>
            </div>

        </div>
		<n:confirm/>
    </body>
</html>
