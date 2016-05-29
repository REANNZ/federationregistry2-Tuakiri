<%@ page import="aaf.fr.foundation.AttributeCategory" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'attributeCategory.label', default: 'AttributeCategory')}" />
  </head>
  <body>
    <div id="create-attributeCategory" class="content scaffold-create" role="main">
      <h3><g:message encodeAs="HTML" code="label.createspecific" args="[entityName]" /></h3>
      <g:if test="${flash.message}">
      <div class="message" role="status">${flash.message.encodeAsHTML()}</div>
      </g:if>
      <g:hasErrors bean="${attributeCategoryInstance}">
        <ul class="clean alert alert-error">
          <g:eachError bean="${attributeCategoryInstance}" var="error">
          <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message encodeAs="HTML" error="${error}"/></li>
          </g:eachError>
        </ul>
      </g:hasErrors>
      <g:form action="save" >
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset>
          <div class="form-actions">
            <g:submitButton name="create" class="save" value="${message(code: 'label.create', default: 'Create')}" class="btn btn-success"/>
            <g:link action="list" class="btn"><g:message encodeAs="HTML" code="label.cancel"/></g:link>
          </div>
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
