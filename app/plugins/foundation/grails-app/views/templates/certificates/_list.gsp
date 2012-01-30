<div id="certificates">
  <g:if test="${descriptor.keyDescriptors && descriptor.keyDescriptors.findAll{it.disabled == false}.size() > 0}">
    <g:each in="${descriptor.keyDescriptors.sort{it.id}}" status="i" var="kd">  
      <g:if test="${!kd.disabled}">
        <div class="certificate">
          <table class="borderless">
            <tbody>
              <tr>
                <th><g:message code="label.id" /></th>
                <td colspan="2">${kd.id.encodeAsHTML()}</td>
              </tr>
              <tr>
                <th><g:message code="label.keytype" /></th>
                <td>${kd.keyType.encodeAsHTML()}</td>
              <td>
                <fr:hasPermission target="descriptor:${descriptor.id}:crypto:delete">
                  <a class="confirm-delete-certificate btn" data-certificate="${kd.id}"><g:message code='label.delete'/></a>
                </fr:hasPermission>
                </td>
              </tr>
              <tr>
                <th><g:message code="label.name" /></th>
                <td colspan="2">${(kd.keyInfo.keyName?:"N/A").encodeAsHTML()}</td>
              </tr>
              <tr>
                <th><g:message code="label.issuer" /></th>
                <td colspan="2">${kd.keyInfo.certificate.issuer.encodeAsHTML()}</td>
              </tr>
              <tr>
                <th><g:message code="label.subject" /></th>
                <td>${kd.keyInfo.certificate.subject.encodeAsHTML()}</td>
              </tr>
              <tr>
                <th><g:message code="label.expirydate" /></th>
                <td colspan="2">
                  ${kd.keyInfo.certificate.expiryDate.encodeAsHTML()}
                  <g:if test="${kd.keyInfo.certificate.criticalAlert()}">
                    <div class="alert-message block-message error">
                      <p class="icon icon_exclamation"><g:message code="label.certificatecritical"/></p>
                    </div>
                  </g:if>
                  <g:else>
                    <g:if test="${kd.keyInfo.certificate.warnAlert()}">
                      <div class="alert-message block-message warn">
                        <p><g:message code="label.certificatewarning"/></p>
                      </div>
                    </g:if>
                    <g:else>
                      <g:if test="${kd.keyInfo.certificate.infoAlert()}">
                        <div class="alert-message block-message info">
                          <p><g:message code="label.certificateinfo"/></p>
                        </div>
                      </g:if>
                    </g:else>
                  </g:else>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </g:if>
    </g:each>
  </g:if>
  <g:else>
    <p class="alert-message block-message error"><g:message code="fedreg.templates.certificates.none" /></p>
  </g:else>
</div>
