<g:if test="${entity.idpDescriptors}">
  <table class="table borderless">
    <thead>
      <tr>
        <th><g:message code="label.name" /></th>
        <th><g:message code="label.description" /></th>
        <th><g:message code="label.functioning" /></th>
        <th/>
      </tr>
    </thead>
    <tbody>
        <g:each in="${entity.idpDescriptors}" var="idp" status="i">
          <tr>
            <td>${idp.displayName.encodeAsHTML()}</td>
            <td>${idp.description.encodeAsHTML()}</td>
            <td>
              <g:if test="${idp.functioning()}">
                <g:message code="label.yes"/>
              </g:if>
              <g:else>
                <span class="not-functioning"><g:message code="label.no"/></span>
              </g:else>
            </td>
            <td><a href="${createLink(controller:'identityProvider', action:'show', id:idp.id)}" class="btn btn-small"><g:message code="label.view"/></a></td>
          </tr>
        </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <p class="alert alert-message"><g:message code="fedreg.view.members.entity.show.no.identityproviders" />
</g:else>