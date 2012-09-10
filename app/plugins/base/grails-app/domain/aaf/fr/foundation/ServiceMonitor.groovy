package aaf.fr.foundation

/**
 * @author Bradley Beddoes
 */
class ServiceMonitor  {
  static auditable = true
  
  MonitorType type
  String url
  String node
  boolean enabled = true
  int checkPeriod = 0

  static constraints = {
    type(nullable: false)
    url(nullable: false, blank: false)
    node(nullable:true)
  }
  
  static belongsTo = [roleDescriptor:RoleDescriptor]

  public String toString() {  "servicemonitor:[id:$id, url: $url, type:$type]" }
}
