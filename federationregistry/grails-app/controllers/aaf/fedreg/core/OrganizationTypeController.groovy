package aaf.fedreg.core

class OrganizationTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [organizationTypeInstanceList: OrganizationType.list(params), organizationTypeInstanceTotal: OrganizationType.count()]
    }

    def create = {
        def organizationTypeInstance = new OrganizationType()
        organizationTypeInstance.properties = params
        return [organizationTypeInstance: organizationTypeInstance]
    }

    def save = {
        def organizationTypeInstance = new OrganizationType(params)
        if (organizationTypeInstance.save(flush: true)) {
			flash.type="success"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'organizationType.label'), organizationTypeInstance.toString()])}"
            redirect(action: "show", id: organizationTypeInstance.id)
        }
        else {
            render(view: "create", model: [organizationTypeInstance: organizationTypeInstance])
        }
    }

    def show = {
        def organizationTypeInstance = OrganizationType.get(params.id)
        if (!organizationTypeInstance) {
			flash.type="error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organizationType.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            [organizationTypeInstance: organizationTypeInstance]
        }
    }

    def edit = {
        def organizationTypeInstance = OrganizationType.get(params.id)
        if (!organizationTypeInstance) {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organizationType.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [organizationTypeInstance: organizationTypeInstance]
        }
    }

    def update = {
        def organizationTypeInstance = OrganizationType.get(params.id)
        if (organizationTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (organizationTypeInstance.version > version) {
                    
                    organizationTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'organizationType.label')])
                    render(view: "edit", model: [organizationTypeInstance: organizationTypeInstance])
                    return
                }
            }
            organizationTypeInstance.properties = params
            if (!organizationTypeInstance.hasErrors() && organizationTypeInstance.save(flush: true)) {
				flash.type = "success"
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'organizationType.label'), organizationTypeInstance.id])}"
                redirect(action: "show", id: organizationTypeInstance.id)
            }
            else {
                render(view: "edit", model: [organizationTypeInstance: organizationTypeInstance])
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organizationType.label'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def organizationTypeInstance = OrganizationType.get(params.id)
        if (organizationTypeInstance) {
            try {
                organizationTypeInstance.delete(flush: true)
				flash.type = "success"
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'organizationType.label'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.type = "error"
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'organizationType.label'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organizationType.label'), params.id])}"
            redirect(action: "list")
        }
    }

}
