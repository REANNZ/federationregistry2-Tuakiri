<html>
  <head>
    <meta name="layout" content="members" />
    <title><g:message code="fedreg.view.members.entity.create.title" /></title>
  </head>
  <body>

    <g:hasErrors>
      <div class="alert-message block-message error">
        <p><strong><g:message code="fedreg.view.members.entity.create.errors" /></strong></p>
        <p>
          <g:message code="label.identifiederrors"/>
          <g:renderErrors bean="${entity}" as="list" />
          <g:renderErrors bean="${contact}" as="list"/>
        </p>
      </div>
    </g:hasErrors>

    <h2><g:message code="fedreg.view.members.entity.create.heading" /></h2>

    <g:form controller="entityDescriptor" action="save" name="entitycreateform" class="validating">
      <g:hiddenField name="active" value="true"/>

      <div class="row">
        <div class="span14 offset1">
          <div id="overview">
            <p><g:message code="fedreg.view.members.entity.create.overview.details" /></p>
          </div>

          <hr>

          <div id="basic">
              <h3>2. <g:message code="fedreg.view.members.entity.create.basicinformation.heading" /></h3>
              <p><g:message code="fedreg.view.members.entity.create.basicinformation.details" /></p>

            <fieldset class="span12">
              <div class="clearfix">
                <label for="organization.id"><g:message code="label.organization" /></label>
                <div class="input">
                  <g:select name="organization.id" from="${organizationList.sort{it.displayName}}" optionKey="id" optionValue="displayName" value="${organization?.id}"/>
                </div>
              </div>

              <div class="clearfix">
                <label for="entity.identifier"><g:message code="label.entityid" /></label>
                <div class="input">
                  <g:textField name="entity.identifier" value="${entity.entityID}" size="50" class="required"/>
                </div>    
              </div>
          </div>

          <hr>

          <div id="creationsummary">
            <h3>3. <g:message code="fedreg.view.members.entity.create.summary.heading" /></h3>
            <p><g:message code="fedreg.view.members.entity.create.summary.details" /></p>

            <div class="row">
              <div class="offset12">
                <g:submitButton name="submit" value="Submit" class="btn success"/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </g:form>

    <r:script>
      $('form').validate();
    </r:script>

	</body>
</html>
