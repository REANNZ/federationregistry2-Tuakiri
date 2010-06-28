<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
  "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <title><g:message code="fedreg.title" /> | <g:layoutTitle /></title>
    
	<nh:nimblecore/>
    <nh:nimbleui/>
  
    <nh:growl/>
    <script type="text/javascript">
      	<njs:flashgrowl/>

		$(function() {
		  	$("#working").hide();
			$("#working").bind("fedreg.working", function(){
				if( $(this).is(':hidden') ) {
					$(this).css({left: $("body").scrollLeft() + 10, top: $("body").scrollTop() + 10})
					$(this).show('blind')
				}
			 }).bind("ajaxComplete", function(){
				if( $(this).is(':visible') ) {
					$(this).hide('blind');
				}
			 });
		});
    </script>

	<link rel="stylesheet" href="${resource(file: '/css/icons.css')}"/>
	<link rel="stylesheet" href="${resource(file: '/css/fedreg.css')}"/>
	
    <g:layoutHead />
</head>

<body>

  <div id="doc">
    <div id="hd">
		<g:render template='/templates/aafheader' model="['navigation':true]"/>
    </div>
    <div id="bd">
		<div id="working"><img src="${resource(dir:'images', file:'spinner.gif')}" width="20" height="20"><br/><g:message code="fedreg.label.working"/></div>
		
		<div class="container">
	    	<div class="localnavigation">
			  <h3><g:message code="fedreg.layout.workflow.navigation.title" /></h3>
			    <ul>
					
					<g:if test="${controllerName == 'workflowProcess' && actionName in ['show', 'edit']}">
					<li>
						<g:link controller="workflowProcess" action="show" id="${process.id}"><h4>${process.name?.encodeAsHTML()}</h4></g:link>
						<ul>
							<li><g:link controller="workflowProcess" action="edit" id="${process.id}"><g:message code="fedreg.link.edit"/></g:link></li>
						</ul>
						<hr>
					</li>
					</g:if>
					<g:if test="${controllerName == 'workflowScript' && actionName in ['show', 'edit']}">
					<li>
						<g:link controller="workflowScript" action="show" id="${script.id}"><h4>${script.name?.encodeAsHTML()}</h4></g:link>
						<ul>
							<li><g:link controller="workflowScript" action="edit" id="${script.id}"><g:message code="fedreg.link.edit"/></g:link></li>
						</ul>
						<hr>
					</li>
					</g:if>
					
					<!-- Processes -->
				  	<li>
						<h4><g:message code="fedreg.layout.workflow.navigation.process.title" /></h4>
						<ul>
							<li><g:link controller="workflowProcess" action="list"><g:message code="fedreg.link.list" /></g:link></li>
							<li><g:link controller="workflowProcess" action="create"><g:message code="fedreg.link.create" /></g:link></li>
						</ul>
					</li>
					
					<!-- Scripts -->
				  	<li>
						<h4><g:message code="fedreg.layout.workflow.navigation.script.title" /></h4>
						<ul>
							<li><g:link controller="workflowScript" action="list"><g:message code="fedreg.link.list" /></g:link></li>
							<li><g:link controller="workflowScript" action="create"><g:message code="fedreg.link.create" /></g:link></li>
						</ul>
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
