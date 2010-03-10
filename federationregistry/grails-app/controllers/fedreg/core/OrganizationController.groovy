package fedreg.core

class OrganizationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 20, 100)
        [organizationList: Organization.list(params), organizationTotal: Organization.count()]
    }

    def create = {
        def organization = new Organization()
        organization.properties = params
        return [organization: organization]
    }

    def save = {
        def organization = new Organization(params)
        if (organization.save(flush: true)) {
			flash.type="success"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'organization.label'), organization.toString()])}"
            redirect(action: "show", id: organization.id)
        }
        else {
            render(view: "create", model: [organization: organization])
        }
    }

    def show = {
        def organization = Organization.get(params.id)
        if (!organization) {
			flash.type="error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organization.label'), params.id])}"
            redirect(action: "list")
        }
        else {
			def entities = EntityDescriptor.findAllWhere(organization:organization)
            [organization: organization, entities:entities]
        }
    }

    def edit = {
        def organization = Organization.get(params.id)
        if (!organization) {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organization.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [organization: organization]
        }
    }

    def update = {
        def organization = Organization.get(params.id)
        if (organization) {
            if (params.version) {
                def version = params.version.toLong()
                if (organization.version > version) {
                    
                    organization.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'organization.label')])
                    render(view: "edit", model: [organization: organization])
                    return
                }
            }
            organization.properties = params
            if (!organization.hasErrors() && organization.save(flush: true)) {
				flash.type = "success"
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'organization.label'), organization.id])}"
                redirect(action: "show", id: organization.id)
            }
            else {
                render(view: "edit", model: [organization: organization])
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organization.label'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def organization = Organization.get(params.id)
        if (organization) {
            try {
                organization.delete(flush: true)
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
