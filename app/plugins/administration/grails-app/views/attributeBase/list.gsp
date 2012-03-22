
<%@ page import="aaf.fr.foundation.AttributeBase" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'attributeBase.label', default: 'AttributeBase')}" />
  </head>
  <body>

    <div id="list-attributeBase" class="scaffold-list">
      <h3>List of AttributeBase</h3>
      <table class="table borderless table-admin-sortable">
        <thead>
          <tr>
          
            <th><g:message code="attributeBase.name.label" default="Name" /></th>
            <th><g:message code="attributeBase.oid.label" default="OID" /></th>
            <th><g:message code="attributeBase.legacyName.label" default="Legacy Name" /></th>
          
          <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${attributeBaseInstanceList}" status="i" var="attributeBaseInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
          
            <td>${fieldValue(bean: attributeBaseInstance, field: "name")}</td>
          
            <td>${fieldValue(bean: attributeBaseInstance, field: "oid")}</td>
          
          
            <td>${fieldValue(bean: attributeBaseInstance, field: "legacyName")}</td>
          
            <td><g:link action="show" id="${attributeBaseInstance.id}" class="btn btn-small"><g:message code="label.view"/></g:link>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </body>
</html>
