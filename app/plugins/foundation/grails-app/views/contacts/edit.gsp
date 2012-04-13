<html>
  <head>
    <meta name="layout" content="members" />
  </head>
  <body>
      <h2><g:message code="fedreg.view.members.contacts.edit.heading" args="[contact.givenName, contact.surname]"/></h2>

      <g:render template="/templates/flash" plugin="foundation"/>
      
      <g:form action="update" id="${contact.id}" method="PUT" class="form validating">
        <fieldset>
          <div class="control-group">
            <label for="organisation"><g:message code="label.organization" /></label>
            <div class="controls">
              <g:select name="organization"
                        from="${organizations}"
                        value="${contact?.organization?.id}"
                        optionKey="id" optionValue="name"/>
            </div>
          </div>

          <div class="control-group">
            <label for="givenname"><g:message code="label.givenname" /></label>
            <div class="controls">
              <input type="text" name="givenname" value="${fieldValue(bean: contact, field: 'givenName')}" class="required"/>
            </div>
          </div>

          <div class="control-group">
            <label for="surname"><g:message code="label.surname" /></label>
            <div class="controls"> 
              <input type="text" name="surname" value="${fieldValue(bean: contact, field: 'surname')}" class="required"/>
            </div>
          </div>

          <div class="control-group">
            <label for="email"><g:message code="label.email" /></label>
            <div class="controls"> 
              <input type="text" name="email" value="${fieldValue(bean: contact, field: 'email')}" class="required email"/>
            </div>
          </div>

          <div class="control-group">
            <label for="workPhone"><g:message code="label.workphone" /></label>
            <div class="controls">
              <input type="text" name="workPhone" value="${fieldValue(bean: contact, field: 'workPhone')}" />
            </div>
          </div>

          <div class="control-group">
            <label for="mobilePhone"><g:message code="label.mobilephone" /></label>
            <div class="controls">
              <input type="text" name="mobilePhone" value="${fieldValue(bean: contact, field: 'mobilePhone')}" />
            </div>
          </div>
        </fieldset>
        
        <div class="form-actions">
          <button type="submit" class="btn btn-success"><g:message code="label.update" default="Update"/></button>
          <g:link action="show" id="${contact.id}" class="btn"><g:message code="label.cancel" default="Cancel"/></g:link>
        </div>
      </g:form>
  </body>
</html>