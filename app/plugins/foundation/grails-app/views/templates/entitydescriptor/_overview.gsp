<div id="overview-entitydescriptor">
  <g:render template="/templates/entitydescriptor/overview_editable" plugin="foundation" model="[entity:entity]" />
  <fr:hasPermission target="federation:management:descriptor:${entity.id}:update">
    <a class="show-edit-entitydescriptor btn btn-small btn-info"><g:message code="label.edit"/></a>
  </fr:hasPermission>
  <fr:hasPermission target="federation:management:advanced">
    <a class="show-migrate-organisation btn btn-small"><g:message code="label.migrateorg"/></a>
    <g:if test="${entity.archived}"> 
      <g:if test="${!entity.organization.archived}"> 
        <a class="confirm-unarchive-entitydescriptor btn btn-small"><g:message code="label.unarchive"/></a>
      </g:if>
    </g:if> 
    <g:else>
      <a class="confirm-archive-entitydescriptor btn btn-small"><g:message code="label.archive"/></a>
    </g:else>
    <a class="confirm-delete-entitydescriptor btn btn-small"><g:message code="label.delete"/></a>
  </fr:hasPermission>
</div>
<g:render template="/templates/entitydescriptor/edit" plugin="foundation" model="[entity:entity, organizations:organizations]" />
<hr>

<div id="internalstate-entitydescriptor">
  <h4><g:message code="templates.fr.entitydescriptor.overview.internalstate" /></h4>
  <table class="table borderless fixed">
    <tbody>
      <tr>
        <th><g:message code="label.organization"/></th>
        <td><g:link controller="organization" action="show" id="${entity.organization?.id}">${fieldValue(bean: entity, field: "organization.displayName")}</g:link></td>
      </tr>
      <tr>
        <th><g:message code="label.archived"/></th>
        <td>
          <g:if test="${entity.archived}"> 
            <span class="label label-warning"><g:message code="label.warningmetadataarchived" /></span>
          </g:if>
          <g:else>
            <g:message code="label.no" /> 
          </g:else>
        </td>
      </tr>
      <tr>
        <th><g:message code="label.approved"/></th>
        <td>
          <g:if test="${entity.approved}">
            <g:message code="label.yes" />
          </g:if>
          <g:else>
            <span class="label label-important"><g:message code="label.undergoingapproval" /></span>
          </g:else>
        </td>
      </tr> 
      <tr>
        <th><g:message code="label.datecreated" /></th>
        <td>${fieldValue(bean: entity, field: "dateCreated")}</td>
      </tr> 
      <tr>
        <th><g:message code="label.lastupdated" /></th>
        <td>${fieldValue(bean: entity, field: "lastUpdated")}</td>
      </tr> 
    </tbody>
  </table>
</div>