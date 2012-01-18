
<html>
  <head>
    <meta name="layout" content="members" />

    <title><g:message code="fedreg.view.members.serviceprovider.show.title" /></title>
    
    <r:script>
      var activeContact
      var contactCreateEndpoint = "${createLink(controller:'descriptorContact', action:'create', id:serviceProvider.id )}";
      var contactDeleteEndpoint = "${createLink(controller:'descriptorContact', action:'delete' )}";
      var contactListEndpoint = "${createLink(controller:'descriptorContact', action:'list', id:serviceProvider.id ) }";
      var contactSearchEndpoint = "${createLink(controller:'descriptorContact', action:'search')}";
      
      var acsAddAttr = "${createLink(controller:'attributeConsumingService', action:'addRequestedAttribute')}";
      var acsListAttr = "${createLink(controller:'attributeConsumingService', action:'listRequestedAttributes')}";
      var acsRemoveAttr = "${createLink(controller:'attributeConsumingService', action:'removeRequestedAttribute')}";
      var acsUpdateAttr = "${createLink(controller:'attributeConsumingService', action:'updateRequestedAttribute')}";
      var acsAddSpecAttrVal = "${createLink(controller:'attributeConsumingService', action:'addSpecifiedAttributeValue')}";
      var acsRemoveSpecAttrVal = "${createLink(controller:'attributeConsumingService', action:'removeSpecifiedAttributeValue')}";
      var acsListSpecAttrValues = "${createLink(controller:'attributeConsumingService', action:'listSpecifiedAttributeValue')}";
      
      var certificateListEndpoint = "${createLink(controller:'roleDescriptorCrypto', action:'list', id:serviceProvider.id )}";
      var certificateCreationEndpoint = "${createLink(controller:'roleDescriptorCrypto', action:'create', id:serviceProvider.id)}";
      var certificateDeleteEndpoint = "${createLink(controller:'roleDescriptorCrypto', action:'delete')}";
      var certificateValidationEndpoint = "${createLink(controller:'coreUtilities', action:'validateCertificate')}";
      
      var endpointDeleteEndpoint = "${createLink(controller:'descriptorEndpoint', action:'delete')}";
      var endpointListEndpoint = "${createLink(controller:'descriptorEndpoint', action:'list', id:serviceProvider.id)}";
      var endpointCreationEndpoint = "${createLink(controller:'descriptorEndpoint', action:'create', id:serviceProvider.id)}";
      var endpointToggleStateEndpoint = "${createLink(controller:'descriptorEndpoint', action:'toggle')}";
      var endpointMakeDefaultEndpoint = "${createLink(controller:'descriptorEndpoint', action:'makeDefault')}";
      var endpointEditEndpoint = "${createLink(controller:'descriptorEndpoint', action:'edit')}";
      var endpointUpdateEndpoint = "${createLink(controller:'descriptorEndpoint', action:'update')}";
      
      var nameIDFormatRemoveEndpoint = "${createLink(controller:'descriptorNameIDFormat', action:'remove', id:serviceProvider.id )}";
      var nameIDFormatListEndpoint = "${createLink(controller:'descriptorNameIDFormat', action:'list', id:serviceProvider.id )}";
      var nameIDFormatAddEndpoint = "${createLink(controller:'descriptorNameIDFormat', action:'add', id:serviceProvider.id )}";
      
      var serviceCategoryListEndpoint = "${createLink(controller:'serviceCategory', action:'list', id:serviceProvider.id )}";
      var serviceCategoryAddEndpoint = "${createLink(controller:'serviceCategory', action:'add', id:serviceProvider.id )}";
      var serviceCategoryRemoveEndpoint = "${createLink(controller:'serviceCategory', action:'remove', id:serviceProvider.id )}";
      
      var attributeRemoveEndpoint = "${createLink(controller:'descriptorAttribute', action:'remove', id:serviceProvider.id )}";
      var attributeListEndpoint = "${createLink(controller:'descriptorAttribute', action:'list', id:serviceProvider.id )}";
      var attributeAddEndpoint = "${createLink(controller:'descriptorAttribute', action:'add', id:serviceProvider.id )}";
      
      var descriptorFullAdministratorGrantEndpoint = "${createLink(controller:'descriptorAdministration', action:'grantFullAdministration', id:serviceProvider.id)}";
      var descriptorFullAdministratorRevokeEndpoint = "${createLink(controller:'descriptorAdministration', action:'revokeFullAdministration', id:serviceProvider.id)}";
      var descriptorFullAdministratorListEndpoint = "${createLink(controller:'descriptorAdministration', action:'listFullAdministration', id:serviceProvider.id)}";
      var descriptorFullAdministratorSearchEndpoint = "${createLink(controller:'descriptorAdministration', action:'searchFullAdministration', id:serviceProvider.id)}";
      
      var monitorDeleteEndpoint = "${createLink(controller:'roleDescriptorMonitor', action:'delete')}";
      var monitorListEndpoint = "${createLink(controller:'roleDescriptorMonitor', action:'list', id:serviceProvider.id )}";
      var monitorCreateEndpoint = "${createLink(controller:'roleDescriptorMonitor', action:'create', id:serviceProvider.id )}";
      
      var descriptorMetadataEndpoint = "${createLink(controller:'metadata', action:'entity', id:serviceProvider.entityDescriptor.id )}";

      var spReportsSessionsEndpoint = "${createLink(controller:'spReports', action:'sessionsjson', id:serviceProvider.id)}"
      var spReportsLoginsEndpoint = "${createLink(controller:'spReports', action:'loginsjson', id:serviceProvider.id)}"
      var spReportsTotalsEndpoint = "${createLink(controller:'spReports', action:'totalsjson', id:serviceProvider.id)}"
      var spReportsConnectivityEndpoint = "${createLink(controller:'spReports', action:'connectivityjson', id:serviceProvider.id)}"
      
      $(function() {
        fedreg.descriptor_metadata();
      });
    </r:script>
  </head>
  <body>
    
    <h2><g:message code="fedreg.view.members.serviceprovider.show.heading" args="[serviceProvider.displayName]"/></h2>

    <g:if test="${!serviceProvider.functioning()}">
      <div class="alert-message block-message error">
        <p><g:message code="fedreg.view.members.serviceprovider.show.notfunctioning"/></p>
      </div>
    </g:if>
      
    <ul class="tabs">
      <li class="active"><a href="#tab-overview"><g:message code="label.overview" /></a></li>
      <li><a href="#tab-categories"><g:message code="label.categories" /></a></li>
      <li><a href="#tab-contacts"><g:message code="label.contacts" /></a></li>
      <li class="level"><a href="#tab-saml"><g:message code="label.saml" /></a></li>
      
      <fr:hasAnyPermission in='["descriptor:${serviceProvider.id}:reporting:logins" , "federation:reporting"]'>
        <li><a href="#tab-reports" ><g:message code="label.reporting" /></a></li>
      </fr:hasAnyPermission>
      <li><a href="#tab-monitors" class="icon icon_database_key"><g:message code="label.monitoring" /></a></li>
      
      <g:if test="${serviceProvider.approved}">
        <li><a href="#tab-admins" class="icon icon_database_key"><g:message code="label.administrators" /></a></li>
      </g:if>
    </ul>

    <div class="tab-content">
      
      <div id="tab-overview" class="tab-pane active">
        <table class="borderless">
          <tbody>   
            <tr>
              <th><g:message code="label.displayname"/></th>
              <td>${fieldValue(bean: serviceProvider, field: "displayName")}</td>
            </tr>
            <tr>
              <th>
                <g:message code="label.description" />
              </th>
              <td>
                ${fieldValue(bean: serviceProvider, field: "description")}
              </td>
            </tr>
            <tr>
              <th><g:message code="label.organization"/></th>
              <td><g:link controller="organization" action="show" id="${serviceProvider.organization.id}">${fieldValue(bean: serviceProvider, field: "organization.displayName")}</g:link></td>
            </tr>
            <fr:hasPermission target="saml:advanced">
              <tr>
                <th><g:message code="label.entitydescriptor"/></th>
                <td><g:link controller="entityDescriptor" action="show" id="${serviceProvider.entityDescriptor.id}">${fieldValue(bean: serviceProvider, field: "entityDescriptor.entityID")}</g:link></td>
              </tr>
            </fr:hasPermission>
            <fr:lacksPermission target="saml:advanced">
              <tr>
                <th><g:message code="label.entitydescriptor"/></th>
                <td>${fieldValue(bean: serviceProvider, field: "entityDescriptor.entityID")}</td>
              </tr>
            </fr:lacksPermission>
            <tr>
              <th>
                <g:message code="label.serviceurl" />
              </th>
              <td>
                ${fieldValue(bean: serviceProvider, field: "serviceDescription.connectURL")}
              </td>
            </tr>
            <tr>
              <th>
                <g:message code="label.servicelogourl" />
              </th>
              <td>
                ${fieldValue(bean: serviceProvider, field: "serviceDescription.logoURL")}
              </td>
            </tr>
            <tr>
              <th><g:message code="label.protocolsupport"/></th>
              <td>
                <g:each in="${serviceProvider.protocolSupportEnumerations}" status="i" var="pse">
                  ${fieldValue(bean: pse, field: "uri")}<br>
                </g:each>
              </td>
            <g:if test="${serviceProvider.errorURL}">
            <tr>
              <th><g:message code="label.errorurl"/></th>
              <td><a href="${serviceProvider.errorURL}">${fieldValue(bean: serviceProvider, field: "errorURL")}</a></td>
            </tr>
            </g:if>
            <tr>
              <th><g:message code="label.status"/></th>
              <td>
                <g:if test="${serviceProvider.active}">
                  <g:message code="label.active" />
                </g:if>
                <g:else>
                  <span class="more-info not-in-federation" rel="twipsy" data-original-title="${g.message(code:'label.warningmetadata')}" data-placement="right"><g:message code="label.inactive" /></span>
                </g:else>
              </td>
            </tr>
            <tr>
              <th><g:message code="label.archived"/></th>
              <td>
                <g:if test="${serviceProvider.archived}"> 
                  <span class="alert-message warning"><g:message code="label.yes" /></span>
                  <span class="more-info not-in-federation"  rel="twipsy" data-original-title="${g.message(code:'label.warningmetadataarchived')}" data-placement="right"><g:message code="label.no" /></span>
                </g:if>
                <g:else>
                  <g:message code="label.no" /> 
                </g:else>
              </td>
            </tr>
            <tr>
              <th><g:message code="label.approved"/></th>
              <td>
                <g:if test="${serviceProvider.approved}">
                  <g:message code="label.yes" />
                </g:if>
                <g:else>
                  <span class="more-info not-in-federation" rel="twipsy" data-original-title="${g.message(code:'label.undergoingapproval')}" data-placement="right"><g:message code="label.no" /></span>
                </g:else>
              </td>
            </tr>
            <tr>
              <th><g:message code="label.datecreated" /></th>
              <td>${fieldValue(bean: serviceProvider, field: "dateCreated")}</td>
            </tr>
          </tbody>
        </table>
        <fr:hasPermission target="descriptor:${serviceProvider.id}:update">
          <div>
            <g:link action="edit" id="${serviceProvider.id}" class="btn info"><g:message code="label.editsp"/></g:link>
          </div>
        </fr:hasPermission>
      </div>
      
      <div id="tab-categories" class="tab-pane">
        <div id="categories">
          <g:render template="/templates/servicecategories/list" plugin="foundation" model="[descriptor:serviceProvider, categories:serviceProvider.serviceCategories, containerID:'categories']" />
        </div>

        <g:render template="/templates/servicecategories/add" plugin="foundation" model="[descriptor:serviceProvider]"/>
      </div>

      <div id="tab-contacts" class="tab-pane">
        <div id="contacts">
          <g:render template="/templates/contacts/list" plugin="foundation" model="[host:serviceProvider]" />
        </div>

        <g:render template="/templates/contacts/create" plugin="foundation" model="[host:serviceProvider, contactTypes:contactTypes]" />
      </div>

      <div id="tab-saml" class="tab-pane">
        <div class="row">
          <div class="span14 offset1">
            <ul class="tabs">
              <li class="active"><a href="#tab-crypto"><g:message code="label.crypto" /></a></li>
              <li><a href="#tab-endpoints"><g:message code="label.endpoints" /></a></li>
              <li><a href="#tab-attributes"><g:message code="label.attributeconsumingservices" /></a></li>
              <li><a href="#tab-nameidformats"><g:message code="label.supportednameidformats" /></a></li>
              <li><a href="#tab-metadata" ><g:message code="label.metadata" /></a></li>
            </ul>

            <div class="tab-content">
              <div id="tab-crypto" class="tab-pane">
                <div id="certificates">
                  <g:render template="/templates/certificates/list" plugin="foundation" model="[descriptor:serviceProvider, allowremove:true]" />
                </div>
                <g:render template="/templates/certificates/create" plugin="foundation" model="[descriptor:serviceProvider]"/>
              </div>

              <div id="tab-endpoints" class="tab-pane">
                <ul class="tabs">
                  <li class="active"><a href="#tab-acs">Assertion</a></li>
                  <li><a href="#tab-ars">Artifact</a></li>
                  <li><a href="#tab-slo">Logout</a></li>
                  <li><a href="#tab-drs">Discovery</a></li>
                  <li><a href="#tab-nim">Name ID</a></li>
                </ul>

                <div class="tab-content">
                  <div id="tab-acs" class="tab-pane">
                    <div id="assertionconsumerendpoints">
                      <g:render template="/templates/endpoints/list" plugin="foundation" model="[endpoints:serviceProvider.assertionConsumerServices, allowremove:true, endpointType:'assertionConsumerServices', containerID:'assertionconsumerendpoints']" />
                    </div> 
                    <g:render template="/templates/endpoints/create" plugin="foundation" model="[descriptor:serviceProvider, endpointType:'assertionConsumerServices', containerID:'assertionconsumerendpoints', indexed:true]" />
                  </div>

                  <div id="tab-ars" class="tab-pane">
                    <div id="artifactendpoints">
                      <g:render template="/templates/endpoints/list" plugin="foundation" model="[endpoints:serviceProvider.artifactResolutionServices, allowremove:true, endpointType:'artifactResolutionServices', containerID:'artifactendpoints']" />
                    </div>
                    <g:render template="/templates/endpoints/create" plugin="foundation" model="[descriptor:serviceProvider, endpointType:'artifactResolutionServices', containerID:'artifactendpoints', indexed:true]" />
                  </div>

                  <div id="tab-slo" class="tab-pane">
                    <div id="singlelogoutendpoints">
                      <g:render template="/templates/endpoints/list" plugin="foundation" model="[endpoints:serviceProvider.singleLogoutServices, allowremove:true, endpointType:'singleLogoutServices', containerID:'singlelogoutendpoints']" />
                    </div>
                    <g:render template="/templates/endpoints/create" plugin="foundation" model="[descriptor:serviceProvider, endpointType:'singleLogoutServices', containerID:'singlelogoutendpoints']" />
                  </div>

                  <div id="tab-drs" class="tab-pane">
                    <div id="discoveryresponseservices">
                      <g:render template="/templates/endpoints/list" plugin="foundation" model="[endpoints:serviceProvider.discoveryResponseServices, allowremove:true, endpointType:'discoveryResponseServices', containerID:'discoveryresponseservices']" />
                    </div>
                    <g:render template="/templates/endpoints/create" plugin="foundation" model="[descriptor:serviceProvider, endpointType:'discoveryResponseServices', containerID:'discoveryresponseservices', indexed:true]" />
                  </div>

                  <div id="tab-nim" class="tab-pane">
                    <div id="managenameidservices">
                      <g:render template="/templates/endpoints/list" plugin="foundation" model="[endpoints:serviceProvider.manageNameIDServices, allowremove:true, endpointType:'manageNameIDServices', containerID:'managenameidservices']" />
                    </div>
                    <g:render template="/templates/endpoints/create" plugin="foundation" model="[descriptor:serviceProvider, endpointType:'manageNameIDServices', containerID:'managenameidservices']" />
                  </div>

                  <g:render template="/templates/endpoints/modals" plugin="foundation" />
                </div>
              </div>

              <div id="tab-attributes" class="tab-pane">
                <g:render template="/templates/acs/list" plugin="foundation" model="[attributeConsumingServices:serviceProvider.attributeConsumingServices]"/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
  </body>
</html>
