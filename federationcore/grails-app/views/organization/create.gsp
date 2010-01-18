
<%@ page import="fedreg.core.Organization" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'organization.label')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.create.label" args="[entityName]" /></h2>

            <g:hasErrors bean="${organizationInstance}">
            <div class="errors">
                <g:renderErrors bean="${organizationInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="organization.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${organizationInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="displayName"><g:message code="organization.displayName.label" default="Display Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'displayName', 'errors')}">
                                    <g:textField name="displayName" value="${organizationInstance?.displayName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lang"><g:message code="organization.lang.label" default="Lang" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'lang', 'errors')}">
                                    <g:textField name="lang" value="${organizationInstance?.lang}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="url"><g:message code="organization.url.label" default="Url" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'url', 'errors')}">
                                    <g:textField name="url" value="${organizationInstance?.url}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="extensions"><g:message code="organization.extensions.label" default="Extensions" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'extensions', 'errors')}">
                                    <g:textField name="extensions" value="${organizationInstance?.extensions}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="primary"><g:message code="organization.primary.label" default="Primary" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organizationInstance, field: 'primary', 'errors')}">
                                    <g:select name="primary.id" from="${fedreg.core.OrganizationType.list()}" optionKey="id" value="${organizationInstance?.primary?.id}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
					<button class="button icon icon_create icon_create_organization" type="submit"><g:message code="organization.create.label"/></button>
				    <g:link action="list" class="button icon icon_cancel icon_cancel_organization"><g:message code="default.button.cancel.label"/></g:link>
                </div>
            </g:form>
        </div>
    </body>
</html>
