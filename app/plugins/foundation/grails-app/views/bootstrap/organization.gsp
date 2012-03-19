<html>
    <head>
        <meta name="layout" content="public" />
        <title><g:message code="fedreg.view.members.bootstrap.organization.title" /></title>
    </head>
  
    <body>
      <h2><g:message code="fedreg.view.members.bootstrap.organization.heading" /></h2>  
      <g:render template="/templates/organization/create" plugin="foundation" model="[saveAction:'saveorganization', requiresContactDetails:true]"/>
    </body>
</html>