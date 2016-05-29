<%@ page import="aaf.fr.foundation.ServiceCategory" %>

<div class="fieldcontain ${hasErrors(bean: serviceCategoryInstance, field: 'name', 'error')} ">
  <label for="name">
    <g:message encodeAs="HTML" code="serviceCategory.name.label" default="Name" />

  </label>
  <g:textField name="name" value="${serviceCategoryInstance?.name}" class="span4" />
</div>

<div class="fieldcontain ${hasErrors(bean: serviceCategoryInstance, field: 'description', 'error')} ">
  <label for="description">
    <g:message encodeAs="HTML" code="serviceCategory.description.label" default="Description" />

  </label>
  <g:textField name="description" value="${serviceCategoryInstance?.description}" class="span4" />
</div>





