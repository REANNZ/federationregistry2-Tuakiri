<?php

// WAYF IDP Configuration File

// Generation: ${new Date()} by Federation Registry <g:meta name="app.version"/>
<g:each var="ot" in="${organizationTypes}">
$IDProviders['${ot.name}'] = array (
	'Name' => '${ot.description}',
	'Type' => 'category',
);
<g:each var="idp" in="${identityProviders.sort { it.displayName }}"><g:if test="${idp.organization.primary.name == ot.name}">
$IDProviders['${idp.entityDescriptor.entityID}']['SSO'] = '${ssoPostEndpoints.get(idp.id)}';
$IDProviders['${idp.entityDescriptor.entityID}']['Name'] = '${idp.displayName}';
$IDProviders['${idp.entityDescriptor.entityID}']['Type'] = '${idp.organization.primary.name}';
</g:if></g:each>
</g:each>

?>