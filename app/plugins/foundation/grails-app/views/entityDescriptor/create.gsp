<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message code="fedreg.view.members.entity.create.title" /></title>
  </head>
  <body>

    <g:hasErrors>
      <div class="alert alert-message alert-danger">
        <p><strong><g:message code="fedreg.view.members.entity.create.errors" /></strong></p>
        <p>
          <g:message code="label.identifiederrors"/>
          <g:renderErrors bean="${entity}" as="list" />
          <g:renderErrors bean="${contact}" as="list"/>
        </p>
      </div>
    </g:hasErrors>

    <h2><g:message code="fedreg.view.members.entity.create.heading" /></h2>

    <g:form controller="entityDescriptor" action="save" name="entitycreateform" class="form-horizontal validating">
      <g:hiddenField name="active" value="true"/>

      <div id="overview">
        <p><g:message code="fedreg.view.members.entity.create.overview.details" /></p>
      </div>

      <hr>

      <div id="basic">
        <h3>2. <g:message code="fedreg.view.members.entity.create.basicinformation.heading" /></h3>
        <p><g:message code="fedreg.view.members.entity.create.basicinformation.details" /></p>

        <fieldset>
          <div class="control-group">
            <label for="organization.id"><g:message code="label.organization" /></label>
            <div class="controls">
              <g:select name="organization.id" from="${organizationList.sort{it.displayName}}" optionKey="id" optionValue="displayName" value="${organization?.id}"/>
            </div>
          </div>

          <div class="control-group">
            <label for="entity.identifier"><g:message code="label.entityid" /></label>
            <div class="controls">
              <g:textField name="entity.identifier" value="${entity.entityID}" class="required span4"/>
            </div>    
          </div>
        </fieldset>
      </div>

      <hr>

      <div id="creationsummary">
        <h3>3. <g:message code="fedreg.view.members.entity.create.summary.heading" /></h3>
        <p><g:message code="fedreg.view.members.entity.create.summary.details" /></p>

        <div class="form-actions">
          <g:submitButton name="submit" value="Submit" class="btn btn-success"/>
        </div>
      </div>
    </g:form>

    <r:script>
      $('form').validate();
    </r:script>

	</body>
</html>
