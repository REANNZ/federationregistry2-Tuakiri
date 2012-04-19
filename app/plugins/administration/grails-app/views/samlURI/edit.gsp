<%@ page import="aaf.fr.foundation.SamlURI" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'samlURI.label', default: 'SamlURI')}" />
  </head>
  <body>
    <div id="edit-samlURI" class="content scaffold-edit" role="main">
      <h3>Editing SamlURI</h3>

      <g:hasErrors bean="${samlURIInstance}">
      <ul class="clean alert alert-error">
        <g:eachError bean="${samlURIInstance}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:eachError>
      </ul>
      </g:hasErrors>
      <g:form method="post" >
        <g:hiddenField name="id" value="${samlURIInstance?.id}" />
        <g:hiddenField name="version" value="${samlURIInstance?.version}" />
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset>
          <div class="form-actions">
            <g:actionSubmit class="save" action="update" class="btn btn-success" value="${message(code: 'label.update', default: 'Update')}" />
            <g:link action="show" id="${samlURIInstance.id}" class="btn"><g:message code="label.cancel"/></g:link>
          </div>
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
