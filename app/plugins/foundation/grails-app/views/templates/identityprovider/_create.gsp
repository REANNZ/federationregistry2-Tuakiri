<g:hasErrors>
<div class="alert alert-message alert-danger">
  <p><strong><g:message code="templates.fr.identityprovider.create.errors" /></strong></p>
  <p>
    <g:message code="label.identifiederrors"/>
    <g:renderErrors bean="${identityProvider}" as="list" />
    <g:renderErrors bean="${contact}" as="list"/>
    <g:renderErrors bean="${entityDescriptor}" as="list"/>
  </p>
</div>
</g:hasErrors>

<g:form action="${saveAction}" name="idpssodescriptorcreateform" class="form-horizontal validating">
  <g:hiddenField name="active" value="true"/>
  <g:hiddenField name="idp.autoacceptservices" value="true"/>
  <g:hiddenField name="aa.create" value="true"/>
  <g:hiddenField name="contact.type" value="technical" />

    <div id="overview">
      <p><g:message code="templates.fr.identityprovider.create.overview.details" /></p>
    </div>

    <hr>

    <div id="contact">
      <h3>1. <g:message code="templates.fr.identityprovider.create.contact.heading" /></h3>
      <p><g:message code="templates.fr.identityprovider.create.contact.details" /></p>
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

    <div id="basic">
        <h3>2. <g:message code="templates.fr.identityprovider.create.basicinformation.heading" /></h3>
        <p><g:message code="templates.fr.identityprovider.create.basicinformation.details" /></p>

      <fieldset>
        <div class="control-group">
          <label for="organization.id"><g:message code="label.organization" /></label>
          <div class="controls">
            <g:select name="organization.id" from="${organizationList.sort{it.displayName}}" optionKey="id" optionValue="displayName" value="${organization?.id}" class="span4" />
          </div>
        </div>

        <div class="control-group">
          <label for="idp.displayName"><g:message code="label.displayname" /></label>
          <div class="controls">
            <g:hiddenField name="aa.displayName" value=""/>
            <g:textField name="idp.displayName" class="required span4" value="${identityProvider?.displayName}"/>
            <fr:tooltip code='help.fr.identityprovider.displayname' />
          </div>    
        </div>

        <div class="control-group">
          <label for="idp.description"><g:message code="label.description" /></label>
          <div class="controls">
            <g:hiddenField name="aa.description" />
            <g:textArea name="idp.description"  class="required span4" rows="8" value="${identityProvider?.description}"/>
            <fr:tooltip code='help.fr.identityprovider.description' />
          </div>
        </div>

      </fieldset>
    </div>

    <hr>

    <div id="saml">
      <h3>3. <g:message code="templates.fr.identityprovider.create.saml.heading" /></h3>
      <p>
        <g:message code="templates.fr.identityprovider.create.saml.details" />
      </p>

      <div id="samlbasicmode" class="hero-unit">
        <h3><g:message code="templates.fr.identityprovider.create.saml.known.heading" /></h3>
        <p><g:message code="templates.fr.identityprovider.create.saml.known.descriptive" /></p>

        <fieldset>
          <div class="control-group">
            <label for="knownimpl"><g:message code="label.implementation" /></label>
            <div class="controls">
              <span id="knownimpl"></span>
            </div>
          </div>

          <div class="control-group">
            <label for="hostname"><g:message code="label.url" /></label>
            <div class="controls">
              <g:textField name="hostname" class="url span4" value="${hostname}" placeholder="https://idp.example.edu.au" />
              <fr:tooltip code='help.fr.identityprovider.hostname' />
            </div>
          </div>
        </fieldset>
      </div>

      <div class="centered">
        <h2>OR</h2>
      </div>
      
      <div id="samladvancedmode" class="hero-unit">
        <h3><g:message code="templates.fr.identityprovider.create.saml.advanced.heading" /></h3>
        <p><g:message code="templates.fr.identityprovider.create.saml.advanced.descriptive" /></p>

        <fieldset>
          <div class="control-group">
            <label for="entity.identifier"><g:message code="label.entityid" /></label>
            <div class="controls">
              <g:textField name="entity.identifier" size="64" class="required url span4" value="${entityDescriptor?.entityID}"/>
              <fr:tooltip code='help.fr.identityprovider.entitydescriptor' />
            </div>
          </div>
        </fieldset>
        <hr>
        <fieldset>
          <div class="control-group">
            <label for="idp.post"><g:message code="label.httppostendpoint" /></label>
            <div class="controls">
              <g:textField name="idp.post" size="64" class="required url span4" value="${httpPost?.location}"/>
              <fr:tooltip code='help.fr.identityprovider.authpost' />
              <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-POST</span>
            </div>
          </div>

          <div class="control-group">
            <label for="idp.redirect"><g:message code="label.httpredirectendpoint" /></label>
            <div class="controls">
              <g:textField name="idp.redirect" size="64" class="required url span4" value="${httpRedirect?.location}"/>
              <fr:tooltip code='help.fr.identityprovider.authredirect' />
              <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-Redirect</span>
            </div>
          </div>
        </fieldset>
        <hr>
        <fieldset>
          <div class="control-group">
            <label for="idp.artifact"><g:message code="label.soapartifactendpoint" /></label>
            <div class="controls">
              <g:textField name="idp.artifact" size="64" class="required url span4" value="${soapArtifact?.location}"/>

              <span class="index">Index:</span>
              <g:textField name="idp.artifact-index" size="2" class="required number index span1" value="${soapArtifact?.index}"/>
              <fr:tooltip code='help.fr.identityprovider.authartifact' />
              <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-Artifact</span>
            </div>
          </div>
        </fieldset>
        <hr>
        <fieldset>
          <div class="control-group">
            <label for="aa.attributeservice"><g:message code="label.soapatrributequeryendpoint" /></label>
            
            <div class="controls">
              <g:textField name="aa.attributeservice" size="64" class="required url span4" value="${soapAttributeService?.location}"/>
              <fr:tooltip code='help.fr.identityprovider.aasoap' />
              <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:SOAP</span>
            </div>
          </div>
        </fieldset>
      </div>
    </div>

    <hr>

    <div id="scope">
      <h3>4. <g:message code="templates.fr.identityprovider.create.scope.heading" /></h3>
      <p><g:message code="templates.fr.identityprovider.create.scope.details" /></p>
      <p><g:message code="templates.fr.identityprovider.create.scope.example" /></p>

      <fieldset>
        <div class="control-group">
            <label for="scope"><g:message code="label.scope" /></label>
            <div class="controls">
              <g:textField name="idp.scope" class="required span4" value="${scope}" placeholder="example.edu.au"/>
              <fr:tooltip code='help.fr.identityprovider.scope' />
            </div>
        </div>
      </fieldset>
    </div>

    <hr>

    <div id="crypto">
      <h3>5. <g:message code="templates.fr.identityprovider.create.crypto.heading" /></h3>
      <p><g:message code="templates.fr.identityprovider.create.crypto.details" /></p>

      <fieldset>
        <div class="control-group">
            <label for="newcertificatedata"><g:message code="label.certificate" /></label>
            <div class="controls">
              <div id="newcertificatedetails">
              </div>
              <g:hiddenField name="idp.crypto.sig" value="${true}" />
              <g:textArea name="cert" id="cert" class="cert required" rows="25" cols="60" value="${certificate}"/>
              <fr:tooltip code='help.fr.identityprovider.certificate' />
            </div>
      </fieldset>
    </div>

    <hr>

    <div id="attributesupport">
      <h3>6. <g:message code="templates.fr.identityprovider.create.attributesupport.heading" /></h3>
      <p><g:message code="templates.fr.identityprovider.create.attributesupport.details" /></p>
      <table class="table table-striped borderless">
        <tr>
          <th class="span9"><g:message code="label.name" /></th>
          <th class="centered"><g:message code="label.category" /></th>
          <th class="centered"><g:message code="label.supported" /></th>
        </tr>
        <g:each in="${attributeList.sort{it.category.name}}" var="attr" status="i">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
          <td>
            <strong class="highlight">${fieldValue(bean: attr, field: "name")}</strong><br>
            <code>oid:${fieldValue(bean: attr, field: "oid")}</code>
            <br><br><em>${fieldValue(bean: attr, field: "description")}</em>
          </td>
          <td class="centered">
            ${fieldValue(bean: attr, field: "category.name")}
          </td>
          <td class="centered">
            <g:checkBox name="idp.attributes.${attr.id}" checked="${supportedAttributes?.contains(attr)}"/>
          </td>
        </tr>
        </g:each>
      </table>
    </div>

    <hr>

    <div id="creationsummary">
      <h3>7. <g:message code="templates.fr.identityprovider.create.summary.heading" /></h3>
      <p><g:message code="templates.fr.identityprovider.create.summary.details" /></p>

      <div class="form-action">
        <button type="submit" name="submit" value="submit" class="btn btn-success btn-large"><g:message code="label.submit"/></button>
      </div>
    </div>

</g:form>

<r:script>
var certificateValidationEndpoint = "${createLink(controller:'coreUtilities', action:'validateCertificate')}";
var knownIDPImplEndpoint = "${createLink(controller:'coreUtilities', action:'knownIDPImpl')}";
var newCertificateValid = false;

var knownIDPImpl = "";
var currentImpl = "";

$(function() {
  $.ajax({
    type: "GET",
    cache: false,
    dataType: 'json',
    url: knownIDPImplEndpoint,
    success: function(res) {
      knownIDPImpl = res;
      $.each(knownIDPImpl, function(key, value) {
        if(knownIDPImpl[key].selected) {
          currentImpl = key
          $('<input type="radio" class="currentimpl" name="knownimpls" checked value='+key+'> <strong>' + knownIDPImpl[key].displayName + '</strong><br>').appendTo($("#knownimpl"));
        }
        else
          $('<input type="radio" class="currentimpl" name="knownimpls" value='+key+'> <strong>' + knownIDPImpl[key].displayName + '</strong><br>').appendTo($("#knownimpl"));
      });
      
      $('input.currentimpl').change(function() {
        currentImpl = $(this).val();
        fedreg.configureIdentityProviderSAML($('#hostname').val());
      });
      },
      error: function (xhr, ajaxOptions, thrownError) {
      
      }
  });
  
  $('#hostname').alphanumeric({nocaps:true, ichars:';'});
  $('#idp\\.scope').alphanumeric({nocaps:true, allow:'.'});
  
  $('form').validate({
      rules: {
        'hostname': {
          required: function() {
            return ($("#entity\\.identifier").val() == "");
          }
        }
      },
      keyup: false,
  });

  jQuery.validator.addMethod("validcert", function(value, element, params) { 
    fedreg.validateCertificate();
    return valid_certificate; 
  }, jQuery.format("PEM data invalid"));
  
  
  $('#cert').rules("add", {
       required: true,
       validcert: true
  });
  
  $('#hostname').bind('change',  function() {
    var val = $.trim($(this).val());
    if( val.indexOf('/', val.length - 1) !== -1 && val.length > 9)
      val = val.substring(0, val.length - 1);

    if( val.indexOf('/idp/shibboleth', val.length - 15) !== -1 && val.length > 9)
      val = val.substring(0, val.length - 15);

    fedreg.configureIdentityProviderSAML(val);
    $(this).val(val);
  });
});

</r:script>