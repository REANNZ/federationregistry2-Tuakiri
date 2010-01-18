
<%@ page import="aaf.fedreg.core.Attribute" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'attribute.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.list.label" args="[entityName]" /></h2>
            <div class="list">
                <table class="cleantable buttons">
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="name" title="${message(code: 'attribute.name.label')}" />
                        
                            <th><g:message code="attribute.category.label" /></th>
                   	    
                            <g:sortableColumn property="friendlyName" title="${message(code: 'attribute.friendlyName.label')}" />
                        
                            <g:sortableColumn property="oid" title="${message(code: 'attribute.oid.label')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${attributeInstanceList}" status="i" var="attributeInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean: attributeInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: attributeInstance, field: "category")}</td>
                        
                            <td>${fieldValue(bean: attributeInstance, field: "friendlyName")}</td>
                        
                            <td>${fieldValue(bean: attributeInstance, field: "oid")}</td>
                        
							<td><g:link action="show" id="${attributeInstance.id}" class="button icon icon_view icon_view_attribute"><g:message code="attribute.view.label" /></g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${attributeInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
