
<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message code="fedreg.view.members.serviceprovider.list.title" /></title>
  </head>
  <body>

    <h2><g:message code="fedreg.view.members.serviceprovider.list.heading" /></h2>
    <table  class="sortable-table">
      <thead>
        <tr>
          <th><g:message code="label.serviceprovider" /></th>
          <th><g:message code="label.organization" /></th>
          <th><g:message code="label.entitydescriptor" /></th>
          <th><g:message code="label.functioning" /></th>
          <th/>
        </tr>
      </thead>
      <tbody>
      <g:each in="${serviceProviderList}" status="i" var="serviceProvider">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
          <td>${fieldValue(bean: serviceProvider, field: "displayName")}</td>
          <td>${fieldValue(bean: serviceProvider, field: "organization.name")}</td>
          <td>${fieldValue(bean: serviceProvider, field: "entityDescriptor.entityID")}</td>
          <td>${serviceProvider.functioning()}</td>
          <td>
            <a href="${createLink(controller:'serviceProvider', action:'show', id: serviceProvider.id)}" class="btn"><g:message code='label.view'/></a>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>

  </body>
</html>
