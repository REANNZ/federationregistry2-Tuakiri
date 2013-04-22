<%@ page import="aaf.fr.foundation.ContactType" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'contactType.label', default: 'ContactType')}" />
  </head>
  <body>
    <div id="edit-contactType" class="content scaffold-edit" role="main">
      <h3>Editing ContactType</h3>

      <g:hasErrors bean="${contactTypeInstance}">
      <ul class="clean alert alert-error">
        <g:eachError bean="${contactTypeInstance}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message encodeAs="HTML" error="${error}"/></li>
        </g:eachError>
      </ul>
      </g:hasErrors>
      <g:form method="post" >
        <g:hiddenField name="id" value="${contactTypeInstance?.id}" />
        <g:hiddenField name="version" value="${contactTypeInstance?.version}" />
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset>
          <div class="form-actions">
            <g:actionSubmit class="save" action="update" class="btn btn-success" value="${message(code: 'label.update', default: 'Update')}" />
            <g:link action="show" id="${contactTypeInstance.id}" class="btn"><g:message encodeAs="HTML" code="label.cancel"/></g:link>
          </div>
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
