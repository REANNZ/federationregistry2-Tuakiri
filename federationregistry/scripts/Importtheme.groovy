includeTargets << grailsScript("Init")
includeTargets << grailsScript("_GrailsArgParsing")

USAGE = """
    importtheme dir

where
    dir  = The directory where custom theme files for your deployment are located (managed by external VCS)
"""

target(main: "This script imports custom themes for FR deployment from the directory specified in arguments") {
    
	def source = parseArgs()

	// Add files here to extend theme import. Files will be copied from source at this location and installed in Federation Registry at the same location
	// All files listed here must appear in .gitignore to ensure they aren't commited to revision control (they should be controlled in their own external project)
	def files = [	"grails-app/i18n/messages-deployment.properties",
					"grails-app/views/layouts/access.gsp",
					"grails-app/views/layouts/bootstrap.gsp",
					"grails-app/views/layouts/compliance.gsp",
					"grails-app/views/layouts/dashboard.gsp",
					"grails-app/views/layouts/email.gsp",
					"grails-app/views/layouts/members.gsp",
					"grails-app/views/layouts/metadata.gsp",
					"grails-app/views/layouts/monitoring.gsp",
					"grails-app/views/layouts/public.gsp",
					"grails-app/views/layouts/workflow.gsp",
					"grails-app/views/templates/_frfooter.gsp",
					"grails-app/views/templates/_frheader.gsp",
					"grails-app/views/templates/_frtopnavigation.gsp",
					"grails-app/views/templates/mail/workflows/default/_activated_idp.gsp",
					"grails-app/views/templates/mail/workflows/default/_activated_organization.gsp",
					"grails-app/views/templates/mail/workflows/default/_activated_sp.gsp",
					"grails-app/views/templates/mail/workflows/default/_registered_idp.gsp",
					"grails-app/views/templates/mail/workflows/default/_registered_organization.gsp",
					"grails-app/views/templates/mail/workflows/default/_registered_sp.gsp",
					"grails-app/views/templates/mail/workflows/default/_rejected_idp.gsp",
					"grails-app/views/templates/mail/workflows/default/_rejected_organization.gsp",
					"grails-app/views/templates/mail/workflows/default/_rejected_sp.gsp",
					"web-app/css/frtheme.less",
					"web-app/images/logo.jpg"	]
				
	println source.path
	files.each {
		( new AntBuilder ( ) ).copy ( file : "${source.path}${File.separator}$it" , tofile : "${basedir}${File.separator}$it" )
	}

}

def parseArgs() {
	args = args ? args.split('\n') : []
	switch (args.size()) {
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
			
			return source
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
