package aaf.fr.foundation

import org.springframework.dao.DataIntegrityViolationException

class CAKeyInfoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        [CAKeyInfoInstanceList: CAKeyInfo.list(), CAKeyInfoInstanceTotal: CAKeyInfo.count()]
    }

    def create() {
        [CAKeyInfoInstance: new CAKeyInfo(params)]
    }

    def save() {
        def CAKeyInfoInstance = new CAKeyInfo(params)

        CAKeyInfoInstance.certificate = new CACertificate(data: params.cacertdata)
        CAKeyInfoInstance.certificate.caKeyInfo = CAKeyInfoInstance

        if (!CAKeyInfoInstance.save(flush: true)) {
            render(view: "create", model: [CAKeyInfoInstance: CAKeyInfoInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'CAKeyInfo.label', default: 'CAKeyInfo'), CAKeyInfoInstance.id])
        redirect(action: "show", id: CAKeyInfoInstance.id)
    }

    def show() {
        def CAKeyInfoInstance = CAKeyInfo.get(params.id)
        if (!CAKeyInfoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'CAKeyInfo.label', default: 'CAKeyInfo'), params.id])
            redirect(action: "list")
            return
        }

        [CAKeyInfoInstance: CAKeyInfoInstance]
    }

    def edit() {
        def CAKeyInfoInstance = CAKeyInfo.get(params.id)
        if (!CAKeyInfoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'CAKeyInfo.label', default: 'CAKeyInfo'), params.id])
            redirect(action: "list")
            return
        }

        [CAKeyInfoInstance: CAKeyInfoInstance]
    }

    def update() {
        def CAKeyInfoInstance = CAKeyInfo.get(params.id)
        if (!CAKeyInfoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'CAKeyInfo.label', default: 'CAKeyInfo'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (CAKeyInfoInstance.version > version) {
                CAKeyInfoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'CAKeyInfo.label', default: 'CAKeyInfo')] as Object[],
                          "Another user has updated this CAKeyInfo while you were editing")
                render(view: "edit", model: [CAKeyInfoInstance: CAKeyInfoInstance])
                return
            }
        }

        CAKeyInfoInstance.properties = params
        CAKeyInfoInstance.certificate.data = params.cacertdata

        if (!CAKeyInfoInstance.save(flush: true)) {
            render(view: "edit", model: [CAKeyInfoInstance: CAKeyInfoInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'CAKeyInfo.label', default: 'CAKeyInfo'), CAKeyInfoInstance.id])
        redirect(action: "show", id: CAKeyInfoInstance.id)
    }

    def delete() {
        def CAKeyInfoInstance = CAKeyInfo.get(params.id)
        if (!CAKeyInfoInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'CAKeyInfo.label', default: 'CAKeyInfo'), params.id])
            redirect(action: "list")
            return
        }

        try {
            CAKeyInfoInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'CAKeyInfo.label', default: 'CAKeyInfo'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'CAKeyInfo.label', default: 'CAKeyInfo'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
