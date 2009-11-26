
<%@ page import="aaf.fedreg.core.AttributeCategory" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'attributeCategory.label')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.edit.label" args="[entityName]" /></h2>

            <g:hasErrors bean="${attributeCategoryInstance}">
            <div class="errors">
                <g:renderErrors bean="${attributeCategoryInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form action="update" method="post" >
                <g:hiddenField name="id" value="${attributeCategoryInstance?.id}" />
                <g:hiddenField name="version" value="${attributeCategoryInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="attributeCategory.name.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: attributeCategoryInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${attributeCategoryInstance?.name}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>

				<div class="buttons">
		            <button class="button icon icon_update icon_update_attributeCategory" type="submit"><g:message code="attributeCategory.update.label"/></button>
		            <g:link action="show" id="${attributeCategoryInstance?.id}" class="button icon icon_cancel icon_cancel_attributeCategory"><g:message code="default.button.cancel.label"/></g:link>
		        </div>
            </g:form>
        </div>
    </body>
</html>
