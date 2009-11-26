<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
  "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <title><g:message code="fedreg.title" /> | <g:layoutTitle /></title>

  <n:jquery/>
  <n:growl/>
  <n:flashgrowl/>
  <n:menu/>
  <n:admin/>

  <link rel="stylesheet" href="${resource(dir: nimblePath, file: '/css/fedreg.css')}"/>
  <link rel="stylesheet" href="${resource(dir: nimblePath, file: '/css/icons.css')}"/>

  <g:layoutHead/>
</head>

<body>

  <div id="doc">
    <div id="hd">
		<div id="banner">
	  		<h1><g:message code="fedreg.title" /></h1>
	     	<img src="${resource(dir:'images',file:'aaf_logo.gif')}" alt="${message(code:'fedreg.title')}" border="0" />
	
			<n:isLoggedIn>
			<div id="userops">
				<g:message code="fedreg.user.greeting" /> <n:principalName/> | <g:link controller="auth" action="logout" class=""><g:message code="fedreg.user.logout" /></g:link>
			</div>
	  		</n:isLoggedIn>
		</div>
		<n:isLoggedIn>
      	<g:render template='/templates/nimble/navigation/topnavigation'/>
		</n:isLoggedIn>
    </div>
    <div id="bd">
      	<g:layoutBody/>
    </div>
    <div id="ft">
	
    </div>
  </div>

<n:sessionterminated/>


</body>

</html>