package fedreg.host

class InvitationController {
	
	def invitationService
	
	def claim = {
		if(!params.code) {
			response.sendError(403)
			return
		}
		
		def invite = invitationService.claim(params.code)
		if(invite) {
			log.info "The account $authenticatedUser successfully claimed $invite"
			redirect controller:invite.controller, action:invite.action, id:invite.objID
		}
		else {
			log.info "The account $authenticatedUser tried to claim ${params.code} which was previously used, denying."
			response.sendError(403)
			return
		}
	}
}