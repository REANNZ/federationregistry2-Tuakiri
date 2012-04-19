<%=packageName%>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
  </head>
  <body>
    <div id="create-${domainClass.propertyName}" class="content scaffold-create" role="main">
      <h3><g:message code="default.create.label" args="[entityName]" /></h3>
      <g:if test="\${flash.message}">
      <div class="message" role="status">\${flash.message}</div>
      </g:if>
      <g:hasErrors bean="\${${propertyName}}">
        <ul class="clean alert alert-error">
          <g:eachError bean="\${${propertyName}}" var="error">
          <li <g:if test="\${error in org.springframework.validation.FieldError}">data-field-id="\${error.field}"</g:if>><g:message error="\${error}"/></li>
          </g:eachError>
        </ul>
      </g:hasErrors>
      <g:form action="save" <%= multiPart ? ' enctype="multipart/form-data"' : '' %>>
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset>
          <div class="form-actions">
            <g:submitButton name="create" class="save" value="\${message(code: 'label.create', default: 'Create')}" class="btn btn-success"/>
            <g:link action="list" class="btn"><g:message code="label.cancel"/></g:link>
          </div>
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
