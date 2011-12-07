<html>
    <head>
        <meta name="layout" content="public" />
        <title><g:message code="fedreg.view.members.bootstrap.organization.title" /></title>
    </head>
	
    <body>
		
        <section>
            <h2><g:message code="fedreg.view.members.bootstrap.organization.heading" /></h2>	
			<g:render template="/templates/organization/create" plugin="federationcore" model="[saveAction:'saveorganization', requiresContactDetails:true]"/>

        </section>
    </body>
</html>