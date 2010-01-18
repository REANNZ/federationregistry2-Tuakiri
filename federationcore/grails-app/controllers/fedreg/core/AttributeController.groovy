package fedreg.core

class AttributeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [attributeInstanceList: Attribute.list(params), attributeInstanceTotal: Attribute.count()]
    }

    def create = {
        def attributeInstance = new Attribute()
        attributeInstance.properties = params
        return [attributeInstance: attributeInstance]
    }

    def save = {
        def attributeInstance = new Attribute(params)
        if (attributeInstance.save(flush: true)) {
			flash.type="success"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'attribute.label'), attributeInstance.toString()])}"
            redirect(action: "show", id: attributeInstance.id)
        }
        else {
            render(view: "create", model: [attributeInstance: attributeInstance])
        }
    }

    def show = {
        def attributeInstance = Attribute.get(params.id)
        if (!attributeInstance) {
			flash.type="error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'attribute.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            [attributeInstance: attributeInstance]
        }
    }

    def edit = {
        def attributeInstance = Attribute.get(params.id)
        if (!attributeInstance) {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'attribute.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [attributeInstance: attributeInstance]
        }
    }

    def update = {
        def attributeInstance = Attribute.get(params.id)
        if (attributeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (attributeInstance.version > version) {
                    
                    attributeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'attribute.label')])
                    render(view: "edit", model: [attributeInstance: attributeInstance])
                    return
                }
            }
            attributeInstance.properties = params
            if (!attributeInstance.hasErrors() && attributeInstance.save(flush: true)) {
				flash.type = "success"
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'attribute.label'), attributeInstance.id])}"
                redirect(action: "show", id: attributeInstance.id)
            }
            else {
                render(view: "edit", model: [attributeInstance: attributeInstance])
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'attribute.label'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def attributeInstance = Attribute.get(params.id)
        if (attributeInstance) {
            try {
                attributeInstance.delete(flush: true)
				flash.type = "success"
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'attribute.label'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.type = "error"
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'attribute.label'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'attribute.label'), params.id])}"
            redirect(action: "list")
        }
    }

}
