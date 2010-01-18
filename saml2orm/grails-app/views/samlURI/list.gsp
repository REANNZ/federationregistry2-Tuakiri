
<%@ page import="fedreg.saml2.metadata.orm.SamlURI" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'samlURI.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.list.label" args="[entityName]" /></h2>
            <div class="list">
                <table class="cleantable buttons">
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="description" title="${message(code: 'samlURI.description.label')}" />
                        
                            <g:sortableColumn property="type" title="${message(code: 'samlURI.type.label')}" />
                        
                            <g:sortableColumn property="uri" title="${message(code: 'samlURI.uri.label')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${samlURIInstanceList}" status="i" var="samlURIInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean: samlURIInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: samlURIInstance, field: "type")}</td>
                        
                            <td>${fieldValue(bean: samlURIInstance, field: "uri")}</td>
                        
							<td><g:link action="show" id="${samlURIInstance.id}" class="button icon icon_view icon_view_samlURI"><g:message code="samlURI.view.label" /></g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${samlURIInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
