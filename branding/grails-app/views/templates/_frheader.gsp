<r:img dir='images' file='logo.jpg' plugin="federationregistry" alt="${message(code:'fr.branding.title', default:'Federation Registry')}" width="102" height="50" />
<h1><g:message code='fr.branding.title' default='Federation Registry' /> <span style="font-size: 11px;">(Version: <strong><g:meta name="app.version"/></strong> - Problems? <a href="http://support.aaf.edu.au" style="color:#fff; text-decoration:underline;">Let us know</a>)</span></h1> 
<g:if test="${grailsApplication.config.federation.environment == 'test'}">
  <h2>AAF Test Federation</h2>
</g:if>
