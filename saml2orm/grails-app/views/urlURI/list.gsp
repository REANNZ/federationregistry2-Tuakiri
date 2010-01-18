
<%@ page import="fedreg.saml2.metadata.orm.UrlURI" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'urlURI.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.list.label" args="[entityName]" /></h2>
            <div class="list">
                <table class="cleantable buttons">
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="description" title="${message(code: 'urlURI.description.label')}" />
                        
                            <g:sortableColumn property="uri" title="${message(code: 'urlURI.uri.label')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${urlURIInstanceList}" status="i" var="urlURIInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean: urlURIInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: urlURIInstance, field: "uri")}</td>
                        
							<td><g:link action="show" id="${urlURIInstance.id}" class="button icon icon_view icon_view_urlURI"><g:message code="urlURI.view.label" /></g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${urlURIInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
