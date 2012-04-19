<%@page import="aaf.fr.foundation.SamlURI" %>
<%@page import="aaf.fr.foundation.SamlURIType" %>

<fr:hasPermission target="descriptor:${descriptor.id}:endpoint:create">
  <hr>

  <div id="add-${endpointType}">
    <a class="show-create-endpoint btn" data-type="${endpointType}"><g:message code="label.addendpoint"/></a>
  </div>

  <div id="new-${endpointType}" class="revealable">
    <h4><g:message code="template.fr.endpoints.add.heading"/></h4>
    <form id="new${endpointType}data" class="form-horizontal validating">
      <fieldset>
        <input type="hidden"
 name="endpointType" value="${endpointType}">

        <div class="control-group">
          <label for="binding"><g:message code="label.binding"/></label>
          <div class="controls">
            <g:select name="binding" from="${SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding)}" optionKey="id" optionValue="uri" class="span4"/>
            <fr:tooltip code='help.fr.endpoint.binding' />
          </div>
        </div>

        <div class="control-group">
          <label for="location"><g:message code="label.location"/></label>
          <div class="controls">
            <input name="location" type="text" class="required url span4"/>
            <fr:tooltip code='help.fr.endpoint.location' />
          </div>
        </div>

        <g:if test="${resloc}">
          <div class="control-group">
            <label for="responselocation"><g:message code="label.responselocation"/></label>
            <div class="controls">
              <input name="responselocation" type="text" class="span4"/>
            </div>
          </div>
        </g:if>

        <g:if test="${indexed}">
          <div class="control-group">
            <label for="index"><g:message code="label.index" /></label>
            <div class="controls">
              <input name="samlindex" type="text" class="required number span1" />
              <fr:tooltip code='help.fr.endpoint.index' />
            </div>
          </div>
        </g:if>

        <div class="control-group">
          <label for="active"><g:message code="label.active"/></label>
          <div class="controls">
            <g:checkBox name="active" value="true" />
            <fr:tooltip code='help.fr.endpoint.active' />
          </div>
        </div>

        <div class="form-actions">
          <a class="create-endpoint btn btn-success" data-type="${endpointType}"><g:message code="label.add"/></a>
          <a class="cancel-create-endpoint btn" data-type="${endpointType}"><g:message code="label.cancel"/></a>
        </div>

      </fieldset>
    </form>
  </div>

</fr:hasPermission>