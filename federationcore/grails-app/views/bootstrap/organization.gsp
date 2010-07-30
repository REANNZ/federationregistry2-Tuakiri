<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="members" />
        <title><g:message code="fedreg.view.members.bootstrap.organization.title" /></title>
    </head>
	
    <body>
		
        <section>
            <h2><g:message code="fedreg.view.members.bootstrap.organization.heading" /></h2>
			
			<g:render template="/templates/organization/create" model="[saveAction:'saveorganization', requiresContactDetails:true]"/>

        </section>
    </body>
</html>