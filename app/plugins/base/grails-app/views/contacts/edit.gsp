<html>
  <head>
    <meta name="layout" content="members" />
  </head>
  <body>
      <h2><g:message encodeAs="HTML" code="views.fr.foundation.contacts.edit.heading" args="[contact.givenName, contact.surname]"/></h2>

      <g:render template="/templates/flash" plugin="foundation"/>

      <g:form action="update" id="${contact.id}" method="PUT" class="form form-horizontal validating">
        <fieldset>
          <div class="control-group">
            <label class="control-label" for="organisation"><g:message encodeAs="HTML" code="label.organization" /></label>
            <div class="controls">
              <g:select name="organization"
                        from="${organizations}"
                        value="${contact?.organization?.id}"
                        optionKey="id" optionValue="${{ it.displayName?.encodeAsHTML() }}"/>
            </div>
          </div>

          <div class="control-group">
            <label class="control-label" for="givenname"><g:message encodeAs="HTML" code="label.givenname" /></label>
            <div class="controls">
              <input type="text" name="givenname" value="${fieldValue(bean: contact, field: 'givenName')}" class="required"/>
            </div>
          </div>

          <div class="control-group">
            <label class="control-label" for="surname"><g:message encodeAs="HTML" code="label.surname" /></label>
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


          <div class="control-group">
            <label class="control-label" for="status"><g:message encodeAs="HTML" code="label.status" /></label>
            <div class="controls">
              <input type="radio" name="active" value="true"  ${contact.active ? 'checked' : ''}/>
              <g:message encodeAs="HTML" code="label.active"/>
            
              <input type="radio" name="active" alue="false" ${contact.active ? '' : 'checked'}/>
              <g:message encodeAs="HTML" code="label.inactive"/>
            </div>
          </div>
        </fieldset>

        <div class="form-actions">
          <button type="submit" class="btn btn-success"><g:message encodeAs="HTML" code="label.update" default="Update"/></button>
          <g:link action="show" id="${contact.id}" class="btn"><g:message encodeAs="HTML" code="label.cancel" default="Cancel"/></g:link>
        </div>
      </g:form>
  </body>
</html>
