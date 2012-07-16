
<%@ page import="aaf.fr.foundation.MonitorType" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'monitorType.label', default: 'MonitorType')}" />
  </head>
  <body>

    <div id="show-monitorType" class="content scaffold-show" role="main">
      <h3>Viewing MonitorType</h3>
      <ul class="property-list monitorType clean">
      
        <g:if test="${monitorTypeInstance?.id}">
        <li class="fieldcontain">
          <strong><span id="id-label" class="property-label"><g:message code="monitorType.id.label" default="Id" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${monitorTypeInstance}" field="id"/></span>
          
        </li>
        </g:if>
      
        <g:if test="${monitorTypeInstance?.name}">
        <li class="fieldcontain">
          <strong><span id="name-label" class="property-label"><g:message code="monitorType.name.label" default="Name" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${monitorTypeInstance}" field="name"/></span>
          
        </li>
        </g:if>

        <g:if test="${monitorTypeInstance?.description}">
        <li class="fieldcontain">
          <strong><span id="description-label" class="property-label"><g:message code="monitorType.description.label" default="Description" /></span></strong>: 
          
            <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${monitorTypeInstance}" field="description"/></span>
          
        </li>
        </g:if>
      
      </ul>
      <g:form>
        <fieldset class="buttons">
          <g:hiddenField name="id" value="${monitorTypeInstance?.id}" />
          <g:link class="edit" action="edit" id="${monitorTypeInstance?.id}" class="btn btn-info"><g:message code="label.edit" default="Edit" /></g:link>
          <g:actionSubmit class="delete btn" action="delete" value="${message(code: 'label.delete', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
