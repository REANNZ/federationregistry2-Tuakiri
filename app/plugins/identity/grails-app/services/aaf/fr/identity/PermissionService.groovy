package aaf.fr.identity

/**
 * Provides methods for interacting with permissions.
 *
 * @author Bradley Beddoes
 */
class PermissionService {

  boolean transactional = true

  /**
   * Assigns a permission object to an owner and performs checks to ensure permission is correctly applied
   *
   * @param permission The populated permission object to persist
   * @param owner An object that extends PermissionOwner (e.g. Subject, Group, Role)
   *
   * @return A permission object. The saved object is all was successful or the permission object with error details if persistence fails.
   * 
   * @throws RuntimeException if an unrecoverable/unexpected error occurs (Rolls back transaction)
   */
  def createPermission(permission, owner) {

    permission.owner = owner
    def savedPermission = permission.save()
    if (!savedPermission) {
      log.error("Unable to persist new permission")
      permission.errors.each {
        log.error(it)
      }

      throw new RuntimeException("Unable to persist new permission")
    }

    owner.addToPermissions(permission)
    def savedOwner = owner.save(flush: true)

    if (!savedOwner) {
      log.error("Unable to add permission $savedPermission.id to owner $owner.id")
      owner.errors.each {
        log.error(it)
      }

      throw new RuntimeException("Unable to add permission $savedPermission.id to owner $owner.id")
    }

    log.info("Successfully added permission $savedPermission.id to owner $owner.id")
    return savedPermission
  }

  /**
   * Removes permission from owner and deletes reference in data repository.
   *
   * @permission The populated permission object to delete. If permission is managed reference must be removed from management object before invocation
   *
   * @throws RuntimeException if an unrecoverable/unexpected error occurs (Rolls back transaction)
   */
  def deletePermission(permission) {
    def owner = permission.owner

    owner.removeFromPermissions(permission)
    def savedOwner = owner.save()

    if (!savedOwner) {
      log.error("Unable to remove permission $savedPermission.id from subject [$owner.id]$owner.name")
      owner.errors.each {
        log.error(it)
      }

      throw new RuntimeException("Unable to remove permission $savedPermission.id from subject [$owner.id]$owner.name")
    }

    permission.delete();
    log.info("Successfully removed permission $permission.id from owner $owner.id")
  }

}
