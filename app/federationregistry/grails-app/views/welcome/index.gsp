<html>
  <head>      
      <meta name="layout" content="public" />
  </head>

  <body>
    <div class="hero-unit">
      <h1><g:message code="label.welcome"/></h1>
      <p><g:message code="fedreg.view.index.overview.descriptive"/></p>

      <div class="row row-spacer">
        <div class="span5">
          <g:message code="fedreg.view.index.returninguser"/>
          <g:link class="btn btn-success btn-large" controller="auth"><g:message code="fedreg.navigation.login" /></g:link>
        </div>

        <div class="span5">
          <g:message code="fedreg.view.index.newuser"/>
          <g:link class="btn btn-info btn-large" controller="bootstrap" action="organization"><g:message code="fedreg.navigation.registerorganization" /></g:link>
          <br><br>
          <g:link class="btn btn-info btn-large" controller="bootstrap" action="idp"><g:message code="fedreg.navigation.registeridentityprovider" /></g:link>
          </a>
          <br><br>
          <g:link class="btn btn-info btn-large" controller="bootstrap" action="sp"><g:message code="fedreg.navigation.registerserviceprovider" /></g:link>
        </div>
      </div>
    </div>

    <div class="problems-logging-on hidden">
      <hr>
      <a name="problems"></a>
      <g:message code="fedreg.view.index.problems"/>
    </div>
  </body>
</html>