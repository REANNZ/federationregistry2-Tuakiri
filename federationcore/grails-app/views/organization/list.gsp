
<%@ page import="aaf.fedreg.core.Organization" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'organization.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.list.label" args="[entityName]" /></h2>
            <div class="list">
                <table class="cleantable buttons">
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="name" title="${message(code: 'organization.name.label')}" />
                        
                            <g:sortableColumn property="displayName" title="${message(code: 'organization.displayName.label')}" />
                        
                            <g:sortableColumn property="lang" title="${message(code: 'organization.lang.label')}" />
                        
                            <g:sortableColumn property="url" title="${message(code: 'organization.url.label')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${organizationInstanceList}" status="i" var="organizationInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean: organizationInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: organizationInstance, field: "displayName")}</td>
                        
                            <td>${fieldValue(bean: organizationInstance, field: "lang")}</td>
                        
                            <td>${fieldValue(bean: organizationInstance, field: "url")}</td>
                        
							<td><g:link action="show" id="${organizationInstance.id}" class="button icon icon_view icon_view_organization"><g:message code="organization.view.label" /></g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${organizationInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
