
<%@ page import="aaf.fr.foundation.AttributeCategory" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'attributeCategory.label', default: 'AttributeCategory')}" />
  </head>
  <body>

    <div id="show-attributeCategory" class="content scaffold-show" role="main">
      <h3>Viewing AttributeCategory</h3>
      <ul class="property-list attributeCategory clean">
      
        <g:if test="${attributeCategoryInstance?.id}">
        <li class="fieldcontain">
          <strong><span id="id-label" class="property-label"><g:message code="attributeCategory.id.label" default="id" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${attributeCategoryInstance}" field="id"/></span>
          
        </li>
        </g:if>

        <g:if test="${attributeCategoryInstance?.name}">
        <li class="fieldcontain">
          <strong><span id="name-label" class="property-label"><g:message code="attributeCategory.name.label" default="Name" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${attributeCategoryInstance}" field="name"/></span>
          
        </li>
        </g:if>
      
        <g:if test="${attributeCategoryInstance?.dateCreated}">
        <li class="fieldcontain">
          <strong><span id="dateCreated-label" class="property-label"><g:message code="attributeCategory.dateCreated.label" default="Date Created" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${attributeCategoryInstance?.dateCreated}" /></span>
          
        </li>
        </g:if>
      
        <g:if test="${attributeCategoryInstance?.lastUpdated}">
        <li class="fieldcontain">
          <strong><span id="lastUpdated-label" class="property-label"><g:message code="attributeCategory.lastUpdated.label" default="Last Updated" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${attributeCategoryInstance?.lastUpdated}" /></span>
          
        </li>
        </g:if>
      
      </ul>
      <g:form>
        <fieldset class="buttons">
          <g:hiddenField name="id" value="${attributeCategoryInstance?.id}" />
          <g:link class="edit" action="edit" id="${attributeCategoryInstance?.id}" class="btn btn-info"><g:message code="label.edit" default="Edit" /></g:link>
          <g:actionSubmit class="delete btn" action="delete" value="${message(code: 'label.delete', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
