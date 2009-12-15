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
</head>

<body>

  <div id="doc">
    <div id="hd">
		<g:render template='/templates/aafheader' />
    </div>
    <div id="bd">
      	<div class="container">
	        <g:render template="/templates/nimble/navigation/sidenavigation" />
			<div class="content">
	      		<g:layoutBody/>
			</div>
		</div>
    </div>
    <div id="ft">
	
    </div>
  </div>

<n:sessionterminated/>
<n:confirm/>

</body>

</html>