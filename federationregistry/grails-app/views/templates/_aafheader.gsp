<div id="banner">
	<h1><g:message code="fedreg.title" /></h1>
 	<img src="${resource(dir:'images',file:'aaf_logo.png')}" alt="${message(code:'fedreg.title')}" border="0" />
</div>

<g:if test="${navigation}">
	<n:isLoggedIn>
	  <g:render template='/templates/aaftopnavigation'/>
	</n:isLoggedIn>
</g:if>