
<%@ page import="aaf.fr.foundation.OrganizationType" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'organizationType.label', default: 'OrganizationType')}" />
  </head>
  <body>

    <div id="show-organizationType" class="content scaffold-show" role="main">
      <h3>Viewing OrganizationType</h3>
      <ul class="property-list organizationType clean">
      
        <g:if test="${organizationTypeInstance?.id}">
        <li class="fieldcontain">
          <strong><span id="id-label" class="property-label"><g:message encodeAs="HTML" code="organizationType.id.label" default="Id" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${organizationTypeInstance}" field="id"/></span>
          
        </li>
        </g:if>

        <g:if test="${organizationTypeInstance?.name}">
        <li class="fieldcontain">
          <strong><span id="name-label" class="property-label"><g:message encodeAs="HTML" code="organizationType.name.label" default="Name" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${organizationTypeInstance}" field="name"/></span>
          
        </li>
        </g:if>

        <g:if test="${organizationTypeInstance?.displayName}">
        <li class="fieldcontain">
          <strong><span id="displayName-label" class="property-label"><g:message encodeAs="HTML" code="organizationType.displayName.label" default="Display Name" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="displayName-label"><g:fieldValue bean="${organizationTypeInstance}" field="displayName"/></span>
          
        </li>
        </g:if>
      
        <g:if test="${organizationTypeInstance?.description}">
        <li class="fieldcontain">
          <strong><span id="description-label" class="property-label"><g:message encodeAs="HTML" code="organizationType.description.label" default="Description" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${organizationTypeInstance}" field="description"/></span>
          
        </li>
        </g:if>
      
        <li class="fieldcontain">
          <strong><span id="discoveryServiceCategory-label" class="property-label"><g:message encodeAs="HTML" code="organizationType.discoveryServiceCategory.label" default="Discovery Service Category" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="discoveryServiceCategory-label"><g:formatBoolean boolean="${organizationTypeInstance?.discoveryServiceCategory}" /></span>
          
        </li>

        <g:if test="${organizationTypeInstance?.dateCreated}">
        <li class="fieldcontain">
          <strong><span id="dateCreated-label" class="property-label"><g:message encodeAs="HTML" code="organizationType.dateCreated.label" default="Date Created" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${organizationTypeInstance?.dateCreated}" /></span>
          
        </li>
        </g:if>
      
        <g:if test="${organizationTypeInstance?.lastUpdated}">
        <li class="fieldcontain">
          <strong><span id="lastUpdated-label" class="property-label"><g:message encodeAs="HTML" code="organizationType.lastUpdated.label" default="Last Updated" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${organizationTypeInstance?.lastUpdated}" /></span>
          
        </li>
        </g:if>
      

      
      </ul>
      <g:form>
        <fieldset class="buttons">
          <g:hiddenField name="id" value="${organizationTypeInstance?.id}" />
          <g:link class="edit" action="edit" id="${organizationTypeInstance?.id}" class="btn btn-info"><g:message encodeAs="HTML" code="label.edit" default="Edit" /></g:link>
          <g:actionSubmit class="delete btn" action="delete" value="${message(code: 'label.delete', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
