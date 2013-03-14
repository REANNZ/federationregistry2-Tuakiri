<%@ page import="aaf.fr.foundation.SamlURI" %>


<div class="fieldcontain ${hasErrors(bean: samlURIInstance, field: 'uri', 'error')} ">
  <label for="uri">
    <g:message encodeAs="HTML" code="samlURI.uri.label" default="Uri" />
    
  </label>
  <g:textField name="uri" value="${samlURIInstance?.uri}" class="span4" />
</div>

<div class="fieldcontain ${hasErrors(bean: samlURIInstance, field: 'description', 'error')} ">
  <label for="description">
    <g:message encodeAs="HTML" code="samlURI.description.label" default="Description" />
    
  </label>
  <g:textField name="description" value="${samlURIInstance?.description}" class="span4" />
</div>

<div class="fieldcontain ${hasErrors(bean: samlURIInstance, field: 'type', 'error')} ">
  <label for="type">
    <g:message encodeAs="HTML" code="samlURI.type.label" default="Type" />
    
  </label>
  <g:select name="type" from="${aaf.fr.foundation.SamlURIType?.values()}" keys="${aaf.fr.foundation.SamlURIType.values()*.name()}" required="" value="${samlURIInstance?.type?.name()}"/>
</div>



