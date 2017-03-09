grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6

grails.project.dependency.resolution = {
  inherits("global") {}

  log "warn"
  checksums true
  
  repositories {
    inherits true

    flatDir name:"aaf-patched-groovy", dirs:"../../../aaf-patched-groovy/target/libs"

    grailsPlugins()
    grailsHome()
    grailsCentral()

    mavenLocal()
    mavenCentral()

    mavenRepo "http://repo.grails.org/grails/repo/"
    mavenRepo "http://download.java.net/maven/2/"
    mavenRepo "http://repository.jboss.com/maven2/"
  }

  dependencies {
    compile "org.codehaus:groovy-all:2.0.8+aaf.groovy7664"
    compile "commons-collections:commons-collections:3.2.2"

    test 'mysql:mysql-connector-java:5.1.18'
    test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
  }

  plugins {
    build ":tomcat:$grailsVersion"

    compile ":federated-grails:0.6.0"
    compile ":build-test-data:2.0.3"
    compile ":mail:1.0"
    compile ":hibernate:$grailsVersion"

    test(":spock:0.7") {
      exclude "spock-grails-support"
    }
    test ":resources:1.1.6"
    provided ":greenmail:1.3.4"
  }
}
