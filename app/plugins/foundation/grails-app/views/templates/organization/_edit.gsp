<div id="editor-organization" class="revealable">
  <g:form action="update" id="${organization.id}" method="PUT" class="form-horizontal validating">
    <g:hiddenField name="organization.lang" value="en" />
    
    <fieldset>
      <div class="control-group">
        <label for="organization.status"><g:message code="label.status" /></label>
        <div class="controls">
          <g:radioGroup name="organization.active" values="['true', 'false']" labels="['label.active', 'label.inactive']" value="${organization.active}">
             ${it.radio} <g:message code="${it.label}" />
          </g:radioGroup>
          <fr:tooltip code='fedreg.help.organization.status' />
        </div>
      </div>

      <div class="control-group">
        <label for="organization.name"><g:message code="label.name" /></label>
        <div class="controls">
          <g:textField name="organization.name"  value="${organization.name}" class="required span4" minlength="4" maxlength="255"/>
          <fr:tooltip code='fedreg.help.organization.name' />
        </div>
      </div>

      <div class="control-group">
        <label for="organization.displayName"><g:message code="label.displayname" /></label>
        <div class="controls">
          <g:textField name="organization.displayName"  value="${organization.displayName}" class="required span4" minlength="4" maxlength="255" />
          <fr:tooltip code='fedreg.help.organization.displayName' />
        </div>
      </div>

      <div class="control-group">
        <label for="organization.url"><g:message code="label.url" /></label>
        <div class="controls">
          <g:textField name="organization.url"  class="required url span4" minlength="4" maxlength="255" value="${organization.url}"/>
          <fr:tooltip code='fedreg.help.organization.url' />
        </div>
      </div>

      <div class="control-group">
        <label for="organization.primary"><g:message code="label.organizationtype" /></label>
        <div class="controls">
          <g:select name="organization.primary" from="${organizationTypes}" optionKey="id" optionValue="displayName" value="${organization.primary.id}"/>
          <fr:tooltip code='fedreg.help.organization.edit.type' />
        </div>
      </div>

      <div class="control-group">
        <label for="organization.types"><g:message code="label.organizationsecondarytypes" /></label>
        <div class="controls">
          <ul class="clean">
            <g:each in="${organizationTypes}" var="t">
              <g:if test="${t.name != organization.primary.name}">
                <li><g:checkBox name="organization.types.${t.id}" value="on" checked="${organization.types.contains(t)}"/> ${fieldValue(bean: t, field: "displayName")}</li>
              </g:if>
            </g:each>
          </ul>
        </div>
      </div>
      
      <div class="form-actions">
        <button type="submit" class="edit-organization btn btn-success"><g:message code="label.save"/></button>
        <a class="cancel-edit-organization btn"><g:message code="label.cancel"/></a>
      </div>

    </fieldset>
  </g:form>
</div>

<div id="unarchive-organization-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message code="fedreg.templates.organization.unarchive.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message code="fedreg.templates.organization.unarchive.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message code="label.cancel" /></a>

    <g:form controller="organization" action="unarchive" id="${organization.id}" style="padding: 0px;">
      <input name="_method" type="hidden" value="post" />
      <button type="submit" class="btn btn-warning"><g:message code="label.unarchive" /></a>
    </g:form>
  </div>
</div>

<div id="archive-organization-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message code="fedreg.templates.organization.archive.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message code="fedreg.templates.organization.archive.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message code="label.cancel" /></a>

    <g:form controller="organization" action="archive" id="${organization.id}" style="padding: 0px;">
      <input name="_method" type="hidden" value="post" />
      <button type="submit" class="btn btn-warning"><g:message code="label.archive" /></a>
    </g:form>
  </div>
</div>

<div id="delete-organization-modal" class="modal hide fade">
  <div class="modal-header">
    <a class="close close-modal">&times;</a>
    <h3><g:message code="fedreg.templates.organization.delete.confirm.title"/></h3>
  </div>
  <div class="modal-body">
    <p><g:message code="fedreg.templates.organization.delete.confirm.descriptive"/></p>
  </div>
  <div class="modal-footer">
    <a class="btn close-modal"><g:message code="label.cancel" /></a>
    <g:form controller="organization" action="delete" id="${organization.id}" style="padding: 0px;">
      <input name="_method" type="hidden" value="delete" />
      <button type="submit" class="btn btn-danger"><g:message code="label.delete" /></a>
    </g:form>
  </div>
</div>