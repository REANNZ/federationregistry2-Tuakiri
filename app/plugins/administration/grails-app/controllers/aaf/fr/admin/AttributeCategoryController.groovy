package aaf.fr.admin

import org.springframework.dao.DataIntegrityViolationException
import aaf.fr.foundation.*

class AttributeCategoryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        [attributeCategoryInstanceList: AttributeCategory.list(), attributeCategoryInstanceTotal: AttributeCategory.count()]
    }

    def create() {
        [attributeCategoryInstance: new AttributeCategory(params)]
    }

    def save() {
        def attributeCategoryInstance = new AttributeCategory(params)
        if (!attributeCategoryInstance.save(flush: true)) {
            render(view: "create", model: [attributeCategoryInstance: attributeCategoryInstance])
            return
        }

    flash.message = message(code: 'default.created.message', args: [message(code: 'attributeCategory.label', default: 'AttributeCategory'), attributeCategoryInstance.id])
        redirect(action: "show", id: attributeCategoryInstance.id)
    }

    def show() {
        def attributeCategoryInstance = AttributeCategory.get(params.id)
        if (!attributeCategoryInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'attributeCategory.label', default: 'AttributeCategory'), params.id])
            redirect(action: "list")
            return
        }

        [attributeCategoryInstance: attributeCategoryInstance]
    }

    def edit() {
        def attributeCategoryInstance = AttributeCategory.get(params.id)
        if (!attributeCategoryInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'attributeCategory.label', default: 'AttributeCategory'), params.id])
            redirect(action: "list")
            return
        }

        [attributeCategoryInstance: attributeCategoryInstance]
    }

    def update() {
        def attributeCategoryInstance = AttributeCategory.get(params.id)
        if (!attributeCategoryInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'attributeCategory.label', default: 'AttributeCategory'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (attributeCategoryInstance.version > version) {
                attributeCategoryInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'attributeCategory.label', default: 'AttributeCategory')] as Object[],
                          "Another user has updated this AttributeCategory while you were editing")
                render(view: "edit", model: [attributeCategoryInstance: attributeCategoryInstance])
                return
            }
        }

        attributeCategoryInstance.properties = params

        if (!attributeCategoryInstance.save(flush: true)) {
            render(view: "edit", model: [attributeCategoryInstance: attributeCategoryInstance])
            return
        }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'attributeCategory.label', default: 'AttributeCategory'), attributeCategoryInstance.id])
        redirect(action: "show", id: attributeCategoryInstance.id)
    }

    def delete() {
        def attributeCategoryInstance = AttributeCategory.get(params.id)
        if (!attributeCategoryInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'attributeCategory.label', default: 'AttributeCategory'), params.id])
            redirect(action: "list")
            return
        }

        try {
            attributeCategoryInstance.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'attributeCategory.label', default: 'AttributeCategory'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'attributeCategory.label', default: 'AttributeCategory'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
