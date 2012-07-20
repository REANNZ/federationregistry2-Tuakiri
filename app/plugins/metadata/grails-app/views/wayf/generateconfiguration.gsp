<?php

// WAYF IDP Configuration File

// Generation: ${new Date()} by Federation Registry <g:meta name="app.version"/>
<g:each var="ot" in="${organizationTypes.sort{it.description}}">
$IDProviders['${ot.name}'] = array (
	'Name' => '${ot.description}',
	'Type' => 'category',
);
<g:each var="idp" in="${ot.idpList.sort { it.displayName }}">
$IDProviders['${idp.entityID}']['SSO'] = '${idp.location}';
$IDProviders['${idp.entityID}']['Name'] = '${idp.displayName}';
$IDProviders['${idp.entityID}']['Type'] = '${ot.name}';
</g:each>
</g:each>

?>