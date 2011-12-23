<g:if test="${requestedAttributes}">
  <table class="borderless">
    <thead>
      <tr>
        <th><g:message code="label.attribute" /></th>
        <th><g:message code="label.category" /></th>
        <th><g:message code="label.approved" /></th>
        <th><g:message code="label.required" /></th>
        <th><g:message code="label.reason" /></th>
      </tr>
    </thead>
    <tbody>
      <g:each in="${requestedAttributes}" status="j" var="ra">
        <tr id="ra-${ra.id}">
          <td>
            <strong>${fieldValue(bean: ra, field: "base.name")}</strong><br>
            <code>oid:${fieldValue(bean: ra, field: "base.oid")}</code>
            <br><br><em>${fieldValue(bean: ra, field: "base.description")}</em>
          </td>
          <td>
              ${fieldValue(bean: ra, field: "base.category.name")}
          </td>
          <td>
            <g:if test="${ra.approved}">
              <g:message code="label.yes" />
            </g:if>
            <g:else>
              <span class="not-in-federation"><g:message code="fedreg.templates.acs.reqattributes.workflow" /></span>
            </g:else>
          </td>
          <td>
            <g:if test="${ra.isRequired}">
              <g:message code="label.yes" />
            </g:if>
            <g:else>
              <g:message code="label.no" />
            </g:else>
          </td>
          <td> 
            <div id="ra-reason-${ra.id}">
              ${fieldValue(bean: ra, field: "reasoning")}
            </div>
          </td>
        </tr>
        <tr>
          <td colspan="5">
            <fr:hasPermission target="descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:add">
              <a onclick="$('#ra-${ra.id}').hide(); $('#ra-edit-${ra.id}').fadeIn(); return false;" class="btn"><g:message code="label.edit"/></a>
            </fr:hasPermission>
            <fr:hasPermission target="descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:remove">
              <n:confirmaction action="fedreg.acs_reqattribute_remove(${ra.id}, ${ra.attributeConsumingService.id}, '${containerID}' );" title="${message(code: 'fedreg.templates.acs.reqattributes.remove.confirm.title')}" msg="${message(code: 'fedreg.templates.acs.reqattributes.remove.confirm.descriptive', args:[ra.base.name.encodeAsHTML()])}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" class="delete-button" label="label.remove" />
            </fr:hasPermission>
          </td>
        </tr>
        <tr id="ra-edit-${ra.id}" class="hidden">
        <td>
        ${ra.base.name.encodeAsHTML()}
        <pre>OID: ${ra.base.oid?.encodeAsHTML()}</pre>
        </td>
        <td>${ra.base.category.name.encodeAsHTML()}</td>
        <td>
        <g:if test="${ra.approved}">
        <g:message code="label.yes" />
        </g:if>
        <g:else>
        <span class="warning"><g:message code="fedreg.templates.acs.reqattributes.workflow" /></span>
        </g:else>
        </td>
        <td>
        <g:checkBox name="ra-edit-${ra.id}-required" id="ra-edit-${ra.id}-required" checked="${ra?.isRequired}"/>
        <fr:tooltip code='fedreg.help.serviceprovider.attribute.isrequired' />
        </td>
        <td> 
        <form class="needsvalidation">
        <input name="ra-edit-${ra.id}-reason" id="ra-edit-${ra.id}-reason" type="text" class="required" size="40" value="${ra.reasoning?.encodeAsHTML()}"/>
        <fr:tooltip code='fedreg.help.acs.reason' /><br>
        </form>
        </td>
        <td>
        <n:button onclick="if( \$('#ra-edit-${ra.id}-reason').parent().valid() ) fedreg.acs_reqattribute_update( ${ra.attributeConsumingService.id}, ${ra.id}, \$('#ra-edit-${ra.id}-reason').val(), \$('#ra-edit-${ra.id}-required').is(':checked'), '${containerID}');" label="${message(code:'label.update')}" class="update-button"/>
        <n:button onclick="\$('#ra-edit-${ra.id}').hide(); \$('#ra-${ra.id}').fadeIn(); return false;" label="${message(code:'label.cancel')}" class="close-button"/>
        </td>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
<div class="alert-message block-message info">
<g:message code="fedreg.templates.acs.reqattributes.not.requested" />
</div>
</g:else>