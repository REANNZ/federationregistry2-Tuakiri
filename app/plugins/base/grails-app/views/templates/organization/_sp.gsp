<g:if test="${serviceproviders}">
  <table class="table borderless">
    <thead>
      <tr>
        <th><g:message encodeAs="HTML" code="label.state" /></th>
        <th><g:message encodeAs="HTML" code="label.name" /></th>
        <th><g:message encodeAs="HTML" code="label.entitydescriptor" /></th>
        <th><g:message encodeAs="HTML" code="label.functioning" /></th>
        <th/>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td colspan="5"><strong><g:message encodeAs="HTML" code="label.active"/></strong></td>
      </tr>
      <g:each in="${serviceproviders.findAll{!it.archived}}" var="sp">
        <tr>
          <td/>
          <td>${fieldValue(bean: sp, field: "displayName")}</td>
          <td>${fieldValue(bean: sp, field: "entityDescriptor.entityID")}</td>
          <td>
            <g:if test="${sp.functioning()}">
              <g:message encodeAs="HTML" code="label.yes"/>
            </g:if>
            <g:else>
              <span class="label label-important"><g:message encodeAs="HTML" code="label.no"/></span>
            </g:else>
          </td>
          <td>
            <a href="${createLink(controller:'serviceProvider', action:'show', id:sp.id)}" class="btn btn-small"><g:message encodeAs="HTML" code="label.view"/></a>
          </td>
        </tr>
      </g:each>

      <tr>
        <td colspan="5"><strong><g:message encodeAs="HTML" code="label.archived"/></strong></td>
      </tr>
      <g:each in="${serviceproviders.findAll{it.archived}}" var="sp">
        <tr>
          <td/>
          <td>${fieldValue(bean: sp, field: "displayName")}</td>
          <td>${fieldValue(bean: sp, field: "entityDescriptor.entityID")}</td>
          <td>
            <g:if test="${sp.functioning()}">
              <g:message encodeAs="HTML" code="label.yes"/>
            </g:if>
            <g:else>
              <span class="label label-important"><g:message encodeAs="HTML" code="label.no"/></span>
            </g:else>
          </td>
          <td>
            <a href="${createLink(controller:'serviceProvider', action:'show', id:sp.id)}" class="btn btn-small"><g:message encodeAs="HTML" code="label.view"/></a>
          </td>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <p><g:message encodeAs="HTML" code="views.fr.foundation.organization.no.serviceproviders" /></p>
</g:else>
