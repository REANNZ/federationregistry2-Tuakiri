
<%@ page import="aaf.fedreg.core.IdentityProvider" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'identityProvider.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.list.label" args="[entityName]" /></h2>
            <div class="list">
                <table class="cleantable buttons">
                    <thead>
                        <tr>
                        
                            <th><g:message code="identityProvider.organization.label" /></th>
                   	    
                            <g:sortableColumn property="extensions" title="${message(code: 'identityProvider.extensions.label')}" />
                        
                            <th><g:message code="identityProvider.errorURL.label" /></th>
                   	    
                            <g:sortableColumn property="wantAuthnRequestsSigned" title="${message(code: 'identityProvider.wantAuthnRequestsSigned.label')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${identityProviderInstanceList}" status="i" var="identityProviderInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean: identityProviderInstance, field: "organization")}</td>
                        
                            <td>${fieldValue(bean: identityProviderInstance, field: "extensions")}</td>
                        
                            <td>${fieldValue(bean: identityProviderInstance, field: "errorURL")}</td>
                        
                            <td><g:formatBoolean boolean="${identityProviderInstance.wantAuthnRequestsSigned}" /></td>
                        
							<td><g:link action="show" id="${identityProviderInstance.id}" class="button icon icon_view icon_view_identityProvider"><g:message code="identityProvider.view.label" /></g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${identityProviderInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
