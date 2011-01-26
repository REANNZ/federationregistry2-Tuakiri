/*
 *  Nimble, an extensive application base for Grails
 *  Copyright (C) 2010 Bradley Beddoes
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package grails.plugins.nimble.core

import org.apache.shiro.SecurityUtils

/**
 * Various Nimble specific pieces of logic, shouldn't need to be called by any
 * host application.
 *
 * @author Bradley Beddoes
 */
class NimbleService {

    def grailsApplication
	def permissionsService
    
    boolean transactional = true

    /**
     * Integrates with extended Nimble bootstrap process, sets up basic Nimble environment
     * once all domain objects etc ave dynamic methods available to them.
     */
    public void init() {

        // Perform all base Nimble setup
        def userRole = Role.findByName(UserService.USER_ROLE)
        if (!userRole) {
            userRole = new Role()
            userRole.description = 'Issued to all users'
            userRole.name = UserService.USER_ROLE
            userRole.protect = true
            userRole.save()

            if (userRole.hasErrors()) {
                userRole.errors.each {
                    log.error(it)
                }
                throw new RuntimeException("Unable to create valid users role")
            }
        }

        def adminRole = Role.findByName(AdminsService.ADMIN_ROLE)
        if (!adminRole) {
            adminRole = new Role()
            adminRole.description = 'Assigned to users who are considered to be system wide administrators'
            adminRole.name = AdminsService.ADMIN_ROLE
            adminRole.protect = true
            def savedAdminRole = adminRole.save()

            if (adminRole.hasErrors()) {
                adminRole.errors.each {
                    log.error(it)
                }
                throw new RuntimeException("Unable to create valid administrative role")
            }

			// Grant administrative 'ALL' permission
            Permission adminPermission = new Permission(target:'*')
            adminPermission.managed = true
            adminPermission.type = Permission.adminPerm

            permissionsService.createPermission(savedAdminRole, user)
        }

        // Execute all service init that relies on base Nimble environment
        def services = grailsApplication.getArtefacts("Service")
        for (service in services) {
            if(service.clazz.methods.find{it.name == 'nimbleInit'} != null) {
                def serviceBean = grailsApplication.mainContext.getBean(service.propertyName)
                serviceBean.nimbleInit()
            }
        }
    }
}
