
<%@ page import="fedreg.core.OrganizationType" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'organizationType.label')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.edit.label" args="[entityName]" /></h2>

            <g:hasErrors bean="${organizationTypeInstance}">
            <div class="errors">
                <g:renderErrors bean="${organizationTypeInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form action="update" method="post" >
                <g:hiddenField name="id" value="${organizationTypeInstance?.id}" />
                <g:hiddenField name="version" value="${organizationTypeInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="description"><g:message code="organizationType.description.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationTypeInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${organizationTypeInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="organizationType.name.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationTypeInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${organizationTypeInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="displayName"><g:message code="organizationType.displayName.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationTypeInstance, field: 'displayName', 'errors')}">
                                    <g:textField name="displayName" value="${organizationTypeInstance?.displayName}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>

				<div class="buttons">
		            <button class="button icon icon_update icon_update_organizationType" type="submit"><g:message code="organizationType.update.label"/></button>
		            <g:link action="show" id="${organizationTypeInstance?.id}" class="button icon icon_cancel icon_cancel_organizationType"><g:message code="default.button.cancel.label"/></g:link>
		        </div>
            </g:form>
        </div>
    </body>
</html>
