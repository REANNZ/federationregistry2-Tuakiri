def jqver = org.codehaus.groovy.grails.plugins.jquery.JQueryConfig.SHIPPED_VERSION

modules = { 
  overrides {
    'jquery' {
      resource id:'js', url:[plugin: 'jquery', dir:'js/jquery', file:"jquery-${jqver}.min.js"], disposition:'head', nominify: true
    }
    'modernizr' {
      defaultBundle 'app'
    }
    'bootstrap' {
      defaultBundle 'app'
    }
  }
  'jreject' {
    defaultBundle 'app'
    dependsOn 'jquery'

    resource url:'js/jquery.reject.min.js', disposition: 'head'
    resource url:'css/jquery.reject.css'
  }
  'validate' {
    dependsOn 'jquery'
    defaultBundle false
    resource url:[dir:'js/jquery', file:'jquery.validate.min.js']
    resource url:[dir:'js/jquery', file:'jquery.validate.additional.js']
  }

  'datatables' {
    defaultBundle 'app'

    dependsOn 'jquery, codemirror' 
    resource url:[dir:'js/jquery', file:'jquery.datatables.min.js'], disposition: 'head', nominify: true
    resource url:[dir:'js/jquery', file:'jquery.datatables.bootstrap.js'], disposition: 'head', nominify: true
  }
  'alphanumeric' {
    defaultBundle 'app'

    dependsOn 'jquery'
    resource url:[dir:'js/jquery', file:'jquery.alphanumeric.min.js']
  }
  'codemirror' {
    resource url: [dir:'/js/codemirror/js', file:'codemirror.min.js'], disposition:'head', nominify: true
  }
  'codemirror-groovy' {
    resource url: [dir:'/js/codemirror/js', file:'codemirror.groovy.inframe.min.js'], disposition:'head', nominify: true
    resource url: [dir:'/js/codemirror/css', file:'groovycolors.css']
  }
  'images' {
    resource url:[dir:'images', file:'logo.jpg'], disposition:'image', attrs:[width:102, height:50]
    resource url:[dir:'images', file:'spinner.gif'], disposition:'image', attrs:[width:20, height:20]
  }
  'protovis' {
    defaultBundle 'protovis'

    resource url:'js/protovis-r3.2.js'
    resource url:'js/protovis-tipsy.js'
  }
  'highcharts' {
    defaultBundle 'app'

    resource url:'js/highcharts-2.2.0.js'
  }
  'bootstrap-datepicker' {
    // Source: http://www.eyecon.ro/bootstrap-datepicker/
    defaultBundle 'app'

    resource url:'js/bootstrap-datepicker.js'
    resource url:'css/bootstrap-datepicker.css'
  }
  'bootstrap-notify' {
    // Source: http://nijikokun.github.com/bootstrap-notify/
    defaultBundle 'app'

    resource url:'js/bootstrap-notify.js'
    resource url:'css/bootstrap-notify.css'
  }
  'app' {
    defaultBundle 'app'

    resource url: 'css/application.css', attrs:[rel:'stylesheet/less']
    resource url:'js/application.js'
    resource url:'js/application-reporting.js'
    resource url:'js/application-administration.js'
    resource url:'js/less.min.js'
    dependsOn(['jquery', 'protovis', 'highcharts'])
  }
}
