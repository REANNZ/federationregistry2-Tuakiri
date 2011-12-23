<html>
    <head>
        <meta name="layout" content="public" />
        <title><g:message code="fedreg.view.members.bootstrap.serviceprovider.title" /></title>
    </head>
	
    <body>
		
        <section>
            <h2><g:message code="fedreg.view.members.bootstrap.serviceprovider.heading" /></h2>
			
			<g:render template="/templates/spssodescriptor/create"  plugin="foundation" model="[saveAction:'savesp', requiresContactDetails:true]"/>

        </section>
    </body>
</html>