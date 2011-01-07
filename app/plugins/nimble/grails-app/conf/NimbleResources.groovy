modules = {
	'nimble' { 
		dependsOn 'jquery'
		resource url:[dir: 'js', file: 'nimble.js', plugin: 'nimble'], disposition:'head'
	}
	'nimble-ui' {
		dependsOn 'jquery-ui'
		resource url:[dir: 'js', file: 'nimble-ui.js', plugin: 'nimble']
	}
}