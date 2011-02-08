// FR Base (createFRBase.groovy)
roleService = ctx.getBean("roleService")

// Create federation-administrators role, used in workflows etc
def fedAdminRole = Role.findWhere(name:"federation-administrators")
if(!fedAdminRole)
	roleService.createRole("federation-administrators", "Role representing federation level administrators who can make decisions onbehalf of the entire federation, particuarly in workflows", false)

// Populate default administrative account if required
if(User.count() == 0) {
	def profile = new Profile(email:'internaladministrator@not.valid')
	def user = new User(username:'internaladministrator', enabled: false, external:false, federated: false, profile: profile)
	user.save(flush: true)
}