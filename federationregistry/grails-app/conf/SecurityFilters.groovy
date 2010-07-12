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
import grails.plugins.nimble.core.AdminsService
import grails.plugins.nimble.core.UserService

/**
 * Filter that works with Nimble security model to protect controllers, actions, views for Federation Registry
 *
 * @author Bradley Beddoes
 */
public class SecurityFilters extends grails.plugins.nimble.security.NimbleFilterBase {

    def filters = {

        // Members
        descriptors(controller: "(organization|IDPSSODescriptor|contacts|descriptorContacts|desccriptorKeyDescriptor|descriptorEndpoint|descriptorNameIDFormat|descriptorAttribute)") {
            before = {
                accessControl (auth: false) {
					role(UserService.USER_ROLE)
				}
            }
        }
		
		// Compliance
		descriptors(controller: "(idpAttributeCompliance|attributeRelease|certifyingAuthorityUsage)") {
            before = {
                accessControl (auth: false) {
					role(UserService.USER_ROLE)
				}
            }
        }

		// Workflow
		workflow(controller: "workflow*") {
            before = {
                accessControl {
                    role(UserService.USER_ROLE)
                }
            }
        }

		// Data reload functionality
		datamgt(controller: "dataManagement", action:"(index|refreshdata)") {
            before = {
                accessControl {
                    role(AdminsService.ADMIN_ROLE)
                }
            }
        }

		// Monitoring functionality
		monitoring(controller: "monitor") {
            before = {
                accessControl {
                    role(AdminsService.ADMIN_ROLE)
                }
            }
        }

		// Administrative components
		administration(controller: "(admins|user|group|role)") {
            before = {
                accessControl {
                    role(AdminsService.ADMIN_ROLE)
                }
            }
        }

		// Groovy Console
		console(controller: "(code|console)") {
            before = {
                accessControl {
                    role(AdminsService.ADMIN_ROLE)
                }
            }
        }
    }

}