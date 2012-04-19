<div id="overview-organization">
  <g:render template="/templates/organization/overview_editable" plugin="foundation" model="[organization:organization]" />
  <fr:hasPermission target="organization:${organization.id}:update">
    <a class="show-edit-organization btn btn-small btn-info"><g:message code="label.edit"/></a>
  </fr:hasPermission>
  <fr:hasPermission target="federation:management:advanced">
    <g:if test="${organization.archived}"> 
      <a class="confirm-unarchive-organization btn btn-small"><g:message code="label.unarchive"/></a>
    </g:if> 
    <g:else>
      <a class="confirm-archive-organization btn btn-small"><g:message code="label.archive"/></a>
    </g:else>
    <a class="confirm-delete-organization btn btn-small"><g:message code="label.delete"/></a>
  </fr:hasPermission>
</div>
<g:render template="/templates/organization/edit" plugin="foundation" model="[organization:organization, organizationTypes:organizationTypes]" />

<hr>

<div id="internalstate-organization">
  <h4><g:message code="template.fr.organization.overview.internalstate" /></h4>
  <table class="table borderless fixed">
    <tbody>
      <tr>
        <th><g:message code="label.archived"/></th>
        <td>
          <g:if test="${organization.archived}"> 
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
            <g:if test="${organization.approved}">
              <g:message code="label.yes" />
            </g:if>
            <g:else>
              <span class="label label-important"><g:message code="label.undergoingapproval" /></span>
            </g:else>
          </td>
        </tr>
        <tr>
          <th><g:message code="label.datecreated" /></th>
          <td>${fieldValue(bean: organization, field: "dateCreated")}</td>
        </tr>  
        <tr>
          <th><g:message code="label.lastupdated" /></th>
          <td>${fieldValue(bean: organization, field: "lastUpdated")}</td>
        </tr>  
      </tbody>
    </table>
  </div>