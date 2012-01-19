<div id="overview-serviceprovider-editable">
  <table class="borderless">
    <tbody>  
      <tr>
        <th><g:message code="label.status"/></th>
        <td>
          <g:if test="${serviceProvider.active}">
            <g:message code="label.active" />
          </g:if>
          <g:else>
            <span class="more-info not-in-federation" rel="twipsy" data-original-title="${g.message(code:'label.warningmetadata')}" data-placement="right"><g:message code="label.inactive" /></span>
          </g:else>
        </td>
      </tr> 
      <tr>
        <th><g:message code="label.displayname"/></th>
        <td>${fieldValue(bean: serviceProvider, field: "displayName")}</td>
      </tr>
      <tr>
        <th>
          <g:message code="label.description" />
        </th>
        <td>
          ${fieldValue(bean: serviceProvider, field: "description")}
        </td>
      </tr>
      <tr>
        <th>
          <g:message code="label.serviceurl" />
        </th>
        <td>
          ${fieldValue(bean: serviceProvider, field: "serviceDescription.connectURL")}
        </td>
      </tr>
      <tr>
        <th>
          <g:message code="label.servicelogourl" />
        </th>
        <td>
          ${fieldValue(bean: serviceProvider, field: "serviceDescription.logoURL")}
        </td>
      </tr>
    </tbody>
  </table>
</div>