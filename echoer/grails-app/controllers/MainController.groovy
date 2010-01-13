class MainController {

    def grailsApplication

    def index = {
        def attr = [:]
		attr.put('REMOTE_USER', request.remoteUser)
		
		request.headerNames.each {
			attr.put(it, request.getHeader(it))
		}

		
		return [attr: attr]
    }
}