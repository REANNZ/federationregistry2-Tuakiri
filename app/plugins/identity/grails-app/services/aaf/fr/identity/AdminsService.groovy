package aaf.fr.identity

import org.apache.shiro.authz.permission.AllPermission
import grails.plugins.federatedgrails.Role

/**
 * Provides methods for granting and removing super administrator role.
 *
 * @author Bradley Beddoes
 */
class AdminsService {

    public static String ADMIN_ROLE = "SYSTEM ADMINISTRATOR"

    boolean transactional = true

    def permissionService

    /**
     * Provides administrator capability to a subject account.
     *
     * @param subject A valid subject object that should be assigned global admin rights
     *
     * @pre Passed subject object must have been validated to ensure
     * that hibernate does not auto persist the object to the repository prior to service invocation
     *
     * @throws RuntimeException When internal state requires transaction rollback
     */
    def add(Subject subject) {
        // Grant administrative role
        def adminRole = Role.findByName(AdminsService.ADMIN_ROLE)

        if (!adminRole) {
            log.error("Unable to located default administative role")
            throw new RuntimeException("Unable to locate default administrative role")
        }

        adminRole.addToSubjects(subject)
        subject.addToRoles(adminRole)

        if (!adminRole.save()) {
            log.error "Unable to grant administration privilege to $subject"
            adminRole.errors.each {
                log.error '[$subject] - ' + it
            }

            adminRole.discard()
            subject.discard()
            return false
        }
        else {
            if (!subject.save()) {
                log.error "Unable to grant administration role to $subject failed to modify subject account"
                subject.errors.each {
                    log.error it
                }

                throw new RuntimeException("Unable to grant administration role to $subject")
            }

            log.info "Granted administration privileges to $subject"
            return true
        }
    }

    /**
     * Removes administrator capability from a subject account.
     *
     * @param subject A valid subject object that should have global admin rights removed
     *
     * @pre Passed subject object must have been validated to ensure
     * that hibernate does not auto persist the object to the repository prior to service invocation
     *
     * @throws RuntimeException When internal state requires transaction rollback
     */
    def remove(Subject subject) {
        def adminRole = Role.findByName(AdminsService.ADMIN_ROLE)

        if (!adminRole) {
            log.error("Unable to located default administative role")
            throw new RuntimeException("Unable to locate default administrative role")
        }

        if(adminRole.subjects.size() < 2) {
            log.warn("Unable to remove subject from administration, would leave no system administrator available")
            return false
        }

        adminRole.removeFromSubjects(subject)
        subject.removeFromRoles(adminRole)

        if (!adminRole.save()) {
            log.error "Unable to revoke administration privilege from $subject"
            adminRole.errors.each {
                log.error it
            }

            adminRole.discard()
            subject.discard()
            return false
        }
        else {
            if (!subject.save()) {
                log.error "Unable to revoke administration privilege from $subject failed to modify subject account"
                subject.errors.each {
                    log.error it
                }

                throw new RuntimeException("Unable to revoke administration privilege from $subject failed to modify subject account")
            }

            log.info "Revoked administration privilege from $subject"
            return true
        }
    }
}
