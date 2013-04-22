
<%@ page import="aaf.fr.foundation.OrganizationType" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'organizationType.label', default: 'OrganizationType')}" />
  </head>
  <body>

    <div id="list-organizationType" class="scaffold-list">
      <h3>List of OrganizationType</h3>
      <table class="table borderless table-admin-sortable">
        <thead>
          <tr>
            <th><g:message encodeAs="HTML" code="organizationType.name.label" default="Name" /></th>
            <th><g:message encodeAs="HTML" code="organizationType.displayName.label" default="Display Name" /></th>
            <th><g:message encodeAs="HTML" code="organizationType.discoveryServiceCategory.label" default="Discovery Service Category" /></th>
            <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${organizationTypeInstanceList}" status="i" var="organizationTypeInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>${fieldValue(bean: organizationTypeInstance, field: "name")}</td>
            <td>${fieldValue(bean: organizationTypeInstance, field: "displayName")}</td>
            <td><g:formatBoolean boolean="${organizationTypeInstance.discoveryServiceCategory}" /></td>
            <td><g:link action="show" id="${organizationTypeInstance.id}" class="btn btn-small"><g:message encodeAs="HTML" code="label.view"/></g:link>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </body>
</html>
