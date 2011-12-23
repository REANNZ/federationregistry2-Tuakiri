<r:script>
  var certificateValidationEndpoint = "${createLink(controller:'coreUtilities', action:'validateCertificate')}";
  var knownSPImplEndpoint = "${createLink(controller:'coreUtilities', action:'knownSPImpl')}";
  
  var newCertificateValid = false;
  var knownSPImpl;
  var currentImpl;
  
  $(function() {
    $.ajax({
      type: "GET",
      cache: false,
      dataType: 'json',
      url: knownSPImplEndpoint,
      success: function(res) {
        knownSPImpl = res;
        
        $.each(knownSPImpl, function(key, value) {
          if(knownSPImpl[key].selected) {
            currentImpl = key
            $('<input type="radio" class="currentimpl" name="knownimpls" checked value='+key+'> <strong>' + knownSPImpl[key].displayName + '</strong><br>').appendTo($("#knownimpl"));
          }
          else
            $('<input type="radio" class="currentimpl" name="knownimpls" value='+key+'> <strong>' + knownSPImpl[key].displayName + '</strong><br>').appendTo($("#knownimpl"));
        });
        
        $('input.currentimpl').change(function() {
          currentImpl = $(this).val();
          fedreg.configureServiceProviderSAML($('#hostname').val());
        });
        },
        error: function (xhr, ajaxOptions, thrownError) {
        nimble.growl('error', xhr.responseText);
        }
    });
    
    $('#hostname').alphanumeric({nocaps:true, ichars:';'});
    
    $('form').validate({
        ignore: ":disabled",
        keyup: false,
        errorClass: "validation-error",
        errorElement: "div"
    });

    jQuery.validator.addMethod("validcert", function(value, element, params) { 
      fedreg.validateCertificate();
      return valid_certificate; 
    }, jQuery.format("PEM data invalid"));
    
    $('#cert').rules("add", {
         required: true,
         validcert: true
    });
    
    $('#hostname').bind('blur',  function() {
      if( $(this).val().indexOf('/', $(this).val().length - 1) !== -1 && $(this).val().length > 9)
        $(this).val($(this).val().substring(0, $(this).val().length - 1));
      
      fedreg.configureServiceProviderSAML( $(this).val() );
    });
  });
</r:script>

<g:hasErrors>
  <div class="alert-message block-message error">
    <g:message code="fedreg.templates.service.create.errors" />
    <br>
    <strong><g:message code="label.identifiederrors"/></strong>
    <g:renderErrors bean="${serviceProvider}" as="list" />
    <g:renderErrors bean="${contact}" as="list"/>
    <g:renderErrors bean="${entityDescriptor}" as="list"/>
  </div>
</g:hasErrors>

<div class="row">
<div class="span14 offset1">

<g:form action="${saveAction}">
  <g:hiddenField name="active" value="true"/>
  <g:hiddenField name="aa.create" value="true"/>
  <g:hiddenField name="contact.type" value="administrative" />
  
  <div class="step" id="overview">
    <p><g:message code="fedreg.templates.serviceprovider.create.overview.details" /></p>
  </div>

  <hr>
  
  <div class="step" id="contact">
    <h3>1. <g:message code="fedreg.templates.serviceprovider.create.contact.heading" /></h3>
    <p><g:message code="fedreg.templates.serviceprovider.create.contact.details" /></p>
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
    <h3>2. <g:message code="fedreg.templates.serviceprovider.create.basicinformation.heading" /></h3>
    <p><g:message code="fedreg.templates.serviceprovider.create.basicinformation.details" /></p>

    <fieldset class="span12">
      <div class="clearfix">
        <label for="organization.id"><g:message code="label.organization" /></label>
        <div class="input">
          <g:select name="organization.id" from="${organizationList.sort{it.displayName}}" optionKey="id" optionValue="displayName" value="${organization?.id}"/>
        </div>
      </div>
      
      <div class="clearfix">
        <label for="sp.displayName"><g:message code="label.displayname" /></label>
        <div class="input">
          <g:textField name="sp.displayName"  size="50" class="required" value="${serviceProvider?.displayName}"/>
          <fr:tooltip code='fedreg.help.serviceprovider.displayname' />
        </div>
      </div>
      
      <div class="clearfix">
        <label for="sp.description"><g:message code="label.description" /></label>
        <div class="input">
          <g:textArea name="sp.description"  class="required" rows="8" cols="36" value="${serviceProvider?.description}"/>
          <fr:tooltip code='fedreg.help.serviceprovider.description' />
        </div>
      </div>
      
      <div class="clearfix">
        <label for="sp.servicedescription.connecturl"><g:message code="label.serviceurl" /></label>
        <div class="input">
          <g:textField name="sp.servicedescription.connecturl" size="50" class="required url" value="${servicedescription?.connecturl}"/>
          <fr:tooltip code='fedreg.help.serviceprovider.connecturl' />
        </div>
      </div>
      
      <div class="clearfix">
        <label for="sp.servicedescription.logourl"><g:message code="label.servicelogourl" /></label>
        <div class="input">
          <g:textField name="sp.servicedescription.logourl" size="50" class="url" value="${servicedescription?.logourl}"/>
          <fr:tooltip code='fedreg.help.serviceprovider.logourl' />
        </div>
      </div>
    </fieldset>
  </div>

  <hr>

  <div class="step" id="saml">
    <h3>3. <g:message code="fedreg.templates.serviceprovider.create.saml.heading" /></h3>
    <p><g:message code="fedreg.templates.serviceprovider.create.saml.details" /></p>
    
    <div id="samlbasicmode">
      <h4><g:message code="fedreg.templates.serviceprovider.create.saml.known.heading" /></h4>
      <p><g:message code="fedreg.templates.serviceprovider.create.saml.known.descriptive" /></p>

      <div class="row">
        <div class="span4 offset12">
          <a href="#" class="btn info" onClick="$('#samlbasicmode').hide(); $('#samladvancedmode').fadeIn(); return false;"><g:message code="fedreg.templates.serviceprovider.create.saml.basic.switch" /></a>
        </div>
      </div>

      <fieldset class="span12">
        <div class="clearfix">
          <label for="knownimpl"><g:message code="label.implementation" /></label>
          <div class="input">
            <div id="knownimpl">
              <strong><g:message code="fedreg.templates.serviceprovider.create.saml.known.shib13" /></strong>&nbsp;&nbsp;<span class="label important"><g:message code="fedreg.templates.serviceprovider.create.saml.known.shib13.descriptive" /></span></span><br><br>
            </div>
          </div>
        </div>

        <div class="clearfix">
          <label for="hostname"><g:message code="label.host" /></label>
          <div class="input">
            <g:textField name="hostname" id="hostname" size="50" class="url"  value="${hostname}"/>
            <fr:tooltip code='fedreg.help.serviceprovider.hostname' />
          </div>
        </div>
      <fieldset>
    </div>
    
    <div id="samladvancedmode" class="hidden">
      <h4><g:message code="fedreg.templates.serviceprovider.create.saml.advanced.heading" /></h4>
      <p><g:message code="fedreg.templates.serviceprovider.create.saml.advanced.descriptive" /></p>

      <div class="row">
        <div class="span4 offset12">
          <a href="#" class="btn info" onClick="$('#samladvancedmode').hide(); $('#samlbasicmode').fadeIn(); return false;"><g:message code="fedreg.templates.identityprovider.create.saml.advanced.switch" /></a>
        </div>
      </div>

      <fieldset>
        <div class="clearfix">
          <label for="entity.identifier"><g:message code="label.entitydescriptor" /></label>
          <div class="input">
            <g:textField name="entity.identifier" size="75" class="required url"  value="${entityDescriptor?.entityID}"/>
            <fr:tooltip code='fedreg.help.serviceprovider.entitydescriptor' />
          </div>
        </div>
      </fieldset>
      <fieldset>
        <div class="clearfix">
          <label for="sp.acs.post"><g:message code="label.acspostendpoint" /></label>
          <div class="input">
            <g:hiddenField name="sp.acs.post-isdefault" value="true" />
            <g:textField name="sp.acs.post" size="75" class="required url"  value="${httpPostACS?.location}"/>
            
            <span class="index">Index:</span>
            <g:textField name="sp.acs.post-index" size="2" class="required number index" value="${httpPostACS?.index}"/>
            <fr:tooltip code='fedreg.help.serviceprovider.acspost' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-POST</span>
          </div>
        </div>
        
        <div class="clearfix">
          <label for="sp.acs.artifact"><g:message code="label.acsartifactendpoint" /></label>
          <div class="input">
            <g:hiddenField name="sp.acs.artifact-isdefault" value="false" />
            <g:textField name="sp.acs.artifact" size="75" class="required url" value="${httpArtifactACS?.location}"/>
            
            <span class="index">Index:</span>
            <g:textField name="sp.acs.artifact-index" size="2" class="required number index" value="${httpArtifactACS?.index}"/>
            <fr:tooltip code='fedreg.help.serviceprovider.acsartifcate' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-Artifact</span>
          </div>
        </div>
      </fieldset>
      <fieldset>
        <div class="clearfix">
          <label for="sp.slo.artifact"><g:message code="label.sloartifactendpoint" /></label>
          <div class="input">
            <g:textField name="sp.slo.artifact" size="75" class="samloptional url" value="${sloArtifact?.location}"/>
            <fr:tooltip code='fedreg.help.serviceprovider.sloartifact' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-Artifact</span>
          </div>
        </div>

        <div class="clearfix">
          <label for="sp.slo.redirect"><g:message code="label.sloredirectendpoint" /></label>
          <div class="input">
            <g:textField name="sp.slo.redirect" size="75" class="samloptional url" value="${sloRedirect?.location}"/>
            <fr:tooltip code='fedreg.help.serviceprovider.sloredriect' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-Redirect</span>
          </div>
        </div>
        
        <div class="clearfix">
          <label for="sp.slo.soap"><g:message code="label.slosoapendpoint" /></label>
          <div class="input">
            <g:textField name="sp.slo.soap" size="75" class="samloptional url" value="${sloSOAP?.location}"/>
            <fr:tooltip code='fedreg.help.serviceprovider.slosoap' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:SOAP</span>
          </div>
        </div>
        
        <div class="clearfix">
          <label for="sp.slo.post"><g:message code="label.slopostendpoint" /></label>
          <div class="input">
            <g:textField name="sp.slo.post" size="75" class="samloptional url"  value="${sloPost?.location}"/>
            <fr:tooltip code='fedreg.help.serviceprovider.slopost' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-POST</span>
          </div>
        </div>
      </fieldset>
      <fieldset>
        <div class="clearfix">
          <label for="sp.drs"><g:message code="label.drsendpoint" /></label>
          <div class="input">
            <g:textField name="sp.drs" size="75" class="samloptional url" value="${discoveryResponseService?.location}"/>
            <g:hiddenField name="sp.drs-isdefault" value="true" />
            <fr:tooltip code='fedreg.help.serviceprovider.disco' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:profiles:SSO:idp-discovery-protocol</span>
          </div>
        </div>
      </fieldset>
      <fieldset>
        <div class="clearfix">
          <label for="sp.mnid.artifact"><g:message code="label.mnidartifactendpoint" /></label>
          <div class="input">
            <g:textField name="sp.mnid.artifact" size="75" class="samloptional url" value="${mnidArtifact?.location}"/>
            <fr:tooltip code='fedreg.help.serviceprovider.mnidaritfact' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-Artifact</span>
          </div>
        </div>

        <div class="clearfix">
          <label for="sp.mnid.redirect"><g:message code="label.mnidredirectendpoint" /></label>
          <div class="input">
            <g:textField name="sp.mnid.redirect" size="75" class="samloptional url" value="${mnidRedirect?.location}"/>
            <fr:tooltip code='fedreg.help.serviceprovider.mnidredirect' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-Redirect</span>
          </div>
        </div>

        <div class="clearfix">
          <label for="sp.mnid.soap"><g:message code="label.mnidsoapendpoint" /></label>
          <div class="input">
            <g:textField name="sp.mnid.soap" size="75" class="samloptional url" value="${mnidSOAP?.location}"/>
            <fr:tooltip code='fedreg.help.serviceprovider.mnidsoap' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:SOAP</span>
          </div>
        </div>

        <div class="clearfix">
          <label for="sp.mnid.post"><g:message code="label.mnidpostendpoint" /></label>
          <div class="input">
            <g:textField name="sp.mnid.post" size="75" class="samloptional url" value="${mnidPost?.location}"/>
            <fr:tooltip code='fedreg.help.serviceprovider.mnidpost' />
            <br><span class="binding"><strong><g:message code="label.binding" /></strong>: SAML:2.0:bindings:HTTP-POST</span>
          </div>
        </div>
      <fieldset>
    </div>
  </div

  <hr>
  
  <div class="step" id="crypto">
    <h3>4. <g:message code="fedreg.templates.serviceprovider.create.crypto.heading" /></h3>
    <p><g:message code="fedreg.templates.serviceprovider.create.crypto.details" /></p>

    <fieldset>
      <div class="clearfix">
          <label for="newcertificatedata"><g:message code="label.certificate" /></label>
        <div class="input">
          <div id="newcertificatedetails">
          </div>
          <g:hiddenField name="sp.crypto.sig" value="${true}" />
          <g:hiddenField name="sp.crypto.enc" value="${true}" />
          <g:textArea name="cert" id="cert" class="cert" rows="25" cols="60" value="${certificate}"/>
          <fr:tooltip code='fedreg.help.serviceprovider.certificate' />
        </div>
      </div>
  </div>

  <hr>
  
  <div class="step" id="attributesupport">
    <h3>5. <g:message code="fedreg.templates.serviceprovider.create.attributesupport.heading" /></h3>
    <p><g:message code="fedreg.templates.serviceprovider.create.attributesupport.details" /></p>
    <p><strong><g:message code="fedreg.help.serviceprovider.attribute.isrequired" /></strong></p>

    <table>
      <tbody>
        <tr>
          <th><g:message code="label.name" /></th>
          <th><g:message code="label.category" /></th>
          <th><g:message code="label.requested" /></th>
          <th class="nowrap"><g:message code="label.required" /> <fr:tooltip code='fedreg.help.serviceprovider.attribute.isrequired' /></th>
          <th><g:message code="label.reasonrequested" /> <fr:tooltip code='fedreg.help.serviceprovider.attribute.reason' /></th>
        </tr>
        <g:each in="${attributeList}" var="attr" status="i">
          <g:if test="${!attr.specificationRequired}">
            <g:set var="ra" value="${supportedAttributes.find {it.base == attr}}" />
            <tr>
              <td>
                <strong>${fieldValue(bean: attr, field: "name")}</strong><br>
                <code>oid:${fieldValue(bean: attr, field: "oid")}</code>
                <br><br><em>${fieldValue(bean: attr, field: "description")}</em>
              </td>
              <td class="centered">
                ${fieldValue(bean: attr, field: "category.name")}
              </td>
              <td class="centered">
                <g:checkBox name="sp.attributes.${attr.id}.requested" id="spattributes${attr.id}requested" onClick="\$('#spattributes${attr.id}reasoning').toggleClass('required'); if(!\$(this).is(':checked') && \$('#spattributes${attr.id}required').is(':checked')) {\$('#spattributes${attr.id}required').attr('checked', false);}" checked="${ra}"/>
              </td>
              <td class="centered">
                <g:checkBox name="sp.attributes.${attr.id}.required" id="spattributes${attr.id}required" checked="${ra?.isRequired}" onClick="if(\$(this).is(':checked') && !\$('#spattributes${attr.id}requested').is(':checked')) {\$('#spattributes${attr.id}requested').attr('checked', true);}"/>
              </td>
              <td>
                <input name="sp.attributes.${attr.id}.reasoning" id="spattributes${attr.id}reasoning" size="40" value="${ra?.reasoning}" />
              </td>
            </tr>
          </g:if>
        </g:each>
      </tbody>
    </table>
  </div>
  
  <hr>
  
  <div class="step submit_step" id="creationsummary">
    <h3>6. <g:message code="fedreg.templates.serviceprovider.create.summary.heading" /></h3>
    <p><g:message code="fedreg.templates.serviceprovider.create.summary.details" /></p>

    <div class="row">
      <div class="offset12">
        <g:submitButton name="submit" value="Submit" class="btn success"/>
      </div>
    </div>
  </div>

  </div>
  </div>

</g:form>