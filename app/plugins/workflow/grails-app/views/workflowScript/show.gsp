
<html>
  <head>
    <meta name="layout" content="workflow" />
  </head>
  
  <body>
      <h2><g:message code="fedreg.view.workflow.script.show.heading" args="[script.name]"/></h2>

      <table class="table borderless">
        <tbody>   
          <tr>
            <th><g:message code="label.description" /></th>
            <td>${fieldValue(bean: script, field: "description")}</td>
          </tr>
          <tr>
            <th><g:message code="label.created" /></th>
            <td>${fieldValue(bean: script, field: "dateCreated")}</td>
          </tr>
          <tr>
            <th><g:message code="label.lastupdated" /></th>
            <td>${fieldValue(bean: script, field: "lastUpdated")}</td>
          </tr>
          <tr>
            <th><g:message code="label.definition" /></th>
            <td></td>
          </tr>
        </tbody>
      </table>
      <pre style="padding: 24px;">${fieldValue(bean: script, field: "definition")}</pre>

  </body>
</html>