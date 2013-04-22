<html>
    <head>
        <meta name="layout" content="public" />
        <title><g:message encodeAs="HTML" code="views.fr.foundation.bootstrap.organization.title" /></title>
    </head>
  
    <body>
      <h2><g:message encodeAs="HTML" code="views.fr.foundation.bootstrap.organization.heading" /></h2>  
      <g:render template="/templates/organization/create" plugin="foundation" model="[saveAction:'saveorganization', requiresContactDetails:true]"/>
    </body>
</html>
