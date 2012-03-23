
<%@ page import="aaf.fr.foundation.AttributeCategory" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'attributeCategory.label', default: 'AttributeCategory')}" />
  </head>
  <body>

    <div id="list-attributeCategory" class="scaffold-list">
      <h3>List of AttributeCategory</h3>
      <table class="table borderless table-admin-sortable">
        <thead>
          <tr>          
            <th><g:message code="attributeCategory.name.label" default="Name" /></th>   
            <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${attributeCategoryInstanceList}" status="i" var="attributeCategoryInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">          
            <td>${fieldValue(bean: attributeCategoryInstance, field: "name")}</td>
          
            <td><g:link action="show" id="${attributeCategoryInstance.id}" class="btn btn-small"><g:message code="label.view"/></g:link>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </body>
</html>
