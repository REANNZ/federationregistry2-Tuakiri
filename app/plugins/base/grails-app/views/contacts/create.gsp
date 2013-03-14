<html>
  <head>
    <meta name="layout" content="members" />
  </head>
  <body>
    <h2><g:message encodeAs="HTML" code="views.fr.foundation.contacts.create.heading" /></h2>
    
    <g:render template="/templates/flash" plugin="foundation"/>
    
    <g:form action="save" class="form form-horizontal validating">
      <fieldset>
        <div class="control-group">
          <label class="control-label" for="organisation"><g:message encodeAs="HTML" code="label.organization" /></label>
          <div class="controls">
            <g:select name="organization"
                      from="${organizations}"
                      value="${contact?.organization?.id}"
                      optionKey="id" optionValue="displayName" class="span4"/>
          </div>
        </div>

        <div class="control-group">
          <label class="control-label" for="givenname"><g:message encodeAs="HTML" code="label.givenname" /></label>
          <div class="controls">
            <input type="text" name="givenname" value="${fieldValue(bean: contact, field: 'givenName')}" class="required"/>
          </div>
        </div>

        <div class="control-group">
          <label class="control-label"for="surname"><g:message encodeAs="HTML" code="label.surname" /></label>
          <div class="controls"> 
            <input type="text" name="surname" value="${fieldValue(bean: contact, field: 'surname')}" class="required"/>
          </div>
        </div>

        <div class="control-group">
          <label class="control-label" for="email"><g:message encodeAs="HTML" code="label.email" /></label>
          <div class="controls"> 
            <input type="text" name="email" value="${fieldValue(bean: contact, field: 'email')}" class="required email"/>
          </div>
        </div>

        <div class="control-group">
          <label class="control-label" for="workPhone"><g:message encodeAs="HTML" code="label.workphone" /></label>
          <div class="controls">
            <input type="text" name="workPhone" value="${fieldValue(bean: contact, field: 'workPhone')}" />
          </div>
        </div>

        <div class="control-group">
          <label class="control-label" for="mobilePhone"><g:message encodeAs="HTML" code="label.mobilephone" /></label>
          <div class="controls">
            <input type="text" name="mobilePhone" value="${fieldValue(bean: contact, field: 'mobilePhone')}" />
          </div>
        </div>
      </fieldset>
      
      <div class="form-actions">
        <button type="submit" class="btn btn-success"><g:message encodeAs="HTML" code="label.save" default="Save"/></button>
        <g:link action="list" class="btn"><g:message encodeAs="HTML" code="label.cancel" default="Cancel"/></g:link>
      </div>
      
    </g:form>
  </body>
</html>
