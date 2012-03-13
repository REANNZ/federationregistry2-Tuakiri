
<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message code="fedreg.view.members.identityprovider.list.title" /></title>
  </head>
  <body>

    <h2><g:message code="fedreg.view.members.identityprovider.list.heading" /></h2>
    <table class="span11 table borderless table-striped table-sortable">
      <thead>
        <tr>
          <th><g:message code="label.identityprovider" /></th>
          <th><g:message code="label.organization" /></th>
          <th><g:message code="label.entitydescriptor" /></th>
          <th><g:message code="label.functioning" /></th>
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
              <g:message code="label.yes"/>
            </g:if>
            <g:else>
              <span class="not-functioning"><g:message code="label.no"/></span>
            </g:else>
          </td>
          <td>
            <a href="${createLink(controller:'identityProvider', action:'show', id: identityProvider.id)}" class="btn btn-small"><g:message code="label.view"/></a>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>

  </body>
</html>
