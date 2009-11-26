
<%@ page import="aaf.fedreg.core.IdentityProvider" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'identityProviderAttributeCompliance.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code='identityProviderAttributeCompliance.label' /></h2>
            <div class="list">
                <table class="cleantable buttons">
                    <thead>
                        <tr>
                        
                            <th><g:message code="organization.displayName.label" /></th>
							                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${idpInstanceList}" status="i" var="identityProviderInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${identityProviderInstance?.organization?.displayName.encodeAsHTML()}</td>
                        
                            <td>&nbsp;</td>
                        
							<td><g:link action="show" id="${identityProviderInstance.id}" class="button icon icon_view icon_view_attributeCompliance"><g:message code="attributeCompliance.view.label" /></g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${idpInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
