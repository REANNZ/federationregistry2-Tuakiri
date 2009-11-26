
<%@ page import="aaf.fedreg.saml2.metadata.orm.UrlURI" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'urlURI.label')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="container">
            <h2><g:message code="default.show.label" args="[entityName]" /></h2>

			<div class="actions">
				<ul class="horizmenu">
					<li>
						<g:link action="edit" id="${urlURIInstance?.id}" class="icon icon_edit icon_edit_urlURI"><g:message code="urlURI.edit.label"/></g:link>
					</li>
					<n:hasPermission target="urlURI:delete:${urlURIInstance.id}">
					<li>
						<g:form action="delete" name="deleteurlURI">
							<g:hiddenField name="id" value="${urlURIInstance?.id}"/>
							<n:confirmaction action="document.deleteurlURI.submit()" title="${message(code: 'delete.confirm.title')}" msg="${message(code: 'urlURI.delete.confirm.msg')}" accept="${message(code: 'default.button.accept.label')}" cancel="${message(code: 'default.button.cancel.label')}" class="icon icon_delete icon_delete_urlURI"><g:message code="urlURI.delete.label" /></n:confirmaction>
						</g:form>
					</li>
					</n:hasPermission>
				</ul>
			</div>

            <div class="details">
                <table>
                    <tbody>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="urlURI.id.label" default="Id" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: urlURIInstance, field: "id")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="urlURI.description.label" default="Description" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: urlURIInstance, field: "description")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="urlURI.uri.label" default="Uri" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: urlURIInstance, field: "uri")}</td>
					                            
					                        </tr>
					                    
					                        <tr class="prop">
					                            <td valign="top" class="name"><g:message code="urlURI.version.label" default="Version" /></td>
					                            
					                            <td valign="top" class="value">${fieldValue(bean: urlURIInstance, field: "version")}</td>
					                            
					                        </tr>
					                    
					                    </tbody>
                </table>
            </div>

        </div>
		<n:confirm/>
    </body>
</html>
