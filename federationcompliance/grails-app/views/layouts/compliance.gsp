<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
  "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <title><g:message code="fedreg.title" /> | <g:layoutTitle /></title>
    
	<nh:jquery/>
    <nh:basecss/>
    <nh:nimbleui/>
    <nh:admin/>
  
    <nh:growl/>
    <script type="text/javascript">
      <njs:flashgrowl/>
    </script>

	<nh:famfamfam/>
	<link rel="stylesheet" href="${resource(file: '/css/icons.css')}"/>
	<link rel="stylesheet" href="${resource(file: '/css/fedreg.css')}"/>
	
  <g:layoutHead />
</head>

<body>

  <div id="doc">
    <div id="hd">
		<g:render template="/templates/aafheader" />
    </div>
    <div id="bd">
		<div class="container">
	    	<div class="localnavigation">
			  <h3>Compliance Navigation</h3>
			    <ul>
				  <li><g:link controller="attributeCompliance" action="summary">Attribute Summary</g:link>
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
