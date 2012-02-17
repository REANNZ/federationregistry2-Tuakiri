<g:if test="${flash.type == 'info'}">
  <div class="alert alert-info">
    <strong><g:message code="label.info"/></strong><br>
    <span>${flash.message}</span>
  </div>
</g:if>
<g:if test="${flash.type == 'success'}">
  <div class="alert alert-success">
    <strong><g:message code="label.success"/></strong><br>
    <span>${flash.message}</span>
  </div>
</g:if>
<g:if test="${flash.type == 'error'}">
  <div class="alert alert-error">
    <strong><g:message code="label.error"/></strong><br>
    <span>${flash.message}</span>
  </div>
</g:if>