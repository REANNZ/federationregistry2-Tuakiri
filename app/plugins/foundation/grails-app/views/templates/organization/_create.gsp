<g:hasErrors>
  <div class="alert alert-message alert-danger">
    <p><strong><g:message code="fedreg.templates.organization.create.errors" /></strong></p>
    <p>
      <g:message code="label.identifiederrors"/>
      <g:renderErrors bean="${organization}" as="list" />
      <g:renderErrors bean="${contact}" as="list"/>
    </p>
  </div>
</g:hasErrors>

<g:form action="${saveAction}" class="form-horizontal">
  <g:hiddenField name="active" value="true"/>
  <g:hiddenField name="contact.type" value="administrative" />
  <g:hiddenField name="organization.lang" value="en" />
  
  <div class="step" id="overview">
    <g:message code="fedreg.templates.organization.create.overview.details" />
  </div>

  <hr>
  
  <div id="contact" class="step">
    <h3>1. <g:message code="fedreg.templates.organization.create.contact.heading" /></h3>
    <p><g:message code="fedreg.templates.organization.create.contact.details" /></p>
    <fieldset>
      <div class="control-group">
        <label for="contact.givenName"><g:message code="label.givenname" /></label>
        <div class="controls">
          <g:if test="${contact?.givenName}">
            <g:textField name="contact.givenName"  class="required" value="${contact?.givenName}"/>
          </g:if>
          <g:else>
            <g:textField name="contact.givenName"  class="required" value="${fr?.subject() ? fr.subject().givenName:''}"/>
          </g:else>
        </div>
      </div>

      <div class="control-group">
        <label for="contact.surname"><g:message code="label.surname" /></label>
        <div class="controls">
          <g:if test="${contact?.surname}">
            <g:textField name="contact.surname"  class="required" value="${contact?.surname}"/>
          </g:if>
          <g:else>
            <g:textField name="contact.givenName"  class="required" value="${fr?.subject() ? fr.subject().surname:''}"/>
          </g:else>
        </div>
      </div>

      <div class="control-group">
        <label for="contact.email"><g:message code="label.email" /></label>
        <div class="controls">
          <g:if test="${contact?.email}">
            <g:textField name="contact.email"  class="required email" value="${contact?.email}"/>
          </g:if>
          <g:else>
            <g:textField name="contact.givenName"  class="required" value="${fr?.subject() ? fr.subject().email:''}"/>
          </g:else>
        </div>
      </div>
    </fieldset>
  </div>

  <hr>
  
  <div class="step" id="basicinformation">
    <h3>2. <g:message code="fedreg.templates.organization.create.basicinformation.heading" /></h3>
    <p>
      <g:message code="fedreg.templates.organization.create.basicinformation.details" />
    </p>
    <fieldset>
      <div class="control-group">
      <label for="organization.name"><g:message code="label.name" /></label>
        <div class="controls">
          <g:textField name="organization.name" class="required span4" value="${organization?.name}"/>
          <fr:tooltip code='fedreg.help.organization.name' />
        </div>
      </div>

      <div class="control-group">
      <label for="organization.displayName"><g:message code="label.displayname" /></label>
        <div class="controls">
          <g:textField name="organization.displayName" class="required span4" value="${organization?.displayName}"/>
          <fr:tooltip code='fedreg.help.organization.displayName' />
        </div>
      </div>

      <div class="control-group">
      <label for="organization.url"><g:message code="label.organizationurl" /></label>
        <div class="controls">
          <g:textField name="organization.url" class="required url span4"  value="${organization?.url}"/>
          <fr:tooltip code='fedreg.help.organization.url' />
        </div>
      </div>

      <div class="control-group">
      <label for="organization.primary"><g:message code="label.organizationtype" /></label>
        <div class="controls">
          <g:select name="organization.primary" from="${organizationTypes}" optionKey="id" optionValue="displayName" value="${organization?.primary?.id}" class="span4" />
          <fr:tooltip code='fedreg.help.organization.type' />
        </div>
      </div>
    </fieldset>
  </div>

  <hr>
  
  <div class="step" id="creationsummary">
    <h3>3. <g:message code="fedreg.templates.organization.create.summary.heading" /></h3>
    <p>
      <g:message code="fedreg.templates.organization.create.summary.details" />
    </p>

    <div class="form-action">
      <button type="submit" name="submit" value="submit" class="btn btn-success btn-large"><g:message code="label.submit"/></button>
    </div>
  </div>

</g:form>

<r:script disposition='head'>
  var certificateValidationEndpoint="${createLink(controller:'coreUtilities', action:'validateCertificate')}";
  var newCertificateValid=false;
  
  $(function() {  
    $('form').validate({
        keyup: false
    });
  });
</r:script>