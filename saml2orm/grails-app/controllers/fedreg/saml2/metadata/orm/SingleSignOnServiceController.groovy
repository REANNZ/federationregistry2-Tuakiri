package fedreg.saml2.metadata.orm

class SingleSignOnServiceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [singleSignOnServiceInstanceList: SingleSignOnService.list(params), singleSignOnServiceInstanceTotal: SingleSignOnService.count()]
    }

    def create = {
        def singleSignOnServiceInstance = new SingleSignOnService()
        singleSignOnServiceInstance.properties = params
        return [singleSignOnServiceInstance: singleSignOnServiceInstance]
    }

    def save = {
        def singleSignOnServiceInstance = new SingleSignOnService(params)
        if (singleSignOnServiceInstance.save(flush: true)) {
			flash.type="success"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'singleSignOnService.label'), singleSignOnServiceInstance.toString()])}"
            redirect(action: "show", id: singleSignOnServiceInstance.id)
        }
        else {
            render(view: "create", model: [singleSignOnServiceInstance: singleSignOnServiceInstance])
        }
    }

    def show = {
        def singleSignOnServiceInstance = SingleSignOnService.get(params.id)
        if (!singleSignOnServiceInstance) {
			flash.type="error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'singleSignOnService.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            [singleSignOnServiceInstance: singleSignOnServiceInstance]
        }
    }

    def edit = {
        def singleSignOnServiceInstance = SingleSignOnService.get(params.id)
        if (!singleSignOnServiceInstance) {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'singleSignOnService.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [singleSignOnServiceInstance: singleSignOnServiceInstance]
        }
    }

    def update = {
        def singleSignOnServiceInstance = SingleSignOnService.get(params.id)
        if (singleSignOnServiceInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (singleSignOnServiceInstance.version > version) {
                    
                    singleSignOnServiceInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'singleSignOnService.label')])
                    render(view: "edit", model: [singleSignOnServiceInstance: singleSignOnServiceInstance])
                    return
                }
            }
            singleSignOnServiceInstance.properties = params
            if (!singleSignOnServiceInstance.hasErrors() && singleSignOnServiceInstance.save(flush: true)) {
				flash.type = "success"
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'singleSignOnService.label'), singleSignOnServiceInstance.id])}"
                redirect(action: "show", id: singleSignOnServiceInstance.id)
            }
            else {
                render(view: "edit", model: [singleSignOnServiceInstance: singleSignOnServiceInstance])
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'singleSignOnService.label'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def singleSignOnServiceInstance = SingleSignOnService.get(params.id)
        if (singleSignOnServiceInstance) {
            try {
                singleSignOnServiceInstance.delete(flush: true)
				flash.type = "success"
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'singleSignOnService.label'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.type = "error"
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'singleSignOnService.label'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'singleSignOnService.label'), params.id])}"
            redirect(action: "list")
        }
    }

}
