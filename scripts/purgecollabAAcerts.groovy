// Required for a 0.9.2 to 0.9.3 upgrade where data already exists
import fedreg.core.*

def idpList = IDPSSODescriptor.list()

idpList.each{ idp ->

  def aa = idp.collaborator
  if(aa) {
    def keys = KeyDescriptor.findAllWhere(roleDescriptor:aa)
    keys.each {
		println "Removing $it"
		it.delete()
	}
  }
}

​