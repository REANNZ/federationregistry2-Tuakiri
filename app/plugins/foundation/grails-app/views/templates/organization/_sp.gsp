<g:if test="${serviceproviders}">
  <table class="table borderless">
    <thead>
      <tr>
        <th><g:message code="label.name" /></th>
        <th><g:message code="label.entitydescriptor" /></th>
        <th><g:message code="label.functioning" /></th>
        <th/>
      </tr>
    </thead>
    <tbody>
      <g:each in="${serviceproviders}" var="sp">
        <tr>
          <td>${fieldValue(bean: sp, field: "displayName")}</td>
          <td>${fieldValue(bean: sp, field: "entityDescriptor.entityID")}</td>
          <td>
            <g:if test="${sp.functioning()}">
              <g:message code="label.yes"/>
            </g:if>
            <g:else>
              <span class="not-functioning"><g:message code="label.no"/></span>
            </g:else>
          </td>
          <td>
            <a href="${createLink(controller:'serviceProvider', action:'show', id:sp.id)}" class="btn btn-small"><g:message code="label.view"/></a>
          </td>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <p><g:message code="fedreg.view.members.organization.no.serviceproviders" /></p>
</g:else>