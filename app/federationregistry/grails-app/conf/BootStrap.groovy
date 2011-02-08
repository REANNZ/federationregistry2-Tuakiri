
class BootStrap {
	def nimbleService

     def init = { servletContext ->
		nimbleService.init()
     }

     def destroy = {
     }
} 