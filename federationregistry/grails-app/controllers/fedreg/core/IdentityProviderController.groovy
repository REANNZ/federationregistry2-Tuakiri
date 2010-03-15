package fedreg.core

class IdentityProviderController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 20, 100)
        [identityProviderList: IDPSSODescriptor.list(params), identityProviderTotal: IDPSSODescriptor.count()]
    }

    def create = {
        def identityProvider = new IDPSSODescriptor()
        identityProvider.properties = params
        return [identityProvider: identityProvider]
    }

    def save = {
        def identityProvider = new IDPSSODescriptor(params)
        if (identityProvider.save(flush: true)) {
			flash.type="success"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'identityProvider.label'), identityProvider.toString()])}"
            redirect(action: "show", id: identityProvider.id)
        }
        else {
            render(view: "create", model: [identityProvider: identityProvider])
        }
    }

    def show = {
        def identityProvider = IDPSSODescriptor.get(params.id)
        if (!identityProvider) {
			flash.type="error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'identityProvider.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            [identityProvider: identityProvider]
        }
    }

    def edit = {
        def identityProvider = IDPSSODescriptor.get(params.id)
        if (!identityProvider) {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'identityProvider.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [identityProvider: identityProvider]
        }
    }

    def update = {
        def identityProvider = IDPSSODescriptor.get(params.id)
        if (identityProvider) {
            if (params.version) {
                def version = params.version.toLong()
                if (identityProvider.version > version) {
                    
                    identityProvider.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'identityProvider.label')])
                    render(view: "edit", model: [identityProvider: identityProvider])
                    return
                }
            }
            identityProvider.properties = params
            if (!identityProvider.hasErrors() && identityProvider.save(flush: true)) {
				flash.type = "success"
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'identityProvider.label'), identityProvider.id])}"
                redirect(action: "show", id: identityProvider.id)
            }
            else {
                render(view: "edit", model: [identityProvider: identityProvider])
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'identityProvider.label'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def identityProvider = IDPSSODescriptor.get(params.id)
        if (identityProvider) {
            try {
                identityProvider.delete(flush: true)
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
