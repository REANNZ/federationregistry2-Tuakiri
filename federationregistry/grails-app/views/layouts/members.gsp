<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
  "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <title><g:message code="fedreg.title" /> | <g:layoutTitle /></title>
    
	<nh:nimblecore/>
    <nh:nimbleui/>
    <nh:admin/>
  
    <nh:growl/>
    <script type="text/javascript">
      <njs:flashgrowl/>
    </script>

	<link rel="stylesheet" href="${resource(file: '/css/icons.css')}"/>
	<link rel="stylesheet" href="${resource(file: '/css/fedreg.css')}"/>
	
	
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.jqplot.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/plugins/jqplot.pieRenderer.min.js')}"></script>
	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.jqplot.min.css')}" />
	
	<!-- RAR -->
    <g:layoutHead />
</head>

<body>

  <div id="doc">
    <div id="hd">
		<g:render template='/templates/aafheader' model="['navigation':true]"/>
    </div>
    <div id="bd">
		<div class="container">
	    	<div class="localnavigation">
			  <h3><g:message code="fedreg.layout.members.navigation.title" /></h3>
			    <ul>
					<li>
						<g:link controller="organization" action="list"><g:message code="fedreg.link.organizations" /></g:link>
					</li>
					<li>
						<g:link controller="entity" action="list"><g:message code="fedreg.link.entitydescriptors" /></g:link>
					</li>
					<li>
						<g:link controller="identityProvider" action="list"><g:message code="fedreg.link.identityproviders" /></g:link>
					</li>
				</ul>
			</div>
			<div class="content">
	      		<g:layoutBody/>
			</div>
		</div>
    </div>
    <div id="ft">
	
    </div>
  </div>

<n:sessionterminated/>


</body>

</html>
