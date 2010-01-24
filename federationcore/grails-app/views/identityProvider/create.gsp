
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'identityProvider.label')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.create.label" args="[entityName]" /></h2>

            <g:hasErrors bean="${identityProviderInstance}">
            <div class="errors">
                <g:renderErrors bean="${identityProviderInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="organization"><g:message code="identityProvider.organization.label" default="Organization" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'organization', 'errors')}">
                                    <g:select name="organization.id" from="${fedreg.core.Organization.list()}" optionKey="id" value="${identityProviderInstance?.organization?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="extensions"><g:message code="identityProvider.extensions.label" default="Extensions" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'extensions', 'errors')}">
                                    <g:textField name="extensions" value="${identityProviderInstance?.extensions}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="errorURL"><g:message code="identityProvider.errorURL.label" default="Error URL" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'errorURL', 'errors')}">
                                    <g:select name="errorURL.id" from="${fedreg.core.UrlURI.list()}" optionKey="id" value="${identityProviderInstance?.errorURL?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="wantAuthnRequestsSigned"><g:message code="identityProvider.wantAuthnRequestsSigned.label" default="Want Authn Requests Signed" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'wantAuthnRequestsSigned', 'errors')}">
                                    <g:checkBox name="wantAuthnRequestsSigned" value="${identityProviderInstance?.wantAuthnRequestsSigned}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
					<button class="button icon icon_create icon_create_identityProvider" type="submit"><g:message code="identityProvider.create.label"/></button>
				    <g:link action="list" class="button icon icon_cancel icon_cancel_identityProvider"><g:message code="default.button.cancel.label"/></g:link>
                </div>
            </g:form>
        </div>
    </body>
</html>
