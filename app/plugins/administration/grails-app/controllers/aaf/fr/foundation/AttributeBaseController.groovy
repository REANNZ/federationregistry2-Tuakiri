package aaf.fr.foundation

import org.springframework.dao.DataIntegrityViolationException

class AttributeBaseController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        [attributeBaseInstanceList: AttributeBase.list(), attributeBaseInstanceTotal: AttributeBase.count()]
    }

    def create() {
        [attributeBaseInstance: new AttributeBase(params)]
    }

    def save() {
        def attributeBaseInstance = new AttributeBase(params)
        if (!attributeBaseInstance.save(flush: true)) {
            render(view: "create", model: [attributeBaseInstance: attributeBaseInstance])
            return
        }

    flash.message = message(code: 'default.created.message', args: [message(code: 'attributeBase.label', default: 'AttributeBase'), attributeBaseInstance.id])
        redirect(action: "show", id: attributeBaseInstance.id)
    }

    def show() {
        def attributeBaseInstance = AttributeBase.get(params.id)
        if (!attributeBaseInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'attributeBase.label', default: 'AttributeBase'), params.id])
            redirect(action: "list")
            return
        }

        [attributeBaseInstance: attributeBaseInstance]
    }

    def edit() {
        def attributeBaseInstance = AttributeBase.get(params.id)
        if (!attributeBaseInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'attributeBase.label', default: 'AttributeBase'), params.id])
            redirect(action: "list")
            return
        }

        [attributeBaseInstance: attributeBaseInstance]
    }

    def update() {
        def attributeBaseInstance = AttributeBase.get(params.id)
        if (!attributeBaseInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'attributeBase.label', default: 'AttributeBase'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (attributeBaseInstance.version > version) {
                attributeBaseInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'attributeBase.label', default: 'AttributeBase')] as Object[],
                          "Another user has updated this AttributeBase while you were editing")
                render(view: "edit", model: [attributeBaseInstance: attributeBaseInstance])
                return
            }
        }

        attributeBaseInstance.properties = params

        if (!attributeBaseInstance.save(flush: true)) {
            render(view: "edit", model: [attributeBaseInstance: attributeBaseInstance])
            return
        }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'attributeBase.label', default: 'AttributeBase'), attributeBaseInstance.id])
        redirect(action: "show", id: attributeBaseInstance.id)
    }

    def delete() {
        def attributeBaseInstance = AttributeBase.get(params.id)
        if (!attributeBaseInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'attributeBase.label', default: 'AttributeBase'), params.id])
            redirect(action: "list")
            return
        }

        try {
            attributeBaseInstance.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'attributeBase.label', default: 'AttributeBase'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'attributeBase.label', default: 'AttributeBase'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
