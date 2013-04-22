
<%@ page import="aaf.fr.foundation.MonitorType" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'monitorType.label', default: 'MonitorType')}" />
  </head>
  <body>

    <div id="list-monitorType" class="scaffold-list">
      <h3>List of MonitorType</h3>
      <table class="table borderless table-admin-sortable">
        <thead>
          <tr>
          
            <th><g:message encodeAs="HTML" code="monitorType.name.label" default="Name" /></th>
            <th><g:message encodeAs="HTML" code="monitorType.description.label" default="Description" /></th>
          
          <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${monitorTypeInstanceList}" status="i" var="monitorTypeInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
          
            <td>${fieldValue(bean: monitorTypeInstance, field: "name")}</td>
            <td>${fieldValue(bean: monitorTypeInstance, field: "description")}</td>
            <td><g:link action="show" id="${monitorTypeInstance.id}" class="btn btn-small"><g:message encodeAs="HTML" code="label.view"/></g:link>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </body>
</html>
