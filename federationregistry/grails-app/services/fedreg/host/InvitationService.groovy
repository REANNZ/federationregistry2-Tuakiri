package fedreg.host

import java.security.MessageDigest
import grails.plugins.nimble.core.*

class InvitationService {
	
	def roleService
	def groupService

	def create(group, role, permission, controller, action, objID) {
		def invitation = new Invitation(group:group, role:role, permission:permission, controller:controller, action:action, objID:objID)
		
		def hash = "$group-$role-$permission-$controller-$action-$objID-${System.currentTimeMillis().toString()}"
		
		def messageDigest = MessageDigest.getInstance("SHA1")
		messageDigest.update( hash.getBytes() );
		def sha1Hex = new BigInteger(1, messageDigest.digest()).toString(16).padLeft( 40, '0' )

		invitation.inviteCode = sha1Hex
		invitation.save()
		if(invitation.hasErrors()) {
			invitation.errors.each {
				log.error it
			}
			throw new RuntimeException ("Error when attempting to create invitation") 
		}
		
		invitation
	}
	
	def claim(inviteCode) {
		def invite = Invitation.findWhere(inviteCode:inviteCode)
		if(invite) {
			if(!invite.utilized) {
				invite.utilized = true
				invite.utilizedBy = authenticatedUser
				
				if(invite.role) {
					def role = grails.plugins.nimble.core.Role.get(invite.role) 
					if(role){
						roleService.addMember(authenticatedUser, role)
					} else {
						throw new RuntimeException ("Invite $inviteCode references role with id ${invite.role} but this doesn't exist")
					}
				}
				if(invite.group) {
					def group = grails.plugins.nimble.core.Group.get(invite.group)
					if(group){
						groupService.addMember(authenticatedUser, group)
					} else {
						throw new RuntimeException ("Invite $inviteCode references group with id ${invite.group} but this doesn't exist")
					}
				}
				
				invite.save()
				if(invite.hasErrors()) {
					invite.errors.each { log.warn it }
					throw new RuntimeException ("Error when attempting to utilize invitation, unable to save state")
				}
				invite
			} else {
				log.warn "Attempt by $authenticatedUser to re-use invitation code $inviteCode"
				null
			}
		}
		else {
			log.error "Possible attempt to breach invite system located. Request submitted invite code $inviteCode"
			throw new RuntimeException ("Error when attempting to retrieve invitation, no such identifier")
		}
	}

}