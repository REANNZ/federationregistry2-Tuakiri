
<%@ page import="aaf.fr.foundation.CAKeyInfo" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'CAKeyInfo.label', default: 'CAKeyInfo')}" />
  </head>
  <body>

    <div id="show-CAKeyInfo" class="content scaffold-show" role="main">
      <h3>Viewing CAKeyInfo</h3>
      <ul class="property-list CAKeyInfo clean">
      
        <g:if test="${CAKeyInfoInstance?.id}">
        <li class="fieldcontain">
          <strong><span id="id-label" class="property-label"><g:message code="CAKeyInfo.id.label" default="id" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${CAKeyInfoInstance}" field="id"/></span>
          
        </li>
        </g:if>
      
        <li class="fieldcontain">
          <strong><span id="keyName-label" class="property-label"><g:message code="CAKeyInfo.keyName.label" default="Key Name" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="keyName-label"><g:fieldValue bean="${CAKeyInfoInstance}" field="keyName"/></span>
          
        </li>

        <g:if test="${CAKeyInfoInstance?.certificate}">
        <li class="fieldcontain">
          <strong><span id="certificate-label" class="property-label"><g:message code="CAKeyInfo.certificate.label" default="Certificate" /></span></strong>: 
            <pre>${CAKeyInfoInstance?.certificate?.data.encodeAsHTML()}</pre>          
        </li>
        </g:if>
      
        <g:if test="${CAKeyInfoInstance?.dateCreated}">
        <li class="fieldcontain">
          <strong><span id="dateCreated-label" class="property-label"><g:message code="CAKeyInfo.dateCreated.label" default="Date Created" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${CAKeyInfoInstance?.dateCreated}" /></span>
          
        </li>
        </g:if>
      
        <g:if test="${CAKeyInfoInstance?.expiryDate}">
        <li class="fieldcontain">
          <strong><span id="expiryDate-label" class="property-label"><g:message code="CAKeyInfo.expiryDate.label" default="Expiry Date" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="expiryDate-label"><g:formatDate date="${CAKeyInfoInstance?.expiryDate}" /></span>
          
        </li>
        </g:if>
      
        <g:if test="${CAKeyInfoInstance?.lastUpdated}">
        <li class="fieldcontain">
          <strong><span id="lastUpdated-label" class="property-label"><g:message code="CAKeyInfo.lastUpdated.label" default="Last Updated" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${CAKeyInfoInstance?.lastUpdated}" /></span>
          
        </li>
        </g:if>
      
      </ul>
      <g:form>
        <fieldset class="buttons">
          <g:hiddenField name="id" value="${CAKeyInfoInstance?.id}" />
          <g:link class="edit" action="edit" id="${CAKeyInfoInstance?.id}" class="btn btn-info"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          <g:actionSubmit class="delete btn" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
