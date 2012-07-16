
<%@ page import="aaf.fr.foundation.CAKeyInfo" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'CAKeyInfo.label', default: 'CAKeyInfo')}" />
  </head>
  <body>

    <div id="list-CAKeyInfo" class="scaffold-list">
      <h3>List of CAKeyInfo</h3>
      <table class="table borderless table-admin-sortable">
        <thead>
          <tr>
            <th><g:message code="CAKeyInfo.keyName.label" default="Key Name" /></th>
            <th><g:message code="CAKeyInfo.expiryDate.label" default="Expiry Date" /></th>
            <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${CAKeyInfoInstanceList}" status="i" var="CAKeyInfoInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>${CAKeyInfoInstance.keyName ? fieldValue(bean: CAKeyInfoInstance, field: "keyName") : 'Not Defined'}</td>
            <td><g:formatDate date="${CAKeyInfoInstance.expiryDate}" /></td>          
            <td><g:link action="show" id="${CAKeyInfoInstance.id}" class="btn btn-small"><g:message code="label.view"/></g:link>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </body>
</html>
