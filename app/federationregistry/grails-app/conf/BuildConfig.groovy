
// Local Plugins

grails.plugin.location.identity="../plugins/identity"
grails.plugin.location.foundation="../plugins/foundation"
grails.plugin.location.reporting="../plugins/reporting"
grails.plugin.location.workflow="../plugins/workflow"
grails.plugin.location.metadata="../plugins/metadata"

grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"

grails.project.dependency.resolution = {
    inherits "global"
    log "warn"
    
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }

    dependencies {
        test 'mysql:mysql-connector-java:5.1.18'
    }
}

codenarc.reports = {
    HTMLReport('html') {
        outputFile = 'target/test-reports/codenarc.html' 
        title = 'CodeNarc' 
    } 
}
