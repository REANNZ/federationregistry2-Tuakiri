
<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message code="fedreg.view.members.identityprovider.listarchived.title" /></title>
  </head>
  <body>

    <h2><g:message code="fedreg.view.members.identityprovider.listarchived.heading" /></h2>
    <table class="sortable-table">
      <thead>
        <tr>
          <th><g:message code="label.identityprovider" /></th>
          <th><g:message code="label.organization" /></th>
          <th><g:message code="label.entitydescriptor" /></th>
          <th/>
        </tr>
      </thead>
      <tbody>
      <g:each in="${identityProviderList}" status="i" var="identityProvider">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
          <td>${fieldValue(bean: identityProvider, field: "displayName")}</td>
          <td>${fieldValue(bean: identityProvider, field: "organization.name")}</td>
          <td>${fieldValue(bean: identityProvider, field: "entityDescriptor.entityID")}</td>
          <td>
            <a href="${createLink(controller:'IDPSSODescriptor', action:'show', id: identityProvider.id)}" class="btn"/><g:message code="label.view"/></a>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>

  </body>
</html>
