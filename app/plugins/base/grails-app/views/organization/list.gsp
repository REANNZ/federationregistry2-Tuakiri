
<html>
  <head>
    
    <meta name="layout" content="members" />
    <title><g:message encodeAs="HTML" code="views.fr.foundation.organization.list.title" /></title>
  </head>
  <body>
    <h2><g:message encodeAs="HTML" code="views.fr.foundation.organization.list.heading" /></h2>

    <g:render template="/templates/flash" plugin="foundation"/>

    <table class="table borderless table-sortable">
      <thead>
        <tr>
          <th><g:message encodeAs="HTML" code="label.organization" /></th>
          <th><g:message encodeAs="HTML" code="label.url" /></th>
          <th><g:message encodeAs="HTML" code="label.primarytype" /></th>
          <th><g:message encodeAs="HTML" code="label.functioning" /></th>
          <th />
        </tr>
      </thead>
      <tbody>
      <g:each in="${organizationList.sort{it.name}}" status="i" var="organization">
        <tr>
          <td>${fieldValue(bean: organization, field: "displayName")}</td>
          <td>${fieldValue(bean: organization, field: "url")}</td>
          <td>${fieldValue(bean: organization, field: "primary.displayName")}</td>
          <td>
            <g:if test="${organization.functioning()}">
              <g:message encodeAs="HTML" code="label.yes"/>
            </g:if>
            <g:else>
              <span class="label label-important"><g:message encodeAs="HTML" code="label.no"/></span>
            </g:else>
          </td>
          <td><a href="${createLink(controller:'organization', action:'show', id:organization.id)}" class="btn btn-small" /><g:message encodeAs="HTML" code="label.view"/></a></td>
        </tr>
      </g:each>
      </tbody>
    </table>
  </body>
</html>
