
<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message code="fedreg.view.members.entity.listarchived.title" /></title>
  </head>
  <body>

    <h2><g:message code="fedreg.view.members.entity.listarchived.heading" /></h2>

    <table class="table borderless sortable-table">
      <thead>
        <tr>    
          <th><g:message code="label.entitydescriptor" /></th>
          <th><g:message code="label.organization" /></th>
          <th />
        </tr>
      </thead>
      <tbody>
      <g:each in="${entityList.sort{it.entityID}}" status="i" var="entity">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
          <td>${fieldValue(bean: entity, field: "entityID")}</td>
          <td>${fieldValue(bean: entity, field: "organization.name")}</td>
          <td><a href="${createLink(controller:'entityDescriptor', action:'show', id:entity.id)}" class="btn" /><g:message code="label.view"/></td>
        </tr>
      </g:each>
      </tbody>
    </table>

  </body>
</html>
