<div id="editor-serviceprovider" class="revealable">
  <g:form action="update" id="${serviceProvider.id}" method="PUT" class="form-horizontal validating">

    <fieldset>
      <div class="control-group">
        <label class="control-label" for="sp.status"><g:message encodeAs="HTML" code="label.status" /></label>
        <div class="controls">
          <input type="radio" name="sp.status" value="true" ${serviceProvider.active ? 'checked' : ''} />
          <g:message encodeAs="HTML" code="label.active"/>
        
          <input type="radio" name="sp.status" value="false" ${serviceProvider.active ? '' : 'checked'}/>
          <g:message encodeAs="HTML" code="label.inactive"/>

          <fr:tooltip code='help.fr.serviceprovider.status' />
        </div>
      </div>

      <div class="control-group">
        <label class="control-label" for="sp.displayName"><g:message encodeAs="HTML" code="label.displayname" /></label>
        <div class="controls">
          <g:textField name="sp.displayName"  value="${serviceProvider.displayName}" class="required span4" minlength="4" maxlength="255" />
          <fr:tooltip code='help.fr.serviceprovider.displayname' />
        </div>
      </div>
      
      <div class="control-group">
        <label class="control-label" for="sp.description"><g:message encodeAs="HTML" code="label.description" /></label>
        <div class="controls">
          <g:textArea name="sp.description"  value="${serviceProvider.description}" class="required span4" minlength="4" rows="8" cols="36" maxlength="2000"/>
          <fr:tooltip code='help.fr.serviceprovider.description' />
        </div>
      </div>
      
      <div class="control-group">
        <label class="control-label" for="sp.servicedescription.connecturl"><g:message encodeAs="HTML" code="label.serviceurl" /></label>
        <div class="controls">
          <g:textField name="sp.servicedescription.connecturl" value="${serviceProvider.serviceDescription.connectURL}" class="required url span4" maxlength="255"/>
          <fr:tooltip code='help.fr.serviceprovider.connecturl' />
        </div>
      </div>
      
      <div class="control-group">
        <label class="control-label" for="sp.servicedescription.logourl"><g:message encodeAs="HTML" code="label.servicelogourl" /></label>
        <div class="controls">
          <g:textField name="sp.servicedescription.logourl" value="${serviceProvider.serviceDescription.logoURL}" class="url span4" maxlength="255"/>
          <fr:tooltip code='help.fr.serviceprovider.logourl' />
        </div>
      </div>

      <div class="form-actions">
        <button type="submit" class="edit-serviceprovider btn btn-success"><g:message encodeAs="HTML" code="label.save"/></button>
        <a class="cancel-edit-serviceprovider btn"><g:message encodeAs="HTML" code="label.cancel"/></a>
      </div>

    </fieldset>
  </g:form>
</div>
