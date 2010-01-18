
<%@ page import="fedreg.core.Organization" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'organization.label')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.edit.label" args="[entityName]" /></h2>

            <g:hasErrors bean="${organizationInstance}">
            <div class="errors">
                <g:renderErrors bean="${organizationInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form action="update" method="post" >
                <g:hiddenField name="id" value="${organizationInstance?.id}" />
                <g:hiddenField name="version" value="${organizationInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="organization.name.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${organizationInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="displayName"><g:message code="organization.displayName.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'displayName', 'errors')}">
                                    <g:textField name="displayName" value="${organizationInstance?.displayName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="lang"><g:message code="organization.lang.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'lang', 'errors')}">
                                    <g:textField name="lang" value="${organizationInstance?.lang}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="url"><g:message code="organization.url.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'url', 'errors')}">
                                    <g:textField name="url" value="${organizationInstance?.url}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="extensions"><g:message code="organization.extensions.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'extensions', 'errors')}">
                                    <g:textField name="extensions" value="${organizationInstance?.extensions}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="types"><g:message code="organization.types.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'types', 'errors')}">
                                    <g:select name="types" from="${fedreg.core.OrganizationType.list()}" multiple="yes" optionKey="id" size="5" value="${organizationInstance?.types}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="primary"><g:message code="organization.primary.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'primary', 'errors')}">
                                    <g:select name="primary.id" from="${fedreg.core.OrganizationType.list()}" optionKey="id" value="${organizationInstance?.primary?.id}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>

				<div class="buttons">
		            <button class="button icon icon_update icon_update_organization" type="submit"><g:message code="organization.update.label"/></button>
		            <g:link action="show" id="${organizationInstance?.id}" class="button icon icon_cancel icon_cancel_organization"><g:message code="default.button.cancel.label"/></g:link>
		        </div>
            </g:form>
        </div>
    </body>
</html>
