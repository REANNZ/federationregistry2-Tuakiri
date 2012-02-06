
<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message code="fedreg.view.members.serviceprovider.list.title" /></title>
  </head>
  <body>

    <h2><g:message code="fedreg.view.members.serviceprovider.list.heading" /></h2>
    <table class="table sortable-table">
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
          <td>
            <g:if test="${serviceProvider.functioning()}">
              <g:message code="label.yes"/>
            </g:if>
            <g:else>
              <span class="not-functioning"><g:message code="label.no"/></span>
            </g:else>
          </td>
          <td>
            <a href="${createLink(controller:'serviceProvider', action:'show', id: serviceProvider.id)}" class="btn"><g:message code='label.view'/></a>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>

  </body>
</html>
