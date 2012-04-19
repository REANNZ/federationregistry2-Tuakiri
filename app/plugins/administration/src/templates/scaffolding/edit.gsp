<%=packageName%>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="admin">
    <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
  </head>
  <body>
    <div id="edit-${domainClass.propertyName}" class="content scaffold-edit" role="main">
      <h3>Editing ${domainClass.name}</h3>

      <g:hasErrors bean="\${${propertyName}}">
      <ul class="clean alert alert-error">
        <g:eachError bean="\${${propertyName}}" var="error">
        <li <g:if test="\${error in org.springframework.validation.FieldError}">data-field-id="\${error.field}"</g:if>><g:message error="\${error}"/></li>
        </g:eachError>
      </ul>
      </g:hasErrors>
      <g:form method="post" <%= multiPart ? ' enctype="multipart/form-data"' : '' %>>
        <g:hiddenField name="id" value="\${${propertyName}?.id}" />
        <g:hiddenField name="version" value="\${${propertyName}?.version}" />
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset>
          <div class="form-actions">
            <g:actionSubmit class="save" action="update" class="btn btn-success" value="\${message(code: 'label.update', default: 'Update')}" />
            <g:link action="show" id="\${${propertyName}.id}" class="btn"><g:message code="label.cancel"/></g:link>
          </div>
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
