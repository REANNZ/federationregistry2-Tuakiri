<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message code="fedreg.view.members.identityprovider.list.title" /></title>
    <r:use modules="alphanumeric, validate"/>
  </head>
  <body>
    <h2><g:message code="fedreg.view.members.identityprovider.create.heading" /></h2>
    <g:render template="/templates/identityprovider/create" plugin="foundation" model="[saveAction:'save']"/>
  </body>
</html>