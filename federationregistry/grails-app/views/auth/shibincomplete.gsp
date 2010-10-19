<html>
    <head>
        
        <meta name="layout" content="public" />
        <title><g:message code="fedreg.view.login.shibincomplete.title"/></title>
    </head>
    <body>
	
		<h2><g:message code="fedreg.view.login.shibincomplete.heading"/></h2>
		<p>
			<g:message code="fedreg.view.login.shibincomplete.descriptive"/>
		</p>
		
		<div class="error">
			<ul>
				<g:each in="${errors}">
					<li><g:message code="${it}" /></li>
				</g:each>
			</ul>
		</div>
		
		<h3><g:message code="fedreg.view.login.shibincomplete.reqdetails.heading"/></h3>
		<g:include controller="auth" action="echo" />

    </body>
</html>