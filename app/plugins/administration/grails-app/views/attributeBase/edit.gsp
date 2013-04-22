<%@ page import="aaf.fr.foundation.AttributeBase" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'attributeBase.label', default: 'AttributeBase')}" />
  </head>
  <body>
    <div id="edit-attributeBase" class="content scaffold-edit" role="main">
      <h3>Editing AttributeBase</h3>

      <g:hasErrors bean="${attributeBaseInstance}">
      <ul class="clean alert alert-error">
        <g:eachError bean="${attributeBaseInstance}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message encodeAs="HTML" error="${error}"/></li>
        </g:eachError>
      </ul>
      </g:hasErrors>
      <g:form method="post" >
        <g:hiddenField name="id" value="${attributeBaseInstance?.id}" />
        <g:hiddenField name="version" value="${attributeBaseInstance?.version}" />
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset>
          <div class="form-actions">
            <g:actionSubmit class="save" action="update" class="btn btn-success" value="${message(code: 'label.update', default: 'Update')}" />
            <g:link action="show" id="${attributeBaseInstance.id}" class="btn"><g:message encodeAs="HTML" code="label.cancel"/></g:link>
          </div>
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
