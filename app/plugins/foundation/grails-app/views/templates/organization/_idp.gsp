<g:if test="${identityproviders}">
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
      <g:each in="${identityproviders}" var="idp">
        <tr>
          <td>${fieldValue(bean: idp, field: "displayName")}</td>
          <td>${fieldValue(bean: idp, field: "entityDescriptor.entityID")}</td>
          <td>
            <g:if test="${idp.functioning()}">
              <g:message code="label.yes"/>
            </g:if>
            <g:else>
              <span class="not-functioning"><g:message code="label.no"/></span>
            </g:else>
          </td>
          <td>
            <a href="${createLink(controller:'identityProvider', action:'show', id:idp.id)}" class="btn"><g:message code="label.view"/></a>
          </td>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <p><g:message code="fedreg.view.members.organization.no.identityproviders" /></p>
</g:else>