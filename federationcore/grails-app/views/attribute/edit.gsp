
<%@ page import="fedreg.core.Attribute" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'attribute.label')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.edit.label" args="[entityName]" /></h2>

            <g:hasErrors bean="${attributeInstance}">
            <div class="errors">
                <g:renderErrors bean="${attributeInstance}" as="list" />
            </div>
            </g:hasErrors>

            <g:form action="update" method="post" >
                <g:hiddenField name="id" value="${attributeInstance?.id}" />
                <g:hiddenField name="version" value="${attributeInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="attribute.name.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: attributeInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${attributeInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="nameFormat"><g:message code="attribute.nameFormat.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: attributeInstance, field: 'nameFormat', 'errors')}">
                                    <g:select name="nameFormat.id" from="${fedreg.saml2.metadata.orm.SamlURI.list()}" optionKey="id" value="${attributeInstance?.nameFormat?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="friendlyName"><g:message code="attribute.friendlyName.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: attributeInstance, field: 'friendlyName', 'errors')}">
                                    <g:textField name="friendlyName" value="${attributeInstance?.friendlyName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="oid"><g:message code="attribute.oid.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: attributeInstance, field: 'oid', 'errors')}">
                                    <g:textField name="oid" value="${attributeInstance?.oid}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="headerName"><g:message code="attribute.headerName.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: attributeInstance, field: 'headerName', 'errors')}">
                                    <g:textField name="headerName" value="${attributeInstance?.headerName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="alias"><g:message code="attribute.alias.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: attributeInstance, field: 'alias', 'errors')}">
                                    <g:textField name="alias" value="${attributeInstance?.alias}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="description"><g:message code="attribute.description.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: attributeInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${attributeInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="scope"><g:message code="attribute.scope.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: attributeInstance, field: 'scope', 'errors')}">
                                    <g:select name="scope.id" from="${fedreg.core.AttributeScope.list()}" optionKey="id" value="${attributeInstance?.scope?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="category"><g:message code="attribute.category.label"/></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: attributeInstance, field: 'category', 'errors')}">
                                    <g:select name="category.id" from="${fedreg.core.AttributeCategory.list()}" optionKey="id" value="${attributeInstance?.category?.id}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>

				<div class="buttons">
		            <button class="button icon icon_update icon_update_attribute" type="submit"><g:message code="attribute.update.label"/></button>
		            <g:link action="show" id="${attributeInstance?.id}" class="button icon icon_cancel icon_cancel_attribute"><g:message code="default.button.cancel.label"/></g:link>
		        </div>
            </g:form>
        </div>
    </body>
</html>
