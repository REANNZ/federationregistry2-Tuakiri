
<%@ page import="fedreg.saml2.metadata.orm.UrlURI" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'urlURI.label')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.create.label" args="[entityName]" /></h2>

            <g:hasErrors bean="${urlURIInstance}">
            <div class="errors">
                <g:renderErrors bean="${urlURIInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="description"><g:message code="urlURI.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: urlURIInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${urlURIInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="uri"><g:message code="urlURI.uri.label" default="Uri" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: urlURIInstance, field: 'uri', 'errors')}">
                                    <g:textField name="uri" value="${urlURIInstance?.uri}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
					<button class="button icon icon_create icon_create_urlURI" type="submit"><g:message code="urlURI.create.label"/></button>
				    <g:link action="list" class="button icon icon_cancel icon_cancel_urlURI"><g:message code="default.button.cancel.label"/></g:link>
                </div>
            </g:form>
        </div>
    </body>
</html>
