<div id="editor-serviceprovider" class="hidden">
  <g:form action="update" id="${serviceProvider.id}" method="PUT" class="span11 validating">

    <fieldset>
      <div class="clearfix">
        <label for="sp.status"><g:message code="label.status" /></label>
        <div class="input">
          <g:radioGroup name="sp.status" values="['true', 'false']" labels="['label.active', 'label.inactive']" value="${serviceProvider.active}" >
             ${it.radio} <g:message code="${it.label}" />
          </g:radioGroup>
          <fr:tooltip code='fedreg.help.serviceprovider.status' />
        </div>
      </div>

      <div class="clearfix">
        <label for="sp.displayName"><g:message code="label.displayname" /></label>
        <div class="input">
        <g:textField name="sp.displayName"  value="${serviceProvider.displayName}" size="50" class="required" minlength="4" maxlength="255" />
        <fr:tooltip code='fedreg.help.serviceprovider.displayname' />
        </div>
      </div>
      
      <div class="clearfix">
        <label for="sp.description"><g:message code="label.description" /></label>
        <div class="input">
        <g:textArea name="sp.description"  value="${serviceProvider.description}" class="required" minlength="4" rows="8" cols="36" maxlength="2000"/>
        <fr:tooltip code='fedreg.help.serviceprovider.description' />
        </div>
      </div>
      
      <div class="clearfix">
        <label for="sp.servicedescription.connecturl"><g:message code="label.serviceurl" /></label>
        <div class="input">
        <g:textField name="sp.servicedescription.connecturl" value="${serviceProvider.serviceDescription.connectURL}" size="50" class="required url" maxlength="255"/>
        <fr:tooltip code='fedreg.help.serviceprovider.connecturl' />
        </div>
      </div>
      
      <div class="clearfix">
        <label for="sp.servicedescription.logourl"><g:message code="label.servicelogourl" /></label>
        <div class="input">
        <g:textField name="sp.servicedescription.logourl" value="${serviceProvider.serviceDescription.logoURL}" size="50" class="url" maxlength="255"/>
        <fr:tooltip code='fedreg.help.serviceprovider.logourl' />
        </div>
      </div>

      <div class="input">
        <a class="edit-serviceprovider btn success"><g:message code="label.save"/></a>
        <a class="cancel-edit-serviceprovider btn"><g:message code="label.cancel"/></a>
      </div>

    </fieldset>
  </g:form>
</div>