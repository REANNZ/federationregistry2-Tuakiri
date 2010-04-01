
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="members" />
        <title><g:message code="fedreg.view.members.identityprovider.list.title" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="fedreg.view.members.identityprovider.list.heading" /></h2>
            <div>
                <table class="enhancedtabledata">
                    <thead>
                        <tr>
							<g:sortableColumn property="displayName" title="${message(code: 'fedreg.label.identityprovider')}" />
                            <g:sortableColumn property="organization" title="${message(code: 'fedreg.label.organization')}" />
                            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${identityProviderList}" status="i" var="identityProvider">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
							<td>${fieldValue(bean: identityProvider, field: "displayName")}</td>
							
                            <td>${fieldValue(bean: identityProvider, field: "organization")}</td>
                        
							<td><g:link action="show" id="${identityProvider.id}" class="button icon icon_view icon_view_identityProvider"><g:message code="fedreg.link.view" /></g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${identityProviderTotal}" />
            </div>
        </div>
    </body>
</html>
