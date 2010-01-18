package aaf.fedreg

class AttributeCategoryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [attributeCategoryInstanceList: AttributeCategory.list(params), attributeCategoryInstanceTotal: AttributeCategory.count()]
    }

    def create = {
        def attributeCategoryInstance = new AttributeCategory()
        attributeCategoryInstance.properties = params
        return [attributeCategoryInstance: attributeCategoryInstance]
    }

    def save = {
        def attributeCategoryInstance = new AttributeCategory(params)
        if (attributeCategoryInstance.save(flush: true)) {
			flash.type="success"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'attributeCategory.label'), attributeCategoryInstance.toString()])}"
            redirect(action: "show", id: attributeCategoryInstance.id)
        }
        else {
            render(view: "create", model: [attributeCategoryInstance: attributeCategoryInstance])
        }
    }

    def show = {
        def attributeCategoryInstance = AttributeCategory.get(params.id)
        if (!attributeCategoryInstance) {
			flash.type="error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'attributeCategory.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            [attributeCategoryInstance: attributeCategoryInstance]
        }
    }

    def edit = {
        def attributeCategoryInstance = AttributeCategory.get(params.id)
        if (!attributeCategoryInstance) {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'attributeCategory.label'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [attributeCategoryInstance: attributeCategoryInstance]
        }
    }

    def update = {
        def attributeCategoryInstance = AttributeCategory.get(params.id)
        if (attributeCategoryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (attributeCategoryInstance.version > version) {
                    
                    attributeCategoryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'attributeCategory.label')])
                    render(view: "edit", model: [attributeCategoryInstance: attributeCategoryInstance])
                    return
                }
            }
            attributeCategoryInstance.properties = params
            if (!attributeCategoryInstance.hasErrors() && attributeCategoryInstance.save(flush: true)) {
				flash.type = "success"
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'attributeCategory.label'), attributeCategoryInstance.id])}"
                redirect(action: "show", id: attributeCategoryInstance.id)
            }
            else {
                render(view: "edit", model: [attributeCategoryInstance: attributeCategoryInstance])
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'attributeCategory.label'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def attributeCategoryInstance = AttributeCategory.get(params.id)
        if (attributeCategoryInstance) {
            try {
                attributeCategoryInstance.delete(flush: true)
				flash.type = "success"
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'attributeCategory.label'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.type = "error"
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'attributeCategory.label'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
			flash.type = "error"
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'attributeCategory.label'), params.id])}"
            redirect(action: "list")
        }
    }

}
