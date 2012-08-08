grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6

grails.project.dependency.resolution = {
  inherits("global") {
      // excludes 'ehcache'
  }
  log "warn"
  repositories {
    grailsCentral()
  }
  dependencies {
    compile 'org.grails.plugins:federated-grails:0.2.2'
  }

  plugins {
    build(":tomcat:$grailsVersion",
          ":release:1.0.0.RC3") {
      export = false
    }
  }
}
