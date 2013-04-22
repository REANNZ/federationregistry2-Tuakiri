<r:img dir='images' file='logo.jpg' plugin="federationregistry" alt="${message(code:'fr.branding.title', default:'Federation Registry')}" width="102" height="50" />
<h1><g:message encodeAs="HTML"  code='fr.branding.title' default='Federation Registry' /></h1> 
<g:if test="${grailsApplication.config.federation.environment == 'test'}">
  <h2>Test Federation</h2>
</g:if>
