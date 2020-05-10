
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6

grails.plugin.location.base = "../base"

grails.project.dependency.resolution = {
  inherits("global") {}

  log "warn"
  checksums true

  repositories {
    inherits true

    grailsPlugins()
    grailsHome()
    grailsCentral()

    mavenLocal()
    // mavenCentral()
    mavenRepo "https://repo1.maven.org/maven2"

    mavenRepo "http://repo.grails.org/grails/plugins-releases/"
    mavenRepo "https://download.java.net/maven/2/"
    mavenRepo "https://repository.jboss.org/maven2/"
  }

  dependencies {
    compile "commons-collections:commons-collections:3.2.2"
    compile "org.springframework:spring-aop:4.0.9.RELEASE" // needed for shiro
    compile "org.springframework:spring-expression:4.0.9.RELEASE" // needed for tests

    test 'mysql:mysql-connector-java:5.1.49'
  }

  plugins {
    build ":tomcat:7.0.55.2"

    compile ":federated-grails:0.6.0"
    // explicitly require shiro to force upgrade from 1.1.3 pulled by fed-grails
    compile ":shiro:1.2.1"
    compile ":build-test-data:2.4.0"
    compile ":mail:1.0.7"
    compile ":hibernate4:4.3.8.1"

    test ":resources:1.2.14"
    provided ":greenmail:1.3.4"
  }
}
