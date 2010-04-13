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
	
	
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.jqplot.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/plugins/jqplot.pieRenderer.min.js')}"></script>
	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.jqplot.min.css')}" />
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
			  <h3><g:message code="fedreg.layout.datamanagement.navigation.title" /></h3>
			    <ul>
				  <li>
					<n:confirmaction action="\$('#working').trigger('fedreg.working'); document.refreshdata.submit();" title="${message(code: 'fedreg.view.host.datamanagement.confirm.title')}" msg="${message(code: 'fedreg.view.host.datamanagement.confirm.descriptive')}" accept="${message(code: 'nimble.link.accept')}" cancel="${message(code: 'nimble.link.cancel')}" class=""><g:message code="fedreg.link.refreshdata" /></n:confirmaction>
				  </li>
				</ul>
			</div>
			<div class="content">
	      		<g:layoutBody/>
			  	<g:form action="refreshdata" name="refreshdata">
				</g:form>
			</div>
		</div>
    </div>
    <div id="ft">
	
    </div>
  </div>

<n:sessionterminated/>

</body>

</html>
