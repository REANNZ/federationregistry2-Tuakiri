<%@ page import="aaf.fr.foundation.AttributeCategory" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'attributeCategory.label', default: 'AttributeCategory')}" />
  </head>
  <body>
    <div id="edit-attributeCategory" class="content scaffold-edit" role="main">
      <h3>Editing AttributeCategory</h3>

      <g:hasErrors bean="${attributeCategoryInstance}">
      <ul class="clean alert alert-error">
        <g:eachError bean="${attributeCategoryInstance}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:eachError>
      </ul>
      </g:hasErrors>
      <g:form method="post" >
        <g:hiddenField name="id" value="${attributeCategoryInstance?.id}" />
        <g:hiddenField name="version" value="${attributeCategoryInstance?.version}" />
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset>
          <div class="form-actions">
            <g:actionSubmit class="save" action="update" class="btn btn-success" value="${message(code: 'default.button.update.label', default: 'Update')}" />
            <g:link action="show" id="${attributeCategoryInstance.id}" class="btn"><g:message code="label.cancel"/></g:link>
          </div>
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
