<div id="overview-organization">
  <g:render template="/templates/organization/overview_editable" plugin="foundation" model="[organization:organization]" />
  <fr:hasPermission target="federation:management:organization:${organization.id}:update">
    <a class="show-edit-organization btn btn-small btn-info"><g:message encodeAs="HTML" code="label.edit"/></a>
  </fr:hasPermission>
  <fr:hasPermission target="federation:management:advanced">
    <g:if test="${organization.archived}"> 
      <a class="confirm-unarchive-organization btn btn-small"><g:message encodeAs="HTML" code="label.unarchive"/></a>
    </g:if> 
    <g:else>
      <a class="confirm-archive-organization btn btn-small"><g:message encodeAs="HTML" code="label.archive"/></a>
    </g:else>
    <a class="confirm-delete-organization btn btn-small"><g:message encodeAs="HTML" code="label.delete"/></a>
  </fr:hasPermission>
</div>
<g:render template="/templates/organization/edit" plugin="foundation" model="[organization:organization, organizationTypes:organizationTypes]" />

<hr>

<div id="internalstate-organization">
  <h4><g:message encodeAs="HTML" code="templates.fr.organization.overview.internalstate" /></h4>
  <table class="table borderless fixed">
    <tbody>
      <tr>
        <th><g:message encodeAs="HTML" code="label.archived"/></th>
        <td>
          <g:if test="${organization.archived}"> 
            <span class="label label-warning"><g:message encodeAs="HTML" code="label.warningmetadataarchived" /></span>
          </g:if>
          <g:else>
            <g:message encodeAs="HTML" code="label.no" /> 
          </g:else>
        </td>
        </tr>
        <tr>
          <th><g:message encodeAs="HTML" code="label.approved"/></th>
          <td>
            <g:if test="${organization.approved}">
              <g:message encodeAs="HTML" code="label.yes" />
            </g:if>
            <g:else>
              <span class="label label-important"><g:message encodeAs="HTML" code="label.undergoingapproval" /></span>
            </g:else>
          </td>
        </tr>
        <tr>
          <th><g:message encodeAs="HTML" code="label.datecreated" /></th>
          <td>${fieldValue(bean: organization, field: "dateCreated")}</td>
        </tr>  
        <tr>
          <th><g:message encodeAs="HTML" code="label.lastupdated" /></th>
          <td>${fieldValue(bean: organization, field: "lastUpdated")}</td>
        </tr>  
      </tbody>
    </table>
  </div>
