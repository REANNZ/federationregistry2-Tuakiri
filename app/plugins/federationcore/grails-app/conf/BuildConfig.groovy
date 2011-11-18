
// Local Plugins
grails.plugin.location.federationworkflow="../federationworkflow"
grails.plugin.location.nimble="../nimble"

grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"
grails.project.dependency.resolution = {
    inherits "global" // inherit Grails' default dependencies
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsHome()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        // runtime 'com.mysql:mysql-connector-java:5.1.5'
    }

}

clover {
 on = on
 debug = false // a boolean to toggle debugging on or off
 excludes = ["**/*Spec.groovy"] // an array of String Ant Glob Patterns to exclude for instrumentation
}
