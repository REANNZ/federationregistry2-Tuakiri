<g:if test="${entities}">
  <table class="table borderless">
    <thead>
      <tr>
        <th><g:message code="label.state" /></th>
        <th><g:message code="label.entitydescriptor" /></th>
        <th><g:message code="label.functioning" /></th>
        <th/>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td colspan="5"><strong><g:message code="label.active"/></strong></td>
      </tr>
      <g:each in="${entities.findAll{!it.archived}}" var="ent">
        <tr>
          <td/>
          <td>${fieldValue(bean: ent, field: "entityID")}</td>
          <td>
            <g:if test="${ent.functioning()}">
              <g:message code="label.yes"/>
            </g:if>
            <g:else>
              <span class="label label-important"><g:message code="label.no"/></span>
            </g:else>
          </td>
          <td>
            <a href="${createLink(controller:'entityDescriptor', action:'show', id:ent.id)}" class="btn btn-small"><g:message code="label.view"/></a>
          </td>
        </tr>
      </g:each>

      <tr>
        <td colspan="5"><strong><g:message code="label.archived"/></strong></td>
      </tr>
      <g:each in="${entities.findAll{!it.archived}}" var="ent">
        <tr>
          <td/>
          <td>${fieldValue(bean: ent, field: "entityID")}</td>
          <td>
            <g:if test="${ent.functioning()}">
              <g:message code="label.yes"/>
            </g:if>
            <g:else>
              <span class="label label-important"><g:message code="label.no"/></span>
            </g:else>
          </td>
          <td>
            <a href="${createLink(controller:'entityDescriptor', action:'show', id:ent.id)}" class="btn btn-small"><g:message code="label.view"/></a>
          </td>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <p><g:message code="views.fr.foundation.organization.no.entities" /></p>
</g:else>