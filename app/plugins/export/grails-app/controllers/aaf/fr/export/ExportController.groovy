package aaf.fr.export

import grails.converters.*
import aaf.fr.foundation.*

import groovy.json.JsonSlurper

class ExportController {
  def organization(long id) {
    def o = Organization.get(id)

    if(o) {
      def builder = new groovy.json.JsonBuilder()
      builder { organization o.structureAsJson() }

      render text: builder.toPrettyString(), contentType: "text/json"
    } else {
      response.status = 404
      render([error: 'organization is unknown'] as JSON)
    }
  }

  def organizations() {
    def builder = new groovy.json.JsonBuilder()
    builder { organizations  Organization.list().collect { o ->
                            o.structureAsJson()
                          }
    }
    render text: builder.toPrettyString(), contentType: "text/json"
  }

  def serviceprovider(long id) {
    def sp = SPSSODescriptor.get(id)

    if(sp){
      def builder = new groovy.json.JsonBuilder()
      builder { serviceprovider sp.structureAsJson() }
      render text: builder.toPrettyString(), contentType: "text/json"
    } else {
      response.status = 400
      render([error: 'serviceprovider is unknown'] as JSON)
    }
  }

  def serviceproviders() {
    def builder = new groovy.json.JsonBuilder()
    builder { serviceproviders  SPSSODescriptor.list().collect { sp ->
                            sp.structureAsJson()
                          }
    }
    render text: builder.toPrettyString(), contentType: "text/json"
  }

  def identityprovider(long id) {
    def idp = IDPSSODescriptor.get(id)

    if(idp){
      def builder = new groovy.json.JsonBuilder()
      builder { identityprovider idp.structureAsJson() }
      render text: builder.toPrettyString(), contentType: "text/json"
    } else {
      response.status = 400
      render([error: 'identityprovider is unknown'] as JSON)
    }
  }

  def identityproviders() {
    def builder = new groovy.json.JsonBuilder()
    builder { identityproviders  IDPSSODescriptor.list().collect {idp ->
                           idp.structureAsJson()
                          }
    }
    render text: builder.toPrettyString(), contentType: "text/json"
  }
}
