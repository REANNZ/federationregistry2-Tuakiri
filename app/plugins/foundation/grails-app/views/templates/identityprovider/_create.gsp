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
        nimble.growl('error', xhr.responseText);
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
        errorClass: "validation-error",
        errorElement: "div"

    });

    jQuery.validator.addMethod("validcert", function(value, element, params) { 
      fedreg.validateCertificate();
      return newCertificateValid == true; 
    }, jQuery.format("PEM data invalid"));
    
    
    $('#cert').rules("add", {
         required: true,
         validcert: true
    });
    
    $('#hostname').bind('blur',  function() {
      if( $(this).val().indexOf('/', $(this).val().length - 1) !== -1 && $(this).val().length > 9)
        $(this).val($(this).val().substring(0, $(this).val().length - 1));

      fedreg.configureIdentityProviderSAML($(this).val());
    });
  });
  
  function attrchange(id) {
    if($('#idp\\.attributes\\.' + id + ':checked').val() != null)
      $('#aa\\.attributes\\.' + id).val('on');
    else
      $('#aa\\.attributes\\.' + id).val('off');
  }
  
</r:script>

<g:hasErrors>
  <div class="alert-message block-message error">
    <g:message code="fedreg.templates.identityprovider.create.errors" />
    <br>
    <strong><g:message code="label.identifiederrors"/></strong>
    <g:renderErrors bean="${identityProvider}" as="list" />
    <g:renderErrors bean="${contact}" as="list"/>
    <g:renderErrors bean="${entityDescriptor}" as="list"/>
  </div>
</g:hasErrors>

<g:form action="${saveAction}" name="idpssodescriptorcreateform">
  <g:hiddenField name="active" value="true"/>
  <g:hiddenField name="idp.autoacceptservices" value="true"/>
  <g:hiddenField name="aa.create" value="true"/>
  <g:hiddenField name="contact.type" value="administrative" />
  
  <div class="row">
  <div class="span14 offset1">

  <div class="step" id="overview">
    <p><g:message code="fedreg.templates.identityprovider.create.overview.details" /></p>
  </div>

  <hr>
  
  <div class="step" id="contact">
    <h3>1. <g:message code="fedreg.templates.identityprovider.create.contact.heading" /></h3>
    <p><g:message code="fedreg.templates.identityprovider.create.contact.details" /></p>
    
    <fieldset class="span12">
      <div class="clearfix">
      <label for="contact.givenName"><g:message code="label.givenname" /></label>
        <div class="input">
          <g:textField name="contact.givenName"  size="50" class="required" value="${contact?.givenName ?: fr.subject()?.givenName}"/>
        </div>
      </div>

      <div class="clearfix">
      <label for="contact.surname"><g:message code="label.surname" /></label>
        <div class="input">
          <g:textField name="contact.surname"  size="50" class="required" value="${contact?.surname ?: fr.subject()?.surname}"/>
        </div>
      </div>

      <div class="clearfix">
        <label for="contact.email"><g:message code="label.email" /></label>
        <div class="input">
          <g:textField name="contact.email"  size="50" class="required email" value="${contact?.email  ?: fr.subject()?.email}"/>
        </div>
      </div>
    </fieldset>
  </div>

  <hr>
  
  <div class="step" id="basic">
      <h3>2. <g:message code="fedreg.templates.identityprovider.create.basicinformation.heading" /></h3>
      <p><g:message code="fedreg.templates.identityprovider.create.basicinformation.details" /></p>

    <fieldset class="span12">
      <div class="clearfix">
        <label for="organization.id"><g:message code="label.organization" /></label>
        <div class="input">
          <g:select name="organization.id" from="${organizationList.sort{it.displayName}}" optionKey="id" optionValue="displayName" value="${organization?.id}"/>
        </div>
      </div>

      <div class="clearfix">
        <label for="idp.displayName"><g:message code="label.displayname" /></label>
        <div class="input">
          <g:hiddenField name="aa.displayName" value=""/>
          <g:textField name="idp.displayName"  size="50" class="required" value="${identityProvider?.displayName}"/>
          <fr:tooltip code='fedreg.help.identityprovider.displayname' />
        </div>    
      </div>

      <div class="clearfix">
        <label for="idp.description"><g:message code="label.description" /></label>
        <div class="input">
          <g:hiddenField name="aa.description" />
          <g:textArea name="idp.description"  class="required" rows="8" cols="36" value="${identityProvider?.description}"/>
          <fr:tooltip code='fedreg.help.identityprovider.description' />
        </div>
      </div>

    </fieldset>
  </div>
  
  <hr>

  <div class="step" id="saml">
    <h3>3. <g:message code="fedreg.templates.identityprovider.create.saml.heading" /></h3>
    <p>
      <g:message code="fedreg.templates.identityprovider.create.saml.details" />
    </p>

    <div id="samlbasicmode">
      <h4><g:message code="fedreg.templates.identityprovider.create.saml.known.heading" /></h4>
      <p><g:message code="fedreg.templates.identityprovider.create.saml.known.descriptive" /></p>

      <div class="row">
        <div class="span4 offset12">
          <a href="#" class="btn info" onClick="$('#samlbasicmode').hide(); $('#samladvancedmode').fadeIn(); return false;"><g:message code="fedreg.templates.identityprovider.create.saml.known.switch" /></a>
        </div>
      </div>

      <fieldset class="span12">
        <div class="clearfix">
          <label for="knownimpl"><g:message code="label.implementation" /></label>
          <div class="input">
            <span id="knownimpl"></span>
          </div>
        </div>

        <div class="clearfix">
          <label for="hostname"><g:message code="label.host" /></label>
          <div class="input">
            <g:textField name="hostname" size="50" class="url" value="${hostname}"/>
            <fr:tooltip code='fedreg.help.identityprovider.hostname' />
          </div>
        </div>
      </fieldset>
    </div>
    
    <div id="samladvancedmode" class="hidden">
      <h4><g:message code="fedreg.templates.identityprovider.create.saml.advanced.heading" /></h4>
      <p><g:message code="fedreg.templates.identityprovider.create.saml.advanced.descriptive" /></p>

      <div class="row">
        <div class="span4 offset12">
          <a href="#" class="btn info" onClick="$('#samladvancedmode').hide(); $('#samlbasicmode').fadeIn(); return false;"><g:message code="fedreg.templates.identityprovider.create.saml.advanced.switch" /></a>
        </div>
      </div>

      <fieldset>
        <div class="clearfix">
          <label for="entity.identifier"><g:message code="label.entityid" /></label>
          <div class="input">
            <g:textField name="entity.identifier" size="64" class="required url" value="${entityDescriptor?.entityID}"/>
            <fr:tooltip code='fedreg.help.identityprovider.entitydescriptor' />
          </div>
        </div>
      </fieldset>
      <fieldset>
        <div class="clearfix">
          <label for="idp.post"><g:message code="label.httppostendpoint" /></label>
          <div class="input">
            <g:textField name="idp.post" size="64" class="required url" value="${httpPost?.location}"/>
            <fr:tooltip code='fedreg.help.identityprovider.authpost' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-POST</span>
          </div>
        </div>

        <div class="clearfix">
          <label for="idp.redirect"><g:message code="label.httpredirectendpoint" /></label>
          <div class="input">
            <g:textField name="idp.redirect" size="64" class="required url" value="${httpRedirect?.location}"/>
            <fr:tooltip code='fedreg.help.identityprovider.authredirect' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-Redirect</span>
          </div>
        </div>
      </fieldset>
      <fieldset>
        <div class="clearfix">
          <label for="idp.artifact"><g:message code="label.soapartifactendpoint" /></label>
          <div class="input">
            <g:textField name="idp.artifact" size="64" class="required url" value="${soapArtifact?.location}"/>

            <span class="index">Index:</span>
            <g:textField name="idp.artifact-index" size="2" class="required number index" value="${soapArtifact?.index}"/>
            <fr:tooltip code='fedreg.help.identityprovider.authartifact' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-Artifact</span>
          </div>
        </div>
      </fieldset>
      <fieldset>
        <div class="clearfix">
          <label for="aa.attributeservice"><g:message code="label.soapatrributequeryendpoint" /></label>
          
          <div class="input">
            <g:textField name="aa.attributeservice" size="64" class="required url" value="${soapAttributeService?.location}"/>
            <fr:tooltip code='fedreg.help.identityprovider.aasoap' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:SOAP</span>
          </div>
        </div>
      </fieldset>
    </div>
  </div>

  <hr>
  
  <div class="step" id="scope">
    <h3>4. <g:message code="fedreg.templates.identityprovider.create.scope.heading" /></h3>
    <p><g:message code="fedreg.templates.identityprovider.create.scope.details" /></p>
    <p><g:message code="fedreg.templates.identityprovider.create.scope.example" /></p>

    <fieldset>
      <div class="clearfix">
          <label for="scope"><g:message code="label.scope" /></label>
          <div class="input">
            <g:textField name="idp.scope" size="50" class="required" value="${scope}"/>
            <fr:tooltip code='fedreg.help.identityprovider.scope' />
          </div>
      </div>
    </fieldset>
  </div>

  <hr>
  
  <div class="step" id="crypto">
    <h3>5. <g:message code="fedreg.templates.identityprovider.create.crypto.heading" /></h3>
    <p><g:message code="fedreg.templates.identityprovider.create.crypto.details" /></p>

    <fieldset>
      <div class="clearfix">
          <label for="newcertificatedata"><g:message code="label.certificate" /></label>
          <div class="input">
            <div id="newcertificatedetails">
            </div>
            <g:hiddenField name="idp.crypto.sig" value="${true}" />
            <g:textArea name="cert" id="cert" class="cert" rows="25" cols="60" value="${certificate}"/>
            <fr:tooltip code='fedreg.help.identityprovider.certificate' />
          </div>
    </fieldset>
  </div>

  <hr>
  
  <div class="step" id="attributesupport">
    <h3>6. <g:message code="fedreg.templates.identityprovider.create.attributesupport.heading" /></h3>
    <p><g:message code="fedreg.templates.identityprovider.create.attributesupport.details" /></p>
    <table class="borderless">
      <tr>
        <th><g:message code="label.name" /></th>
        <th><g:message code="label.category" /></th>
        <th><g:message code="label.description" /></th>
        <th><g:message code="label.supported" /></th>
      </tr>
      <g:each in="${attributeList.sort{it.category.name}}" var="attr" status="i">
      <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
        <td>
          ${fieldValue(bean: attr, field: "name")}<br>
          <code>oid:${fieldValue(bean: attr, field: "oid")}</code>
        </td>
        <td class="centered">
          ${fieldValue(bean: attr, field: "category.name")}
        </td>
        <td style="width:400px;">
          ${fieldValue(bean: attr, field: "description")}
        </td>
        <td class="centered">
          <g:hiddenField name="aa.attributes.${attr.id}" />
          <g:checkBox name="idp.attributes.${attr.id}" onchange="attrchange(${attr.id})" checked="${supportedAttributes?.contains(attr)}"/>
        </td>
      </tr>
      </g:each>
    </table>
  </div>
  
  <hr>
  
  <div class="step submit_step" id="creationsummary">
    <h3>7. <g:message code="fedreg.templates.identityprovider.create.summary.heading" /></h3>
    <p><g:message code="fedreg.templates.identityprovider.create.summary.details" /></p>

    <div class="row">
      <div class="offset12">
        <g:submitButton name="submit" value="Submit" class="btn success"/>
      </div>
    </div>
  </div>

        </div>
      </div>

</g:form>