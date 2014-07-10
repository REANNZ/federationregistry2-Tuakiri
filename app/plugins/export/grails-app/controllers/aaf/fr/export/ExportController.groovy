package aaf.fr.export

import grails.converters.*
import aaf.fr.foundation.*

import groovy.json.JsonSlurper

class ExportController {
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

  private def export(def clazz, long id, String json_name) {
    def obj = clazz.get(id)

    if(obj){
      def builder = new groovy.json.JsonBuilder()
      builder { "${json_name}" obj.structureAsJson() }
      render text: builder.toPrettyString(), contentType: "text/json"
    } else {
      response.status = 400
      render([error: "${clazz.name} instance is unavailable"] as JSON)
    }
  }

  private def exportList(def clazz, String json_name) {
    def builder = new groovy.json.JsonBuilder()
    builder { "${json_name}" clazz.list().collect { it.structureAsJson() }
    }
    render text: builder.toPrettyString(), contentType: "text/json"
  }
}
