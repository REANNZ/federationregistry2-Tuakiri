package fedreg.saml2.metadata.orm

class UrlURIController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [urlURIInstanceList: UrlURI.list(params), urlURIInstanceTotal: UrlURI.count()]
    }

    def create = {
        def urlURIInstance = new UrlURI()
        urlURIInstance.properties = params
        return [urlURIInstance: urlURIInstance]
    }

    def save = {
        def urlURIInstance = new UrlURI(params)
        if (urlURIInstance.save(flush: true)) {
			flash.type="success"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'urlURI.label'), urlURIInstance.toString()])}"
            redirect(action: "show", id: urlURIInstance.id)
        }
        else {
            render(view: "create", model: [urlURIInstance: urlURIInstance])
        }
    }

    def show = {
        def urlURIInstance = UrlURI.get(params.id)
        if (!urlURIInstance) {
			flash.type="error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'urlURI.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            [urlURIInstance: urlURIInstance]
        }
    }

    def edit = {
        def urlURIInstance = UrlURI.get(params.id)
        if (!urlURIInstance) {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'urlURI.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [urlURIInstance: urlURIInstance]
        }
    }

    def update = {
        def urlURIInstance = UrlURI.get(params.id)
        if (urlURIInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (urlURIInstance.version > version) {
                    
                    urlURIInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'urlURI.label')])
                    render(view: "edit", model: [urlURIInstance: urlURIInstance])
                    return
                }
            }
            urlURIInstance.properties = params
            if (!urlURIInstance.hasErrors() && urlURIInstance.save(flush: true)) {
				flash.type = "success"
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'urlURI.label'), urlURIInstance.id])}"
                redirect(action: "show", id: urlURIInstance.id)
            }
            else {
                render(view: "edit", model: [urlURIInstance: urlURIInstance])
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'urlURI.label'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def urlURIInstance = UrlURI.get(params.id)
        if (urlURIInstance) {
            try {
                urlURIInstance.delete(flush: true)
				flash.type = "success"
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'urlURI.label'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.type = "error"
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'urlURI.label'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'urlURI.label'), params.id])}"
            redirect(action: "list")
        }
    }

}
