grails.servlet.version = "3.0"

grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6

// Local Plugins
grails.plugin.location.base = "../plugins/base"
grails.plugin.location.reporting = "../plugins/reporting"
grails.plugin.location.metadata = "../plugins/metadata"
grails.plugin.location.administration = "../plugins/administration"
grails.plugin.location.api = "../plugins/api"
grails.plugin.location.export  = "../plugins/export"

grails.project.dependency.resolution = {
  inherits("global") {
  }

  log "warn"
  checksums true

  repositories {
    inherits true

    grailsCentral()
    grailsPlugins()
    grailsHome()

    mavenLocal()
    // mavenCentral()
    mavenRepo "https://repo1.maven.org/maven2"

    mavenRepo "https://repo.grails.org/grails/plugins-releases/"
    mavenRepo "https://download.java.net/maven/2/"
    mavenRepo "https://repository.jboss.org/maven2/"
  }

  dependencies {
    compile "commons-collections:commons-collections:3.2.2"
    compile "org.apache.shiro:shiro-quartz:1.2.5"
    compile "org.apache.shiro:shiro-web:1.2.5"
    compile "org.apache.shiro:shiro-spring:1.2.5"
    compile "org.apache.shiro:shiro-ehcache:1.2.5"
    compile "org.apache.shiro:shiro-core:1.2.5"

    test 'mysql:mysql-connector-java:5.1.49'
  }

  plugins {
    /*
    build: Dependencies for the build system only
    compile: Dependencies for the compile step
    runtime: Dependencies needed at runtime but not for compilation (see above)
    test: Dependencies needed for testing but not at runtime (see above)
    provided: Dependencies needed at development time, but not during WAR deployment
    */

    build ":tomcat:7.0.55.2"

    compile ':cache:1.1.8'
    compile ":mail:1.0.7"
    compile ":hibernate4:4.3.10"

    runtime ":resources:1.2.14"
    runtime ":zipped-resources:1.0.1"
    runtime ":cached-resources:1.1"
    runtime ":yui-minify-resources:0.1.4"
    runtime ":database-migration:1.4.0"
    runtime ":jquery:1.11.1"
    runtime ":modernizr:2.5.3"
    runtime (":twitter-bootstrap:2.3.2.3") { excludes "svn" }
    runtime 'org.grails.plugins:constraintkeys:0.1'
    runtime ":console:1.5.9"
    runtime ":cache-headers:1.1.7"
    runtime ":audit-logging:1.0.7"

    test ":build-test-data:2.4.0"

    provided ":greenmail:1.3.4"
  }
}

grails.project.dependency.resolver = "maven"

codenarc.reports = {
    HTMLReport('html') {
        outputFile = 'target/test-reports/codenarc.html'
        title = 'CodeNarc'
    }
}
