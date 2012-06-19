import org.apache.commons.io.FileUtils

includeTargets << grailsScript("Init")
includeTargets << grailsScript("_GrailsArgParsing")

USAGE = """
    importtheme dir

where
    dir  = The directory where custom theme files for your deployment are located (managed by external VCS)
"""

target(main: "This script imports custom themes for FR deployment from the directory specified in arguments") {
    
	def source = parseArgs()

	// Add directories and files here to extend theme import. Files will be copied from source at this location and installed in Federation Registry at the same location
	// All files listed here must appear in .gitignore to ensure they aren't commited to revision control (they should be controlled in their own external project)
	//def dirs = [	"grails-app/i18n"	]
	def files = [ 
          "grails-app/i18n/messages-branding.properties",
          "grails-app/views/layouts/admin.gsp",
					"grails-app/views/layouts/bootstrap.gsp",
					"grails-app/views/layouts/reporting.gsp",
					"grails-app/views/layouts/dashboard.gsp",
          "grails-app/views/layouts/error.gsp",
					"grails-app/views/layouts/email.gsp",
					"grails-app/views/layouts/members.gsp",
					"grails-app/views/layouts/metadata.gsp",
					"grails-app/views/layouts/public.gsp",
					"grails-app/views/layouts/workflow.gsp",
					"grails-app/views/templates/_frfooter.gsp",
					"grails-app/views/templates/_frheader.gsp",
					"grails-app/views/templates/_frtopnavigation.gsp",
          "grails-app/views/templates/_frbrowsercheck.gsp",
          "grails-app/views/templates/layouts/_members_nav.gsp",
          "grails-app/views/templates/layouts/_reporting_nav.gsp",
          "grails-app/views/templates/layouts/_administration_nav.gsp",
          "grails-app/views/templates/layouts/_workflow_nav.gsp",
					"grails-app/views/templates/mail/workflows/default/_activated_idp.gsp",
					"grails-app/views/templates/mail/workflows/default/_activated_organization.gsp",
					"grails-app/views/templates/mail/workflows/default/_activated_sp.gsp",
					"grails-app/views/templates/mail/workflows/default/_registered_idp.gsp",
					"grails-app/views/templates/mail/workflows/default/_registered_organization.gsp",
					"grails-app/views/templates/mail/workflows/default/_registered_sp.gsp",
					"grails-app/views/templates/mail/workflows/default/_rejected_idp.gsp",
					"grails-app/views/templates/mail/workflows/default/_rejected_organization.gsp",
					"grails-app/views/templates/mail/workflows/default/_rejected_sp.gsp",
					"web-app/css/application.css",
					"web-app/images/logo.jpg",
          "web-app/images/emailbranding.gif"	]
				
	def ant = new AntBuilder()
	
	files.each { file ->
		def dst = new File("${basedir}/$file")
		def src = new File("${source}/$file")
		
		FileUtils.copyFile(src, dst)
	}
	
	println "Completed theme import"
}

def parseArgs() {
	args = args ? args.split('\n') : []
	switch (args.size()) {
		case 0: 
			println "Importing default AAF based theme from ../../branding"
			return "../../branding"
			break
		case 1:
			println "Importing theme from ${args[0]}"
			
			def source = new File(args[0])
			if(!source.exists()) {
				println "Supplied directory does not exist"
				usage()
			}
			
			source.eachDir {
				if (!it.isHidden() && ( !it.name.equals("grails-app") && !it.name.equals("web-app"))) {
					println "Supplied directory does not contain valid theme structure of grails-app and web-app directories. Located invalid directory ${it.name}"
					usage()
				}
				
			}
			
			return args[0]
			break
		default:
			usage()
			break
	}
}

private void usage() {
	println "Usage:\n${USAGE}"
	System.exit(1)
}

setDefaultTarget(main)
