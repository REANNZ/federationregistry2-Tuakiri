<div id="banner">
	<h1><g:message code="fedreg.title" /></h1>
 	<img src="${resource(dir:'images',file:'aaf_logo.png')}" alt="${message(code:'fedreg.title')}" border="0" />
	
	<g:if test="${navigation}">
		<n:isLoggedIn>
			<div id="userops">
				<g:message code="fedreg.label.usergreeting" /> <n:principalName /> | <g:link controller="auth" action="logout" class=""><g:message code="fedreg.label.logout" /></g:link>
			</div>
		</n:isLoggedIn>
	</g:if>
</div>

<g:if test="${navigation}">
	<n:isLoggedIn>
	  <g:render template='/templates/nimble/navigation/topnavigation'/>
	</n:isLoggedIn>
</g:if>