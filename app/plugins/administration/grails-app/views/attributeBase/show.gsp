
<%@ page import="aaf.fr.foundation.AttributeBase" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'attributeBase.label', default: 'AttributeBase')}" />
  </head>
  <body>

    <div id="show-attributeBase" class="content scaffold-show" role="main">
      <h3>Viewing AttributeBase</h3>
      <ul class="property-list attributeBase clean">
      
        <g:if test="${attributeBaseInstance?.id}">
        <li class="fieldcontain">
          <strong><span id="id-label" class="property-label"><g:message code="attributeBase.id.label" default="id" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${attributeBaseInstance}" field="id"/></span>
          
        </li>
        </g:if>
      
        <g:if test="${attributeBaseInstance?.name}">
        <li class="fieldcontain">
          <strong><span id="name-label" class="property-label"><g:message code="attributeBase.name.label" default="Name" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${attributeBaseInstance}" field="name"/></span>
          
        </li>
        </g:if>
        <g:if test="${attributeBaseInstance?.oid}">
        <li class="fieldcontain">
          <strong><span id="oid-label" class="property-label"><g:message code="attributeBase.oid.label" default="oid" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="oid-label"><g:fieldValue bean="${attributeBaseInstance}" field="oid"/></span>
          
        </li>
        </g:if>      
        <g:if test="${attributeBaseInstance?.legacyName}">
        <li class="fieldcontain">
          <strong><span id="legacyName-label" class="property-label"><g:message code="attributeBase.legacyName.label" default="Legacy Name" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="legacyName-label"><g:fieldValue bean="${attributeBaseInstance}" field="legacyName"/></span>
          
        </li>
        </g:if>
      

        <g:if test="${attributeBaseInstance?.nameFormat}">
        <li class="fieldcontain">
          <strong><span id="nameFormat-label" class="property-label"><g:message code="attributeBase.nameFormat.label" default="Name Format" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="nameFormat-label"><g:link controller="samlURI" action="show" id="${attributeBaseInstance?.nameFormat?.id}">${attributeBaseInstance?.nameFormat?.uri.encodeAsHTML()}</g:link></span>
          
        </li>
        </g:if>
        <g:if test="${attributeBaseInstance?.description}">
        <li class="fieldcontain">
          <strong><span id="description-label" class="property-label"><g:message code="attributeBase.description.label" default="Description" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${attributeBaseInstance}" field="description"/></span>
          
        </li>
        </g:if>      

        <g:if test="${attributeBaseInstance?.specificationRequired}">
        <li class="fieldcontain">
          <strong><span id="specificationRequired-label" class="property-label"><g:message code="attributeBase.specificationRequired.label" default="Specification Required" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="specificationRequired-label"><g:formatBoolean boolean="${attributeBaseInstance?.specificationRequired}" /></span>
          
        </li>
        </g:if>
        <g:if test="${attributeBaseInstance?.category}">
        <li class="fieldcontain">
          <strong><span id="category-label" class="property-label"><g:message code="attributeBase.category.label" default="Category" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="category-label"><g:link controller="attributeCategory" action="show" id="${attributeBaseInstance?.category?.id}">${attributeBaseInstance?.category?.name.encodeAsHTML()}</g:link></span>
          
        </li>
        </g:if>



        <g:if test="${attributeBaseInstance?.dateCreated}">
        <li class="fieldcontain">
          <strong><span id="dateCreated-label" class="property-label"><g:message code="attributeBase.dateCreated.label" default="Date Created" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${attributeBaseInstance?.dateCreated}" /></span>
          
        </li>
        </g:if>      
        <g:if test="${attributeBaseInstance?.lastUpdated}">
        <li class="fieldcontain">
          <strong><span id="lastUpdated-label" class="property-label"><g:message code="attributeBase.lastUpdated.label" default="Last Updated" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${attributeBaseInstance?.lastUpdated}" /></span>
          
        </li>
        </g:if>
      
      </ul>
      <g:form>
        <fieldset class="buttons">
          <g:hiddenField name="id" value="${attributeBaseInstance?.id}" />
          <g:link class="edit" action="edit" id="${attributeBaseInstance?.id}" class="btn btn-info"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          <g:actionSubmit class="delete btn" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
