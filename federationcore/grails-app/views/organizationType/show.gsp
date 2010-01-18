
<%@ page import="fedreg.core.OrganizationType" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'organizationType.label')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>

			<div class="actions">
				<ul class="horizmenu">
					<li>
						<g:link action="edit" id="${organizationTypeInstance?.id}" class="icon icon_edit icon_edit_organizationType"><g:message code="organizationType.edit.label"/></g:link>
					</li>
					<n:hasPermission target="organizationType:delete:${organizationTypeInstance.id}">
					<li>
						<g:form action="delete" name="deleteorganizationType">
							<g:hiddenField name="id" value="${organizationTypeInstance?.id}"/>
							<n:confirmaction action="document.deleteorganizationType.submit()" title="${message(code: 'delete.confirm.title')}" msg="${message(code: 'organizationType.delete.confirm.msg')}" accept="${message(code: 'default.button.accept.label')}" cancel="${message(code: 'default.button.cancel.label')}" class="icon icon_delete icon_delete_organizationType"><g:message code="organizationType.delete.label" /></n:confirmaction>
						</g:form>
					</li>
					</n:hasPermission>
				</ul>
			</div>

            <div class="details">
                <table>
                    <tbody>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organizationType.id.label" default="Id" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: organizationTypeInstance, field: "id")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organizationType.description.label" default="Description" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: organizationTypeInstance, field: "description")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organizationType.name.label" default="Name" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: organizationTypeInstance, field: "name")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organizationType.displayName.label" default="Display Name" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: organizationTypeInstance, field: "displayName")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organizationType.version.label" default="Version" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: organizationTypeInstance, field: "version")}</td>
					                            
					                        </tr>
					                    
					                    </tbody>
                </table>
            </div>

        </div>
		<n:confirm/>
    </body>
</html>
