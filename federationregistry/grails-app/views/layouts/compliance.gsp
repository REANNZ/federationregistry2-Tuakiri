<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
  "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <title><g:message code="fedreg.title" /> | <g:layoutTitle /></title>

	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.jqplot.min.css')}" />
	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery-ui-1.8.2.custom.css')}" />
	<link rel="stylesheet/less" href="${resource(dir:'css',file:'aaftheme.less')}" />
		
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery-1.4.2.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery-ui-1.8.2.custom.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'less-1.0.32.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.jgrowl.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.validate.pack.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.validate.additional-methods.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.form.wizard-2.0.1-min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.jqplot.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jqplotplugins/jqplot.pieRenderer.min.js')}"></script>		
	
	<script type="text/javascript" src="${resource(dir: 'js', file: '/fedreg-members.js')}"></script>
		
    <g:layoutHead />
</head>

<body>

    <header>
		<g:render template='/templates/aafheader' />
    </header>
	
	<nav>
	<n:isLoggedIn>
		<g:render template='/templates/aaftopnavigation'/>
		
		<ul class="level2a">
			<li class="${controllerName == 'IDPSSODescriptorAttributeCompliance' ? 'active':''}">
				<g:link controller="IDPSSODescriptorAttributeCompliance" action="summary"><g:message code="label.attributesummary" /></g:link>
		 	</li>
			<li class="${controllerName == 'attributeRelease' ? 'active':''}">
				<g:link controller="attributeRelease" action="index"><g:message code="label.attributerelease" /></g:link>
		 	</li>
			<li class="${controllerName == 'certifyingAuthorityUsage' ? 'active':''}">
				<g:link controller="certifyingAuthorityUsage" action="index"><g:message code="label.cautilization" /></g:link>
			</li>
		</ul>	
	</n:isLoggedIn>
	</nav>

	<section>
		<div id="working"><img src="${resource(dir:'images', file:'spinner.gif')}" width="20" height="20"><br/><g:message code="label.working"/></div>

		<g:layoutBody/>
    </section>

<n:sessionterminated/>

</body>

</html>
