<g:if test="${ra?.values?.size() > 0}">
  <table class="table borderless table-condensed">
    <tbody>
      <g:each in="${ra.values.sort{it.id}}" var="val">
        <tr>
          <td> ${val.value?.encodeAsHTML()}</td>
          <fr:hasPermission target="federation:management:descriptor:${ra.attributeConsumingService.descriptor.id}:attribute:value:remove">
            <td>
              <a class="confirm-delete-ra-value btn btn-danger" data-acsid="${acs.id}" data-raid="${ra.id}" data-ravalueid="${val.id}"><g:message encodeAs="HTML" code="label.remove"/></a>
            </td>
          </fr:hasPermission>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <div class="alert alert-message alert-danger">
    <g:message encodeAs="HTML" code="templates.fr.acs.specattributes.no.values.currently.requested" args="[ra.base.name, ra.base.oid]"/>
  </div>
</g:else>
