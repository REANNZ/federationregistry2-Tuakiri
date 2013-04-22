
<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message encodeAs="HTML" code="views.fr.foundation.serviceprovider.listarchived.title" /></title>
  </head>
  <body>

    <h2><g:message encodeAs="HTML" code="views.fr.foundation.serviceprovider.listarchived.heading" /></h2>
    <table class="table borderless table-sortable">
      <thead>
        <tr>
          <th><g:message encodeAs="HTML" code="label.serviceprovider" /></th>
          <th><g:message encodeAs="HTML" code="label.organization" /></th>
          <th><g:message encodeAs="HTML" code="label.entitydescriptor" /></th>
          <th/>
        </tr>
      </thead>
      <tbody>
      <g:each in="${serviceProviderList}" status="i" var="serviceProvider">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
          <td>${fieldValue(bean: serviceProvider, field: "displayName")}</td>
          <td>${fieldValue(bean: serviceProvider, field: "organization.name")}</td>
          <td>${fieldValue(bean: serviceProvider, field: "entityDescriptor.entityID")}</td>
          <td>
            <a href="${createLink(controller:'serviceProvider', action:'show', id: serviceProvider.id)}" class="btn btn-small"><g:message encodeAs="HTML" code="label.view"/></a>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>

  </body>
</html>
