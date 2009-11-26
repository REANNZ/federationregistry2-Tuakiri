
<%@ page import="aaf.fedreg.saml2.metadata.orm.SingleSignOnService" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'singleSignOnService.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.list.label" args="[entityName]" /></h2>
            <div class="list">
                <table class="cleantable buttons">
                    <thead>
                        <tr>
                        
                            <th><g:message code="singleSignOnService.binding.label" /></th>
                   	    
                            <th><g:message code="singleSignOnService.location.label" /></th>
                   	    
                            <th><g:message code="singleSignOnService.responseLocation.label" /></th>
                   	    
                            <th><g:message code="singleSignOnService.owner.label" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${singleSignOnServiceInstanceList}" status="i" var="singleSignOnServiceInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean: singleSignOnServiceInstance, field: "binding")}</td>
                        
                            <td>${fieldValue(bean: singleSignOnServiceInstance, field: "location")}</td>
                        
                            <td>${fieldValue(bean: singleSignOnServiceInstance, field: "responseLocation")}</td>
                        
                            <td>${fieldValue(bean: singleSignOnServiceInstance, field: "owner")}</td>
                        
							<td><g:link action="show" id="${singleSignOnServiceInstance.id}" class="button icon icon_view icon_view_singleSignOnService"><g:message code="singleSignOnService.view.label" /></g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${singleSignOnServiceInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
