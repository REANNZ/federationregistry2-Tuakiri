<%@ page import="aaf.fr.foundation.CAKeyInfo" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="${message(code: 'CAKeyInfo.label', default: 'CAKeyInfo')}" />
  </head>
  <body>
    <div id="edit-CAKeyInfo" class="content scaffold-edit" role="main">
      <h3>Editing CAKeyInfo</h3>

      <div class="alert alert-warning">
        <p>
          <strong>WARNING: CA certificates are NOT verified on submission.</strong>
        </p>
        <p>
          Ensure you provide valid data only including the -----BEGIN CERTIFICATE----- and -----END CERTIFICATE----- bannners.
        </p>
        <p><strong>ERRONOUS DATA HERE MAY CAUSE METADATA TO BECOME INVALID</strong></p>
      </div>

      <g:hasErrors bean="${CAKeyInfoInstance}">
      <ul class="clean alert alert-error">
        <g:eachError bean="${CAKeyInfoInstance}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:eachError>
      </ul>
      </g:hasErrors>
      <g:form method="post" >
        <g:hiddenField name="id" value="${CAKeyInfoInstance?.id}" />
        <g:hiddenField name="version" value="${CAKeyInfoInstance?.version}" />
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset>
          <div class="form-actions">
            <g:actionSubmit class="save" action="update" class="btn btn-success" value="${message(code: 'default.button.update.label', default: 'Update')}" />
            <g:link action="show" id="${CAKeyInfoInstance.id}" class="btn"><g:message code="label.cancel"/></g:link>
          </div>
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
