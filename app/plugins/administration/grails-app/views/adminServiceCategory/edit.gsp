<%@ page import="aaf.fr.foundation.ServiceCategory" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'serviceCategory.label', default: 'ServiceCategory')}" />
  </head>
  <body>
    <div id="edit-serviceCategory" class="content scaffold-edit" role="main">
      <h3>Editing ServiceCategory</h3>

      <g:hasErrors bean="${serviceCategoryInstance}">
      <ul class="clean alert alert-error">
        <g:eachError bean="${serviceCategoryInstance}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message encodeAs="HTML" error="${error}"/></li>
        </g:eachError>
      </ul>
      </g:hasErrors>
      <g:form method="post" >
        <g:hiddenField name="id" value="${serviceCategoryInstance?.id}" />
        <g:hiddenField name="version" value="${serviceCategoryInstance?.version}" />
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset>
          <div class="form-actions">
            <g:actionSubmit class="save" action="update" class="btn btn-success" value="${message(code: 'label.update', default: 'Update')}" />
            <g:link action="show" id="${serviceCategoryInstance.id}" class="btn"><g:message encodeAs="HTML" code="label.cancel"/></g:link>
          </div>
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
