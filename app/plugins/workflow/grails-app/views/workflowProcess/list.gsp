
<html>
  <head>
    <meta name="layout" content="workflow" />
  </head>
  <body>
    <h2><g:message code="views.fr.workflow.process.list.heading" /></h2>
  
    <table class="table borderless table-sortable">
      <thead>
        <tr>
          <th><g:message code="label.name" default="Name"/></th>
          <th><g:message code="label.description" default="Description"/></th>
          <th/>
        </tr>
      </thead>
      <tbody>
      <g:each in="${processList.sort{it.name}}" var="p" status="i">
        <tr>
          <td>${fieldValue(bean: p, field: "name")}</td>
          <td>${fieldValue(bean: p, field: "description")}</td>
          <td>
            <a href="${createLink(controller:'workflowProcess', action:'show', id: p.id)}" class="btn"><g:message code="label.view" default="View"/></a>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
  </body>
</html>