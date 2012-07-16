
<%@ page import="aaf.fr.foundation.ContactType" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'contactType.label', default: 'ContactType')}" />
  </head>
  <body>

    <div id="list-contactType" class="scaffold-list">
      <h3>List of ContactType</h3>
      <table class="table borderless table-admin-sortable">
        <thead>
          <tr>
            <th><g:message code="contactType.name.label" default="Name" /></th>
            <th><g:message code="contactType.displayName.label" default="Display Name" /></th>
            <th><g:message code="contactType.description.label" default="Description" /></th>          
          <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${contactTypeInstanceList}" status="i" var="contactTypeInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>${fieldValue(bean: contactTypeInstance, field: "name")}</td>
            <td>${fieldValue(bean: contactTypeInstance, field: "displayName")}</td>    
            <td>${fieldValue(bean: contactTypeInstance, field: "description")}</td>
            <td><g:link action="show" id="${contactTypeInstance.id}" class="btn btn-small"><g:message code="label.view"/></g:link>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </body>
</html>
