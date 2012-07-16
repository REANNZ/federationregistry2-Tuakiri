<%@ page import="aaf.fr.foundation.CAKeyInfo" %>

<div class="fieldcontain ${hasErrors(bean: CAKeyInfoInstance, field: 'certificate', 'error')} ">
  <label for="certificate">
    <g:message code="CAKeyInfo.certificate.label" default="Certificate" />  
  </label>
  <g:textArea name="cacertdata" rows="25" class="span6" value="${CAKeyInfoInstance?.certificate?.data}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: CAKeyInfoInstance, field: 'expiryDate', 'error')} ">
  <label for="expiryDate">
    <g:message code="CAKeyInfo.expiryDate.label" default="Expiry Date" />
  </label>
  <g:datePicker name="expiryDate" precision="day" value="${CAKeyInfoInstance?.expiryDate}" />
</div>

<div class="fieldcontain ${hasErrors(bean: CAKeyInfoInstance, field: 'keyName', 'error')} ">
  <label for="keyName">
    <g:message code="CAKeyInfo.keyName.label" default="Key Name" />
  </label>
  <g:textField name="keyName" value="${CAKeyInfoInstance?.keyName}" class="span4" />
</div>

