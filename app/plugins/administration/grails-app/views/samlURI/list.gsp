
<%@ page import="aaf.fr.foundation.SamlURI" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'samlURI.label', default: 'SamlURI')}" />
  </head>
  <body>

    <div id="list-samlURI" class="scaffold-list">
      <h3>List of SamlURI</h3>
      <table class="table borderless table-admin-sortable">
        <thead>
          <tr>
            <th><g:message encodeAs="HTML" code="samlURI.uri.label" default="Uri" /></th>
            <th><g:message encodeAs="HTML" code="samlURI.type.label" default="Type" /></th>
            <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${samlURIInstanceList}" status="i" var="samlURIInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>${fieldValue(bean: samlURIInstance, field: "uri")}</td>
            <td>${fieldValue(bean: samlURIInstance, field: "type")}</td>
            <td><g:link action="show" id="${samlURIInstance.id}" class="btn btn-small"><g:message encodeAs="HTML" code="label.view"/></g:link>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </body>
</html>
