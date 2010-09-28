package fedreg.host

class Invitation {

	String inviteCode
	
	Boolean utilized = false
	User utilizedBy
		
	Long role
	Long group
	Long permission
	
	String controller
	String action
	String objID
	
	static constraints = {
		inviteCode(nullable:false, unique:true)
		
		role(nullable:true)
		group(nullable:true)
		permission(nullable:true)
		
		controller(nullable:true)
		action(nullable:true)
		objID(nullable:true)
		
		utilizedBy(nullable:true)
	}
	
	static mapping = {
		group column: "grp__"
		role column: "role__"
		permission column: "perm__"
	}
	
}
