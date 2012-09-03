
<g:if test="${identityproviders}">
  <table class="table borderless">
    <thead>
      <tr>
        <th><g:message code="label.state" /></th>
        <th><g:message code="label.name" /></th>
        <th><g:message code="label.entitydescriptor" /></th>
        <th><g:message code="label.functioning" /></th>
        <th/>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td colspan="5"><strong><g:message code="label.active"/></strong></td>
      </tr>
      <g:each in="${identityproviders.findAll{!it.archived}}" var="idp">
        <tr>
          <td/>
          <td>${fieldValue(bean: idp, field: "displayName")}</td>
          <td>${fieldValue(bean: idp, field: "entityDescriptor.entityID")}</td>
          <td>
            <g:if test="${idp.functioning()}">
              <g:message code="label.yes"/>
            </g:if>
            <g:else>
              <span class="label label-important"><g:message code="label.no"/></span>
            </g:else>
          </td>
          <td>
            <a href="${createLink(controller:'identityProvider', action:'show', id:idp.id)}" class="btn btn-small"><g:message code="label.view"/></a>
          </td>
        </tr>
      </g:each>
  
      <tr>
        <td colspan="5"><strong><g:message code="label.archived"/></strong></td>
      </tr>
      <g:each in="${identityproviders.findAll{it.archived}}" var="idp">
        <tr>
          <td/>
          <td>${fieldValue(bean: idp, field: "displayName")}</td>
          <td>${fieldValue(bean: idp, field: "entityDescriptor.entityID")}</td>
          <td>
            <g:if test="${idp.functioning()}">
              <g:message code="label.yes"/>
            </g:if>
            <g:else>
              <span class="label label-important"><g:message code="label.no"/></span>
            </g:else>
          </td>
          <td>
            <a href="${createLink(controller:'identityProvider', action:'show', id:idp.id)}" class="btn btn-small"><g:message code="label.view"/></a>
          </td>
        </tr>
      </g:each>
    </tbody>
  </table>

</g:if>
<g:else>
  <p><g:message code="views.fr.foundation.organization.no.identityproviders" /></p>
</g:else>