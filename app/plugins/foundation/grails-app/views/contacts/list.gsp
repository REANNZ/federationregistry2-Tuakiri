
<html>
  <head>
    <meta name="layout" content="members" />
  </head>
  <body>
    <h2><g:message code="fedreg.view.members.contacts.list.heading" /></h2>

    <table class="table table-sortable borderless">
      <thead>
        <tr>
          <th><g:message code='label.givenname' /></th>
          <th><g:message code='label.surname' /></th>
          <th><g:message code='label.email' /></th>
          <th><g:message code='label.organization' /></th>
          <th/>
        </tr>
      </thead>
      <tbody>
      <g:each in="${contactList}" status="i" var="contact">
        <tr>
          <td>${fieldValue(bean: contact, field: "givenName")}</td>
          <td>${fieldValue(bean: contact, field: "surname")}</td>
          <td><a href="mailto:${fieldValue(bean: contact, field: "email")}">${fieldValue(bean: contact, field: "email")}</a></td>
          <td>${fieldValue(bean: contact, field: "organization.displayName")}</td>
          <td><a href="${createLink(action:'show', id:contact.id)}" class="btn"><g:message code="label.view" default="View"/></a></td>
        </tr>
      </g:each>
      </tbody>
    </table>
  </body>
</html>
