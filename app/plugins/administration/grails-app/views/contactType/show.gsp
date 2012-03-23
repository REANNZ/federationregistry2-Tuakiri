
<%@ page import="aaf.fr.foundation.ContactType" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'contactType.label', default: 'ContactType')}" />
  </head>
  <body>

    <div id="show-contactType" class="content scaffold-show" role="main">
      <h3>Viewing ContactType</h3>
      <ul class="property-list contactType clean">
      
        <g:if test="${contactTypeInstance?.id}">
        <li class="fieldcontain">
          <strong><span id="id-label" class="property-label"><g:message code="contactType.id.label" default="id" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${contactTypeInstance}" field="id"/></span>
          
        </li>
        </g:if>
        <g:if test="${contactTypeInstance?.name}">
        <li class="fieldcontain">
          <strong><span id="name-label" class="property-label"><g:message code="contactType.name.label" default="Name" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${contactTypeInstance}" field="name"/></span>
          
        </li>
        </g:if>      
        <g:if test="${contactTypeInstance?.displayName}">
        <li class="fieldcontain">
          <strong><span id="displayName-label" class="property-label"><g:message code="contactType.displayName.label" default="Display Name" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="displayName-label"><g:fieldValue bean="${contactTypeInstance}" field="displayName"/></span>
          
        </li>
        </g:if>
      
        <g:if test="${contactTypeInstance?.description}">
        <li class="fieldcontain">
          <strong><span id="description-label" class="property-label"><g:message code="contactType.description.label" default="Description" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${contactTypeInstance}" field="description"/></span>
          
        </li>
        </g:if>
      


        <g:if test="${contactTypeInstance?.dateCreated}">
        <li class="fieldcontain">
          <strong><span id="dateCreated-label" class="property-label"><g:message code="contactType.dateCreated.label" default="Date Created" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${contactTypeInstance?.dateCreated}" /></span>
          
        </li>
        </g:if>      
        <g:if test="${contactTypeInstance?.lastUpdated}">
        <li class="fieldcontain">
          <strong><span id="lastUpdated-label" class="property-label"><g:message code="contactType.lastUpdated.label" default="Last Updated" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${contactTypeInstance?.lastUpdated}" /></span>
          
        </li>
        </g:if>
      
      </ul>
      <g:form>
        <fieldset class="buttons">
          <g:hiddenField name="id" value="${contactTypeInstance?.id}" />
          <g:link class="edit" action="edit" id="${contactTypeInstance?.id}" class="btn btn-info"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          <g:actionSubmit class="delete btn" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
