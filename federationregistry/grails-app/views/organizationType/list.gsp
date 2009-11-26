
<%@ page import="aaf.fedreg.core.OrganizationType" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'organizationType.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.list.label" args="[entityName]" /></h2>
            <div class="list">
                <table class="cleantable buttons">
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="description" title="${message(code: 'organizationType.description.label')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'organizationType.name.label')}" />
                        
                            <g:sortableColumn property="displayName" title="${message(code: 'organizationType.displayName.label')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${organizationTypeInstanceList}" status="i" var="organizationTypeInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean: organizationTypeInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: organizationTypeInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: organizationTypeInstance, field: "displayName")}</td>
                        
							<td><g:link action="show" id="${organizationTypeInstance.id}" class="button icon icon_view icon_view_organizationType"><g:message code="organizationType.view.label" /></g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${organizationTypeInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
