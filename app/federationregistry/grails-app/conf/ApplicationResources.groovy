
modules = { 
	'validate' {
		dependsOn 'jquery'
		defaultBundle false
		resource url:[dir:'js/jquery', file:'jquery.validate.min.js']
		resource url:[dir:'js/jquery', file:'jquery.validate.additional.js']
	}
	'datatables' {
		dependsOn 'jquery' 
		defaultBundle false
		resource url:[dir:'js/jquery', file:'jquery.datatables.min.js'], disposition:'head', bundle: 'extjs' 
	}
	'alphanumeric' {
		dependsOn 'jquery'
		defaultBundle false
		resource url:[dir:'js/jquery', file:'jquery.alphanumeric.min.js'], bundle: 'extjs'
	}
	'blockui' {
		dependsOn 'jquery'
		defaultBundle false
		resource url:[dir:'js/jquery', file:'jquery.blockui.js'], bundle: 'extjs' 
	}
	'zenbox' { 
		defaultBundle false
		resource url:'js/zenbox.js', disposition:'head'	// 2.0
		resource url:'css/zenbox.css', bundle: 'css'
	}
	'codemirror' {	// 0.9.2
		defaultBundle 'codemirror' 
		resource url: [dir:'/js/codemirror/js', file:'codemirror.min.js'], disposition:'head'
	}
	'codemirror-groovy' {
		resource url: [dir:'/js/codemirror/js', file:'codemirror.groovy.inframe.min.js']
		resource url: [dir:'/js/codemirror/css', file:'groovycolors.css']
	}
	'app' {
	 resource url: 'css/application.css', disposition:'head', attrs:[rel:'stylesheet/less']
	 resource url:'js/application.js'
	 resource url:'js/less.min.js', disposition:'head'		// 1.0.35 
	}
	'images' {
		resource url:[dir:'images', file:'logo.jpg'], disposition:'image', attrs:[width:102, height:50]
		resource url:[dir:'images', file:'spinner.gif'], disposition:'image', attrs:[width:20, height:20]
	}
	'protovis' {
		resource url:'js/protovis-r3.2.js', disposition:'head'
		resource url:'js/protovis-tipsy.js', disposition:'head'
	}
}
