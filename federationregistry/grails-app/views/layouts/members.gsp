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

		$(function() {
			nimble.createTabs('tabs');
			nimble.createTabs('tabs2');
		});
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
				<li class="${controllerName == 'organization' ? 'active':''}">
					<g:link controller="organization" action="list"><g:message code="label.organizations" /></g:link>
				</li>
				<li class="${controllerName == 'entityDescriptor' ? 'active':''}">
					<g:link controller="entityDescriptor" action="list"><g:message code="label.entitydescriptors" /></g:link>
				</li>
				<li class="${controllerName == 'IDPSSODescriptor' ? 'active':''}">
					<g:link controller="IDPSSODescriptor" action="list" ><g:message code="label.identityproviders" /></g:link>
				</li>
				<li class="${controllerName == 'SPSSODescriptor' ? 'active':''}">
					<g:link controller="SPSSODescriptor" action="list" ><g:message code="label.serviceproviders" /></g:link>
				</li>
				<li class="${controllerName == 'contacts' ? 'active':''}">
					<g:link controller="contacts" action="list"><g:message code="label.contacts" /></g:link>
				</li>
			</ul>	
			
			<g:if test="${controllerName == 'organization'}">
				<ul class="level3a">
					<li class="${actionName == 'list' ? 'active':''}"><g:link controller="organization" action="list"><g:message code="label.list"/></g:link></li>
						<li class="${actionName == 'create' ? 'active':''}"><g:link controller="organization" action="create"><g:message code="label.create"/></g:link></li>
					<g:if test="${actionName in ['show', 'edit']}">
					<li> | </li>
					<li><g:message code="fedreg.view.members.organization.show.heading" args="[organization.displayName]"/>: </li>
					<li class="${actionName == 'show' ? 'active':''}"><g:link controller="organization" action="show" id="${organization.id}"><g:message code="label.view"/></g:link></li>
					<n:hasPermission target="organization:${organization.id}:update">
						<li class="${actionName == 'edit' ? 'active':''}"><g:link controller="organization" action="edit" id="${organization.id}" class="${actionName == 'edit' ? 'active':''}"><g:message code="label.edit"/></g:link></li>
					</n:hasPermission>
					</g:if>
				</ul>
			</g:if>
			
			<g:if test="${controllerName == 'entityDescriptor'}">
				<ul class="level3a">
					<li class="${actionName == 'list' ? 'active':''}"><g:link controller="entityDescriptor" action="list"><g:message code="label.list"/></g:link></li>
						<li class="${actionName == 'create' ? 'active':''}"><g:link controller="entityDescriptor" action="create"><g:message code="label.create"/></g:link></li>
					<g:if test="${actionName in ['show', 'edit']}">
					<li> | </li>
					<li><g:message code="fedreg.view.members.entity.show.heading" args="[entity.entityID]"/>: </li>
					<li class="${actionName == 'show' ? 'active':''}"><g:link controller="entityDescriptor" action="show" id="${entity.id}"><g:message code="label.view"/></g:link></li>
					<n:hasPermission target="descriptor:${entity.id}:update">
						<li class="${actionName == 'edit' ? 'active':''}"><g:link controller="entityDescriptor" action="edit" id="${entity.id}" class="${actionName == 'edit' ? 'active':''}"><g:message code="label.edit"/></g:link></li>
					</n:hasPermission>
					</g:if>
				</ul>
			</g:if>
			
			<g:if test="${controllerName == 'IDPSSODescriptor'}">
				<ul class="level3a">
					<li class="${actionName == 'list' ? 'active':''}"><g:link controller="IDPSSODescriptor" action="list"><g:message code="label.list"/></g:link></li>
						<li class="${actionName in ['create', 'save'] ? 'active':''}"><g:link controller="IDPSSODescriptor" action="create"><g:message code="label.create"/></g:link></li>
					<g:if test="${actionName in ['show', 'edit']}">
					<li> | </li>
					<li><g:message code="fedreg.view.members.identityprovider.show.heading" args="[identityProvider.displayName]"/>: </li>
					<li class="${actionName == 'show' ? 'active':''}"><g:link controller="IDPSSODescriptor" action="show" id="${identityProvider.id}"><g:message code="label.view"/></g:link></li>
					<n:hasPermission target="descriptor:${identityProvider.id}:update">
						<li class="${actionName == 'edit' ? 'active':''}"><g:link controller="IDPSSODescriptor" action="edit" id="${identityProvider.id}" class="${actionName == 'edit' ? 'active':''}"><g:message code="label.edit"/></g:link></li>
					</n:hasPermission>
					</g:if>
				</ul>
			</g:if>
			
			<g:if test="${controllerName == 'SPSSODescriptor'}">
				<ul class="level3a">
					<li class="${actionName == 'list' ? 'active':''}"><g:link controller="SPSSODescriptor" action="list"><g:message code="label.list"/></g:link></li>
						<li class="${actionName == 'create' ? 'active':''}"><g:link controller="SPSSODescriptor" action="create"><g:message code="label.create"/></g:link></li>
					<g:if test="${actionName in ['show', 'edit']}">
						<li> | </li>
						<li><g:message code="fedreg.view.members.serviceprovider.show.heading" args="[serviceProvider.displayName]"/>: </li>
						<li class="${actionName == 'show' ? 'active':''}"><g:link controller="SPSSODescriptor" action="show" id="${serviceProvider.id}"><g:message code="label.view"/></g:link></li>
						<n:hasPermission target="descriptor:${serviceProvider.id}:update">
							<li class="${actionName == 'edit' ? 'active':''}"><g:link controller="SPSSODescriptor" action="edit" id="${serviceProvider.id}" class="${actionName == 'edit' ? 'active':''}"><g:message code="label.edit"/></g:link></li>
						</n:hasPermission>
					</g:if>
				</ul>
			</g:if>
			
			<g:if test="${controllerName == 'contacts'}">
				<ul class="level3a">
					<li class="${actionName == 'list' ? 'active':''}"><g:link controller="contacts" action="list"><g:message code="label.list"/></g:link></li>
					<n:hasPermission target="contact:create">
						<li class="${actionName == 'create' ? 'active':''}"><g:link controller="contacts" action="create"><g:message code="label.create"/></g:link></li>
					</n:hasPermission>
					<g:if test="${actionName in ['show', 'edit']}">
						<li> | </li>
						<li><g:message code="fedreg.view.members.contacts.show.heading" args="[contact.givenName, contact.surname]"/>: </li>
						<li class="${actionName == 'show' ? 'active':''}"><g:link controller="contacts" action="show" id="${contact.id}"><g:message code="label.view"/></g:link>
						<n:hasPermission target="contact:${contact.id}:update">
							<li class="${actionName == 'edit' ? 'active':''}"><g:link controller="contacts" action="edit" id="${contact.id}"><g:message code="label.edit"/></g:link></li>
						</n:hasPermission>
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
</body>

</html>
