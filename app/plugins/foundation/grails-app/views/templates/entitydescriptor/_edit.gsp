<div id="editor-entitydescriptor" class="revealable">

  <g:form action="update" id="${entity.id}" method="PUT" class="form-horizontal validating">
    <fieldset>

      <div class="control-group">
        <label for="entity.status"><g:message code="label.status" /></label>
        <div class="controls">
          <g:radioGroup name="entity.active" values="['true', 'false']" labels="['label.active', 'label.inactive']" value="${entity.active}">
             ${it.radio} <g:message code="${it.label}" />
          </g:radioGroup>
          <fr:tooltip code='fedreg.help.entity.status' />
        </div>
      </div>
    
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
        <button type="submit" class="edit-entitydescriptor btn btn-success"><g:message code="label.save"/></button>
        <a class="cancel-edit-entitydescriptor btn"><g:message code="label.cancel"/></a>
      </div>

    </fieldset>
  </g:form>

</div>

<div id="editor-entitydescriptor-migrateorg" class="revealable">

  <p class="alert alert-info"><g:message code="fedreg.templates.entitydescriptor.overview.migrateorganization" /></p>

  <g:form action="migrate" id="${entity.id}" method="PUT" class="form-horizontal validating">
    <fieldset>
      <div class="control-group">
        <label><g:message code="label.currentorganisation"/></label>
        <div class="controls">
          <g:link controller="organization" action="show" id="${entity.organization?.id}">${fieldValue(bean: entity, field: "organization.displayName")}</g:link>
        </div>
      </div>

      <div class="control-group">
        <label><g:message code="label.neworganisation"/></label>
        <div class="controls">
          <g:select name="newOrgId" from="${organizations.sort{it.displayName}}" optionKey="id" optionValue="displayName" />
        </div>
      </div>



      <div class="form-actions">
        <button type="submit" class="edit-entitydescriptor btn btn-success"><g:message code="label.save"/></button>
        <a class="cancel-entitydescriptor-migrateorg btn"><g:message code="label.cancel"/></a>
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
      <input name="_method" type="hidden" value="put" />
      <button type="submit" class="btn btn-warning"><g:message code="label.archive" /></a>
    </g:form>
  </div>
</div>

<div id="unarchive-entitydescriptor-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message code="fedreg.templates.entitydescriptor.unarchive.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message code="fedreg.templates.entitydescriptor.unarchive.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message code="label.cancel" /></a>

    <g:form controller="entityDescriptor" action="unarchive" id="${entity.id}" style="padding: 0px;">
      <input name="_method" type="hidden" value="put" />
      <button type="submit" class="btn btn-warning"><g:message code="label.unarchive" /></a>
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