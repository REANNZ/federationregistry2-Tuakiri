
<html>
  <head>
    <meta name="layout" content="workflow" />
  </head>
  
  <body>
      <h2><g:message encodeAs="HTML" code="views.fr.workflow.process.show.heading" args="[process.name]"/></h2>

      <table class="table borderless">
        <tbody>   
          <tr>
            <th><g:message encodeAs="HTML" code="label.description" /></th>
            <td>${fieldValue(bean: process, field: "description")}</td>
          </tr>
          <tr>
            <th><g:message encodeAs="HTML" code="label.active" /></th>
            <td>${fieldValue(bean: process, field: "active")}</td>
          </tr>
          <tr>
            <th><g:message encodeAs="HTML" code="label.version" /></th>
            <td>${fieldValue(bean: process, field: "processVersion")}</td>
          </tr>
          <tr>
            <th><g:message encodeAs="HTML" code="label.created" /></th>
            <td>${fieldValue(bean: process, field: "dateCreated")}</td>
          </tr>
          <tr>
            <th><g:message encodeAs="HTML" code="label.definition" />:</th>
            <td></td>
          </tr>
        </tbody>
      </table>
      <pre>${fieldValue(bean: process, field: "definition")}</pre>
    
  </body>
</html>
