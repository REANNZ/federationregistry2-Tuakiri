package aaf.fedreg.core

class AttributeScopeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [attributeScopeInstanceList: AttributeScope.list(params), attributeScopeInstanceTotal: AttributeScope.count()]
    }

    def create = {
        def attributeScopeInstance = new AttributeScope()
        attributeScopeInstance.properties = params
        return [attributeScopeInstance: attributeScopeInstance]
    }

    def save = {
        def attributeScopeInstance = new AttributeScope(params)
        if (attributeScopeInstance.save(flush: true)) {
			flash.type="success"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'attributeScope.label'), attributeScopeInstance.toString()])}"
            redirect(action: "show", id: attributeScopeInstance.id)
        }
        else {
            render(view: "create", model: [attributeScopeInstance: attributeScopeInstance])
        }
    }

    def show = {
        def attributeScopeInstance = AttributeScope.get(params.id)
        if (!attributeScopeInstance) {
			flash.type="error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'attributeScope.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            [attributeScopeInstance: attributeScopeInstance]
        }
    }

    def edit = {
        def attributeScopeInstance = AttributeScope.get(params.id)
        if (!attributeScopeInstance) {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'attributeScope.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [attributeScopeInstance: attributeScopeInstance]
        }
    }

    def update = {
        def attributeScopeInstance = AttributeScope.get(params.id)
        if (attributeScopeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (attributeScopeInstance.version > version) {
                    
                    attributeScopeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'attributeScope.label')])
                    render(view: "edit", model: [attributeScopeInstance: attributeScopeInstance])
                    return
                }
            }
            attributeScopeInstance.properties = params
            if (!attributeScopeInstance.hasErrors() && attributeScopeInstance.save(flush: true)) {
				flash.type = "success"
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'attributeScope.label'), attributeScopeInstance.id])}"
                redirect(action: "show", id: attributeScopeInstance.id)
            }
            else {
                render(view: "edit", model: [attributeScopeInstance: attributeScopeInstance])
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'attributeScope.label'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def attributeScopeInstance = AttributeScope.get(params.id)
        if (attributeScopeInstance) {
            try {
                attributeScopeInstance.delete(flush: true)
				flash.type = "success"
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'attributeScope.label'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.type = "error"
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'attributeScope.label'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'attributeScope.label'), params.id])}"
            redirect(action: "list")
        }
    }

}
