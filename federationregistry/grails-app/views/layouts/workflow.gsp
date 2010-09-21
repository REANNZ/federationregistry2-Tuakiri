<!DOCTYPE html>

<html>
  <head>
    <title><g:message code="fedreg.title" /> | <g:layoutTitle /></title>

	<!--[if lt IE 9]>
		<script type="text/javascript" src="${resource(dir: 'js', file: '/html5.js')}"></script>
	<![endif]-->
	<script type="text/javascript" src="${resource(dir: 'js', file: '/modernizr-1.5.min.js')}"></script>
    
	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery-ui-1.8.2.custom.css')}" />
	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.jqplot.min.css')}" />
	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.jgrowl.css')}" />
	
	<link rel="stylesheet/less" href="${resource(dir:'css',file:'frtheme.less')}" />
		
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery-1.4.2.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery-ui-1.8.2.custom.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'less-1.0.35.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.jgrowl.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.validate.pack.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.validate.additional-methods.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.form.wizard-2.0.1-min.js')}"></script>
			
	<script type="text/javascript" src="${resource(dir: 'js', file: '/fedreg-members.js')}"></script>
	<nh:nimbleui/>
		
	<script type="text/javascript">
      <njs:flashgrowl/>
	</script>
	
    <g:layoutHead />

</head>

<body>

    <header>
		<g:render template='/templates/frheader' />
    </header>
    
	<nav>
		<n:isLoggedIn>
			<g:render template='/templates/frtopnavigation'/>

			<ul class="level2">
				<li class="${controllerName == 'workflowApproval' ? 'active':''}">
					<g:link controller="workflowApproval" action="list"><g:message code="label.approvals" /></g:link>
				</li>
				<n:hasPermission target="workflow:processes:view">
					<li class="${controllerName == 'workflowProcess' ? 'active':''}">
						<g:link controller="workflowProcess" action="list"><g:message code="label.processes" /></g:link>
					</li>
				</n:hasPermission>
				<n:hasPermission target="workflow:scripts:view">
					<li class="${controllerName == 'workflowScript' ? 'active':''}">
						<g:link controller="workflowScript" action="list"><g:message code="label.scripts" /></g:link>
					</li>
				</n:hasPermission>
			</ul>
			
			<g:if test="${controllerName == 'workflowApproval'}">
				<ul class="level3a">
					<li class="${actionName == 'list' ? 'active':''}"><g:link controller="workflowApproval" action="list"><g:message code="label.pending"/></g:link></li>
					<n:hasPermission target="workflow:approval:administrator">
						<li class="${actionName == 'administrative' ? 'active':''}"><g:link controller="workflowApproval" action="administrative"><g:message code="label.all"/></g:link></li>
					</n:hasPermission>
				</ul>
			</g:if>
			
			<g:if test="${controllerName == 'workflowProcess'}">
				<ul class="level3a">
					<li class="${actionName == 'list' ? 'active':''}"><g:link controller="workflowProcess" action="list"><g:message code="label.list"/></g:link></li>
					<li class="${actionName == 'create' ? 'active':''}"><g:link controller="workflowProcess" action="create"><g:message code="label.create"/></g:link></li>
					<g:if test="${actionName in ['show', 'edit']}">
					<li> | </li>
					<li><g:message code="fedreg.view.workflow.process.show.heading" args="[process.name]"/>: </li>
					<li class="${actionName == 'show' ? 'active':''}"><g:link controller="workflowProcess" action="show" id="${process.id}"><g:message code="label.view"/></g:link></li>
					<li class="${actionName == 'edit' ? 'active':''}"><g:link controller="workflowProcess" action="edit" id="${process.id}" class="${actionName == 'edit' ? 'active':''}"><g:message code="label.edit"/></g:link></li>
					</g:if>
				</ul>
			</g:if>
			
			<g:if test="${controllerName == 'workflowScript'}">
				<ul class="level3a">
					<li class="${actionName == 'list' ? 'active':''}"><g:link controller="workflowScript" action="list"><g:message code="label.list"/></g:link></li>
					<li class="${actionName == 'create' ? 'active':''}"><g:link controller="workflowScript" action="create"><g:message code="label.create"/></g:link></li>
					<g:if test="${actionName in ['show', 'edit']}">
					<li> | </li>
					<li><g:message code="fedreg.view.workflow.script.show.heading" args="[script.name]"/>: </li>
					<li class="${actionName == 'show' ? 'active':''}"><g:link controller="workflowScript" action="show" id="${script.id}"><g:message code="label.view"/></g:link></li>
					<li class="${actionName == 'edit' ? 'active':''}"><g:link controller="workflowScript" action="edit" id="${script.id}" class="${actionName == 'edit' ? 'active':''}"><g:message code="label.edit"/></g:link></li>
					</g:if>
				</ul>
			</g:if>
			
		</n:isLoggedIn>
	</nav>
	<section>
		<div id="working"><img src="${resource(dir:'images', file:'spinner.gif')}" width="20" height="20"> <br> <g:message code="label.working"/></div>

		<g:layoutBody/>
    </section>

	<footer>
		<g:render template='/templates/frfooter' />
	</footer>

<n:sessionterminated/>

</body>

</html>
