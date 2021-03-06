<div id="editor-organization" class="revealable">
  <g:form action="update" id="${organization.id}" method="PUT" class="form-horizontal validating">
    <g:hiddenField name="organization.lang" value="en" />
    
    <fieldset>
      <div class="control-group">
        <label class="control-label" for="organization.status"><g:message encodeAs="HTML" code="label.status" /></label>
        <div class="controls">
          <input type="radio" name="organization.active" value="true"  ${organization.active ? 'checked' : ''}/>
          <g:message encodeAs="HTML" code="label.active"/>
        
          <input type="radio" name="organization.active" alue="false" ${organization.active ? '' : 'checked'}/>
          <g:message encodeAs="HTML" code="label.inactive"/>
          <fr:tooltip code='help.fr.organization.status' />
        </div>
      </div>

      <div class="control-group">
        <label class="control-label" for="organization.name"><g:message encodeAs="HTML" code="templates.fr.organization.label.name" /></label>
        <div class="controls">
          <g:textField name="organization.name"  value="${organization.name}" class="required span4" minlength="4" maxlength="255"/>
          <fr:tooltip code='help.fr.organization.name' />
        </div>
      </div>

      <div class="control-group">
        <label class="control-label" for="organization.displayName"><g:message encodeAs="HTML" code="label.displayname" /></label>
        <div class="controls">
          <g:textField name="organization.displayName"  value="${organization.displayName}" class="required span4" minlength="4" maxlength="255" />
          <fr:tooltip code='help.fr.organization.displayName' />
        </div>
      </div>

      <div class="control-group">
        <label class="control-label" for="organization.url"><g:message encodeAs="HTML" code="label.url" /></label>
        <div class="controls">
          <g:textField name="organization.url"  class="required url span4" minlength="4" maxlength="255" value="${organization.url}"/>
          <fr:tooltip code='help.fr.organization.url' />
        </div>
      </div>

      <div class="control-group">
        <label class="control-label" for="organization.primary"><g:message encodeAs="HTML" code="label.organizationtype" /></label>
        <div class="controls">
          <g:select name="organization.primary" from="${organizationTypes.findAll{it.discoveryServiceCategory == true}.sort{it.displayName}}" optionKey="id" optionValue="${{ it.displayName?.encodeAsHTML() }}" value="${organization.primary.id}"/>
          <fr:tooltip code='help.fr.organization.edit.type' />
        </div>
      </div>

      <div class="control-group">
        <label class="control-label" for="organization.types"><g:message encodeAs="HTML" code="label.organizationsecondarytypes" /></label>
        <div class="controls">
          <ul class="clean">
            <g:each in="${organizationTypes.findAll{it.discoveryServiceCategory == true}.sort{it.displayName}}" var="t">
              <g:if test="${t.name != organization.primary.name}">
                <li><g:checkBox name="organization.types.${t.id}" value="on" checked="${organization.types.contains(t)}"/> ${fieldValue(bean: t, field: "displayName")}</li>
              </g:if>
            </g:each>
          </ul>
        </div>
      </div>
      
      <div class="form-actions">
        <button type="submit" class="edit-organization btn btn-success"><g:message encodeAs="HTML" code="label.save"/></button>
        <a class="cancel-edit-organization btn"><g:message encodeAs="HTML" code="label.cancel"/></a>
      </div>

    </fieldset>
  </g:form>
</div>

<div id="unarchive-organization-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message encodeAs="HTML" code="templates.fr.organization.unarchive.confirm.title" /></h3>
  </div>
  <div class="modal-body">
    <p><g:message encodeAs="HTML" code="templates.fr.organization.unarchive.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message encodeAs="HTML" code="label.cancel" /></a>

    <g:form controller="organization" action="unarchive" id="${organization.id}" style="padding: 0px;">
      <input name="_method" type="hidden" value="put" />
      <button type="submit" class="btn btn-warning"><g:message encodeAs="HTML" code="label.unarchive" /></button>
    </g:form>
  </div>
</div>

<div id="archive-organization-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message encodeAs="HTML" code="templates.fr.organization.archive.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message encodeAs="HTML" code="templates.fr.organization.archive.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message encodeAs="HTML" code="label.cancel" /></a>

    <g:form controller="organization" action="archive" id="${organization.id}" style="padding: 0px;">
      <input name="_method" type="hidden" value="put" />
      <button type="submit" class="btn btn-warning"><g:message encodeAs="HTML" code="label.archive" /></button>
    </g:form>
  </div>
</div>

<div id="delete-organization-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message encodeAs="HTML" code="templates.fr.organization.delete.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message encodeAs="HTML" code="templates.fr.organization.delete.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message encodeAs="HTML" code="label.cancel" /></a>
    <g:form controller="organization" action="delete" id="${organization.id}" style="padding: 0px;">
      <input name="_method" type="hidden" value="delete" />
      <button type="submit" class="btn btn-danger"><g:message encodeAs="HTML" code="label.delete" /></button>
    </g:form>
  </div>
</div>
