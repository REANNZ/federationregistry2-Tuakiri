<%@page import="aaf.fr.foundation.SamlURI" %>
<%@page import="aaf.fr.foundation.SamlURIType" %>

<div id="endpoint-${endpoint.id}-editor" class="span12">
  <h5><g:message code="label.editingendpoint"/> ${endpoint.id}</h5>
  <form id="endpoint-edit-${endpoint.id}">
    <fieldset>
      <g:hiddenField name="id" value="${endpoint.id}" />
      
      <div class="clearfix">
        <label for="uri"><g:message code="label.binding"/></label>
        <div class="input">
          <g:select name="binding" from="${SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding)}" optionKey="id" optionValue="uri" value="${endpoint.binding.id}"/>
          <fr:tooltip code='fedreg.help.endpoint.binding' />
        </div>
      </div>

      <div class="clearfix">
        <label for="location"><g:message code="label.location"/></label>
        <div class="input">
          <input name="location" type="text" class="required url" size="60" value="${endpoint.location}"/>
          <fr:tooltip code='fedreg.help.endpoint.location' />
        </div>
      </div>

      <g:if test="${endpoint.instanceOf(aaf.fr.foundation.IndexedEndpoint)}">
        <div class="clearfix">
          <label for="samlindex"><g:message code="label.index" /></label>
          <div class="input">
            <input name="samlindex" type="text" class="required number" size="2" value="${endpoint.index}"/>
            <fr:tooltip code='fedreg.help.endpoint.index' />
          </div>
        </div>
      </g:if>

      <div class="input">
        <a class="update-endpoint btn success" data-id="${endpoint.id}" data-type="${endpointType}"><g:message code="label.update"/></a>
        <a class="cancel-edit-endpoint btn"><g:message code="label.cancel"/></a>
      </div>
    </fieldset>
  </form>
</div>