<g:if test="${ra?.values?.size() > 0}">
  <table class="table borderless table-condensed">
    <tbody>
      <g:each in="${ra.values.sort{it.id}}" var="val">
        <tr>
          <td> ${val.value?.encodeAsHTML()}</td>
          <fr:hasPermission target="descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:value:remove">
            <td>
              <a class="confirm-delete-ra-value btn" data-acsid="${acs.id}" data-raid="${ra.id}" data-ravalueid="${val.id}"><g:message code="label.delete"/></a>
            </td>
          </fr:hasPermission>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <div class="alert alert-message alert-danger">
    <g:message code="fedreg.templates.acs.specattributes.no.values.currently.requested" args="[ra.base.name, ra.base.oid]"/>
  </div>
</g:else>