<%@ page import="aaf.fr.foundation.AttributeCategory" %>



<div class="fieldcontain ${hasErrors(bean: attributeCategoryInstance, field: 'name', 'error')} ">
  <label for="name">
    <g:message code="attributeCategory.name.label" default="Name" />
    
  </label>
  <g:textField name="name" value="${attributeCategoryInstance?.name}" class="span4" />
</div>

