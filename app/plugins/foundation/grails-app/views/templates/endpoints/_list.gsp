<div id="list-${endpointType}">
  <g:if test="${endpoints}">

    <g:if test="${!minEndpoints}">
      <g:set var="minEndpoints" value="${0}" />
    </g:if>

    <g:each in="${endpoints.sort { it.id }}" status="i" var="ep">
      <div id="endpoint-${ep.id}">
        <table class="table borderless fixed">
          <tbody>
            <tr>
              <th><g:message code="label.status" /></th>
              <td>
                <div id="endpoint-${ep.id}-active" class="${ep.active ? '':'revealable'}"><g:message code="label.active" /></div>
                <div id="endpoint-${ep.id}-inactive" class="${ep.active ? 'revealable':''} not-in-federation"><g:message code="label.inactive" /></div>
              </td>
            </tr>
            <tr>
              <th><g:message code="label.approved" /></th>
              <td>
                <g:if test="${ep.approved}">
                  <g:message code="label.yes" />
                </g:if>
                <g:else>
                  <g:message code="label.no" />
                </g:else>
              </td>
            </tr>
            <tr>
              <th><g:message code="label.lastupdated" /></th>
              <td>${ep.lastUpdated.encodeAsHTML()}</td>
            </tr>
            <tr>
              <th><g:message code="label.binding" /></th>
              <td>${ep.binding.uri.encodeAsHTML()}</td>
              <td/>
            </tr>
            <tr>
              <th><g:message code="label.location" /></th>
              <td><strong>${ep.location.encodeAsHTML()}</strong></td>
            </tr>
            <g:if test="${resloc}">
              <tr>
                <th><g:message code="label.responselocation" /></th>
                <td colspan="2">${(ep.responseLocation?:ep.location).encodeAsHTML()}</td>
              </tr>
            </g:if>
            <g:if test="${ep.instanceOf(aaf.fr.foundation.IndexedEndpoint)}">
              <tr>
                <th><g:message code="label.index" /></th>
                <td>
                  ${ep.index.encodeAsHTML()}
                </td>
              </tr>
              <tr>
                <th><g:message code="label.isdefault" /></th>
                <td>
                  <g:if test="${ep.isDefault}">
                    <strong><em><g:message code="label.yes" /></em></strong>
                  </g:if>
                  <g:else>
                    <g:message code="label.no" />
                  </g:else>
                </td>
              </tr>
            </g:if>
            <tr>
              <td colspan="2">
                <fr:hasPermission target="descriptor:${ep.descriptor.id}:endpoint:edit">
                  <a class="edit-endpoint btn btn-small btn-info" data-id="${ep.id}" data-type="${endpointType}"><g:message code="label.edit"/></a>
                </fr:hasPermission>

                <fr:hasPermission target="descriptor:${ep.descriptor.id}:endpoint:toggle">
                  <a class="confirm-toggle-endpoint btn btn-small" data-id="${ep.id}"><g:message code='label.togglestatus'/></a>
                </fr:hasPermission>

                <g:if test="${ep.instanceOf(aaf.fr.foundation.IndexedEndpoint) && !ep.isDefault}">
                  <fr:hasPermission target="descriptor:${ep.descriptor.id}:endpoint:makedefault">
                    <a class="confirm-makedefault-endpoint btn btn-small" data-id="${ep.id}" data-type="${endpointType}"><g:message code='label.makedefault'/></a>
                  </fr:hasPermission>
                </g:if>

                <fr:hasPermission target="descriptor:${ep.descriptor.id}:endpoint:remove">
                  <g:if test="${endpoints.size() > minEndpoints}">
                    <a class="confirm-delete-endpoint btn btn-small" data-id="${ep.id}" data-type="${endpointType}"><g:message code='label.delete'/></a>
                  </g:if>
                </fr:hasPermission>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </g:each>
  </g:if>
  <g:else>
    <p class="alert alert-message alert-danger"><g:message code="template.fr.endpoints.noresults"/></p>
  </g:else>
</div>