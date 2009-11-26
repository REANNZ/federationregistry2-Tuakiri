
<%@ page import="aaf.fedreg.core.Attribute" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'attribute.label')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>

			<div class="actions">
				<ul class="horizmenu">
					<li>
						<g:link action="edit" id="${attributeInstance?.id}" class="icon icon_edit icon_edit_attribute"><g:message code="attribute.edit.label"/></g:link>
					</li>
					<n:hasPermission target="attribute:delete:${attributeInstance.id}">
					<li>
						<g:form action="delete" name="deleteattribute">
							<g:hiddenField name="id" value="${attributeInstance?.id}"/>
							<n:confirmaction action="document.deleteattribute.submit()" title="${message(code: 'delete.confirm.title')}" msg="${message(code: 'attribute.delete.confirm.msg')}" accept="${message(code: 'default.button.accept.label')}" cancel="${message(code: 'default.button.cancel.label')}" class="icon icon_delete icon_delete_attribute"><g:message code="attribute.delete.label" /></n:confirmaction>
						</g:form>
					</li>
					</n:hasPermission>
				</ul>
			</div>

            <div class="details">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="attribute.id.label" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: attributeInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="attribute.name.label" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: attributeInstance, field: "name")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="attribute.nameFormat.label" /></td>
                            
                            <td valign="top" class="value"><g:link controller="samlURI" action="show" id="${attributeInstance?.nameFormat?.id}">${attributeInstance?.nameFormat?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="attribute.friendlyName.label" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: attributeInstance, field: "friendlyName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="attribute.oid.label" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: attributeInstance, field: "oid")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="attribute.headerName.label" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: attributeInstance, field: "headerName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="attribute.alias.label" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: attributeInstance, field: "alias")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="attribute.description.label" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: attributeInstance, field: "description")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="attribute.scope.label" /></td>
                            
                            <td valign="top" class="value"><g:link controller="attributeScope" action="show" id="${attributeInstance?.scope?.id}">${attributeInstance?.scope?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="attribute.category.label" /></td>
                            
                            <td valign="top" class="value"><g:link controller="attributeCategory" action="show" id="${attributeInstance?.category?.id}">${attributeInstance?.category?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>

        </div>
		<n:confirm/>
    </body>
</html>
