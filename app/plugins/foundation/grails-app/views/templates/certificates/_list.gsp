<div id="certificates">
  <g:if test="${descriptor.keyDescriptors && descriptor.keyDescriptors.findAll{it.disabled == false}.size() > 0}">
    <g:each in="${descriptor.keyDescriptors.sort{it.id}}" status="i" var="kd">  
      <g:if test="${!kd.disabled}">
        <div class="certificate">
          <table class="table borderless">
            <tbody>
              <tr>
                <th><g:message code="label.id" /></th>
                <td>${kd.id.encodeAsHTML()}</td>
              </tr>
              <tr>
                <th><g:message code="label.keytype" /></th>
                <td>${kd.keyType.encodeAsHTML()}</td>
              </tr>
              <tr>
                <th><g:message code="label.name" /></th>
                <td>${(kd.keyInfo.keyName?:"N/A").encodeAsHTML()}</td>
              </tr>
              <tr>
                <th><g:message code="label.issuer" /></th>
                <td>${kd.keyInfo.certificate.issuer.encodeAsHTML()}</td>
              </tr>
              <tr>
                <th><g:message code="label.subject" /></th>
                <td>${kd.keyInfo.certificate.subject.encodeAsHTML()}</td>
              </tr>
              <tr>
                <th><g:message code="label.expirydate" /></th>
                <td>
                  ${kd.keyInfo.certificate.expiryDate.encodeAsHTML()}
                  <g:if test="${kd.keyInfo.certificate.criticalAlert()}">
                    <span class="label label-important">
                      <g:message code="label.certificatecritical"/>
                    </span>
                  </g:if>
                  <g:else>
                    <g:if test="${kd.keyInfo.certificate.warnAlert()}">
                      <span class="label label-warning">
                        <g:message code="label.certificatewarning"/>
                      </span>
                    </g:if>
                    <g:else>
                      <g:if test="${kd.keyInfo.certificate.infoAlert()}">
                        <span class="label label-info">
                          <g:message code="label.certificateinfo"/>
                        </span>
                      </g:if>
                    </g:else>
                  </g:else>
                </td>
              </tr>
              <tr>
                <td>
                  <fr:hasPermission target="descriptor:${descriptor.id}:crypto:delete">
                    <a class="confirm-delete-certificate btn btn-mini" data-certificate="${kd.id}"><g:message code='label.delete'/></a>
                  </fr:hasPermission>
                </td>
                <td/>
              </tr>
            </tbody>
          </table>
        </div>
      </g:if>
    </g:each>
  </g:if>
  <g:else>
    <p class="alert alert-message alert-danger"><g:message code="fedreg.templates.certificates.none" /></p>
  </g:else>
</div>
