
<%@page import="aaf.fr.foundation.SPSSODescriptor" %>

<fr:hasPermission target="federation:management:descriptor:${descriptor.id}:crypto:create">
  <hr>

  <div id="addcertificate">
    <a class="show-addnew-certificate btn"><g:message encodeAs="HTML" code="label.addcertificate"/></a>
  </div>

  <div id="newcertificate" class="revealable">
    <h4><g:message encodeAs="HTML" code="templates.fr.certificates.certificatemanagement.addnew.heading"/></h4>
    <p><g:message encodeAs="HTML" code="templates.fr.certificates.certificatemanagement.addnew.requestformat" /></p>

    <div id="newcertificatedetails"></div>

    <form id="newcryptoform" class="form-horizontal" onsubmit="return false;">
      <fieldset>
        <div class="control-group">
          <label class="control-label" for="certname"><g:message encodeAs="HTML" code="label.name"/></label>
          <div class="controls">
            <g:textField name="certname" size="50"/>
            <fr:tooltip code='help.fr.certificate.name' />
          </div>
        </div>

        <div class="control-group">
          <label class="control-label" for="cert"><g:message encodeAs="HTML" code="label.certificate"/></label>
          <div class="controls">
            <g:textArea name="cert" class="cert" rows="25" cols="60"/>
            <fr:tooltip code='help.fr.certificate' />
          </div>
        </div>

        <div class="control-group">
          <label class="control-label" for="signing"><g:message encodeAs="HTML" code="label.signing" /></label>
          <div class="controls">
            <g:checkBox name="signing" value="${true}" />
            <fr:tooltip code='help.fr.certificate.sign' />
          </div>
        </div>
        <br>
        <div class="control-group">
          <label class="control-label" for="encryption"><g:message encodeAs="HTML" code="label.encryption" /></label>
          <div class="controls">
            <g:checkBox name="encryption" value="${descriptor.instanceOf(SPSSODescriptor)}"/>
            <fr:tooltip code='help.fr.certificate.enc' />
          </div>
        </div>

        <div class="form-actions">
          <a data-entity="${descriptor.entityDescriptor.entityID.encodeAsHTML()}" class="add-new-certificate btn btn-success"><g:message encodeAs="HTML" code="label.add"/></a>
          <a class="hide-addnew-certificate btn"><g:message encodeAs="HTML" code="label.cancel"/></a>
        </div>
      </fieldset>
    </form>
  </div>
</fr:hasPermission>

<div id="delete-certificate-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message encodeAs="HTML" code="templates.fr.certificates.remove.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message encodeAs="HTML" code="templates.fr.certificates.remove.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message encodeAs="HTML" code="label.cancel" /></a>
    <a class="btn btn-danger delete-certificate"/><g:message encodeAs="HTML" code="label.delete" /></a>
  </div>
</div>
