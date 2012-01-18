
<g:if test="${nameIDFormats}">
  <table class="borderless">
    <thead>
      <tr>
        <th><g:message code="label.supportedformat" /></th>
        <th><g:message code="label.description" /></th>
        <th/>
      </tr>
    </thead>
    <tbody>
    <g:each in="${ nameIDFormats.sort{it.id} }" status="i" var="nidf">
      <tr>
        <td>${nidf.uri.encodeAsHTML()}</td>
        <td>${nidf.description?.encodeAsHTML()}</td>
        <td>
          <fr:hasPermission target="descriptor:${descriptor.id}:nameidformat:remove">
            <a class="confirm-delete-nameid btn" data-formatid="${nidf.id}"><g:message code="label.delete"/></a>
          </fr:hasPermission>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <div>
    <p class="alert-message block-message error"><g:message code="fedreg.templates.nameidformats.noresults"/></p>
  </div>
</g:else>

<div id="delete-nameid-modal" class="modal hide fade">
  <div class="modal-header">
    <a href="#" class="close">×</a>
    <h3><g:message code="fedreg.templates.nameidformats.remove.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message code="fedreg.templates.nameidformats.remove.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message code="label.cancel" /></a>
    <a class="btn danger delete-nameid"><g:message code="label.delete" /></a>
  </div>
</div>