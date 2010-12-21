<!DOCTYPE html>

<html>
  <head>
    <title><g:message code="fedreg.title" /> | <g:layoutTitle /></title>
	
	<!--[if lt IE 9]>
		<script type="text/javascript" src="${resource(dir: 'js', file: '/html5.js')}"></script>
	<![endif]-->
	<script type="text/javascript" src="${resource(dir: 'js', file: '/modernizr-1.5.min.js')}"></script>

	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery-ui-1.8.2.custom.css')}" />
	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.jgrowl.css')}" />
	<link rel="stylesheet/less" href="${resource(dir:'css',file:'frtheme.less')}" />
		
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery-1.4.2.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery-ui-1.8.2.custom.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'less-1.0.35.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.jgrowl.min.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.validate.pack.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.validate.additional-methods.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: '/jquery/jquery.datatables.min.js')}"></script>
	
	<nh:nimblecore/>
	<nh:nimbleui/>
		
    <g:layoutHead />
</head>

<body>
	<header>
		<g:render template='/templates/frheader' />
    </header>
	
	<nav>
		<g:render template='/templates/frtopnavigation'/>
	<n:isLoggedIn>
		
		<ul class="level2">
			<li class="${controllerName == 'user' ? 'active':''}"><g:link controller="user" action="list"><g:message code="label.users" /></g:link></li>
			<li class="${controllerName == 'role' ? 'active':''}"><g:link controller="role" action="list"><g:message code="label.roles" /></g:link></li>
			<li class="${controllerName == 'group' ? 'active':''}"><g:link controller="group" action="list"><g:message code="label.groups" /></g:link></li>
			<li class="${controllerName == 'admins' ? 'directactive':''}"><g:link controller="admins" action="index"><g:message code="label.admins" /></g:link></li>
		</ul>

		<g:if test="${controllerName == 'user'}">
			<ul class="level3a">
				<li class="${actionName == 'list' ? 'active':''}" ><g:link controller="user" action="list"><g:message code="label.list" /></g:link></li>
				<li class="${actionName == 'create' ? 'active':''}" ><g:link controller="user" action="create"><g:message code="label.create" /></g:link></li>
				<g:if test="${actionName in ['show', 'edit', 'changepassword', 'changelocalpassword']}">
					<li> | </li>
					<li><g:message code="fedreg.view.user.show.heading" args="[user.profile?.fullName ?: user.username]"/>: </li>
					<li class="${actionName == 'show' ? 'active':''}" ><g:link controller="user" action="show" id="${user.id}"><g:message code="label.view" /></g:link></li>
					<li class="${actionName == 'edit' ? 'active':''}"><g:link controller="user" action="edit" id="${user.id}"><g:message code="label.edit" /></g:link></li>
					<g:if test="${user.external}">
						<li class="${actionName == 'changelocalpassword' ? 'active':''}"><g:link controller="user" action="changelocalpassword" id="${user.id}"><g:message code="label.changelocalpassword" /></g:link></li>
					</g:if>
					<g:else>
						<li class="${actionName == 'changepassword' ? 'active':''}"><g:link controller="user" action="changepassword" id="${user.id}"><g:message code="label.changepassword" /></g:link></li>
					</g:else>
					<li id="disableuser"><a href="#" onClick="nimble.disableUser('${user.id}'); return false;"><g:message code="label.disableaccount" /></a></li>
					<li id="enableuser"><a href="#" onClick="nimble.enableUser('${user.id}'); return false;"><g:message code="label.enableaccount" /></a></li>
					<li id="disableuserapi"><a href="#" onClick="nimble.disableAPI('${user.id}'); return false;"><g:message code="label.disableapi" /></a></li>
					<li id="enableuserapi"><a href="#" onClick="nimble.enableAPI('${user.id}'); return false;"><g:message code="label.enableapi" /></a></li>
				</g:if>
			</ul>
		</g:if>

		<g:if test="${controllerName == 'group'}">
			<ul class="level3a">
				<li class="${actionName == 'list' ? 'active':''}"><g:link controller="group" action="list"><g:message code="label.list" /></g:link></li>
				<li class="${actionName == 'create' ? 'active':''}"><g:link controller="group" action="create"><g:message code="label.create" /></g:link></li>
				<g:if test="${actionName in ['show', 'edit']}">
					<li> | </li>
					<li><g:message code="fedreg.view.group.show.heading" args="[group.name]"/>: </li>
					<li class="${actionName == 'show' ? 'active':''}" ><g:link controller="group" action="show" id="${group.id}"><g:message code="label.view" /></g:link></li>
					<li class="${actionName == 'edit' ? 'active':''}"><g:link controller="group" action="edit" id="${group.id}"><g:message code="label.edit" /></g:link></li>
					<li><n:confirmaction action="document.deletegroup.submit();" title="${message(code: 'nimble.template.delete.confirm.title')}" msg="${message(code: 'nimble.group.delete.confirm')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" label="label.delete" plain="true"/></li>
				</g:if>
			</ul>
		</g:if>

		<g:if test="${controllerName == 'role'}">
			<ul class="level3a">
				<li class="${actionName == 'list' ? 'active':''}"><g:link controller="role" action="list"><g:message code="label.list" /></g:link></li>
				<li class="${actionName == 'create' ? 'active':''}"><g:link controller="role" action="create"><g:message code="label.create" /></g:link></li>
				<g:if test="${actionName in ['show', 'edit']}">
					<li> | </li>
					<li><g:message code="fedreg.view.role.show.heading" args="[role.name]"/>: </li>
					<li class="${actionName == 'show' ? 'active':''}" ><g:link controller="role" action="show" id="${role.id}"><g:message code="label.view" /></g:link></li>
					<li class="${actionName == 'edit' ? 'active':''}"><g:link controller="role" action="edit" id="${role.id}"><g:message code="label.edit" /></g:link></li>
					<li><n:confirmaction action="document.deleterole.submit();" title="${message(code: 'nimble.template.delete.confirm.title')}" msg="${message(code: 'nimble.role.delete.confirm')}" accept="${message(code: 'label.accept')}" cancel="${message(code: 'label.cancel')}" label="label.delete" plain="true"/></li>
				</g:if>
			</ul>
		</g:if>
	</n:isLoggedIn>
	</nav>
	
	        
	<section>
		<section>
			<g:layoutBody/>
		</section>
	</section>

	<footer>
		<g:render template='/templates/frfooter' />
	</footer>
	
<n:sessionterminated/>

</body>

</html>