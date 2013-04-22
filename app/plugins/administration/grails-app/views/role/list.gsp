<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
  </head>
  <body>

    <h2><g:message encodeAs="HTML" code="label.listroles" default="Roles" /></h2>

    <g:render template="/templates/flash" />

    <table class="table borderless table-admin-sortable">
      <thead>
        <tr>
          <th><g:message encodeAs="HTML" code="role.description.label" default="Description" /></th>
          <th><g:message encodeAs="HTML" code="role.name.label" default="Name" /></th>
          <th><g:message encodeAs="HTML" code="role.protect.label" default="Protect" /></th>
          <th/>
        </tr>
      </thead>
      <tbody>
      <g:each in="${roles}" status="i" var="role">
        <tr>
          <td>${fieldValue(bean: role, field: "description")}</td>
          <td>${fieldValue(bean: role, field: "name")}</td>
          <td><g:formatBoolean boolean="${role.protect}" /></td>
          <td><g:link action="show" id="${role.id}" class="btn btn-small"><g:message encodeAs="HTML" code="label.view"/></g:link>
        </tr>
      </g:each>
      </tbody>
    </table>

  </body>
</html>
