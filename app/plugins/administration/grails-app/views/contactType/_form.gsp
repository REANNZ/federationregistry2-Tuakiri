<%@ page import="aaf.fr.foundation.ContactType" %>

<div class="fieldcontain ${hasErrors(bean: contactTypeInstance, field: 'name', 'error')} ">
  <label for="name">
    <g:message encodeAs="HTML" code="contactType.name.label" default="Name" />
    
  </label>
  <g:textField name="name" value="${contactTypeInstance?.name}" class="span4" />
</div>

<div class="fieldcontain ${hasErrors(bean: contactTypeInstance, field: 'displayName', 'error')} ">
  <label for="displayName">
    <g:message encodeAs="HTML" code="contactType.displayName.label" default="Display Name" />
    
  </label>
  <g:textField name="displayName" value="${contactTypeInstance?.displayName}" class="span4" />
</div>

<div class="fieldcontain ${hasErrors(bean: contactTypeInstance, field: 'description', 'error')} ">
  <label for="description">
    <g:message encodeAs="HTML" code="contactType.description.label" default="Description" />
    
  </label>
  <g:textField name="description" value="${contactTypeInstance?.description}" class="span4" />
</div>





