
<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message code="views.fr.foundation.organization.create.title" /></title>
  </head>
  <body>
      <h2><g:message code="views.fr.foundation.organization.create.heading" /></h2>
      
      <g:render template="/templates/organization/create" plugin="foundation" model="[saveAction:'save', requiresContactDetails:true]"/>
  </body>
</html>
