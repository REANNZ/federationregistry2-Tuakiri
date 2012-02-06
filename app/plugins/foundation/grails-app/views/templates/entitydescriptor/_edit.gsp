<div id="editor-entitydescriptor" class="revealable">

  <g:form action="update" id="${entity.id}" method="PUT" class="form-horizontal validating">
    <fieldset>
    
      <div class="control-group">
        <label for="entity.identifier"><g:message code="label.entitydescriptor" /></label>
        <div class="controls">
          <g:textField name="entity.identifier" value="${entity.entityID}" class="required span4"/>
        </div>
      </div>

      <div class="control-group">
        <label for="entity.extensions"><g:message code="label.extensions" /></label>
        <div class="controls">
          <g:textArea name="entity.extensions" value="${entity.extensions}" class="span4"/>
        </div>
      </div>

      <div class="form-actions">
        <a class="edit-entitydescriptor btn btn-success"><g:message code="label.save"/></a>
        <a class="cancel-edit-entitydescriptor btn"><g:message code="label.cancel"/></a>
      </div>

    </fieldset>
  </g:form>

</div>

<div id="archive-entitydescriptor-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message code="fedreg.templates.entitydescriptor.archive.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message code="fedreg.templates.entitydescriptor.archive.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message code="label.cancel" /></a>

    <g:form controller="entityDescriptor" action="archive" id="${entity.id}" style="padding: 0px;">
      <input name="_method" type="hidden" value="delete" />
      <button type="submit" class="btn btn-danger"><g:message code="label.archive" /></a>
    </g:form>
  </div>
</div>

<div id="delete-entitydescriptor-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message code="fedreg.templates.entitydescriptor.delete.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message code="fedreg.templates.entitydescriptor.delete.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message code="label.cancel" /></a>
    <g:form controller="entityDescriptor" action="delete" id="${entity.id}" style="padding: 0px;">
      <input name="_method" type="hidden" value="delete" />
      <button type="submit" class="btn btn-danger"><g:message code="label.delete" /></a>
    </g:form>
  </div>
</div>