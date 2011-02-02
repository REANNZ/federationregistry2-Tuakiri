<html>
	<head>   
	    <meta name="layout" content="public" />
	    <title><g:message code="fedreg.view.index.title"/></title>
	</head>

  <body>
    <h2><g:message code="label.erroroccured"/></h2>
	<p class="error">
		<g:message code="fedreg.error.apology" />
	</p>

	<h3><g:message code="label.details"/></h3>
  	<p class="message">
		<strong>Error ${request.'javax.servlet.error.status_code'}:</strong> ${request.'javax.servlet.error.message'.encodeAsHTML()}<br>
		<strong>Servlet:</strong> ${request.'javax.servlet.error.servlet_name'}<br>
		<strong>URI:</strong> ${request.'javax.servlet.error.request_uri'}<br>
		<g:if test="${exception}">
	  		<strong>Exception Message:</strong> ${exception.message?.encodeAsHTML()} <br>
	  		<strong>Caused by:</strong> ${exception.cause?.message?.encodeAsHTML()} <br>
	  		<strong>Class:</strong> ${exception.className} <br>
	  		<strong>At Line:</strong> [${exception.lineNumber}] <br>
		</g:if>
  	</p>

  </body>
</html>