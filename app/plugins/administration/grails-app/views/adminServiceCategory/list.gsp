
<%@ page import="aaf.fr.foundation.ServiceCategory" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'serviceCategory.label', default: 'ServiceCategory')}" />
  </head>
  <body>

    <div id="list-serviceCategory" class="scaffold-list">
      <h3>List of ServiceCategory</h3>
      <table class="table borderless table-admin-sortable">
        <thead>
          <tr>
            <th><g:message encodeAs="HTML" code="serviceCategory.name.label" default="Name" /></th>
            <th><g:message encodeAs="HTML" code="serviceCategory.description.label" default="Description" /></th>
          <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${serviceCategoryInstanceList}" status="i" var="serviceCategoryInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>${fieldValue(bean: serviceCategoryInstance, field: "name")}</td>
            <td>${fieldValue(bean: serviceCategoryInstance, field: "description")}</td>
            <td><g:link action="show" id="${serviceCategoryInstance.id}" class="btn btn-small"><g:message encodeAs="HTML" code="label.view"/></g:link>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </body>
</html>
