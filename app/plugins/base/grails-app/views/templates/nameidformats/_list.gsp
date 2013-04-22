<div id="nameidformats">
  <g:if test="${nameIDFormats}">
    <table class="table borderless">
      <thead>
        <tr>
          <th><g:message encodeAs="HTML" code="label.supportedformat" /></th>
          <th><g:message encodeAs="HTML" code="label.description" /></th>
          <th/>
        </tr>
      </thead>
      <tbody>
      <g:each in="${ nameIDFormats.sort{it.id} }" status="i" var="nidf">
        <tr>
          <td>${nidf.uri.encodeAsHTML()}</td>
          <td>${nidf.description?.encodeAsHTML()}</td>
          <td>
            <fr:hasPermission target="federation:management:descriptor:${descriptor.id}:nameidformat:remove">
              <a class="confirm-delete-nameid btn btn-mini" data-formatid="${nidf.id}"><g:message encodeAs="HTML" code="label.delete"/></a>
            </fr:hasPermission>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
  </g:if>
  <g:else>
    <div>
      <p class="alert alert-message alert-danger"><g:message encodeAs="HTML" code="templates.fr.nameidformats.noresults"/></p>
    </div>
  </g:else>
</div>
