package aaf.fr.export

import grails.converters.*
import aaf.fr.foundation.*

import groovy.json.JsonSlurper

class ExportController {
  final def authorizeRegex = ~/AAF-FR-EXPORT service="([^"]+)", key="([^"]*)?"/

  def grailsApplication
  def beforeInterceptor = [action: this.&auth]

  def organization(long id) {
    export(Organization, id, 'organization')
  }

  def organizations() {
    exportList(Organization, 'organizations')
  }

  def entitydescriptor(long id) {
    export(EntityDescriptor, id, 'entity_descriptor')
  }

  def entitydescriptors() {
    exportList(EntityDescriptor, 'entity_descriptors')
  }

  def serviceprovider(long id) {
    export(SPSSODescriptor, id, 'service_provider')
  }

  def serviceproviders() {
    exportList(SPSSODescriptor, 'service_providers')
  }

  def identityprovider(long id) {
    export(IDPSSODescriptor, id, 'identity_provider')
  }

  def identityproviders() {
    exportList(IDPSSODescriptor, 'identity_providers')
  }

  def attributeauthority(long id) {
    export(AttributeAuthorityDescriptor, id, 'attribute_authority')
  }

  def attributeauthorities() {
    exportList(AttributeAuthorityDescriptor, 'attribute_authorities')
  }

  def attribute(long id) {
    export(AttributeBase, id, 'attribute')
  }

  def attributes() {
    exportList(AttributeBase, 'attributes')
  }

  def attributecategory(long id) {
    export(AttributeCategory, id, 'attributecategory')
  }

  def attributecategories() {
    exportList(AttributeCategory, 'attribute_categories')
  }

  def contact(long id) {
    export(Contact, id, 'contact')
  }

  def contacts() {
    exportList(Contact, 'contacts')
  }

  def contacttype(long id) {
    export(ContactType, id, 'contact_type')
  }

  def contacttypes() {
    exportList(ContactType, 'contact_types')
  }

  def samluri(long id) {
    export(SamlURI, id, 'saml_uri')
  }

  def samluris() {
    exportList(SamlURI, 'saml_uris')
  }

  def monitortype(long id) {
    export(MonitorType, id, 'monitor_type')
  }

  def monitortypes() {
    exportList(MonitorType, 'monitor_types')
  }

  def organizationtype(long id) {
    export(OrganizationType, id, 'organization_type')
  }

  def organizationtypes() {
    exportList(OrganizationType, 'organization_types')
  }

  def subject(long id) {
    export(aaf.fr.identity.Subject, id, 'subject')
  }

  def subjects() {
    exportList(aaf.fr.identity.Subject, 'subjects')
  }

  private auth() {
    if (!grailsApplication.config.aaf.fr.export.enabled) {
      log.error "[Requester: ${request.remoteHost}] - Attempt to access export functionality when not enabled"
      response.status = 404
      return false
    }

    // Authorize header in format:
    // Authorize: AAF-FR-EXPORT service="...", key="..."
    def authorize = request.getHeader('Authorization')
    if(!authorize || !(authorize ==~ authorizeRegex)) {
      response.status = 403
      render([error: "Invalid Authorization token"] as JSON)
      log.error "[Requester: ${request.remoteHost}] - Authentication halted as Authorize header provides invalid data for AAF-FR-EXPORT scheme."
      return false
    }

    def authorizeTokens = authorize =~ authorizeRegex
    def service = authorizeTokens[0][1]
    def key = authorizeTokens[0][2]

    if(key != grailsApplication.config.aaf.fr.export.key) {
      response.status = 403
      render([error: "Invalid Authorization header"] as JSON)
      log.error "[Requester: ${request.remoteHost}] - Authentication halted as Authorize header provided incorrect key."
      return false
    }

    log.info "[Requester: ${request.remoteHost}] - authentication approved for $service to access export functionality"
  }

  private def export(def clazz, long id, String json_name) {
    def obj = clazz.get(id)

    if(obj){
      def builder = new groovy.json.JsonBuilder()
      builder { "${json_name}" obj.structureAsJson() }
      render text: builder.toString(), contentType: "text/json"
    } else {
      response.status = 400
      render([error: "${clazz.name} instance is unavailable"] as JSON)
    }
  }

  private def exportList(def clazz, String json_name) {
    def builder = new groovy.json.JsonBuilder()
    builder { "${json_name}" clazz.list().collect { it.structureAsJson() }
    }
    render text: builder.toString(), contentType: "text/json"
  }
}
