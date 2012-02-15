<div id="editor-serviceprovider" class="revealable">
  <g:form action="update" id="${serviceProvider.id}" method="PUT" class="form-horizontal validating">

    <fieldset>
      <div class="control-group">
        <label for="sp.status"><g:message code="label.status" /></label>
        <div class="controls">
          <g:radioGroup name="sp.status" values="['true', 'false']" labels="['label.active', 'label.inactive']" value="${serviceProvider.active}">
             ${it.radio} &nbsp;&nbsp; <g:message code="${it.label}" /><br>
          </g:radioGroup>
        </div>
      </div>

      <div class="control-group">
        <label for="sp.displayName"><g:message code="label.displayname" /></label>
        <div class="controls">
          <g:textField name="sp.displayName"  value="${serviceProvider.displayName}" class="required span4" minlength="4" maxlength="255" />
          <fr:tooltip code='fedreg.help.serviceprovider.displayname' />
        </div>
      </div>
      
      <div class="control-group">
        <label for="sp.description"><g:message code="label.description" /></label>
        <div class="controls">
          <g:textArea name="sp.description"  value="${serviceProvider.description}" class="required span4" minlength="4" rows="8" cols="36" maxlength="2000"/>
          <fr:tooltip code='fedreg.help.serviceprovider.description' />
        </div>
      </div>
      
      <div class="control-group">
        <label for="sp.servicedescription.connecturl"><g:message code="label.serviceurl" /></label>
        <div class="controls">
          <g:textField name="sp.servicedescription.connecturl" value="${serviceProvider.serviceDescription.connectURL}" class="required url span4" maxlength="255"/>
          <fr:tooltip code='fedreg.help.serviceprovider.connecturl' />
        </div>
      </div>
      
      <div class="control-group">
        <label for="sp.servicedescription.logourl"><g:message code="label.servicelogourl" /></label>
        <div class="controls">
          <g:textField name="sp.servicedescription.logourl" value="${serviceProvider.serviceDescription.logoURL}" class="url span4" maxlength="255"/>
          <fr:tooltip code='fedreg.help.serviceprovider.logourl' />
        </div>
      </div>

      <div class="form-actions">
        <a class="edit-serviceprovider btn btn-success"><g:message code="label.save"/></a>
        <a class="cancel-edit-serviceprovider btn"><g:message code="label.cancel"/></a>
      </div>

    </fieldset>
  </g:form>
</div>