<%@page import="aaf.fr.foundation.SamlURI" %>
<%@page import="aaf.fr.foundation.SamlURIType" %>

<div id="endpoint-${endpoint.id}-editor">
  <h5><g:message encodeAs="HTML" code="label.editingendpoint"/> ${endpoint.id}</h5>
  <form id="endpoint-edit-${endpoint.id}">
    <fieldset>
      <g:hiddenField name="id" value="${endpoint.id}" />
      
      <div class="control-group">
        <label class="control-label" for="uri"><g:message encodeAs="HTML" code="label.binding"/></label>
        <div class="controls">
          <g:select name="binding" from="${SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding)}" optionKey="id" optionValue="uri" value="${endpoint.binding.id}" class="span4"/>
          <fr:tooltip code='help.fr.endpoint.binding' />
        </div>
      </div>

      <div class="control-group">
        <label class="control-label" for="location"><g:message encodeAs="HTML" code="label.location"/></label>
        <div class="controls">
          <input name="location" type="text" class="required url span4" size="60" value="${endpoint.location}"/>
          <fr:tooltip code='help.fr.endpoint.location' />
        </div>
      </div>

      <g:if test="${endpoint.instanceOf(aaf.fr.foundation.IndexedEndpoint)}">
        <div class="control-group">
          <label class="control-label" for="samlindex"><g:message encodeAs="HTML" code="label.index" /></label>
          <div class="controls">
            <input name="samlindex" type="text" class="required number span1" value="${endpoint.index}"/>
            <fr:tooltip code='help.fr.endpoint.index' />
          </div>
        </div>
      </g:if>

      <div class="form-actions">
        <a class="update-endpoint btn btn-success" data-id="${endpoint.id}" data-type="${endpointType}"><g:message encodeAs="HTML" code="label.update"/></a>
        <a class="cancel-edit-endpoint btn"><g:message encodeAs="HTML" code="label.cancel"/></a>
      </div>
    </fieldset>
  </form>
</div>
