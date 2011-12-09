modules = { 
	'html5' {
		resource url:'js/html5.js', disposition:'head', wrapper: { s -> "<!--[if lt IE 9]> $s <![endif]-->" } 
	} 
	'tiptip' {
		dependsOn 'jquery'
		defaultBundle false
		resource url:[dir:'js/jquery', file:'jquery.tiptip.min.js'] 
		resource url:[dir:'css/jquery', file:'jquery.tiptip.css'], bundle: 'css'
	}
	'jgrowl' {
		dependsOn 'jquery'  
		defaultBundle false
		resource url:[dir:'js/jquery', file:'jquery.jgrowl.min.js'], bundle: 'extjs' 
		resource url:[dir:'css/jquery', file:'jquery.jgrowl.css' ], bundle: 'css' 
	}
	'validate' {
		dependsOn 'jquery'
		defaultBundle false
		resource url:[dir:'js/jquery', file:'jquery.validate.min.js']
		resource url:[dir:'js/jquery', file:'jquery.validate.additional.js']
	}
	'formwizard' {
		dependsOn 'jquery-ui'
		defaultBundle false
		resource url:[dir:'js/jquery', file:'jquery.formwizard.min.js']		// 3.0.4
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
	'modernizr' {
		resource url:'js/modernizr.js'
	}
	'app' {
		dependsOn 'modernizr'
		resource url: 'css/application.css', disposition:'head', attrs:[rel:'stylesheet/less']
		resource url:'js/application.js'
		resource url:'js/less.min.js', disposition:'head'		// 1.0.35 
	}
	'images' {
		resource url:[dir:'images', file:'logo.jpg'], disposition:'image', attrs:[width:102, height:50]
		resource url:[dir:'images', file:'spinner.gif'], disposition:'image', attrs:[width:20, height:20]
	}
	'tipsy' {
		dependsOn 'jquery'  
		defaultBundle false
		resource url:[dir:'js/jquery', file:'jquery.tipsy.js'] , disposition:'head'
		resource url:[dir:'css/jquery', file:'jquery.tipsy.css' ], disposition:'head'
	}
	'protvis' {
		resource url:'js/protovis-r3.2.js', disposition:'head'
		resource url:'js/protovis-tipsy.js', disposition:'head'
	}
	
	overrides {
		'jquery-ui' {	
			// We're using smoothness so we override definition from jquery-ui plugin
			// To view and modify this theme, visit http://jqueryui.com/themeroller/?ffDefault=&fwDefault=normal&fsDefault=&cornerRadius=4px&bgColorHeader=cccccc&bgTextureHeader=03_highlight_soft.png&bgImgOpacityHeader=75&borderColorHeader=aaaaaa&fcHeader=222222&iconColorHeader=222222&bgColorContent=ffffff&bgTextureContent=01_flat.png&bgImgOpacityContent=75&borderColorContent=aaaaaa&fcContent=222222&iconColorContent=222222&bgColorDefault=e6e6e6&bgTextureDefault=02_glass.png&bgImgOpacityDefault=75&borderColorDefault=d3d3d3&fcDefault=555555&iconColorDefault=888888&bgColorHover=dadada&bgTextureHover=02_glass.png&bgImgOpacityHover=75&borderColorHover=999999&fcHover=212121&iconColorHover=454545&bgColorActive=ffffff&bgTextureActive=02_glass.png&bgImgOpacityActive=65&borderColorActive=aaaaaa&fcActive=212121&iconColorActive=454545&bgColorHighlight=fbf9ee&bgTextureHighlight=02_glass.png&bgImgOpacityHighlight=55&borderColorHighlight=fcefa1&fcHighlight=363636&iconColorHighlight=2e83ff&bgColorError=fef1ec&bgTextureError=02_glass.png&bgImgOpacityError=95&borderColorError=cd0a0a&fcError=cd0a0a&iconColorError=cd0a0a&bgColorOverlay=aaaaaa&bgTextureOverlay=01_flat.png&bgImgOpacityOverlay=0&opacityOverlay=30&bgColorShadow=aaaaaa&bgTextureShadow=01_flat.png&bgImgOpacityShadow=0&opacityShadow=30&thicknessShadow=8px&offsetTopShadow=-8px&offsetLeftShadow=-8px&cornerRadiusShadow=8px
			resource id:'theme', url:[ dir: 'css/jquery/themes/smoothness', file:'jquery-ui-1.8.7.custom.css'], attrs:[media:'screen, projection'], bundle: 'css'
		}
	}
}
