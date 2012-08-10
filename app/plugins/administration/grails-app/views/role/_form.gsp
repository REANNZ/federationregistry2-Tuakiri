<div class="control-group">
  <label class="control-label" for="name"><g:message code="role.name.label" default="Name" /></label>
  <div class="controls">
    <g:textField name="name" value="${role?.name}" class="span4 required" />
  </div>
</div>

<div class="control-group">
  <label class="control-label" for="description"><g:message code="role.description.label" default="Description" /></label>
  <div class="controls">
    <g:textField name="description" value="${role?.description}" class="span4 required" />
  </div>
</div>

<div class="control-group">
  <label class="control-label" for="protect"><g:message code="role.protect.label" default="Protect" /></label>
  <div class="controls">
    <g:checkBox name="protect" value="${role?.protect}" />
  </div>
</div>