<div id="overview-organization-editable">
  <table class="table borderless fixed">
    <tbody>   
      <tr>
        <th><g:message encodeAs="HTML" code="label.status"/></th>
        <td>
          <g:if test="${organization.active}">
            <g:message encodeAs="HTML" code="label.active" />
          </g:if>
          <g:else>
            <span class="label label-important"><g:message encodeAs="HTML" code="label.inactive" /></span><fr:tooltip code='label.warningmetadata'/>
          </g:else>
        </td>
      </tr> 
      <tr>
        <th><g:message encodeAs="HTML" code="label.name"/></th>
        <td>${fieldValue(bean: organization, field: "name")}</td>
      </tr>
      <tr>
        <th><g:message encodeAs="HTML" code="label.displayname" /></th>
        <td>${fieldValue(bean: organization, field: "displayName")}</td>
      </tr>
      <tr>
        <th><g:message encodeAs="HTML" code="label.url" /></th>
        <td><a href="${organization.url}">${organization.url}</a></td>
      </tr>

      <tr>
        <th><g:message encodeAs="HTML" code="label.primarytype" /></th>
        <td>${fieldValue(bean: organization, field: "primary.displayName")}</td>
      </tr>

      <tr>
        <th><g:message encodeAs="HTML" code="label.organizationsecondarytypes" /></th>
        <td>
          <g:each in="${organization.types}" var="t">
            ${fieldValue(bean: t, field: "displayName")} <br>
          </g:each>
        </td>
      </tr>
    </tbody>
  </table>
</div>
