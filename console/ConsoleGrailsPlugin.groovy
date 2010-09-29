class ConsoleGrailsPlugin {
    def version = '0.2.2',grailsVersion = "1.1 > *"
    def dependsOn = [:], pluginExcludes = ["grails-app/views/error.gsp"]

    def author = "Siegfried Puchbauer/Mingfai Ma", authorEmail = "siegfried.puchbauer@gmail.com / mingfai.ma@gmail.com"
    def title = "Web-based Groovy Console for Grails"
    def description = '''\\
A web-based Groovy console for interactive runtime application management and debugging
'''
    def documentation = "http://grails.org/plugin/console"

}
