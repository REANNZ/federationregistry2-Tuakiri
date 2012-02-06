
<%@page import="aaf.fr.foundation.SPSSODescriptor" %>

<fr:hasPermission target="descriptor:${descriptor.id}:crypto:create">
  <div id="addcertificate">
    <a onclick="$('#addcertificate').fadeOut(); $('#newcertificate').fadeIn();" class="btn btn-info"><g:message code="label.addcertificate"/></a>
  </div>
  
  <div id="newcertificate" class="revealable">
    <h4><g:message code="fedreg.templates.certificates.certificatemanagement.addnew.heading"/></h4>
    <p><g:message code="fedreg.templates.certificates.certificatemanagement.addnew.requestformat" /></p>
    
    <div id="newcertificatedetails"></div>

    <form id="newcryptoform" class="form-horizontal">
      <fieldset>
        <div class="control-group">
          <label for="certname"><g:message code="label.name"/></label>
          <div class="controls">
            <g:textField name="certname" size="50"/>
            <fr:tooltip code='fedreg.help.certificate.name' />
          </div>
        </div>

        <div class="control-group">
          <label for="cert"><g:message code="label.certificate"/></label>
          <div class="controls">
            <g:textArea name="cert" class="cert" rows="25" cols="60"/>
            <fr:tooltip code='fedreg.help.certificate' />
          </div>
        </div>

        <div class="control-group">
          <label for="signing"><g:message code="label.signing" /></label>
          <div class="controls"> 
            <g:checkBox name="signing" value="${true}" />
            <fr:tooltip code='fedreg.help.certificate.sign' />
          </div>
        </div>
        <br>
        <div class="control-group">
          <label for="encryption"><g:message code="label.encryption" /></label>
          <div class="controls">
            <g:checkBox name="encryption" value="${descriptor.instanceOf(SPSSODescriptor)}"/>
            <fr:tooltip code='fedreg.help.certificate.enc' />
          </div>
        </div>

        <div class="form-actions">
          <a data-entity="${descriptor.entityDescriptor.entityID}" class="add-new-certificate btn btn-success"><g:message code="label.add"/></a>
          <a onclick="$('#newcertificate').fadeOut(); $('#addcertificate').fadeIn();" class="btn"><g:message code="label.close"/></a>
        </div>
      </fieldset>
    </form>
  </div>
</fr:hasPermission>

<div id="delete-certificate-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message code="fedreg.templates.certificates.remove.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message code="fedreg.templates.certificates.remove.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message code="label.cancel" /></a>
    <a class="btn btn-danger delete-certificate"/><g:message code="label.delete" /></a>
  </div>
</div>