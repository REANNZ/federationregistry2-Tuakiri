package aaf.fr.admin

import org.springframework.dao.DataIntegrityViolationException
import aaf.fr.foundation.*

class AdminServiceCategoryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        [serviceCategoryInstanceList: ServiceCategory.list(), serviceCategoryInstanceTotal: ServiceCategory.count()]
    }

    def create() {
        [serviceCategoryInstance: new ServiceCategory(params)]
    }

    def save() {
        def serviceCategoryInstance = new ServiceCategory(params)
        if (!serviceCategoryInstance.save(flush: true)) {
            render(view: "create", model: [serviceCategoryInstance: serviceCategoryInstance])
            return
        }

    flash.message = message(code: 'default.created.message', args: [message(code: 'serviceCategory.label', default: 'ServiceCategory'), serviceCategoryInstance.id])
        redirect(action: "show", id: serviceCategoryInstance.id)
    }

    def show() {
        def serviceCategoryInstance = ServiceCategory.get(params.id)
        if (!serviceCategoryInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'serviceCategory.label', default: 'ServiceCategory'), params.id])
            redirect(action: "list")
            return
        }

        [serviceCategoryInstance: serviceCategoryInstance]
    }

    def edit() {
        def serviceCategoryInstance = ServiceCategory.get(params.id)
        if (!serviceCategoryInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'serviceCategory.label', default: 'ServiceCategory'), params.id])
            redirect(action: "list")
            return
        }

        [serviceCategoryInstance: serviceCategoryInstance]
    }

    def update() {
        def serviceCategoryInstance = ServiceCategory.get(params.id)
        if (!serviceCategoryInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'serviceCategory.label', default: 'ServiceCategory'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (serviceCategoryInstance.version > version) {
                serviceCategoryInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'serviceCategory.label', default: 'ServiceCategory')] as Object[],
                          "Another user has updated this ServiceCategory while you were editing")
                render(view: "edit", model: [serviceCategoryInstance: serviceCategoryInstance])
                return
            }
        }

        serviceCategoryInstance.properties = params

        if (!serviceCategoryInstance.save(flush: true)) {
            render(view: "edit", model: [serviceCategoryInstance: serviceCategoryInstance])
            return
        }

    flash.message = message(code: 'default.updated.message', args: [message(code: 'serviceCategory.label', default: 'ServiceCategory'), serviceCategoryInstance.id])
        redirect(action: "show", id: serviceCategoryInstance.id)
    }

    def delete() {
        def serviceCategoryInstance = ServiceCategory.get(params.id)
        if (!serviceCategoryInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'serviceCategory.label', default: 'ServiceCategory'), params.id])
            redirect(action: "list")
            return
        }

        try {
            serviceCategoryInstance.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'serviceCategory.label', default: 'ServiceCategory'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'serviceCategory.label', default: 'ServiceCategory'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
