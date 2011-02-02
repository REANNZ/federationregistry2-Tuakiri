import grails.plugins.nimble.core.*
import fedreg.core.*

def roleService = ctx.getBean('roleService')
def permissionService = ctx.getBean('permissionService')

def idps = IDPSSODescriptor.list()

idps.each { idp ->
  def role = Role.findWhere(name:"descriptor-${idp.id}-administrators")

  if(role && idp.collaborator) {

    def permission = new LevelPermission()       
    permission.populate("descriptor", "${idp.collaborator.id}", "*", null, null, null)
    permission.managed = false
    permissionService.createPermission(permission, role)
      
  }
}​