package aaf.fr.identity

import javax.servlet.http.HttpServletRequest
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.SecurityUtils

/**
 * Provides methods for interacting with subjects.
 *
 * @author Bradley Beddoes
 */
class SubjectService {

    public static String SUBJECT_ROLE = "USER"

    boolean transactional = true

    def grailsApplication
    def permissionService

    /**
     * Activates a disabled subject account
     *
     * @param subject The subject to enable
     *
     * @throws RuntimeException When internal state requires transaction rollback
     */
    def enableSubject(Subject subject) {
        subject.enabled = true

        def savedSubject = subject.save()
        if (savedSubject) {
            log.info("Successfully enabled subject [$subject.id]$subject.subjectname")
            return savedSubject
        }

        log.error("Error enabling subject [$subject.id]$subject.subjectname")
        subject.errors.each {
            log.error it
        }

        throw new RuntimeException("Error enabling subject [$subject.id]$subject.subjectname")
    }

    /**
     * Disables an active subject account
     *
     * @param subject The subject to disable
     *
     * @throws RuntimeException When internal state requires transaction rollback
     */
    def disableSubject(Subject subject) {
        subject.enabled = false

        def savedSubject = subject.save()
        if (savedSubject) {
            log.info("Successfully disabled subject [$subject.id]$subject.subjectname")
            return savedSubject
        }

        log.error("Error disabling subject [$subject.id]$subject.subjectname")
        subject.errors.each {
            log.error it
        }

        throw new RuntimeException("Error disabling subject [$subject.id]$subject.subjectname")
    }

    /**
     * Updates a current subject account.
     *
     * @pre Passed subject object must have been validated to ensure
     * that hibernate does not auto persist the object to the repository prior to service invocation
     *
     * @param subject The subject to update
     *
     * @throws RuntimeException When internal state requires transaction rollback
     */
    def updateSubject(Subject subject) {

        def updatedSubject = subject.save()
        if (updatedSubject) {
            log.debug("Updated subject [$subject.id]$subject.subjectname")
            return updatedSubject
        }

        log.error("Unable to update subject [$subject.id]$subject.subjectname")
        subject.errors.each {
            log.error(it)
        }

        throw new RuntimeException("Unable to update subject [$subject.id]$subject.subjectname")
    }
}
