
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="members" />
        <title><g:message code="fedreg.view.members.identityprovider.list.title" /></title>
		
		<script type="text/javascript">
			var certificateValidationEndpoint = "${createLink(controller:'descriptorKeyDescriptor', action:'validateCertificate')}";
		</script>
		
    </head>
	
    <body>
		
        <section>
            <h2><g:message code="fedreg.view.members.identityprovider.create.heading" /></h2>
			
			<g:render template="/templates/idpssodescriptor/create" />

        </section>
    </body>
</html>
