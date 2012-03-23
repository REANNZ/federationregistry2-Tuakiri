package aaf.fr.admin

import org.springframework.dao.DataIntegrityViolationException
import aaf.fr.foundation.*

class MonitorTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        [monitorTypeInstanceList: MonitorType.list(), monitorTypeInstanceTotal: MonitorType.count()]
    }

    def create() {
        [monitorTypeInstance: new MonitorType(params)]
    }

    def save() {
        def monitorTypeInstance = new MonitorType(params)
        if (!monitorTypeInstance.save(flush: true)) {
            render(view: "create", model: [monitorTypeInstance: monitorTypeInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'monitorType.label', default: 'MonitorType'), monitorTypeInstance.id])
        redirect(action: "show", id: monitorTypeInstance.id)
    }

    def show() {
        def monitorTypeInstance = MonitorType.get(params.id)
        if (!monitorTypeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'monitorType.label', default: 'MonitorType'), params.id])
            redirect(action: "list")
            return
        }

        [monitorTypeInstance: monitorTypeInstance]
    }

    def edit() {
        def monitorTypeInstance = MonitorType.get(params.id)
        if (!monitorTypeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'monitorType.label', default: 'MonitorType'), params.id])
            redirect(action: "list")
            return
        }

        [monitorTypeInstance: monitorTypeInstance]
    }

    def update() {
        def monitorTypeInstance = MonitorType.get(params.id)
        if (!monitorTypeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'monitorType.label', default: 'MonitorType'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (monitorTypeInstance.version > version) {
                monitorTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'monitorType.label', default: 'MonitorType')] as Object[],
                          "Another user has updated this MonitorType while you were editing")
                render(view: "edit", model: [monitorTypeInstance: monitorTypeInstance])
                return
            }
        }

        monitorTypeInstance.properties = params

        if (!monitorTypeInstance.save(flush: true)) {
            render(view: "edit", model: [monitorTypeInstance: monitorTypeInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'monitorType.label', default: 'MonitorType'), monitorTypeInstance.id])
        redirect(action: "show", id: monitorTypeInstance.id)
    }

    def delete() {
        def monitorTypeInstance = MonitorType.get(params.id)
        if (!monitorTypeInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'monitorType.label', default: 'MonitorType'), params.id])
            redirect(action: "list")
            return
        }

        try {
            monitorTypeInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'monitorType.label', default: 'MonitorType'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'monitorType.label', default: 'MonitorType'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
