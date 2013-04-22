<div id="overview-identityprovider">
  <g:render template="/templates/identityprovider/overview_editable" plugin="foundation" model="[descriptor:identityProvider]" />
  <fr:hasPermission target="federation:management:descriptor:${identityProvider.id}:update">
    <a class="show-edit-identityprovider btn btn-small btn-info"><g:message encodeAs="HTML" code="label.edit"/></a>
  </fr:hasPermission>
</div>
<g:render template="/templates/identityprovider/edit" plugin="foundation" model="[descriptor:identityProvider]" />

<hr>

<div id="internalstate-identityprovider">
  <h4><g:message encodeAs="HTML" code="templates.fr.identityprovider.overview.internalstate" /></h4>
  <table class="table borderless fixed">
    <tbody>   
      <tr>
        <th><g:message encodeAs="HTML" code="label.organization"/></th>
        <td><g:link controller="organization" action="show" id="${identityProvider.organization.id}">${fieldValue(bean: identityProvider, field: "organization.displayName")}</g:link></td>
      </tr>
      <fr:hasPermission target="federation:management:saml:advanced">
        <tr>
          <th><g:message encodeAs="HTML" code="label.entitydescriptor"/></th>
          <td><g:link controller="entityDescriptor" action="show" id="${identityProvider.entityDescriptor.id}">${fieldValue(bean: identityProvider, field: "entityDescriptor.entityID")}</g:link></td>
        </tr>
      </fr:hasPermission>
      <fr:lacksPermission target="federation:management:saml:advanced">
        <tr>
          <th><g:message encodeAs="HTML" code="label.entitydescriptor"/></th>
          <td>${fieldValue(bean: identityProvider, field: "entityDescriptor.entityID")}</td>
        </tr>
      </fr:lacksPermission>
      <tr>
        <th><g:message encodeAs="HTML" code="label.protocolsupport"/></th>
        <td>
          <g:each in="${identityProvider.protocolSupportEnumerations}" status="i" var="pse">
          ${fieldValue(bean: pse, field: "uri")}  <br/>
          </g:each>
        </td>
      </tr>
      <tr>
        <th><g:message encodeAs="HTML" code="label.archived"/></th>
        <td>
          <g:if test="${identityProvider.archived}"> 
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
          <g:if test="${identityProvider.approved}">
            <g:message encodeAs="HTML" code="label.yes" />
          </g:if>
          <g:else>
            <span class="label label-important"><g:message encodeAs="HTML" code="label.undergoingapproval" /></span>
          </g:else>
        </td>
      </tr>
      <tr>
        <th><g:message encodeAs="HTML" code="label.requiresignedauthn"/></th>
        <td>
          <g:if test="${identityProvider.wantAuthnRequestsSigned}">
            <g:message encodeAs="HTML" code="label.yes" />
          </g:if>
          <g:else>
            <g:message encodeAs="HTML" code="label.no" />
          </g:else>
        </td>
      </tr>
      <tr>
        <th><g:message encodeAs="HTML" code="label.datecreated" /></th>
        <td>${fieldValue(bean: identityProvider, field: "dateCreated")}</td>
      </tr>
      <tr>
        <th><g:message encodeAs="HTML" code="label.lastupdated" /></th>
        <td>${fieldValue(bean: identityProvider, field: "lastUpdated")}</td>
      </tr> 
    </tbody>
  </table>
</div>
