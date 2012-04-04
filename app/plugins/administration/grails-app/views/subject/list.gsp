
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
  </head>
  <body>
    
    <h2><g:message code="label.listsubjects" default="Subjects" /></h2>

    <table class="table borderless table-admin-sortable">
      <thead>
        <tr>
          <th><g:message code="label.id" default="ID" /></th>
          <th><g:message code="label.principal" default="Principal" /></th>
          <th><g:message code="label.displayname" default="Display Name" /></th>
          <th><g:message code="label.email" default="Email" /></th>
          <th/>
        </tr>
      </thead>
      <tbody>
      <g:each in="${subjects}" status="i" var="subject">
        <tr>
          <td>${fieldValue(bean: subject, field: "id")}</td>
          <td>${fieldValue(bean: subject, field: "principal")}</td>
          <td>${fieldValue(bean: subject, field: "cn")}</td>
          <td>${fieldValue(bean: subject, field: "email")}</td>
          <td><g:link action="show" id="${subject.id}" class="btn btn-small"><g:message code="label.view"/></g:link>
        </tr>
      </g:each>
      </tbody>
    </table>

  </body>
</html>
