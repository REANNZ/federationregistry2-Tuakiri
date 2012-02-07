package aaf.fr.app

/**
 * Provides invitation management endpoints
 *
 * @author Bradley Beddoes
 */
class InvitationController {
	
	def invitationService
	
	def claim = {
		if(!params.code) {
			response.sendError(403)
			return
		}
		
		def invite = invitationService.claim(params.code)
		if(invite) {
			log.info "The account $subject successfully claimed $invite"
			redirect controller:invite.controller, action:invite.action, id:invite.objID
		}
		else {
			log.info "The account $subject tried to claim ${params.code} which was previously used, denying."
			redirect action: invalidcode
		}
	}

	def invalidcode = {}
}