package aaf.fr.reporting

class WayfAccessRecord {
  static auditable = true

  String source
  String requestType
  String dsHost

  // Logs provide us an odd format of idpEntity and SP sessioninitiator endpoint.
  // We store these for when things go weird for manual intervention.
  String idpEntity
  String spEndpoint

  // We use ID instead of direct links to allow for descriptors to be deleted without impacting reporting
  long idpID
  long spID

  boolean robot = false

  Date dateCreated

  static constraints = {
    dateCreated(nullable: true)
  }


  static mapping = {
    dateCreated index: 'DateCreated_Idx'
  }
}
