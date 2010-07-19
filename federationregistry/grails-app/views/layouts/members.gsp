<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
  "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <title><g:message code="fedreg.title" /> | <g:layoutTitle /></title>
  
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
			
			$("form").bind("keypress", function(e) {
				if (e.keyCode == 13) {
					return false;
				}
			});
		});
    </script>

	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.jqplot.min.css')}" />
	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery-ui-1.8.2.custom.css')}" />
	<link rel="stylesheet/less" href="${resource(dir:'css',file:'aaftheme.less')}" />
		
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery-1.4.2.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery-ui-1.8.2.custom.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'less-1.0.32.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/fedreg-members.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.jgrowl.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.validate.pack.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.validate.additional-methods.js')}"></script>
	
    <g:layoutHead />

</head>

<body>
    <header>
		<g:render template='/templates/aafheader' />
    </header>
    
	<nav>
		<n:isLoggedIn>
			<g:render template='/templates/aaftopnavigation'/>

			<ul class="level2">
				<li>
					<g:link controller="organization" action="list"><g:message code="fedreg.link.organizations" /></g:link>
				</li>
				<li>
					<g:link controller="entity" action="list"><g:message code="fedreg.link.entitydescriptors" /></g:link>
				</li>
				<li class="${controllerName == 'IDPSSODescriptor' ? 'active':''}">
					<g:link controller="IDPSSODescriptor" action="list" ><g:message code="fedreg.link.identityproviders" /></g:link>
				</li>
				<li class="${controllerName == 'Contacts' ? 'active':''}">
					<g:link controller="contacts" action="list"><g:message code="fedreg.link.contacts" /></g:link>
				</li>
			</ul>	
			
			<g:if test="${controllerName == 'IDPSSODescriptor'}">
			<ul class="level3">
				<li class="${actionName == 'list' ? 'active':''}"><g:link controller="IDPSSODescriptor" action="list"><g:message code="fedreg.link.list"/></g:link></li>
				<li class="${actionName == 'create' ? 'active':''}"><g:link controller="IDPSSODescriptor" action="create"><g:message code="fedreg.link.create"/></g:link></li>
				<g:if test="${actionName in ['show', 'edit']}">
				<li> | </li>
				<li><g:message code="fedreg.view.members.identityprovider.show.heading" args="[identityProvider.displayName]"/>: </li>
				<li class="${actionName == 'show' ? 'active':''}"><g:link controller="IDPSSODescriptor" action="show" id="${identityProvider.id}"><g:message code="fedreg.link.view"/></g:link></li>
				<li class="${actionName == 'edit' ? 'active':''}"><g:link controller="IDPSSODescriptor" action="edit" id="${identityProvider.id}" class="${actionName == 'edit' ? 'active':''}"><g:message code="fedreg.link.edit"/></g:link></li>
				</g:if>
			</ul>
			</g:if>
			
			<g:if test="${controllerName == 'contacts'}">
			<ul class="level3">
				<g:if test="${actionName in ['list', 'create']}">
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
			</g:if>
		</n:isLoggedIn>
	</nav>
	
	<section>
		<div id="working"><img src="${resource(dir:'images', file:'spinner.gif')}" width="20" height="20"><br/><g:message code="fedreg.label.working"/></div>

		<g:layoutBody/>
    </section>

<n:sessionterminated/>

</body>

</html>
