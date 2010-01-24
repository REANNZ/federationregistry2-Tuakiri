
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'identityProvider.label')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>

			<div class="actions">
				<ul class="horizmenu">
					<li>
						<g:link action="edit" id="${identityProviderInstance?.id}" class="icon icon_edit icon_edit_identityProvider"><g:message code="identityProvider.edit.label"/></g:link>
					</li>
					<n:hasPermission target="identityProvider:delete:${identityProviderInstance.id}">
					<li>
						<g:form action="delete" name="deleteidentityProvider">
							<g:hiddenField name="id" value="${identityProviderInstance?.id}"/>
							<n:confirmaction action="document.deleteidentityProvider.submit()" title="${message(code: 'delete.confirm.title')}" msg="${message(code: 'identityProvider.delete.confirm.msg')}" accept="${message(code: 'default.button.accept.label')}" cancel="${message(code: 'default.button.cancel.label')}" class="icon icon_delete icon_delete_identityProvider"><g:message code="identityProvider.delete.label" /></n:confirmaction>
						</g:form>
					</li>
					</n:hasPermission>
				</ul>
			</div>

            <div class="details">
                <table>
                    <tbody>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.id.label" default="Id" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: identityProviderInstance, field: "id")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.organization.label" default="Organization" /></td>
					                            
					                            <td valign="top" class="value"><g:link controller="organization" action="show" id="${identityProviderInstance?.organization?.id}">${identityProviderInstance?.organization?.encodeAsHTML()}</g:link></td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.extensions.label" default="Extensions" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: identityProviderInstance, field: "extensions")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.errorURL.label" default="Error URL" /></td>
					                            
					                            <td valign="top" class="value"><g:link controller="urlURI" action="show" id="${identityProviderInstance?.errorURL?.id}">${identityProviderInstance?.errorURL?.encodeAsHTML()}</g:link></td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.protocolSupportEnumerations.label" default="Protocol Support Enumerations" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${identityProviderInstance.protocolSupportEnumerations}" var="p">
					                                    <li><g:link controller="samlURI" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.contactPersons.label" default="Contact Persons" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${identityProviderInstance.contactPersons}" var="c">
					                                    <li><g:link controller="contactPerson" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.keyDescriptors.label" default="Key Descriptors" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${identityProviderInstance.keyDescriptors}" var="k">
					                                    <li><g:link controller="keyDescriptor" action="show" id="${k.id}">${k?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.nameIDFormats.label" default="Name IDF ormats" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${identityProviderInstance.nameIDFormats}" var="n">
					                                    <li><g:link controller="samlURI" action="show" id="${n.id}">${n?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.artifactResolutionServices.label" default="Artifact Resolution Services" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${identityProviderInstance.artifactResolutionServices}" var="a">
					                                    <li><g:link controller="artifactResolutionService" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.singleLogoutServices.label" default="Single Logout Services" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${identityProviderInstance.singleLogoutServices}" var="s">
					                                    <li><g:link controller="singleLogoutService" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.manageNameIDServices.label" default="Manage Name IDS ervices" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${identityProviderInstance.manageNameIDServices}" var="m">
					                                    <li><g:link controller="manageNameIDService" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.singleSignOnServices.label" default="Single Sign On Services" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${identityProviderInstance.singleSignOnServices}" var="s">
					                                    <li><g:link controller="singleSignOnService" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.nameIDMappingServices.label" default="Name IDM apping Services" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${identityProviderInstance.nameIDMappingServices}" var="n">
					                                    <li><g:link controller="nameIDMappingService" action="show" id="${n.id}">${n?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.assertionIDRequestServices.label" default="Assertion IDR equest Services" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${identityProviderInstance.assertionIDRequestServices}" var="a">
					                                    <li><g:link controller="assertionIDRequestService" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.attributeProfiles.label" default="Attribute Profiles" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${identityProviderInstance.attributeProfiles}" var="a">
					                                    <li><g:link controller="samlURI" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.attributes.label" default="Attributes" /></td>
					                            
					                            <td valign="top" style="text-align: left;" class="value">
					                                <ul>
					                                <g:each in="${identityProviderInstance.attributes}" var="a">
					                                    <li><g:link controller="attribute" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
					                                </g:each>
					                                </ul>
					                            </td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="identityProvider.wantAuthnRequestsSigned.label" default="Want Authn Requests Signed" /></td>
					                            
					                            <td valign="top" class="value"><g:formatBoolean boolean="${identityProviderInstance?.wantAuthnRequestsSigned}" /></td>
					                            
					                        </tr>
					                    
					                    </tbody>
                </table>
            </div>

        </div>
		<n:confirm/>
    </body>
</html>
