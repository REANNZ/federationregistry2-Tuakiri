package aaf.fedreg.saml2.metadata.orm

class SamlURIController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [samlURIInstanceList: SamlURI.list(params), samlURIInstanceTotal: SamlURI.count()]
    }

    def create = {
        def samlURIInstance = new SamlURI()
        samlURIInstance.properties = params
        return [samlURIInstance: samlURIInstance]
    }

    def save = {
        def samlURIInstance = new SamlURI(params)
        if (samlURIInstance.save(flush: true)) {
			flash.type="success"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'samlURI.label'), samlURIInstance.toString()])}"
            redirect(action: "show", id: samlURIInstance.id)
        }
        else {
            render(view: "create", model: [samlURIInstance: samlURIInstance])
        }
    }

    def show = {
        def samlURIInstance = SamlURI.get(params.id)
        if (!samlURIInstance) {
			flash.type="error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'samlURI.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            [samlURIInstance: samlURIInstance]
        }
    }

    def edit = {
        def samlURIInstance = SamlURI.get(params.id)
        if (!samlURIInstance) {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'samlURI.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [samlURIInstance: samlURIInstance]
        }
    }

    def update = {
        def samlURIInstance = SamlURI.get(params.id)
        if (samlURIInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (samlURIInstance.version > version) {
                    
                    samlURIInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'samlURI.label')])
                    render(view: "edit", model: [samlURIInstance: samlURIInstance])
                    return
                }
            }
            samlURIInstance.properties = params
            if (!samlURIInstance.hasErrors() && samlURIInstance.save(flush: true)) {
				flash.type = "success"
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'samlURI.label'), samlURIInstance.id])}"
                redirect(action: "show", id: samlURIInstance.id)
            }
            else {
                render(view: "edit", model: [samlURIInstance: samlURIInstance])
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'samlURI.label'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def samlURIInstance = SamlURI.get(params.id)
        if (samlURIInstance) {
            try {
                samlURIInstance.delete(flush: true)
				flash.type = "success"
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'samlURI.label'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.type = "error"
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'samlURI.label'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'samlURI.label'), params.id])}"
            redirect(action: "list")
        }
    }

}
