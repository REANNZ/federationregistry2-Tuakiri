package aaf.fr.identity

import grails.plugins.federatedgrails.Role

/**
 * Provides methods for interacting with roles.
 *
 * @author Bradley Beddoes
 */
class RoleService {

  boolean transactional = true

  /**
   * Creates a new role.
   *
   * @param name Name to assign to group
   * @param description Description to assign to group
   * @param protect Boolean indicating if this is a protected group (True disables modification in UI)
   *
   * @throws RuntimeException When internal state requires transaction rollback
   */
  def createRole(String name, String description, boolean protect) {
    def role = new Role()
    role.name = name
    role.description = description
    role.protect = protect

    if(!role.validate()) {
      log.debug("Supplied values for new role are invalid")
      role.errors.each {
        log.debug it
      }
      return role
    }

    def savedRole = role.save()
    if (savedRole) {
      log.info("Created role [$role.id]$role.name")
      return savedRole
    }

    log.error("Error creating new group")
    role.errors.each {
      log.error it
    }

    throw new RuntimeException("Error creating new group, object persistance failed")
  }

  /**
   * Deletes an exisiting Role.
   *
   * @pre Passed role object must have been validated to ensure
   * that hibernate does not auto persist the objects to the repository prior to service invocation
   *
   * @param role The role instance to be deleted
   *
   * @throws RuntimeException When internal state requires transaction rollback
   */
  def deleteRole(Role role) {

    // Remove all subjects from this role
    def subjects = []
    subjects.addAll(role.subjects)
    subjects.each {
      it.removeFromRoles(role)
      it.save()

      if (it.hasErrors()) {
        log.error("Error updating subject [$it.id]$it.name to remove role [$role.id]$role.name")
        it.errors.each {err ->
          log.error err
        }

        throw new RuntimeException("Error updating subject [$it.id]$it.name to remove role [$role.id]$role.name")
      }
    }

    role.delete()
    log.info("Deleted role [$role.id]$role.name")
  }

  /**
   * Updates and existing role.
   *
   * @pre Passed role object must have been validated to ensure
   * that hibernate does not auto persist the objects to the repository prior to service invocation
   *
   * @param role The role to be updated.
   *
   * @throws RuntimeException When internal state requires transaction rollback
   */
  def updateRole(Role role) {

    def updatedRole = role.save()
    if (updatedRole) {
      log.info("Updated role [$role.id]$role.name")
      return updatedRole
    }

    log.error("Error updating role [$role.id]$role.name")
    role.errors.each {err ->
      log.error err
    }

    throw new RuntimeException("Error updating role [$role.id]$role.name")
  }

  /**
   * Assigns a role to a subject.
   *
   * @pre Passed role and subject object must have been validated to ensure
   * that hibernate does not auto persist the objects to the repository prior to service invocation
   *
   * @param subject The subject whole the referenced role should be assigned to
   * @param role The role to be assigned
   *
   * @throws RuntimeException When internal state requires transaction rollback
   */
  def addMember(Subject subject, Role role) {
    role.addToSubjects(subject)
    subject.addToRoles(role)

    def savedRole = role.save()
    if (!savedRole) {
      log.error("Error updating role [$role.id]$role.name to add subject [$subject.id]$subject.displayName")

      role.errors.each {
        log.error(it)
      }

      throw new RuntimeException("Unable to persist role [$role.id]$role.name when adding subject [$subject.id]$subject.displayName")
    }

    def savedSubject = subject.save()
    if (!savedSubject) {
      log.error("Error updating subject [$subject.id]$subject.displayName when adding role [$role.id]$role.name")

      subject.errors.each {
        log.error(it)
      }

      throw new RuntimeException("Error updating subject [$subject.id]$subject.displayName when adding role [$role.id]$role.name")
    }

    log.info("Successfully added $role to $subject")
  }

  /**
   * Removes a role from a subject.
   * 
   * @pre Passed role and subject object must have been validated to ensure
   * that hibernate does not auto persist the objects to the repository prior to service invocation
   *
   * @param subject The subject whole the referenced role should be removed from
   * @param role The role to be assigned
   * 
   * @throws RuntimeException When internal state requires transaction rollback
   */
  def deleteMember(Subject subject, Role role) {
    role.removeFromSubjects(subject)
    subject.removeFromRoles(role)

    def savedRole = role.save()
    if (!savedRole) {
      log.error("Error updating role [$role.id]$role.name to add subject [$subject.id]$subject.displayName")

      role.errors.each {
        log.error(it)
      }

     throw new RuntimeException("Unable to persist role [$role.id]$role.name when removing subject [$subject.id]$subject.displayName")
    }

    def savedSubject = subject.save()
    if (!savedSubject) {
      log.error("Error updating subject [$subject.id]$subject.displayName when adding role [$role.id]$role.name")
      subject.errors.each {
        log.error(it)
      }

      throw new RuntimeException("Error updating subject [$subject.id]$subject.displayName when removing role [$role.id]$role.name")
    }

    log.info("Successfully removed role [$role.id]$role.name to subject [$subject.id]$subject.displayName")
  }
}
