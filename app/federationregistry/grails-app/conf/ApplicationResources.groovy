
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
    resource url:[dir:'js/jquery', file:'jquery.datatables.bootstrap.js'], disposition:'head', bundle: 'extjs' 
  }
  'alphanumeric' {
    dependsOn 'jquery'
    defaultBundle false
    resource url:[dir:'js/jquery', file:'jquery.alphanumeric.min.js'], bundle: 'extjs'
  }
  'zenbox' { 
    defaultBundle false
    resource url:'js/zenbox.js', disposition:'head' // 2.0
    resource url:'css/zenbox.css', bundle: 'css'
  }
  'codemirror' {  // 0.9.2
    defaultBundle 'codemirror' 
    resource url: [dir:'/js/codemirror/js', file:'codemirror.min.js'], disposition:'head'
  }
  'codemirror-groovy' {
    resource url: [dir:'/js/codemirror/js', file:'codemirror.groovy.inframe.min.js']
    resource url: [dir:'/js/codemirror/css', file:'groovycolors.css']
  }
  'images' {
    resource url:[dir:'images', file:'logo.jpg'], disposition:'image', attrs:[width:102, height:50]
    resource url:[dir:'images', file:'spinner.gif'], disposition:'image', attrs:[width:20, height:20]
  }
  'protovis' {
    resource url:'js/protovis-r3.2.js', disposition:'head'
    resource url:'js/protovis-tipsy.js', disposition:'head'
  }
  'highcharts' {
    resource url:'js/highcharts-2.2.0.js', disposition:'head'
  }
  'bootstrap-datepicker' {
    // borrowed from http://dl.dropbox.com/u/143355/datepicker/datepicker.html until support official
    resource url:'js/bootstrap-datepicker.js', disposition:'head'
    resource url:'css/bootstrap-datepicker.css', disposition:'head'
  }
  'app' {
   resource url: 'css/application.css', disposition:'head', attrs:[rel:'stylesheet/less']
   resource url:'js/application.js'
   resource url:'js/application-reporting.js'
   resource url:'js/application-administration.js'
   resource url:'js/less.min.js', disposition:'head'    // 1.0.35
   dependsOn(['jquery', 'protovis', 'highcharts'])
  }
}
