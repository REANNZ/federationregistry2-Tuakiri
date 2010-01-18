
<%@ page import="fedreg.core.Organization" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'organization.label')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>

			<div class="actions">
				<ul class="horizmenu">
					<li>
						<g:link action="edit" id="${organizationInstance?.id}" class="icon icon_edit icon_edit_organization"><g:message code="organization.edit.label"/></g:link>
					</li>
					<n:hasPermission target="organization:delete:${organizationInstance.id}">
					<li>
						<g:form action="delete" name="deleteorganization">
							<g:hiddenField name="id" value="${organizationInstance?.id}"/>
							<n:confirmaction action="document.deleteorganization.submit()" title="${message(code: 'delete.confirm.title')}" msg="${message(code: 'organization.delete.confirm.msg')}" accept="${message(code: 'default.button.accept.label')}" cancel="${message(code: 'default.button.cancel.label')}" class="icon icon_delete icon_delete_organization"><g:message code="organization.delete.label" /></n:confirmaction>
						</g:form>
					</li>
					</n:hasPermission>
				</ul>
			</div>

            <div class="details">
                <table>
                    <tbody>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organization.id.label" default="Id" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: organizationInstance, field: "id")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organization.name.label" default="Name" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: organizationInstance, field: "name")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organization.displayName.label" default="Display Name" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: organizationInstance, field: "displayName")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organization.lang.label" default="Lang" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: organizationInstance, field: "lang")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organization.url.label" default="Url" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: organizationInstance, field: "url")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organization.extensions.label" default="Extensions" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: organizationInstance, field: "extensions")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organization.types.label" default="Types" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${organizationInstance.types}" var="t">
					                                    <li><g:link controller="organizationType" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organization.primary.label" default="Primary" /></td>
					                            
					                            <td valign="top" class="value"><g:link controller="organizationType" action="show" id="${organizationInstance?.primary?.id}">${organizationInstance?.primary?.encodeAsHTML()}</g:link></td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="organization.version.label" default="Version" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: organizationInstance, field: "version")}</td>
					                            
					                        </tr>
					                    
					                    </tbody>
                </table>
            </div>

        </div>
		<n:confirm/>
    </body>
</html>
