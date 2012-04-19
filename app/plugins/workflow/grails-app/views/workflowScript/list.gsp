
<html>
  <head>  
    <meta name="layout" content="workflow" />
  </head>
  <body>
      <h2><g:message code="views.fr.workflow.script.list.heading" /></h2>
    
      <table class="table table-sortable borderless">
        <thead>
          <tr>
            <th><g:message code="label.name" default="Name"/></th>
            <th><g:message code="label.description" default="Description"/></th>
            <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${scriptList}" var="p" status="i">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
            <td>${fieldValue(bean: p, field: "name")}</td>
            <td>${fieldValue(bean: p, field: "description")}</td>
            <td>
              <a href="${createLink(controller:'workflowScript', action:'show', id: p.id)}" class="btn"><g:message code="label.view" default="View"/></a>
            </td>
          </tr>
        </g:each>
        </tbody>
      </table>
  </body>
</html>