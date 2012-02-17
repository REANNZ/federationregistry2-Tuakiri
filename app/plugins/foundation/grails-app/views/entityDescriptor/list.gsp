
<html>
  <head>
    
    <meta name="layout" content="members" />
    <title><g:message code="fedreg.view.members.entity.list.title" /></title>
  </head>
  <body>

    <h2><g:message code="fedreg.view.members.entity.list.heading" /></h2>

    <g:render template="/templates/flash"  plugin="foundation"/>

    <table class="table borderless sortable-table">
      <thead>
        <tr>
          <th><g:message code="label.entitydescriptor" /></th>
          <th><g:message code="label.organization" /></th>
          <th><g:message code="label.functioning" /></th>
          <th />
        
        </tr>
      </thead>
      <tbody>
      <g:each in="${entityList.sort{it.entityID}}" status="i" var="entity">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
          <td>${fieldValue(bean: entity, field: "entityID")}</td>
          <td>${fieldValue(bean: entity, field: "organization.name")}</td>
          <td>
            <g:if test="${entity.functioning()}">
              <g:message code="label.yes"/>
            </g:if>
            <g:else>
              <span class="not-functioning"><g:message code="label.no"/></span>
            </g:else>
          </td>
          <td><a href="${createLink(controller:'entityDescriptor', action:'show', id:entity.id)}" class="btn" /><g:message code="label.view"/></td>
        </tr>
      </g:each>
      </tbody>
    </table>

  </body>
</html>
