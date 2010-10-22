package fedreg.metadata

import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat
import fedreg.core.*

class AttributeFilterGenerationService {

	def generate(builder, id, groupID, idp) {
		builder.mkp.xmlDeclaration(version:'1.0')
		attributeFilterPolicyGroup(builder, id, groupID, idp)
	}
	
	def attributeFilterPolicyGroup(builder, id, groupID, idp) {
		builder.AttributeFilterPolicyGroup(id:id,
			"xmlns":"urn:mace:shibboleth:2.0:afp",
			"xmlns:basic":"urn:mace:shibboleth:2.0:afp:mf:basic",
			"xmlns:saml":"urn:mace:shibboleth:2.0:afp:mf:saml",
			"xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance",
			"xsi:schemaLocation":"urn:mace:shibboleth:2.0:afp classpath:/schema/shibboleth-2.0-afp.xsd urn:mace:shibboleth:2.0:afp:mf:basic classpath:/schema/shibboleth-2.0-afp-mf-basic.xsd urn:mace:shibboleth:2.0:afp:mf:saml classpath:/schema/shibboleth-2.0-afp-mf-saml.xsd") 
			{
				defaultReleasePolicy(builder, groupID)
				// Process all our services
				def identityProvider = IDPSSODescriptor.get(idp)
				if(!identityProvider)
					throw new RuntimeException("Identity Provider specifed ($idp) for Attribute Filter creation does not exist.")
					
				def serviceProviders = SPSSODescriptor.findAllWhere(active:true, approved:true)
				serviceProviders?.each { serviceProvider ->
					servicePolicy(builder, identityProvider, serviceProvider, groupID)
				}
				builder.mkp.yield "\n"
			}
	}

	def defaultReleasePolicy(builder, groupID) {
		builder.mkp.yield "\n"
		builder.AttributeFilterPolicy(id:"afp_default_for:$groupID") {
			PolicyRequirementRule('xsi:type':"basic:ANY")

			AttributeRule(attributeID:"persistentID") {
				PermitValueRule("xsi:type" : "basic:ANY")
			}

			AttributeRule("attributeID" : "transientId") {
				PermitValueRule("xsi:type" : "basic:ANY")
			}
		}
	}
	
	def servicePolicy(builder, identityProvider, serviceProvider, groupID) {
		builder.mkp.yield "\n"
		comment builder, "Attribute release filter for SP ${serviceProvider.displayName} in entity ${serviceProvider.entityDescriptor.entityID}"
		builder.AttributeFilterPolicy(id:"afp_for:${serviceProvider.entityDescriptor.entityID}") {
			PolicyRequirementRule("xsi:type" : "basic:AND") {
				"basic:Rule"("xsi:type":"basic:AttributeRequesterString", value:"${serviceProvider.entityDescriptor.entityID}")
				"basic:Rule"("xsi:type":"saml:AttributeRequesterInEntityGroup", groupID:groupID)
			}
			serviceProvider.attributeConsumingServices.each { acs ->
				acs.requestedAttributes.sort{it.base.alias}.each { ra ->
					if(identityProvider.attributes.findAll{it.base == ra.base}.size() == 1) {
						if(!ra.base.specificationRequired) {
							AttributeRule(attributeID:ra.base.alias){
								PermitValueRule("xsi:type":"basic:ANY")
							}
						} else {
							if(ra.values?.size() > 0) {
								AttributeRule(attributeID:ra.base.alias){
									PermitValueRule("xsi:type":"basic:OR") {
										ra.values.sort{it.value}.each { v ->
											"basic:Rule" ("xsi:type":"basic:AttributeValueString", value:v.value, ignoreCase:"true")
										}
									}
								}
							}
						}
					}
					else{
						comment builder, "Attribute ${ra.base.alias} is not supported by this Identity Provider. Please configure your Attribute Resolver accordingly and indicate your support using Federation Registry"
						if(ra.isRequired)
							comment builder, "Additionally the attribute ${ra.base.alias} is required by this service provider. User access to this service will fail."
					}
				}
			}
		}
	}
	
	def comment(builder, comment) {
		builder.mkp.yieldUnescaped "\n<!-- $comment -->"
	}

}