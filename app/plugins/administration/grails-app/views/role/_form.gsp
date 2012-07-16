  <label for="name">
    <g:message code="role.name.label" default="Name" />
  </label>
  <g:textField name="name" value="${role?.name}" class="span4 required" />


  <label for="description">
    <g:message code="role.description.label" default="Description" />
  </label>
  <g:textField name="description" value="${role?.description}" class="span4 required" />

  <label for="protect">
    <g:message code="role.protect.label" default="Protect" /> 
  </label>
  <g:checkBox name="protect" value="${role?.protect}" />
