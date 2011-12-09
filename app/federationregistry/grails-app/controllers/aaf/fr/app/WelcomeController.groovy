package aaf.fr.app

class WelcomeController {

	def index = {
		if(subject)
			redirect controller:'dashboard', action:'index'
	}
	
}