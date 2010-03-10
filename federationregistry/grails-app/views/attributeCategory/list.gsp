
<%@ page import="fedreg.core.AttributeCategory" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'attributeCategory.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.list.label" args="[entityName]" /></h2>
            <div class="list">
                <table class="cleantable buttons">
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="name" title="${message(code: 'attributeCategory.name.label')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${attributeCategoryInstanceList}" status="i" var="attributeCategoryInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean: attributeCategoryInstance, field: "name")}</td>
                        
							<td><g:link action="show" id="${attributeCategoryInstance.id}" class="button icon icon_view icon_view_attributeCategory"><g:message code="attributeCategory.view.label" /></g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${attributeCategoryInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
