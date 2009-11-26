
<%@ page import="aaf.fedreg.saml2.metadata.orm.SamlURI" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'samlURI.label')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.edit.label" args="[entityName]" /></h2>

            <g:hasErrors bean="${samlURIInstance}">
            <div class="errors">
                <g:renderErrors bean="${samlURIInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form action="update" method="post" >
                <g:hiddenField name="id" value="${samlURIInstance?.id}" />
                <g:hiddenField name="version" value="${samlURIInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="description"><g:message code="samlURI.description.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: samlURIInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${samlURIInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="type"><g:message code="samlURI.type.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: samlURIInstance, field: 'type', 'errors')}">
                                    <g:select name="type" from="${aaf.fedreg.saml2.metadata.orm.SamlURIType?.values()}" value="${samlURIInstance?.type}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="uri"><g:message code="samlURI.uri.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: samlURIInstance, field: 'uri', 'errors')}">
                                    <g:textField name="uri" value="${samlURIInstance?.uri}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>

				<div class="buttons">
		            <button class="button icon icon_update icon_update_samlURI" type="submit"><g:message code="samlURI.update.label"/></button>
		            <g:link action="show" id="${samlURIInstance?.id}" class="button icon icon_cancel icon_cancel_samlURI"><g:message code="default.button.cancel.label"/></g:link>
		        </div>
            </g:form>
        </div>
    </body>
</html>
