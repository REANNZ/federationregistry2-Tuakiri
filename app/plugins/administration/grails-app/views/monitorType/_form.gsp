<%@ page import="aaf.fr.foundation.MonitorType" %>

<div class="fieldcontain ${hasErrors(bean: monitorTypeInstance, field: 'name', 'error')} ">
  <label for="name">
    <g:message encodeAs="HTML" code="monitorType.name.label" default="Name" />
    
  </label>
  <g:textField name="name" value="${monitorTypeInstance?.name}" class="span4" />
</div>

<div class="fieldcontain ${hasErrors(bean: monitorTypeInstance, field: 'description', 'error')} ">
  <label for="description">
    <g:message encodeAs="HTML" code="monitorType.description.label" default="Description" />
    
  </label>
  <g:textField name="description" value="${monitorTypeInstance?.description}" class="span4" />
</div>

