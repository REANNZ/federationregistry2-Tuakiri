<fr:hasPermission target="descriptor:${descriptor.id}:attribute:add">
  <%@page import="aaf.fr.foundation.AttributeBase" %>

  <div id="add-attribute" class="searcharea">
    <a class="show-add-attribute btn btn-info"><g:message code="label.addattribute"/></a>
  </div>
  
  <div id="new-attribute" class="revealable">
    <h3><g:message code="fedreg.templates.attributes.add.heading"/></h3>

    <form id="newattributedata">
      <div class="control-group">
        <label for="attributeID"><g:message code="label.attribute"/></label>
        <div class="controls">
          <g:select name="attributeID" from="${AttributeBase.list().sort{it.name}}" optionKey="id" optionValue="${{ it.name + ' ( oid:' + it.oid + ' )' }}" />
        </div>
      </div>
      <div class="controls">
        <a class="add-attribute btn btn-success"><g:message code="label.add"/></a>
        <a class="cancel-add-attribute btn"><g:message code="label.cancel"/></a>
      </div>
    </form>
  </div>
</fr:hasPermission>

<div id="delete-attribute-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message code="fedreg.templates.attributes.remove.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message code="fedreg.templates.attributes.remove.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="close-modal btn"><g:message code="label.cancel" /></a>
    <a class="delete-attribute btn btn-danger"/><g:message code="label.remove" /></a>
  </div>
</div>