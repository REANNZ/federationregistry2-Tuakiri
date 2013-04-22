<%@ page import="aaf.fr.foundation.OrganizationType" %>

<div class="fieldcontain ${hasErrors(bean: organizationTypeInstance, field: 'name', 'error')} ">
  <label for="name">
    <g:message encodeAs="HTML" code="organizationType.name.label" default="Name" />
    
  </label>
  <g:textField name="name" value="${organizationTypeInstance?.name}" class="span4" />
</div>

<div class="fieldcontain ${hasErrors(bean: organizationTypeInstance, field: 'displayName', 'error')} ">
  <label for="displayName">
    <g:message encodeAs="HTML" code="organizationType.displayName.label" default="Display Name" />
    
  </label>
  <g:textField name="displayName" value="${organizationTypeInstance?.displayName}" class="span4" />
</div>

<div class="fieldcontain ${hasErrors(bean: organizationTypeInstance, field: 'description', 'error')} ">
  <label for="description">
    <g:message encodeAs="HTML" code="organizationType.description.label" default="Description" />
    
  </label>
  <g:textField name="description" value="${organizationTypeInstance?.description}" class="span4" />
</div>

<div class="fieldcontain ${hasErrors(bean: organizationTypeInstance, field: 'discoveryServiceCategory', 'error')} ">
  <label for="discoveryServiceCategory">
    <g:message encodeAs="HTML" code="organizationType.discoveryServiceCategory.label" default="Discovery Service Category" />
    
  </label>
  <g:checkBox name="discoveryServiceCategory" value="${organizationTypeInstance?.discoveryServiceCategory}" />
</div>

