<g:if test="${requestedAttributes}">
  <table class="table borderless">
    <thead>
      <tr>
        <th><g:message encodeAs="HTML" code="label.attribute" /></th>
        <th><g:message encodeAs="HTML" code="label.category" /></th>
        <th><g:message encodeAs="HTML" code="label.approved" /></th>
        <th><g:message encodeAs="HTML" code="label.reason" /></th>
        <th><g:message encodeAs="HTML" code="label.required" /></th>
      </tr>
    </thead>
    <tbody>
      <g:each in="${requestedAttributes}" status="j" var="ra">
        <tr class="show-ra" data-raid="${ra.id}">
          <td>
            <strong class="highlight">${fieldValue(bean: ra, field: "base.name")}</strong><br>
            <code>oid:${fieldValue(bean: ra, field: "base.oid")}</code>
            <br><br><em>${fieldValue(bean: ra, field: "base.description")}</em>
          </td>
          <td>
              ${fieldValue(bean: ra, field: "base.category.name")}
          </td>
          <td>
            <g:if test="${ra.approved}">
              <g:message encodeAs="HTML" code="label.yes" />
            </g:if>
            <g:else>
              <span class="not-in-federation"><g:message encodeAs="HTML" code="templates.fr.acs.reqattributes.workflow" /></span>
            </g:else>
          </td>
          <td> 
            <div id="ra-reason-${ra.id}">
              ${fieldValue(bean: ra, field: "reasoning")}
            </div>
          </td>
          <td> 
            <div>
              <g:if test="${ra.isRequired}">
                <g:message encodeAs="HTML" code="label.yes" />
              </g:if>
              <g:else>
                <g:message encodeAs="HTML" code="label.no" />
              </g:else>
            </div>
          </td>
        </tr>
        <tr class="editor-ra revealable" data-raid="${ra.id}">
        <td>
          <strong>${fieldValue(bean: ra, field: "base.name")}</strong><br>
          <code>oid:${fieldValue(bean: ra, field: "base.oid")}</code>
        </td>
        <td>${ra.base.category.name.encodeAsHTML()}</td>
        <td>
        <g:if test="${ra.approved}">
        <g:message encodeAs="HTML" code="label.yes" />
        </g:if>
        <g:else>
          <span class="not-in-federation"><g:message encodeAs="HTML" code="templates.fr.acs.reqattributes.workflow" /></span>
        </g:else>
        </td>
        <td> 
          <form class="validating">
            <input name="ra-edit-${ra.id}-reason" type="text" class="reason-ra required" data-raid="${ra.id}" size="40" value="${ra.reasoning?.encodeAsHTML()}" rel="twipsy" data-original-title="${g.message(encodeAs:"HTML", code:'help.fr.serviceprovider.attribute.reason')}"/>
          </form>
        </td>
        <td>
          <g:checkBox name="ra-edit-${ra.id}-required" class="required-ra" data-raid="${ra.id}" checked="${ra?.isRequired}" rel="twipsy" data-original-title="${g.message(encodeAs:"HTML", code:'help.fr.serviceprovider.attribute.isrequired')}" />
        </td>
        </tr>
        <g:if test="${specificationAttributes.contains(ra.base)}">
          <tr>
            <td colspan="5" class="highlight">
              <h5>Requested Values</h5>
              <div id="ra-values-${ra.id}">
                <g:render template="/templates/acs/listspecifiedattributevalues" plugin="foundation" model='[acs:acs, ra:ra]' />
              </div>
            </td>
          </tr>
        </g:if>
        <tr>
          <td colspan="5">
            <div class="manage-ra" data-raid="${ra.id}">
              <g:if test="${specificationAttributes.contains(ra.base)}">
                <fr:hasPermission target="federation:management:descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:value:add">
                  <a class="show-add-ra-value btn btn-small" data-raid="${ra.id}"><g:message encodeAs="HTML" code="label.addvalue"/></a>
                </fr:hasPermission>
              </g:if>
              <fr:hasPermission target="federation:management:descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:edit">
                <a data-raid="${ra.id}" class="btn btn-small btn-info edit-ra"><g:message encodeAs="HTML" code="label.edit" /></a>
              </fr:hasPermission>
              <fr:hasPermission target="federation:management:descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:remove">
                <a class="confirm-delete-ra btn btn-small" data-raid="${ra.id}" data-acsid="${ra.attributeConsumingService.id}"><g:message encodeAs="HTML" code="label.remove" /></a>
              </fr:hasPermission>
            </div>

            <fr:hasPermission target="federation:management:descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:edit">
            <div class="manage-update-ra revealable form-actions" data-raid="${ra.id}">
              <a class="update-ra btn btn-success" data-raid="${ra.id}" data-acsid="${ra.attributeConsumingService.id}"><g:message encodeAs="HTML" code="label.update"/></a>
              <a class="cancel-edit-ra btn"><g:message encodeAs="HTML" code="label.cancel"/></a>
            </div>
            </fr:hasPermission>

            <g:if test="${specificationAttributes.contains(ra.base)}">
            <fr:hasPermission target="federation:management:descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:value:add">
            <div id="newspecattributeval${ra.id}" class="revealable">
              <p>
                <g:message encodeAs="HTML" code="templates.fr.acs.specattributes.add.details"/>
              </p>
              <form id="newspecattributedata${ra.id}" class="form-horizontal validating">
                <input type="hidden" name="id" value="${ra.id}">
                <fieldset>
                  <div class="control-group">
                    <label class="control-label" for="value"><g:message encodeAs="HTML" code="label.value"/></label>
                    <div class="controls">
                      <input name="value" type="text" class="required" size="60"/>
                      <fr:tooltip code='help.fr.acs.specvalue' />
                    </div>
                  </div>
                </fieldset>

                <div class="form-actions">
                  <a class="add-ra-value btn btn-success" data-acsid="${acs.id}" data-raid="${ra.id}"><g:message encodeAs="HTML" code="label.add"/></a>
                  <a class="close-add-ra-value btn" data-raid="${ra.id}"><g:message encodeAs="HTML" code="label.cancel"/></a>
                </div>
              </form>
            </div>
            </fr:hasPermission>
            </g:if>
          </td>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <div class="alert alert-message alert-info">
    <g:message encodeAs="HTML" code="templates.fr.acs.reqattributes.not.requested" />
  </div>
</g:else>
