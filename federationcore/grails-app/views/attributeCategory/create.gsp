
<%@ page import="aaf.fedreg.core.AttributeCategory" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'attributeCategory.label')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.create.label" args="[entityName]" /></h2>

            <g:hasErrors bean="${attributeCategoryInstance}">
            <div class="errors">
                <g:renderErrors bean="${attributeCategoryInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="attributeCategory.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: attributeCategoryInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${attributeCategoryInstance?.name}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
					<button class="button icon icon_create icon_create_attributeCategory" type="submit"><g:message code="attributeCategory.create.label"/></button>
				    <g:link action="list" class="button icon icon_cancel icon_cancel_attributeCategory"><g:message code="default.button.cancel.label"/></g:link>
                </div>
            </g:form>
        </div>
    </body>
</html>
