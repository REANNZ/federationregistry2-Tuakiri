<%@ page import="aaf.fr.foundation.MonitorType" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'monitorType.label', default: 'MonitorType')}" />
  </head>
  <body>
    <div id="edit-monitorType" class="content scaffold-edit" role="main">
      <h3>Editing MonitorType</h3>

      <g:hasErrors bean="${monitorTypeInstance}">
      <ul class="clean alert alert-error">
        <g:eachError bean="${monitorTypeInstance}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message encodeAs="HTML" error="${error}"/></li>
        </g:eachError>
      </ul>
      </g:hasErrors>
      <g:form method="post" >
        <g:hiddenField name="id" value="${monitorTypeInstance?.id}" />
        <g:hiddenField name="version" value="${monitorTypeInstance?.version}" />
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset>
          <div class="form-actions">
            <g:actionSubmit class="save" action="update" class="btn btn-success" value="${message(code: 'label.update', default: 'Update')}" />
            <g:link action="show" id="${monitorTypeInstance.id}" class="btn"><g:message encodeAs="HTML" code="label.cancel"/></g:link>
          </div>
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
