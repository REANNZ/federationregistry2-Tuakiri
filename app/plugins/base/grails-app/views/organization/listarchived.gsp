
<html>
  <head>
    
    <meta name="layout" content="members" />
    <title><g:message encodeAs="HTML" code="views.fr.foundation.organization.listarchived.title" /></title>
  </head>
  <body>
    <h2><g:message encodeAs="HTML" code="views.fr.foundation.organization.listarchived.heading" /></h2>

    <table class="table borderless table-sortable">
      <thead>
        <tr>
          <th>${message(code: 'label.organization')}</th>
          <th>${message(code: 'label.url')}</th>
          <th>${message(code: 'label.primarytype')}</th>
          <th />
        </tr>
      </thead>
      <tbody>
      <g:each in="${organizationList.sort{it.name}}" status="i" var="organization">
        <tr>
          <td>${fieldValue(bean: organization, field: "displayName")}</td>
          <td>${fieldValue(bean: organization, field: "url")}</td>
          <td>${fieldValue(bean: organization, field: "primary.displayName")}</td>
          <td><a href="${createLink(controller:'organization', action:'show', id:organization.id)}" class="btn btn-small" /><g:message encodeAs="HTML" code="label.view"/></td>
        </tr>
      </g:each>
      </tbody>
    </table>
  </body>
</html>
