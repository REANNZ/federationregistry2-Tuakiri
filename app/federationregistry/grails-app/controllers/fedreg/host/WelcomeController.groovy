package fedreg.host

class WelcomeController {

	def index = {
		if(authenticatedUser)
			redirect controller:'dashboard', action:'index'
	}
	
}