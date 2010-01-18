
<%@ page import="fedreg.core.AttributeCategory" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'attributeCategory.label')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>

			<div class="actions">
				<ul class="horizmenu">
					<li>
						<g:link action="edit" id="${attributeCategoryInstance?.id}" class="icon icon_edit icon_edit_attributeCategory"><g:message code="attributeCategory.edit.label"/></g:link>
					</li>
					<n:hasPermission target="attributeCategory:delete:${attributeCategoryInstance.id}">
					<li>
						<g:form action="delete" name="deleteattributeCategory">
							<g:hiddenField name="id" value="${attributeCategoryInstance?.id}"/>
							<n:confirmaction action="document.deleteattributeCategory.submit()" title="${message(code: 'delete.confirm.title')}" msg="${message(code: 'attributeCategory.delete.confirm.msg')}" accept="${message(code: 'default.button.accept.label')}" cancel="${message(code: 'default.button.cancel.label')}" class="icon icon_delete icon_delete_attributeCategory"><g:message code="attributeCategory.delete.label" /></n:confirmaction>
						</g:form>
					</li>
					</n:hasPermission>
				</ul>
			</div>

            <div class="details">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="attributeCategory.id.label" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: attributeCategoryInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="attributeCategory.name.label" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: attributeCategoryInstance, field: "name")}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>

        </div>
		<n:confirm/>
    </body>
</html>
