<fr:hasPermission target="descriptor:${descriptor.id}:endpoint:create">

  <%@page import="aaf.fr.foundation.SamlURI" %>
  <%@page import="aaf.fr.foundation.SamlURIType" %>

  <div id="add${endpointType}" class="actions">
    <a onclick="$('#add${endpointType}').fadeOut(); $('#new${endpointType}').fadeIn();" class="btn"><g:message code="label.addendpoint"/></a>
  </div>

  <div id="new${endpointType}" class="actions hidden">
    <h4><g:message code="fedreg.templates.endpoints.add.heading"/></h4>
    <form id="new${endpointType}data" class="validating span8">
      <fieldset>
        <input type="hidden" name="endpointType" value="${endpointType}">

        <div class="clearfix">
          <label for="binding"><g:message code="label.binding"/></label>
          <div class="input">
            <g:select name="binding" from="${SamlURI.findAllWhere(type:SamlURIType.ProtocolBinding)}" optionKey="id" optionValue="uri"/>
            <fr:tooltip code='fedreg.help.endpoint.binding' />
          </div>
        </div>

        <div class="clearfix">
          <label for="location"><g:message code="label.location"/></label>
          <div class="input">
            <input name="location" type="text" class="required url" size="60"/>
            <fr:tooltip code='fedreg.help.endpoint.location' />
          </div>
        </div>

        <g:if test="${resloc}">
          <div class="clearfix">
            <label for="responselocation"><g:message code="label.responselocation"/></label>
            <div class="input">
              <input name="responselocation" type="text" class="easyinput" size="60"/>
            </div>
          </div>
        </g:if>

        <g:if test="${indexed}">
          <div class="clearfix">
            <label for="index"><g:message code="label.index" /></label>
            <div class="input">
              <input name="samlindex" type="text" class="required number" size="2" />
              <fr:tooltip code='fedreg.help.endpoint.index' />
            </div>
          </div>
        </g:if>

        <div class="clearfix">
          <label for="active"><g:message code="label.active"/></label>
          <div class="input">
            <g:checkBox name="active" value="true" />
            <fr:tooltip code='fedreg.help.endpoint.active' />
          </div>
        </div>

        <div class="input">
          <a class="create-endpoint btn success" data-type="${endpointType}" data-container="${containerID}"><g:message code="label.add"/></a>
          <a onclick="$('#new${endpointType}').fadeOut(); $('#add${endpointType}').fadeIn();" class="btn"><g:message code="label.cancel"/></a>
        </div>

      </fieldset>
    </form>
  </div>

</fr:hasPermission>