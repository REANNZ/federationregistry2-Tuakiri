package fedreg.core

class OrganizationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [organizationInstanceList: Organization.list(params), organizationInstanceTotal: Organization.count()]
    }

    def create = {
        def organizationInstance = new Organization()
        organizationInstance.properties = params
        return [organizationInstance: organizationInstance]
    }

    def save = {
        def organizationInstance = new Organization(params)
        if (organizationInstance.save(flush: true)) {
			flash.type="success"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'organization.label'), organizationInstance.toString()])}"
            redirect(action: "show", id: organizationInstance.id)
        }
        else {
            render(view: "create", model: [organizationInstance: organizationInstance])
        }
    }

    def show = {
        def organizationInstance = Organization.get(params.id)
        if (!organizationInstance) {
			flash.type="error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organization.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            [organizationInstance: organizationInstance]
        }
    }

    def edit = {
        def organizationInstance = Organization.get(params.id)
        if (!organizationInstance) {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organization.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [organizationInstance: organizationInstance]
        }
    }

    def update = {
        def organizationInstance = Organization.get(params.id)
        if (organizationInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (organizationInstance.version > version) {
                    
                    organizationInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'organization.label')])
                    render(view: "edit", model: [organizationInstance: organizationInstance])
                    return
                }
            }
            organizationInstance.properties = params
            if (!organizationInstance.hasErrors() && organizationInstance.save(flush: true)) {
				flash.type = "success"
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'organization.label'), organizationInstance.id])}"
                redirect(action: "show", id: organizationInstance.id)
            }
            else {
                render(view: "edit", model: [organizationInstance: organizationInstance])
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organization.label'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def organizationInstance = Organization.get(params.id)
        if (organizationInstance) {
            try {
                organizationInstance.delete(flush: true)
				flash.type = "success"
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'organization.label'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.type = "error"
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'organization.label'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organization.label'), params.id])}"
            redirect(action: "list")
        }
    }

}
