<!DOCTYPE html>

<html>
  <head>
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
 	<nh:nimbleui/>
		
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
			<li class="${controllerName == 'dataManagement' ? 'active':''}">
				<g:link controller="dataManagement" action="index"><g:message code="label.reloads" /></g:link>
			</li>
		  	<li>
				<n:confirmaction action="\$('#working').trigger('fedreg.working'); document.refreshdata.submit();" title="${message(code: 'fedreg.view.host.datamanagement.confirm.title')}" msg="${message(code: 'fedreg.view.host.datamanagement.confirm.descriptive')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" label="${message(code: 'label.refreshdata')}" plain="${true}"/>
		  	</li>
		</ul>
	</n:isLoggedIn>
	</nav>
	
    <section>
		<div id="working"><img src="${resource(dir:'images', file:'spinner.gif')}" width="20" height="20"><br/><g:message code="label.working"/></div>
		
		<g:layoutBody/>
		<g:form action="refreshdata" name="refreshdata">
		</g:form>
	<section>

<n:sessionterminated/>

</body>

</html>
