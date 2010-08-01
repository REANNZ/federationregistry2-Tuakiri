<html>
    <head>
        <meta name="layout" content="public" />
        <title><g:message code="fedreg.view.members.bootstrap.identityprovider.title" /></title>
    </head>
	
    <body>
		
        <section>
            <h2><g:message code="fedreg.view.members.bootstrap.identityprovider.heading" /></h2>
			
			<g:render template="/templates/idpssodescriptor/create" model="[saveAction:'saveidp', requiresContactDetails:true]"/>

        </section>
    </body>
</html>