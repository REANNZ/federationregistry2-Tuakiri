
<%@ page import="fedreg.core.IdentityProvider" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'identityProvider.label')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.edit.label" args="[entityName]" /></h2>

            <g:hasErrors bean="${identityProviderInstance}">
            <div class="errors">
                <g:renderErrors bean="${identityProviderInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form action="update" method="post" >
                <g:hiddenField name="id" value="${identityProviderInstance?.id}" />
                <g:hiddenField name="version" value="${identityProviderInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="organization"><g:message code="identityProvider.organization.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'organization', 'errors')}">
                                    <g:select name="organization.id" from="${fedreg.saml2.metadata.orm.Organization.list()}" optionKey="id" value="${identityProviderInstance?.organization?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="extensions"><g:message code="identityProvider.extensions.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'extensions', 'errors')}">
                                    <g:textField name="extensions" value="${identityProviderInstance?.extensions}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="errorURL"><g:message code="identityProvider.errorURL.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'errorURL', 'errors')}">
                                    <g:select name="errorURL.id" from="${fedreg.saml2.metadata.orm.UrlURI.list()}" optionKey="id" value="${identityProviderInstance?.errorURL?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="protocolSupportEnumerations"><g:message code="identityProvider.protocolSupportEnumerations.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'protocolSupportEnumerations', 'errors')}">
                                    <g:select name="protocolSupportEnumerations" from="${fedreg.saml2.metadata.orm.SamlURI.list()}" multiple="yes" optionKey="id" size="5" value="${identityProviderInstance?.protocolSupportEnumerations}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="contactPersons"><g:message code="identityProvider.contactPersons.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'contactPersons', 'errors')}">
                                    
<ul>
<g:each in="${identityProviderInstance?.contactPersons?}" var="c">
    <li><g:link controller="contactPerson" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="contactPerson" action="create" params="['identityProvider.id': identityProviderInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'contactPerson.label', default: 'ContactPerson')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="keyDescriptors"><g:message code="identityProvider.keyDescriptors.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'keyDescriptors', 'errors')}">
                                    
<ul>
<g:each in="${identityProviderInstance?.keyDescriptors?}" var="k">
    <li><g:link controller="keyDescriptor" action="show" id="${k.id}">${k?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="keyDescriptor" action="create" params="['identityProvider.id': identityProviderInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'keyDescriptor.label', default: 'KeyDescriptor')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="nameIDFormats"><g:message code="identityProvider.nameIDFormats.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'nameIDFormats', 'errors')}">
                                    <g:select name="nameIDFormats" from="${fedreg.saml2.metadata.orm.SamlURI.list()}" multiple="yes" optionKey="id" size="5" value="${identityProviderInstance?.nameIDFormats}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="artifactResolutionServices"><g:message code="identityProvider.artifactResolutionServices.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'artifactResolutionServices', 'errors')}">
                                    
<ul>
<g:each in="${identityProviderInstance?.artifactResolutionServices?}" var="a">
    <li><g:link controller="artifactResolutionService" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="artifactResolutionService" action="create" params="['identityProvider.id': identityProviderInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'artifactResolutionService.label', default: 'ArtifactResolutionService')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="singleLogoutServices"><g:message code="identityProvider.singleLogoutServices.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'singleLogoutServices', 'errors')}">
                                    
<ul>
<g:each in="${identityProviderInstance?.singleLogoutServices?}" var="s">
    <li><g:link controller="singleLogoutService" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="singleLogoutService" action="create" params="['identityProvider.id': identityProviderInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'singleLogoutService.label', default: 'SingleLogoutService')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="manageNameIDServices"><g:message code="identityProvider.manageNameIDServices.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'manageNameIDServices', 'errors')}">
                                    
<ul>
<g:each in="${identityProviderInstance?.manageNameIDServices?}" var="m">
    <li><g:link controller="manageNameIDService" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="manageNameIDService" action="create" params="['identityProvider.id': identityProviderInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'manageNameIDService.label', default: 'ManageNameIDService')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="singleSignOnServices"><g:message code="identityProvider.singleSignOnServices.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'singleSignOnServices', 'errors')}">
                                    
<ul>
<g:each in="${identityProviderInstance?.singleSignOnServices?}" var="s">
    <li><g:link controller="singleSignOnService" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="singleSignOnService" action="create" params="['identityProvider.id': identityProviderInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'singleSignOnService.label', default: 'SingleSignOnService')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="nameIDMappingServices"><g:message code="identityProvider.nameIDMappingServices.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'nameIDMappingServices', 'errors')}">
                                    
<ul>
<g:each in="${identityProviderInstance?.nameIDMappingServices?}" var="n">
    <li><g:link controller="nameIDMappingService" action="show" id="${n.id}">${n?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="nameIDMappingService" action="create" params="['identityProvider.id': identityProviderInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'nameIDMappingService.label', default: 'NameIDMappingService')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="assertionIDRequestServices"><g:message code="identityProvider.assertionIDRequestServices.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'assertionIDRequestServices', 'errors')}">
                                    
<ul>
<g:each in="${identityProviderInstance?.assertionIDRequestServices?}" var="a">
    <li><g:link controller="assertionIDRequestService" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="assertionIDRequestService" action="create" params="['identityProvider.id': identityProviderInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'assertionIDRequestService.label', default: 'AssertionIDRequestService')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="attributeProfiles"><g:message code="identityProvider.attributeProfiles.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'attributeProfiles', 'errors')}">
                                    <g:select name="attributeProfiles" from="${fedreg.saml2.metadata.orm.SamlURI.list()}" multiple="yes" optionKey="id" size="5" value="${identityProviderInstance?.attributeProfiles}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="attributes"><g:message code="identityProvider.attributes.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'attributes', 'errors')}">
                                    <g:select name="attributes" from="${fedreg.saml2.metadata.orm.Attribute.list()}" multiple="yes" optionKey="id" size="5" value="${identityProviderInstance?.attributes}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="wantAuthnRequestsSigned"><g:message code="identityProvider.wantAuthnRequestsSigned.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: identityProviderInstance, field: 'wantAuthnRequestsSigned', 'errors')}">
                                    <g:checkBox name="wantAuthnRequestsSigned" value="${identityProviderInstance?.wantAuthnRequestsSigned}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>

				<div class="buttons">
		            <button class="button icon icon_update icon_update_identityProvider" type="submit"><g:message code="identityProvider.update.label"/></button>
		            <g:link action="show" id="${identityProviderInstance?.id}" class="button icon icon_cancel icon_cancel_identityProvider"><g:message code="default.button.cancel.label"/></g:link>
		        </div>
            </g:form>
        </div>
    </body>
</html>
