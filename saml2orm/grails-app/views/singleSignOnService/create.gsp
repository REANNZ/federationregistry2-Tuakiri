
<%@ page import="fedreg.saml2.metadata.orm.SingleSignOnService" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'singleSignOnService.label')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.create.label" args="[entityName]" /></h2>

            <g:hasErrors bean="${singleSignOnServiceInstance}">
            <div class="errors">
                <g:renderErrors bean="${singleSignOnServiceInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="binding"><g:message code="singleSignOnService.binding.label" default="Binding" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: singleSignOnServiceInstance, field: 'binding', 'errors')}">
                                    <g:select name="binding.id" from="${fedreg.saml2.metadata.orm.SamlURI.list()}" optionKey="id" value="${singleSignOnServiceInstance?.binding?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="location"><g:message code="singleSignOnService.location.label" default="Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: singleSignOnServiceInstance, field: 'location', 'errors')}">
                                    <g:select name="location.id" from="${fedreg.saml2.metadata.orm.UrlURI.list()}" optionKey="id" value="${singleSignOnServiceInstance?.location?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="responseLocation"><g:message code="singleSignOnService.responseLocation.label" default="Response Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: singleSignOnServiceInstance, field: 'responseLocation', 'errors')}">
                                    <g:select name="responseLocation.id" from="${fedreg.saml2.metadata.orm.UrlURI.list()}" optionKey="id" value="${singleSignOnServiceInstance?.responseLocation?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="owner"><g:message code="singleSignOnService.owner.label" default="Owner" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: singleSignOnServiceInstance, field: 'owner', 'errors')}">
                                    <g:select name="owner.id" from="${fedreg.saml2.metadata.orm.IDPSSODescriptor.list()}" optionKey="id" value="${singleSignOnServiceInstance?.owner?.id}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
					<button class="button icon icon_create icon_create_singleSignOnService" type="submit"><g:message code="singleSignOnService.create.label"/></button>
				    <g:link action="list" class="button icon icon_cancel icon_cancel_singleSignOnService"><g:message code="default.button.cancel.label"/></g:link>
                </div>
            </g:form>
        </div>
    </body>
</html>
