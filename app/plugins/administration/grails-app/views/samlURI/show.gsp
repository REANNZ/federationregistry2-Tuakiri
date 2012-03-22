
<%@ page import="aaf.fr.foundation.SamlURI" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'samlURI.label', default: 'SamlURI')}" />
  </head>
  <body>

    <div id="show-samlURI" class="content scaffold-show" role="main">
      <h3>Viewing SamlURI</h3>
      <ul class="property-list samlURI clean">
      
        <g:if test="${samlURIInstance?.id}">
        <li class="fieldcontain">
          <strong><span id="id-label" class="property-label"><g:message code="samlURI.id.label" default="Id" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${samlURIInstance}" field="id"/></span>
          
        </li>
        </g:if>
        <g:if test="${samlURIInstance?.uri}">
        <li class="fieldcontain">
          <strong><span id="uri-label" class="property-label"><g:message code="samlURI.uri.label" default="Uri" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="uri-label"><g:fieldValue bean="${samlURIInstance}" field="uri"/></span>
          
        </li>
        </g:if>
        <g:if test="${samlURIInstance?.description}">
        <li class="fieldcontain">
          <strong><span id="description-label" class="property-label"><g:message code="samlURI.description.label" default="Description" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${samlURIInstance}" field="description"/></span>
          
        </li>
        </g:if>
        <g:if test="${samlURIInstance?.type}">
        <li class="fieldcontain">
          <strong><span id="type-label" class="property-label"><g:message code="samlURI.type.label" default="Type" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${samlURIInstance}" field="type"/></span>
          
        </li>
        </g:if>
      </ul>
      <g:form>
        <fieldset class="buttons">
          <g:hiddenField name="id" value="${samlURIInstance?.id}" />
          <g:link class="edit" action="edit" id="${samlURIInstance?.id}" class="btn btn-info"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          <g:actionSubmit class="delete btn" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
