
<%@ page import="fedreg.saml2.metadata.orm.SamlURI" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'samlURI.label')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.create.label" args="[entityName]" /></h2>

            <g:hasErrors bean="${samlURIInstance}">
            <div class="errors">
                <g:renderErrors bean="${samlURIInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="description"><g:message code="samlURI.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: samlURIInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${samlURIInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="type"><g:message code="samlURI.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: samlURIInstance, field: 'type', 'errors')}">
                                    <g:select name="type" from="${fedreg.saml2.metadata.orm.SamlURIType?.values()}" value="${samlURIInstance?.type}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="uri"><g:message code="samlURI.uri.label" default="Uri" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: samlURIInstance, field: 'uri', 'errors')}">
                                    <g:textField name="uri" value="${samlURIInstance?.uri}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
					<button class="button icon icon_create icon_create_samlURI" type="submit"><g:message code="samlURI.create.label"/></button>
				    <g:link action="list" class="button icon icon_cancel icon_cancel_samlURI"><g:message code="default.button.cancel.label"/></g:link>
                </div>
            </g:form>
        </div>
    </body>
</html>
