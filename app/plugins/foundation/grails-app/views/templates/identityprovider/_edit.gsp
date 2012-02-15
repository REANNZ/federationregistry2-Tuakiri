<div id="editor-identityprovider" class="revealable">
  <g:form action="update" id="${identityProvider.id}" method="PUT" class="form-horizontal validating">

    <fieldset>
      <div class="control-group">
        <label for="idp.status"><g:message code="label.status" /></label>
        <div class="controls">
          <g:radioGroup name="idp.status" values="['true', 'false']" labels="['label.active', 'label.inactive']" value="${identityProvider.active}" >
             ${it.radio} <g:message code="${it.label}" />
          </g:radioGroup>
          <fr:tooltip code='fedreg.help.identityprovider.status' />
        </div>
      </div>
      
      <div class="control-group">
        <label for="idp.displayName"><g:message code="label.displayname" /></label>
        <div class="controls">
          <g:textField name="idp.displayName" value="${identityProvider.displayName}" class="required span4" minlength="3" maxlength="255" />
          <fr:tooltip code='fedreg.help.identityprovider.displayname' />
        </div>
      </div>

      <div class="control-group">
        <label for="idp.description"><g:message code="label.description" /></label>
        <div class="controls">
          <g:textArea name="idp.description"  value="${identityProvider.description}" class="required span4" minlength="4" rows="8" cols="36" maxlength="2000"/>
          <fr:tooltip code='fedreg.help.identityprovider.description' />
        </div>
      </div>

      <div class="control-group">
        <label for="idp.autoAcceptServices"><g:message code="label.usesaa" /></label>
        <div class="controls">
          <g:radioGroup name="idp.autoacceptservices" values="['true', 'false']" labels="['label.yes', 'label.no']" value="${identityProvider.autoAcceptServices}" >
             ${it.radio} <g:message code="${it.label}" />
          </g:radioGroup>
          <fr:tooltip code='fedreg.help.identityprovider.usesaa' />
        </div>
      </div>

      <div class="control-group">
        <label for="idp.scope"><g:message code="label.scope" /></label>
        <div class="controls">
          <g:textField name="idp.scope"  value="${identityProvider.scope}" class="required span4" size="50" class="required" minlength="4" maxlength="255"/>
          <fr:tooltip code='fedreg.help.identityprovider.scope' />
        </div>
      </div>


      <div class="form-actions">
        <a class="edit-identityprovider btn btn-success"><g:message code="label.save"/></a>
        <a class="cancel-edit-identityprovider btn"><g:message code="label.cancel"/></a>
      </div>

    </fieldset>
  </g:form>
</div>