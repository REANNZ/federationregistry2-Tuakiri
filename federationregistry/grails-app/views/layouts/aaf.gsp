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

    <link rel="stylesheet" href="${resource(dir: nimblePath, file: '/css/fedreg.css')}"/>

    <g:layoutHead/>
    <nh:famfamfam/>
</head>

<body>

  <div id="doc">
    <div id="hd">
		<g:render template="/templates/aafheader" />
    </div>
    <div id="bd">
      	<div class="container">
	        <div class="localnavigation">
				<h3>Access Control Navigation</h3>
				<ul>
					<li>
						<g:link controller="user" action="list">Users</g:link>
					</li>
						<g:if test="${users}">
							<ul>
								<li>
									<g:link controller="user" action="create">Create User</g:link>
								</li>
							</ul>
						</g:if>
						<g:if test="${user}">
						    <ul>
								<li>
									<g:link controller="user" action="create">Create User</g:link>
								</li>
								<li>
									<g:if test="${user?.profile?.fullName}">
										<g:link controller="user" action="show" id="${user.id}">${user.profile.fullName?.encodeAsHTML()}</g:link>
									</g:if>
									<g:else>
										<g:link controller="user" action="show" id="${user.id}">${user.username?.encodeAsHTML()}</g:link>
									</g:else>
							
									<ul>
										<li>
									        <g:link controller="user" action="edit" id="${user.id}">Edit</g:link>
									      </li>
									      <li>
									          <g:link controller="user" action="changepassword" id="${user.id}">Change Password</g:link>
									      </li>

									      <li id="disableuser">
									        <a onClick="disableUser('${user.id}'); return false;">Disable User</a>
									      </li>
									      <li id="enableuser">
									        <a onClick="enableUser('${user.id}'); return false;">Enable User</a>
									      </li>

									      <li id="disableuserapi">
									        <a onClick="disableAPI('${user.id}'); return false;">Disable API</a>
									      </li>
									      <li id="enableuserapi">
									        <a onClick="enableAPI('${user.id}'); return false;">Enable API</a>
									      </li>
									  </ul>
								</li>
							</ul>
						</g:if>
					<li>
						<g:link controller="role" action="list">Roles</g:link>
					</li>
					<li>
						<g:link controller="group" action="list">Groups</g:link>
					</li>
					<li>
						<g:link controller="admins" action="index">Admins</g:link>
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