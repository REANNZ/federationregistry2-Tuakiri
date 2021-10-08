<%@page import="aaf.fr.foundation.AttributeBase" %>
<fr:hasPermission target="federation:management:descriptor:${descriptor.id}:attribute:add">
  <hr>

  <div id="add-attribute" class="searcharea">
    <a class="show-add-attribute btn"><g:message encodeAs="HTML" code="label.addattribute"/></a>
  </div>
  
  <div id="new-attribute" class="revealable">
    <h3><g:message encodeAs="HTML" code="templates.fr.attributes.add.heading"/></h3>

    <form id="newattributedata" class="form-horizontal" onsubmit="return false;">
      <div class="control-group">
        <label class="control-label" for="attributeID"><g:message encodeAs="HTML" code="label.attribute"/></label>
        <div class="controls">
          <fr:attributeSelection />
        </div>
      </div>

      <div class="form-actions">
        <a class="add-attribute btn btn-success"><g:message encodeAs="HTML" code="label.add"/></a>
        <a class="cancel-add-attribute btn"><g:message encodeAs="HTML" code="label.cancel"/></a>
      </div>
    </form>
  </div>
</fr:hasPermission>

<div id="delete-attribute-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message encodeAs="HTML" code="templates.fr.attributes.remove.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message encodeAs="HTML" code="templates.fr.attributes.remove.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="close-modal btn"><g:message encodeAs="HTML" code="label.cancel" /></a>
    <a class="delete-attribute btn btn-danger"/><g:message encodeAs="HTML" code="label.remove" /></a>
  </div>
</div>
