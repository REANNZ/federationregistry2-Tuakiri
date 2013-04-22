
<html>
  <head>
    
    <meta name="layout" content="members" />
    <title><g:message encodeAs="HTML" code="views.fr.foundation.entity.list.title" /></title>
  </head>
  <body>

    <h2><g:message encodeAs="HTML" code="views.fr.foundation.entity.list.heading" /></h2>

    <g:render template="/templates/flash"  plugin="foundation"/>

    <table class="table borderless table-sortable">
      <thead>
        <tr>
          <th><g:message encodeAs="HTML" code="label.entitydescriptor" /></th>
          <th><g:message encodeAs="HTML" code="label.organization" /></th>
          <th><g:message encodeAs="HTML" code="label.functioning" /></th>
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
              <g:message encodeAs="HTML" code="label.yes"/>
            </g:if>
            <g:else>
              <span class="label label-important"><g:message encodeAs="HTML" code="label.no"/></span>
            </g:else>
          </td>
          <td><a href="${createLink(controller:'entityDescriptor', action:'show', id:entity.id)}" class="btn btn-small" /><g:message encodeAs="HTML" code="label.view"/></td>
        </tr>
      </g:each>
      </tbody>
    </table>

  </body>
</html>
