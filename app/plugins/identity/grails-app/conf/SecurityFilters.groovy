/**
 * This filters class protects all URLs via access control by convention.
 */
class SecurityFilters {
    def filters = {
        all(uri: "/**") {
            before = {
                // Ignore direct views (e.g. the default main index page).
                if (!controllerName) return true

                // Access control by convention
        // Must only be authenticated we don't look at aaf.fr.identity.Subject permissions (your app SHOULD however).
                accessControl { true }
            }
        }
    }
}
