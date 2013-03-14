<%@page import="aaf.fr.foundation.AttributeBase" %>
<g:if test="${attributeConsumingServices}">
  <g:each in="${attributeConsumingServices}" var="acs">

    <div id="acsreqattr${acs.id}">
      <g:render template="/templates/acs/listrequestedattributes" plugin="foundation" model='[acs:acs, requestedAttributes:acs.sortedAttributes(), specificationAttributes:specificationAttributes]' />
    </div>

    

    <fr:hasPermission target="federation:management:descriptor:${acs.descriptor.id}:attribute:add">
      <hr>
      
      <div id="addattribute${acs.id}">
        <a class="show-create-ra btn" data-acsid="${acs.id}"><g:message encodeAs="HTML" code="label.addattribute"/></a>
      </div>

      <div id="newattribute${acs.id}" class="revealable">
        <h3><g:message encodeAs="HTML" code="templates.fr.acs.reqattributes.add.heading"/></h3>
        <p>
          <g:message encodeAs="HTML" code="templates.fr.acs.reqattributes.add.details"/>
        </p>
        <form id="newattributedata${acs.id}" class="form-horizontal validating">
          <fieldset>
            <div class="control-group">
              <label class="control-label" for="attrid"><g:message encodeAs="HTML" code="label.attribute"/></label>
              <div class="controls">
                <fr:attributeSelection />
              </div>
            </div>

            <div class="control-group">
              <label class="control-label" for="reasoning"><g:message encodeAs="HTML" code="label.reason"/></label>
              <div class="controls">
                <input name="reasoning" type="text" class="required span4"/>
                <fr:tooltip code='help.fr.acs.reason' />
              </div>
            </div>

            <div class="control-group">
              <label class="control-label" for="isrequired"><g:message encodeAs="HTML" code="label.required"/></label>
              <div class="controls">
                <g:checkBox name="isrequired" />
                <fr:tooltip code='help.fr.acs.isrequired' />
              </div>
            </div>

            <div class="form-actions">
              <a class="create-ra btn btn-success" data-acsid="${acs.id}"><g:message encodeAs="HTML" code="label.add"/></a>
              <a class="cancel-create-ra btn" data-acsid="${acs.id}"><g:message encodeAs="HTML" code="label.close"/></a>
            </div>
          </fieldset>
        </form>
      </div>
    </fr:hasPermission>
  </g:each>
</g:if>

<div id="delete-ra-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message encodeAs="HTML" code="templates.fr.acs.reqattributes.remove.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message encodeAs="HTML" code="templates.fr.acs.reqattributes.remove.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="close-modal btn"><g:message encodeAs="HTML" code="label.cancel" /></a>
    <a class="delete-ra btn btn-danger"/><g:message encodeAs="HTML" code="label.remove" /></a>
  </div>
</div>

<div id="delete-ra-value-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message encodeAs="HTML" code="templates.fr.acs.reqattributes.remove.value.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message encodeAs="HTML" code="templates.fr.acs.reqattributes.remove.value.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="close-modal btn"><g:message encodeAs="HTML" code="label.cancel" /></a>
    <a class="delete-ra-value btn btn-danger"/><g:message encodeAs="HTML" code="label.remove" /></a>
  </div>
</div>
