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

		$(function() {
			$("#tabs").tabs();
			$("#tabs2").tabs();
			
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
					<li>
						<g:link controller="contacts" action="list"><g:message code="fedreg.link.contacts" /></g:link>
						
						    <ul>
								<g:if test="${controllerName == 'contacts' && actionName in ['list']}">
									<li><g:link controller="contacts" action="create" class="icon icon_user_add"><g:message code="fedreg.link.create"/></g:link></li>
								</g:if>
								<g:if test="${actionName in ['show', 'edit']}">
								<li>
										<g:link controller="contacts" action="show" id="${contact.id}" class="icon icon_user">${contact.givenName?.encodeAsHTML()} ${contact.surname?.encodeAsHTML()}</g:link>
										<ul>
											<li><g:link controller="contacts" action="edit" id="${contact.id}" class="icon icon_user_edit"><g:message code="fedreg.link.edit"/></g:link></li>
										</ul>
								</li>
								</g:if>
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
