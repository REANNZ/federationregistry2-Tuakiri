<g:hasErrors>
  <div class="alert alert-message alert-danger">
    <p><strong><g:message encodeAs="HTML" code="templates.fr.organization.create.errors" /></strong></p>
    <p>
      <g:message encodeAs="HTML" code="label.identifiederrors"/>
      <g:renderErrors bean="${organization}" as="list" />
      <g:renderErrors bean="${contact}" as="list"/>
    </p>
  </div>
</g:hasErrors>

<g:form action="${saveAction}" class="form-horizontal">
  <g:hiddenField name="active" value="true"/>
  <g:hiddenField name="contact.type" value="technical" />
  <g:hiddenField name="organization.lang" value="en" />
  
  <div class="step" id="overview">
    <g:message code="templates.fr.organization.create.overview.details" />
  </div>

  <hr>
  
  <div id="contact" class="step">
    <h3>1. <g:message encodeAs="HTML" code="templates.fr.organization.create.contact.heading" /></h3>
    <p><g:message encodeAs="HTML" code="templates.fr.organization.create.contact.details" /></p>
    <fieldset>
      <div class="control-group">
        <label class="control-label" for="contact.givenName"><g:message encodeAs="HTML" code="label.givenname" /></label>
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
        <label class="control-label" for="contact.surname"><g:message encodeAs="HTML" code="label.surname" /></label>
        <div class="controls">
          <g:if test="${contact?.surname}">
            <g:textField name="contact.surname"  class="required" value="${contact?.surname}"/>
          </g:if>
          <g:else>
            <g:textField name="contact.surname"  class="required" value="${fr?.subject() ? fr.subject().surname:''}"/>
          </g:else>
        </div>
      </div>

      <div class="control-group">
        <label class="control-label" for="contact.email"><g:message encodeAs="HTML" code="label.email" /></label>
        <div class="controls">
          <g:if test="${contact?.email}">
            <g:textField name="contact.email"  class="required email" value="${contact?.email}"/>
          </g:if>
          <g:else>
            <g:textField name="contact.email"  class="required" value="${fr?.subject() ? fr.subject().email:''}"/>
          </g:else>
        </div>
      </div>
    </fieldset>
  </div>

  <hr>
  
  <div class="step" id="basicinformation">
    <h3>2. <g:message encodeAs="HTML" code="templates.fr.organization.create.basicinformation.heading" /></h3>
    <p>
      <g:message encodeAs="HTML" code="templates.fr.organization.create.basicinformation.details" />
    </p>
    <fieldset>
      <div class="control-group">
      <label class="control-label" for="organization.name"><g:message encodeAs="HTML" code="label.name" /></label>
        <div class="controls">
          <g:textField name="organization.name" class="required span4" value="${organization?.name}" placeholder="example.edu.au" />
          <fr:tooltip code='help.fr.organization.name' />
        </div>
      </div>

      <div class="control-group">
      <label class="control-label" for="organization.displayName"><g:message encodeAs="HTML" code="label.displayname" /></label>
        <div class="controls">
          <g:textField name="organization.displayName" class="required span4" value="${organization?.displayName}" placeholder="Example University"/>
          <fr:tooltip code='help.fr.organization.displayName' />
        </div>
      </div>

      <div class="control-group">
      <label class="control-label" for="organization.url"><g:message encodeAs="HTML" code="label.organizationurl" /></label>
        <div class="controls">
          <g:textField name="organization.url" class="required url span4" value="${organization?.url}" placeholder="http://www.example.edu.au"/>
          <fr:tooltip code='help.fr.organization.url' />
        </div>
      </div>

      <div class="control-group">
      <label class="control-label" for="organization.primary"><g:message encodeAs="HTML" code="label.organizationtype" /></label>
        <div class="controls">
          <g:select name="organization.primary" from="${organizationTypes.findAll{it.discoveryServiceCategory == true}.sort{it.displayName}}" optionKey="id" optionValue="displayName" value="${organization?.primary?.id}" class="span4" />
          <fr:tooltip code='help.fr.organization.type' />
        </div>
      </div>
    </fieldset>
  </div>

  <hr>
  
  <div class="step" id="creationsummary">
    <h3>3. <g:message encodeAs="HTML" code="templates.fr.organization.create.summary.heading" /></h3>
    <p>
      <g:message encodeAs="HTML" code="templates.fr.organization.create.summary.details" />
    </p>

    <div class="form-action">
      <button type="submit" name="submit" value="submit" class="btn btn-success btn-large"><g:message encodeAs="HTML" code="label.submit"/></button>
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
