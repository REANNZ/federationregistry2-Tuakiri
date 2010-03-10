package fedreg.core

class IdentityProviderController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [identityProviderInstanceList: IDPSSODescriptor.list(params), identityProviderInstanceTotal: IDPSSODescriptor.count()]
    }

    def create = {
        def identityProviderInstance = new IDPSSODescriptor()
        identityProviderInstance.properties = params
        return [identityProviderInstance: identityProviderInstance]
    }

    def save = {
        def identityProviderInstance = new IDPSSODescriptor(params)
        if (identityProviderInstance.save(flush: true)) {
			flash.type="success"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'identityProvider.label'), identityProviderInstance.toString()])}"
            redirect(action: "show", id: identityProviderInstance.id)
        }
        else {
            render(view: "create", model: [identityProviderInstance: identityProviderInstance])
        }
    }

    def show = {
        def identityProviderInstance = IDPSSODescriptor.get(params.id)
        if (!identityProviderInstance) {
			flash.type="error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'identityProvider.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            [identityProvider: identityProviderInstance]
        }
    }

    def edit = {
        def identityProviderInstance = IDPSSODescriptor.get(params.id)
        if (!identityProviderInstance) {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'identityProvider.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [identityProviderInstance: identityProviderInstance]
        }
    }

    def update = {
        def identityProviderInstance = IDPSSODescriptor.get(params.id)
        if (identityProviderInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (identityProviderInstance.version > version) {
                    
                    identityProviderInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'identityProvider.label')])
                    render(view: "edit", model: [identityProviderInstance: identityProviderInstance])
                    return
                }
            }
            identityProviderInstance.properties = params
            if (!identityProviderInstance.hasErrors() && identityProviderInstance.save(flush: true)) {
				flash.type = "success"
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'identityProvider.label'), identityProviderInstance.id])}"
                redirect(action: "show", id: identityProviderInstance.id)
            }
            else {
                render(view: "edit", model: [identityProviderInstance: identityProviderInstance])
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'identityProvider.label'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def identityProviderInstance = IDPSSODescriptor.get(params.id)
        if (identityProviderInstance) {
            try {
                identityProviderInstance.delete(flush: true)
				flash.type = "success"
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'identityProvider.label'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.type = "error"
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'identityProvider.label'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'identityProvider.label'), params.id])}"
            redirect(action: "list")
        }
    }

}
