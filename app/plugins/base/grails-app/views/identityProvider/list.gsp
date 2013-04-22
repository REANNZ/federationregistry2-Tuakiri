
<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message encodeAs="HTML" code="views.fr.foundation.identityprovider.list.title" /></title>
  </head>
  <body>

    <h2><g:message encodeAs="HTML" code="views.fr.foundation.identityprovider.list.heading" /></h2>
    <table class="table borderless table-sortable">
      <thead>
        <tr>
          <th><g:message encodeAs="HTML" code="label.identityprovider" /></th>
          <th><g:message encodeAs="HTML" code="label.organization" /></th>
          <th><g:message encodeAs="HTML" code="label.entitydescriptor" /></th>
          <th><g:message encodeAs="HTML" code="label.functioning" /></th>
          <th/>
        </tr>
      </thead>
      <tbody>
      <g:each in="${identityProviderList}" var="identityProvider">
        <tr>
          <td>${fieldValue(bean: identityProvider, field: "displayName")}</td>
          <td>${fieldValue(bean: identityProvider, field: "organization.name")}</td>
          <td>${fieldValue(bean: identityProvider, field: "entityDescriptor.entityID")}</td>
          <td>
            <g:if test="${identityProvider.functioning()}">
              <g:message encodeAs="HTML" code="label.yes"/>
            </g:if>
            <g:else>
              <span class="label label-important"><g:message encodeAs="HTML" code="label.no"/></span>
            </g:else>
          </td>
          <td>
            <a href="${createLink(controller:'identityProvider', action:'show', id: identityProvider.id)}" class="btn btn-small"><g:message encodeAs="HTML" code="label.view"/></a>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>

  </body>
</html>
