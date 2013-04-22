<g:if test="${entity.spDescriptors}">
  <table class="table borderless">
    <thead>
      <tr>
        <th><g:message encodeAs="HTML" code="label.name" /></th>
        <th><g:message encodeAs="HTML" code="label.description" /></th>
        <th><g:message encodeAs="HTML" code="label.functioning" /></th>
        <th/>
      </tr>
    </thead>
    <tbody>
      <g:each in="${entity.spDescriptors}" var="sp">
        <tr>
          <td>${sp.displayName.encodeAsHTML()}</td>
          <td>${sp.description.encodeAsHTML()}</td>
          <td>
            <g:if test="${sp.functioning()}">
              <g:message encodeAs="HTML" code="label.yes"/>
            </g:if>
            <g:else>
              <span class="not-functioning"><g:message encodeAs="HTML" code="label.no"/></span>
            </g:else>
          </td>
          <td><a href="${createLink(controller:'serviceProvider', action:'show', id:sp.id)}" class="btn btn-small"><g:message encodeAs="HTML" code="label.view"/></a></td>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <p class="alert alert-message"><g:message encodeAs="HTML" code="views.fr.foundation.entity.show.no.serviceproviders" /></p>
</g:else>
