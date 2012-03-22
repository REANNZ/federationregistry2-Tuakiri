<%@ page import="aaf.fr.foundation.AttributeBase" %>

<div class="fieldcontain ${hasErrors(bean: attributeBaseInstance, field: 'name', 'error')} ">
  <label for="name">
    <g:message code="attributeBase.name.label" default="Name" />
    
  </label>
  <g:textField name="name" value="${attributeBaseInstance?.name}" class="span4" />
</div>
<div class="fieldcontain ${hasErrors(bean: attributeBaseInstance, field: 'legacyName', 'error')} ">
  <label for="legacyName">
    <g:message code="attributeBase.legacyName.label" default="Legacy Name" />
    
  </label>
  <g:textField name="legacyName" value="${attributeBaseInstance?.legacyName}" class="span4" />
</div>
<div class="fieldcontain ${hasErrors(bean: attributeBaseInstance, field: 'oid', 'error')} ">
  <label for="oid">
    <g:message code="attributeBase.oid.label" default="oid" />
    
  </label>
  <g:textField name="oid" value="${attributeBaseInstance?.oid}" class="span4" />
</div>
<div class="fieldcontain ${hasErrors(bean: attributeBaseInstance, field: 'description', 'error')} ">
  <label for="description">
    <g:message code="attributeBase.description.label" default="Description" />
    
  </label>
  <g:textField name="description" value="${attributeBaseInstance?.description}" class="span4" />
</div>


<div class="fieldcontain ${hasErrors(bean: attributeBaseInstance, field: 'nameFormat', 'error')} ">
  <label for="nameFormat">
    <g:message code="attributeBase.nameFormat.label" default="Name Format" />
    
  </label>
  <g:select id="nameFormat" name="nameFormat.id" from="${aaf.fr.foundation.SamlURI.findAllWhere(type:aaf.fr.foundation.SamlURIType.AttributeNameFormat)}" optionKey="id" optionValue="uri" required="" value="${attributeBaseInstance?.nameFormat?.id}" class="many-to-one span4"/>
</div>


<div class="fieldcontain ${hasErrors(bean: attributeBaseInstance, field: 'category', 'error')} ">
  <label for="category">
    <g:message code="attributeBase.category.label" default="Category" />
    
  </label>
  <g:select id="category" name="category.id" from="${aaf.fr.foundation.AttributeCategory.list()}" optionKey="id" optionValue="name" required="" value="${attributeBaseInstance?.category?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: attributeBaseInstance, field: 'specificationRequired', 'error')} ">
  <label for="specificationRequired">
    <g:message code="attributeBase.specificationRequired.label" default="Specification Required" />
    
  </label>
  <g:checkBox name="specificationRequired" value="${attributeBaseInstance?.specificationRequired}" />
</div>
<div class="fieldcontain ${hasErrors(bean: attributeBaseInstance, field: 'adminRestricted', 'error')} ">
  <label for="adminRestricted">
    <g:message code="attributeBase.adminRestricted.label" default="Admin Restricted" />
    
  </label>
  <g:checkBox name="adminRestricted" value="${attributeBaseInstance?.adminRestricted}" />
</div>
