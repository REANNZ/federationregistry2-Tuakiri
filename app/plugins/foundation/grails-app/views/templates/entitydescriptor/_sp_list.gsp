<g:if test="${entity.spDescriptors}">
  <table>
    <thead>
      <tr>
        <th><g:message code="label.name" /></th>
        <th><g:message code="label.description" /></th>
        <th><g:message code="label.functioning" /></th>
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
              <g:message code="label.yes"/>
            </g:if>
            <g:else>
              <span class="not-functioning"><g:message code="label.no"/></span>
            </g:else>
          </td>
          <td><a href="${createLink(controller:'serviceProvider', action:'show', id:sp.id)}" class="btn"><g:message code="label.view"/></a></td>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <p class="alert-message block-message warn"><g:message code="fedreg.view.members.entity.show.no.serviceproviders" /></p>
</g:else>