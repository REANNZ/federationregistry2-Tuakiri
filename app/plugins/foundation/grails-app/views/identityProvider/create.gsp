
<html>
    <head>
        
        <meta name="layout" content="members" />
        <title><g:message code="fedreg.view.members.identityprovider.list.title" /></title>
    </head>
	
    <body>
		
        <section>
            <h2><g:message code="fedreg.view.members.identityprovider.create.heading" /></h2>
			
			<g:render template="/templates/idpssodescriptor/create" plugin="federationcore" model="[saveAction:'save', requiresContactDetails:false]"/>
        </section>
    </body>
</html>
